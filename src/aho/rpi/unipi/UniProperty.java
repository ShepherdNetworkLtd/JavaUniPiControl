package aho.rpi.unipi;

public class UniProperty {
	private String propertyName;
	private String value;
	
	/**
	 * Create UniPiProperty 
	 * @param propertyName name of property
	 * @param value value of property
	 */
	public UniProperty(String propertyName, String value){
		this.propertyName = propertyName;
		this.value = value;
	}
	
	/**
	 * Create UniPiProperty 
	 * @param propertyName name of property
	 * @param value value of property
	 */
	public UniProperty(String propertyName, Integer value){
		this.propertyName = propertyName;
		this.value = value.toString();
	}
	
	/**
	 * Create UniPiProperty 
	 * @param propertyName name of property
	 * @param value value of property
	 */
	public UniProperty(String propertyName, Double value){
		this.propertyName = propertyName;
		this.value = value.toString();
	}
	
	/**
	 * Return name of property
	 * @return name of property
	 */
	public String getPropertyName(){
		return propertyName;
	}
	
	/**
	 * Returns value as String
	 * @return value as String
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * Returns value as integer
	 * @return value as integer
	 */
	public int getIntValue(){
		return Integer.parseInt(value);
	}
	
	/**
	 * Returns value as double
	 * @return value as double
	 */
	public double getDoubleValue(){
		return Double.parseDouble(value);
	}
	
	/**
	 * Return if device is on
	 * @return true if value is not zero
	 */
	public boolean isOn(){
		if(getIntValue() != 0)
			return true;
		return false;
	}
	
	/**
	 * Sets a new value
	 * @param value new value
	 */
	public void setValue(String value){
		this.value = value;
	}
	
	/**
	 * Sets a new value
	 * @param value new value
	 */
	public void setValue(Integer value){
		this.value = value.toString();
	}
	
	/**
	 * Sets a new value
	 * @param value new value
	 */
	public void setValue(Double value){
		this.value = value.toString();
	}
}
