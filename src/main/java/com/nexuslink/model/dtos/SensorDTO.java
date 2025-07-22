package com.nexuslink.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SensorDTO(

        @NotBlank(message = "Nome do sensor é obrigatório.")
        String name,

        @NotNull(message = "Número do sensor é obrigatório.")
        Double number

) {}
