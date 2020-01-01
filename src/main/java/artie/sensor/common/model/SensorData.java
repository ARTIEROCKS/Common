package artie.sensor.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class SensorData {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	@Lob
	private String data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	/**
	 * Default Constructor
	 */
	public SensorData() {}
	
	/**
	 * Parameterized constructor
	 * @param id
	 * @param data
	 */
	public SensorData(Long id, String data) {
		this.id = id;
		this.data = data;
	}
	
	/**
	 * Parameterized constructor
	 * @param data
	 */
	public SensorData(String data) {
		this.data = data;
	}
	
}
