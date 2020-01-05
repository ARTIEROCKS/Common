package artie.sensor.common.repository;
import java.util.List;

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
	
	/**
	 * Function to get all the sensor data stored in database
	 * @return
	 */
	public List<SensorData> findAll(){
		String sqlQuery = "select * from SensorData";
		List<SensorData> sensorDataList = this.jdbcTemplate.query(sqlQuery,
																  (rs, rowNum) -> new SensorData(rs.getLong("id"),
																		  					     rs.getString("data")));
		return sensorDataList;
	}
	
	/**
	 * Function to delete the sensor data by ID
	 * @param id
	 */
	public void delete(long id) {
		String sqlQuery = "delete from SensorData where id=?";
		this.jdbcTemplate.update(sqlQuery, id);
	}
}
