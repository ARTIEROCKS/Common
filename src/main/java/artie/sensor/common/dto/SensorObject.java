package artie.sensor.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import artie.sensor.common.enums.SensorObjectTypeEnum;

@Component
@Data
@NoArgsConstructor
public class SensorObject {
	//Attributes
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy, hh:mm:ss 'UTC'")
	protected Date date;
	protected long milliseconds;
	protected Object data;
	protected SensorObjectTypeEnum sensorObjectType;
	protected String sensorName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy, hh:mm:ss 'UTC'")
	protected Date fromDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy, hh:mm:ss 'UTC'")
	protected Date toDate;
	
	//Properties
	public void setDate(Date date){
		this.date = date;
		this.milliseconds = this.date.getTime();
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
