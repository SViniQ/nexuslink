package com.nexuslink.controller;

import com.nexuslink.model.dtos.AverageSensorDataDTO;
import com.nexuslink.model.dtos.HomeAssistantSensorDataDTO;
import com.nexuslink.model.entities.HomeAssistantSensorData;
import com.nexuslink.service.SensorDataService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/sensors_data")
public class SensorDataController {

    private final SensorDataService service;

    public SensorDataController(SensorDataService _service){
        service = _service;
    }

    @PostMapping
    public HomeAssistantSensorDataDTO saveData(@RequestBody @Valid HomeAssistantSensorDataDTO dto) {
        HomeAssistantSensorData savedEntity = service.saveData(dto);
        return new HomeAssistantSensorDataDTO(savedEntity);
    }

    @GetMapping("/all")
    public List<HomeAssistantSensorDataDTO> getAllData() {
        return service.getAllData();
    }

    @GetMapping("/average")
    public AverageSensorDataDTO getAverage(Pageable pageable) {
        return service.getLastTenAverage(pageable);
    }
}