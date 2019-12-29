package artie.sensor.common.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import artie.sensor.common.model.SensorData;

@Repository
public interface SensorDataRepository extends CrudRepository<SensorData, Long>{
	Optional<SensorData> findById(Long id);
}
