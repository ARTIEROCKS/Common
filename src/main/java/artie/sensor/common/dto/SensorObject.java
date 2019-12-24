package artie.sensor.common.dto;

import java.util.Date;

import org.springframework.stereotype.Component;

import artie.sensor.common.enums.SensorObjectTypeEnum;

@Component
public class SensorObject {
	//Attributes
	protected Date date;
	protected long milliseconds;
	protected Object data;
	protected SensorObjectTypeEnum sensorObjectType;
	
	//Properties
	public Date getDate(){
		return this.date;
	}
	public void setDate(Date date){
		this.date = date;
		this.milliseconds = this.date.getTime();
	}
	public Object getData(){
		return this.data;
	}
	public void setData(Object data){
		this.data = data;
	}
	public long getMilliseconds(){
		return this.milliseconds;
	}
	
	public SensorObjectTypeEnum getSensorObjectType(){
		return this.sensorObjectType;
	}
	public void setSensorObjectType(SensorObjectTypeEnum sensorObjectType){
		this.sensorObjectType = sensorObjectType;
	}
	
	/**
	 * Default constructor
	 */
	public SensorObject(){
	}
	
	/**
	 * Parameterized constructor
	 * @param date
	 * @param data
	 */
	public SensorObject(Date date, Object data){
		this.date = date;
		this.milliseconds = this.date.getTime();
		this.data = data;
		this.sensorObjectType = SensorObjectTypeEnum.STRING;
	}
	
	/**
	 * Parameterized constructor
	 * @param date
	 * @param data
	 * @param sensorObjectType
	 */
	public SensorObject(Date date, Object data, SensorObjectTypeEnum sensorObjectType){
		this.date = date;
		this.milliseconds = this.date.getTime();
		this.data = data;
		this.sensorObjectType = sensorObjectType;
	}
}
