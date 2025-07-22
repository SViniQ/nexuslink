package com.nexuslink.service;

import com.nexuslink.exception.InvalidDataException;
import com.nexuslink.model.dtos.SensorDTO;
import com.nexuslink.model.entities.HomeAssistantSensorData;
import com.nexuslink.model.entities.Sensor;
import com.nexuslink.repository.SensorDataRepository;
import com.nexuslink.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SensorDataRepository dataRepository; // Repositório de dados injetado

    // Construtor atualizado para receber os dois repositórios
    public SensorService(SensorRepository sensorRepository, SensorDataRepository dataRepository) {
        this.sensorRepository = sensorRepository;
        this.dataRepository = dataRepository;
    }

    // O método de salvar um sensor continua o mesmo
    public Sensor saveSensor(SensorDTO dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new InvalidDataException("Nome do sensor é obrigatório.");
        }

        Sensor sensor = new Sensor(dto);
        return sensorRepository.save(sensor);
    }

    // MÉTODO COMPLETAMENTE REESCRITO COM A LÓGICA DE STATUS
    public List<Sensor> getAllSensors() {
        // 1. Busca todos os sensores cadastrados no banco
        List<Sensor> sensors = sensorRepository.findAll();

        // 2. Para cada sensor, calcula seu status dinamicamente
        sensors.forEach(sensor -> {
            // Busca a última leitura de dados para este sensor específico
            Optional<HomeAssistantSensorData> ultimaLeituraOpt = dataRepository.findTopBySensorOrderByTimestampDesc(sensor);

            // Regra 1: Se não houver nenhuma leitura para o sensor
            if (ultimaLeituraOpt.isEmpty()) {
                sensor.setStatus("SUSPENSO");
                return; // Pula para o próximo sensor do loop
            }

            HomeAssistantSensorData ultimaLeitura = ultimaLeituraOpt.get();

            // Regra 2: Sensor não conectado (última leitura há mais de 5 minutos)
            if (ultimaLeitura.getTimestamp().isBefore(Instant.now().minus(5, ChronoUnit.MINUTES))) {
                sensor.setStatus("SUSPENSO");

                // Regra 3: Erro (falta temperatura ou umidade na última leitura)
            } else if (ultimaLeitura.getTemperature() == null || ultimaLeitura.getHumidity() == null) {
                sensor.setStatus("ERRO");

                // Regra 4: Tudo OK
            } else {
                sensor.setStatus("ATIVO");
            }
        });

        // 3. Retorna a lista de sensores com o campo 'status' atualizado
        return sensors;
    }
}