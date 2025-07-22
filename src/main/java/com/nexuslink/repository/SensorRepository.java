package com.nexuslink.repository;

import com.nexuslink.model.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

}
