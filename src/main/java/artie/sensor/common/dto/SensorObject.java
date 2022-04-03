package artie.sensor.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import artie.sensor.common.enums.SensorObjectTypeEnum;

@Component
public class SensorObject {
	//Attributes
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy, hh:mm:ss")
	protected Date date;
	protected long milliseconds;
	protected Object data;
	protected SensorObjectTypeEnum sensorObjectType;
	protected String sensorName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy, hh:mm:ss")
	protected Date fromDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy, hh:mm:ss")
	protected Date toDate;
	
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
	public void setMilliseconds(long milliseconds) {
		this.milliseconds = milliseconds;
	}
	
	public SensorObjectTypeEnum getSensorObjectType(){
		return this.sensorObjectType;
	}
	public void setSensorObjectType(SensorObjectTypeEnum sensorObjectType){
		this.sensorObjectType = sensorObjectType;
	}
	
	public String getSensorName(){
		return this.sensorName;
	}
	public void setSensorName(String sensorName){
		this.sensorName = sensorName;
	}

	public Date getFromDate(){return this.fromDate;}
	public void setFromDate(Date fromDate){this.fromDate = fromDate;}

	public Date getToDate(){return this.toDate;}
	public void setToDate(Date toDate){this.toDate = toDate;}

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
	public SensorObject(Date date, Object data, String sensorName){
		this.date = date;
		this.milliseconds = this.date.getTime();
		this.sensorObjectType = SensorObjectTypeEnum.STRING;
		this.sensorName = sensorName;
		this.data = data;
		
	}
	
	/**
	 * Parameterized constructor
	 * @param date
	 * @param data
	 * @param sensorObjectType
	 */
	public SensorObject(Date date, Object data, SensorObjectTypeEnum sensorObjectType, String sensorName){
		this.date = date;
		this.milliseconds = this.date.getTime();
		this.sensorObjectType = sensorObjectType;
		this.sensorName = sensorName;
		this.data = data;
	}

	/**
	 * Parameterized constructor
	 * @param date
	 * @param data
	 * @param sensorObjectType
	 * @param sensorName
	 * @param fromDate
	 * @param toDate
	 */
	public SensorObject(Date date, Object data, SensorObjectTypeEnum sensorObjectType, String sensorName, Date fromDate, Date toDate){
		this.date = date;
		this.milliseconds = this.date.getTime();
		this.sensorObjectType = sensorObjectType;
		this.sensorName = sensorName;
		this.data = data;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
}
