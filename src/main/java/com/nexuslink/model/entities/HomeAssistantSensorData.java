package com.nexuslink.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tb_sensors_data")
public class HomeAssistantSensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperature;
    private Double humidity;
    private Instant timestamp;

    // O campo 'sensorNumber' foi removido e substituído pela relação abaixo.

    // NOVO: Relação Muitos-para-Um (Muitos dados pertencem a Um Sensor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id") // Define o nome da coluna da chave estrangeira
    @JsonBackReference // Evita o loop infinito na conversão para JSON
    private Sensor sensor;

    public HomeAssistantSensorData() {}

    // Construtor simplificado. A associação com o Sensor será feita no Service.
    public HomeAssistantSensorData(Double temperature, Double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = Instant.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    // Getters e Setters para a nova relação
    public Sensor getSensor() { return sensor; }
    public void setSensor(Sensor sensor) { this.sensor = sensor; }
}