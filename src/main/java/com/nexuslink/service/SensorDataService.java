package com.nexuslink.service;

import com.nexuslink.exception.InvalidDataException;
import com.nexuslink.exception.ResourceNotFoundException;
import com.nexuslink.model.dtos.AverageSensorDataDTO;
import com.nexuslink.model.dtos.HomeAssistantSensorDataDTO;
import com.nexuslink.model.entities.HomeAssistantSensorData;
import com.nexuslink.model.entities.Sensor; // Importe a entidade Sensor
import com.nexuslink.repository.SensorDataRepository;
import com.nexuslink.repository.SensorRepository; // Importe o SensorRepository
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    private final SensorDataRepository dataRepository;
    private final SensorRepository sensorRepository; // Repositório de Sensor injetado

    // Construtor atualizado para receber os dois repositórios
    public SensorDataService(SensorDataRepository dataRepository, SensorRepository sensorRepository) {
        this.dataRepository = dataRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public HomeAssistantSensorData saveData(HomeAssistantSensorDataDTO dto) {
        if (dto.temperature() == null || dto.humidity() == null || dto.sensorNumber() == null) {
            throw new InvalidDataException("Temperatura, umidade e identificador do sensor são obrigatórios.");
        }

        try {
            // 1. Busca o sensor pelo número recebido no DTO
            // Usamos findById com o 'number' que agora é o ID na entidade Sensor
            Sensor sensor = sensorRepository.findById(dto.sensorNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Sensor com ID " + dto.sensorNumber() + " não encontrado."));

            // 2. Cria a nova entidade de dados
            HomeAssistantSensorData sensorData = new HomeAssistantSensorData(dto.temperature(), dto.humidity());

            // 3. Associa o dado ao sensor encontrado
            sensorData.setSensor(sensor);

            return dataRepository.save(sensorData);

        } catch (Exception e) {
            // Propaga a exceção para ser tratada pelo GlobalExceptionHandler
            throw new InvalidDataException("Erro ao salvar dados do sensor: " + e.getMessage());
        }
    }

    public List<HomeAssistantSensorDataDTO> getAllData() {
        var dataList = dataRepository.findAll();

        if (dataList.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum dado de sensor encontrado.");
        }

        return dataList.stream()
                .map(HomeAssistantSensorDataDTO::new)
                .collect(Collectors.toList());
    }

    public AverageSensorDataDTO getLastTenAverage(Pageable pageable) {
        Pageable sortedByIdDesc = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<HomeAssistantSensorData> page = dataRepository.findAll(sortedByIdDesc);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum dado de sensor encontrado.");
        }

        double avgTemperature = page.getContent().stream()
                .map(HomeAssistantSensorData::getTemperature)
                .filter(java.util.Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double avgHumidity = page.getContent().stream()
                .map(HomeAssistantSensorData::getHumidity)
                .filter(java.util.Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new AverageSensorDataDTO(avgTemperature, avgHumidity);
    }
}