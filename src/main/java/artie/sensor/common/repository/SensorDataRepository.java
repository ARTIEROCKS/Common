package artie.sensor.common.repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import artie.sensor.common.model.SensorData;


@Repository
public class SensorDataRepository{
	
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Parameterized constructor
	 * @param jdbcTemplate
	 */
	public SensorDataRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * Function to save the data in the database
	 * @param sensorData
	 */
	public void save(SensorData sensorData) {
		String sqlQuery = "insert into SensorData(data) values (?)";
		this.jdbcTemplate.update(sqlQuery, sensorData.getData());
	}
}
