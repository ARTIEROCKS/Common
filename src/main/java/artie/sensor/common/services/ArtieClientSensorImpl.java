package artie.sensor.common.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import artie.sensor.common.dto.SensorObject;
import artie.sensor.common.enums.ConfigurationEnum;
import artie.sensor.common.interfaces.ArtieClientSensor;



@Service
public abstract class ArtieClientSensorImpl implements ArtieClientSensor {

	@Autowired
	private ApplicationContext applicationContext;
	
	//Attributes
	protected String name;
	protected String version;
	protected String author;
	protected List<SensorObject> sensorData = new ArrayList<SensorObject>();
	protected Map<String, String> configuration = new HashMap<String, String>();
	
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
	
	
	/**
	 * Default Constructor
	 */
	public ArtieClientSensorImpl(){
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_REGISTRATION.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_FILENAME.toString(), "ARTIE_Sensor.log");
		this.configuration.putIfAbsent(ConfigurationEnum.KAFKA_SERVER_ACTIVE.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.KAFKA_SERVER.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.KAFKA_TOPIC.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.KAFKA_KEY.toString(), "");
	}
	
	/**
	 * Parameterized constructor
	 * @param configuration
	 */
	public ArtieClientSensorImpl(Map<String, String> configuration){
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_REGISTRATION.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_FILENAME.toString(), "ARTIE_Sensor.log");
		this.configuration.putIfAbsent(ConfigurationEnum.KAFKA_SERVER_ACTIVE.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.KAFKA_SERVER.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.KAFKA_TOPIC.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.KAFKA_KEY.toString(),"");
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
	 * Function to send the data to Kafka server
	 */
	public void sendSensorData(){
		
		//Getting the kafka configuration
		boolean kafkaServerActive = Boolean.parseBoolean(this.configuration.get(ConfigurationEnum.KAFKA_SERVER_ACTIVE.toString()));
		String kafkaServer = this.configuration.get(ConfigurationEnum.KAFKA_SERVER.toString());
		String kafkaTopic = this.configuration.get(ConfigurationEnum.KAFKA_TOPIC.toString());
		String kafkaKey = this.configuration.get(ConfigurationEnum.KAFKA_TOPIC.toString());
		
		if(kafkaServerActive && !kafkaServer.isEmpty() && !kafkaTopic.isEmpty() && this.sensorData.size() > 0){
			
			@SuppressWarnings("unchecked")
			KafkaTemplate<String, String> kafkaTemplate = (KafkaTemplate<String, String>) applicationContext.getBean("kafkaTemplate", this.configuration);
			
			//Java Object to JSON
			ObjectMapper objectMapper = new ObjectMapper();
			
			//Sending all the data to kafka
			this.sensorData.forEach(sensorObject->{		
				try {
					String message = objectMapper.writeValueAsString(sensorObject);
					kafkaTemplate.send(kafkaTopic, kafkaKey, message);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});
			
			//Cleaning the sensor data
			this.sensorData.clear();
			
		}
				
	}

}
