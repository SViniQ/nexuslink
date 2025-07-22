package com.nexuslink.model.dtos;

import com.nexuslink.model.entities.HomeAssistantSensorData;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record HomeAssistantSensorDataDTO(
        Long id,
        @NotNull(message = "Temperatura é obrigatória.")
        Double temperature,
        @NotNull(message = "Umidade é obrigatória.")
        Double humidity,
        Long sensorNumber,
        Instant timestamp
) {
    public HomeAssistantSensorDataDTO(HomeAssistantSensorData entity) {
        this(
                entity.getId(),
                entity.getTemperature(),
                entity.getHumidity(),
                (entity.getSensor() != null) ? entity.getSensor().getId() : null,
                entity.getTimestamp()
        );
    }
}