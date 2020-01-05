package artie.sensor.common.interfaces;

import java.util.List;
import java.util.Map;

import artie.sensor.common.dto.SensorObject;

public interface ArtieClientSensor {
	
	//Module properties
	public String getName();
	public String getVersion();
	public String getAuthor();
	
	//Control methods
	public boolean getIsAlive();
	public void start();
	public void stop();
	
	//Data function
	public List<SensorObject> getSensorData();
	public void sendSensorData();
	
	//Configuration
	public Map<String, String> getConfiguration();
	public void setConfiguration(Map<String, String> configuration);
	public void setConfiguration(String configuration);
}
