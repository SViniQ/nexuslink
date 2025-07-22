package com.nexuslink.model.dtos;

public record AverageSensorDataDTO(
        Double averageTemperature,
        Double averageHumidity
) {}