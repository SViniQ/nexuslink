package com.nexuslink.repository;

import com.nexuslink.model.entities.HomeAssistantSensorData;
import com.nexuslink.model.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SensorDataRepository extends JpaRepository<HomeAssistantSensorData, Long> {
    Optional<HomeAssistantSensorData> findTopBySensorOrderByTimestampDesc(Sensor sensor);
}
