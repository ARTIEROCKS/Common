package artie.sensor.common.dto;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class SensorObject {
	//Attributes
	protected Date date;
	protected long milliseconds;
	protected Object data;
	
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
	}
}
