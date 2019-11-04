package artie.sensor.common.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerator;
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
	
	//Properties
	public List<SensorObject> getSensorData(){
		return this.sensorData;
	}
	private void setSensorData(List<SensorObject> sensorData){
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
	 * Start function
	 */
	public void start(){
	}
	
	/**
	 * Stop function
	 */
	public void stop(){
	}
	
	/**
	 * Default Constructor
	 */
	public ArtieClientSensorImpl(){
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_REGISTRATION.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_FILENAME.toString(), "ARTIE_Sensor.log");
	}
	
	/**
	 * Parameterized constructor
	 * @param configuration
	 */
	public ArtieClientSensorImpl(Map<String, String> configuration){
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_REGISTRATION.toString(), "false");
		this.configuration.putIfAbsent(ConfigurationEnum.SENSOR_FILE_FILENAME.toString(), "ARTIE_Sensor.log");
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
	 * Function to write al the data into the file
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

}
