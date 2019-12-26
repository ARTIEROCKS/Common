package artie.sensor.common.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import artie.sensor.common.dto.SensorObject;
import artie.sensor.common.enums.ConfigurationEnum;
import artie.sensor.common.interfaces.ArtieClientSensor;



@Service
public abstract class ArtieClientSensorImpl implements ArtieClientSensor {
	
	//Attributes
	protected String name;
	protected String version;
	protected String author;
	protected List<SensorObject> sensorData = new ArrayList<SensorObject>();
	protected Map<String, String> configuration = new HashMap<String, String>();
	
	
	private JmsTemplate jmsMessagingTemplate;
	
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
		this.configuration.putIfAbsent(ConfigurationEnum.MQ_SERVER_ACTIVE.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.MQ_SERVER.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.MQ_QUEUE.toString(), "");
	}
	
	/**
	 * Parameterized constructor
	 * @param configuration
	 */
	public ArtieClientSensorImpl(Map<String, String> configuration){
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_REGISTRATION.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_FILENAME.toString(), "ARTIE_Sensor.log");
		this.configuration.putIfAbsent(ConfigurationEnum.MQ_SERVER_ACTIVE.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.MQ_SERVER.toString(), "");
		this.configuration.putIfAbsent(ConfigurationEnum.MQ_QUEUE.toString(), "");
		this.configuration.putAll(configuration);
	}
	
	/**
	 * Function that provides the JMSMessaging template
	 * @param brokerUrl
	 * @return
	 */
	private JmsTemplate getJmsMessagingTemplate(String brokerUrl){
		
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(activeMQConnectionFactory);
		
		return new JmsTemplate(cachingConnectionFactory);
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
	 * Function to send the data to a Broker server
	 */
	public void sendSensorData(){
		
		//Getting the mq configuration
		boolean mqServerActive = Boolean.parseBoolean(this.configuration.get(ConfigurationEnum.MQ_SERVER_ACTIVE.toString()));
		String mqServer = this.configuration.get(ConfigurationEnum.MQ_SERVER.toString());
		String mqQueue = this.configuration.get(ConfigurationEnum.MQ_QUEUE.toString());
		
		if(mqServerActive && !mqServer.isEmpty() && !mqQueue.isEmpty() && this.sensorData.size() > 0){
			
			//Java Object to JSON
			ObjectMapper objectMapper = new ObjectMapper();
			
			//Sets the messaging template if the template is null
			if(this.jmsMessagingTemplate == null){
				this.jmsMessagingTemplate = this.getJmsMessagingTemplate(mqServer);
			}
			
			//Sending all the data to the broker
			this.sensorData.forEach(sensorObject->{		
				try {
					String message = objectMapper.writeValueAsString(sensorObject);
					this.jmsMessagingTemplate.convertAndSend(mqQueue, message);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});
			
			//Cleaning the sensor data
			this.sensorData.clear();
			
		}
				
	}

}
