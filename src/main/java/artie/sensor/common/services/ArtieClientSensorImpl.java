package artie.sensor.common.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import artie.sensor.common.config.DataSourceConfig;
import artie.sensor.common.dto.SensorObject;
import artie.sensor.common.enums.ConfigurationEnum;
import artie.sensor.common.interfaces.ArtieClientSensor;
import artie.sensor.common.model.SensorData;
import artie.sensor.common.repository.SensorDataRepository;



@Service
public abstract class ArtieClientSensorImpl implements ArtieClientSensor {
	
	//Attributes
	protected String name;
	protected String version;
	protected String author;
	protected List<SensorObject> sensorData = new ArrayList<SensorObject>();
	protected Map<String, String> configuration = new HashMap<String, String>();
	protected ObjectMapper mapper = new ObjectMapper();
	
	//Connection attributes
	@Autowired
	protected DataSourceConfig dataSourceConfig;
	protected JdbcTemplate jdbcTemplate;
	protected SensorDataRepository sensorDataRepository;
	
	
	//Properties
	public List<SensorObject> getSensorData(){
		return this.sensorData;
	}
	public void setSensorData(List<SensorObject> sensorData){
		this.sensorData = sensorData;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getVersion(){
		return this.version;
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public Map<String,String> getConfiguration(){
		return this.configuration;
	}
	public void setConfiguration(Map<String, String> configuration){
		this.configuration = configuration;
	}
	public void setConfiguration(String jsonSensorConfiguration){
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String,String> configuration = mapper.readValue(jsonSensorConfiguration, new TypeReference<HashMap<String,String>>(){});
			this.configuration = configuration;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Default Constructor
	 */
	public ArtieClientSensorImpl(){
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_REGISTRATION.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_FILENAME.toString(), "ARTIE_Sensor.log");
		this.configuration.putIfAbsent(ConfigurationEnum.DB_DRIVER_CLASS.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.DB_URL.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.DB_USER.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.DB_PASSWD.toString(), "");
	}
	
	/**
	 * Parameterized constructor
	 * @param configuration
	 */
	public ArtieClientSensorImpl(Map<String, String> configuration){
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_REGISTRATION.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_FILENAME.toString(), "ARTIE_Sensor.log");
		this.configuration.putIfAbsent(ConfigurationEnum.DB_DRIVER_CLASS.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.DB_URL.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.DB_USER.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.DB_PASSWD.toString(), "");
		this.configuration.putAll(configuration);
	}
	
	
	/**
	 * Function to add the sensor object to the data collection
	 * @param object
	 */
	public void addSensorObject(SensorObject object){
		this.sensorData.add(object);
	}
	
	/**
	 * Function to write all the data into the file
	 */
	public void writeDataToFile(){
		
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		FileWriter fw = null;
		
		//If the sensor data contains sensor information
		if(this.sensorData.size() > 0){
			try {
				fw = new FileWriter(this.configuration.get(ConfigurationEnum.SENSOR_FILE_FILENAME.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}		
		
			//Writing the sensor data
			this.sensorData.forEach(data->{
				try {
					sw.write(mapper.writeValueAsString(data));
					sw.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			
			//Writing in the file
			try {
				fw.write(sw.toString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Function to replace a configuration setting
	 * @param key
	 * @param value
	 */
	public void replaceConfiguration(String key, String value){
		this.configuration.replace(key, value);
	}
	
	/**
	 * Function to insert the data into the h2 database server
	 */
	public void sendSensorData(){
		
		
		//Checks if the jdbcTemplate is null
		if(this.jdbcTemplate == null && this.dataSourceConfig !=null ) {
			
			//We create a new jdbcTemplate
			try {
				this.jdbcTemplate = this.dataSourceConfig.jdbcTemplate(this.configuration.get(ConfigurationEnum.DB_DRIVER_CLASS.toString()), 
																	   this.configuration.get(ConfigurationEnum.DB_URL.toString()), 
																	   this.configuration.get(ConfigurationEnum.DB_USER.toString()), 
																	   this.configuration.get(ConfigurationEnum.DB_PASSWD.toString()));
			} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		
		//Checks if the repository is null
		if(this.sensorDataRepository == null & this.jdbcTemplate != null) {
			
			//We create the new repository with the jdbcTemplate
			this.sensorDataRepository = new SensorDataRepository(this.jdbcTemplate);
		}
		
		//Checks if the sensor data repository is not null
		if(this.sensorDataRepository != null && this.sensorData.size() > 0) {
			
			//Inserts all the sensor data in json format
			this.sensorData.forEach(data ->{
				
				String jsonSensorData;
				try {
					jsonSensorData = mapper.writeValueAsString(data);
					this.sensorDataRepository.save(new SensorData(jsonSensorData));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
		}	
		
		//Clears all the sensor data
		this.sensorData.clear();
		
	}

}
