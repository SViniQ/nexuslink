package com.nexuslink.model.entities;

import com.nexuslink.model.dtos.SensorDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CAMPO RENOMEADO de 'name' para 'modelo' para alinhar com o frontend
    private String modelo;

    // CAMPOS NOVOS
    private String categoria;
    private String status;

    private Double number;

    // RELAÇÃO NOVA: Um Sensor tem Muitas Leituras de Dados
    @OneToMany(mappedBy = "sensor", fetch = FetchType.LAZY)
    @JsonManagedReference // Evita o loop infinito na conversão para JSON
    private List<HomeAssistantSensorData> leituras;

    public Sensor() {}

    // Construtor atualizado para definir valores padrão ao criar um novo sensor
    public Sensor(SensorDTO dto) {
        // No frontend, o DTO ainda envia um 'name', que mapeamos para 'modelo'
        this.modelo = dto.name();
        this.number = dto.number();
        this.status = "ATIVO"; // Todo sensor novo começa como ATIVO
        this.categoria = "Geral"; // Define uma categoria padrão
    }

    // --- GETTERS E SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getNumber() { return number; }
    public void setNumber(Double number) { this.number = number; }

    public List<HomeAssistantSensorData> getLeituras() { return leituras; }
    public void setLeituras(List<HomeAssistantSensorData> leituras) { this.leituras = leituras; }

    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setStatus(String status) { this.status = status; }

    // --- GETTERS INTELIGENTES (Lógica para tratar dados antigos/nulos) ---

    public String getModelo() {
        // Se o modelo no banco for nulo, retorna um nome padrão para não quebrar o front
        return (this.modelo == null) ? "Sensor " + this.id : this.modelo;
    }

    public String getCategoria() {
        // Se a categoria for nula, retorna um valor padrão
        return (this.categoria == null) ? "Geral" : this.categoria;
    }

    public String getStatus() {
        // Se o status for nulo, o sensor é considerado "ATIVO" por padrão
        // A lógica no SensorService irá sobrescrever isso com o status real (ATIVO, SUSPENSO, ERRO)
        return (this.status == null) ? "ATIVO" : this.status;
    }
}