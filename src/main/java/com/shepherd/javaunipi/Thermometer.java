package com.shepherd.javaunipi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Thermometer {
	private final UniPart device = UniPart.SENSOR;
	private static final String dev = "temp";
	private String circuit;
	private UniPi unipi;
	
	private List<PropertyChangeListener> _listeners = new ArrayList<PropertyChangeListener>();
	private List<Timer> _timers = new ArrayList<Timer>();
	
	/**
	 * Part of the UniPiAPI
	 * @param circuit name of sensor
	 * @param unipi instance of UniPiAPI to send data
	 */
	public Thermometer(UniPi unipi, String circuit){
		this.circuit = circuit;
		this.unipi = unipi;
	}
	
	/**
	 * Part of the UniPiAPI
 It will try to find out instance of dev temp
	 * @param circuit name of sensor
	 * @param unipi instance of UniPiAPI to send data
	 * @throws IOException 
	 */
	public Thermometer(UniPi unipi) throws Exception{
		this.unipi = unipi;
	
		String[][] dataArray = unipi.getAll();
		boolean rightDevFinded = false;
		String deviceCircuit = null;
		for(int i = 0; i < dataArray.length; i++){
			deviceCircuit = null;
			for(int k = 0; k < dataArray[i].length; k ++){
				if(dataArray[i][k].contentEquals("dev")){
					if(dataArray[i][k+1].contentEquals(dev)){
						k++;
						rightDevFinded = true;
					}
				}
				else if(dataArray[i][k].contentEquals("circuit")){
					deviceCircuit = dataArray[i][k+1];
					k++;
				}
				if(rightDevFinded && (deviceCircuit != null)){
					this.circuit = deviceCircuit;
					return;
				}
			}
		}
	}
	
	/**
	 * Add listener for changing value property witch watch for change every 500 milliseconds
	 * @param listener listener for notify
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener) throws Exception{
		addListener(listener, "value", 500);
	}
	
	/**
	 * Add listener for changing value property
	 * @param listener listener for notify
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, int millis) throws Exception{
		addListener(listener, "value", millis);
	}
	
	/**
	 * Add listener for changing value of some property witch watch for change every 500 milliseconds
	 * @param listener listener for notify
	 * @param property property to watch value for
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, String property) throws Exception{
		addListener(listener, property, 500);
	}
	
	/**
	 * Add listener for changing value of some property witch watch for change every 500 milliseconds
	 * @param listener listener for notify
	 * @param property property to watch value for
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, UniProperty property) throws Exception{
		addListener(listener, property.getPropertyName(), 500);
	}
	
	/**
	 * Add listener for changing value of some property
	 * @param listener listener for notify
	 * @param property property to watch value for
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, UniProperty property, int millis) throws Exception{
		addListener(listener, property.getPropertyName(), millis);
	}
	
	/**
	 * Add listener for changing value of some property
	 * @param listener listener for notify
	 * @param property property to watch value for
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, final String property, int millis) throws Exception{
		String currentPropertyValue = unipi.get(device, circuit, property)[0][1];
		_timers.add(new Timer());
		_timers.get(_timers.size() - 1).schedule(new UniWatchTimerTask(_timers.size() - 1, new UniProperty(property, currentPropertyValue)){
			@Override
			public void run() {
				try {
					int index = getIndex();
					if(!isPropertySame()){
						_listeners.get(index).valueChanged(getProperty());
					}
				} catch (Exception ex) {
                                Logger.getLogger(Thermometer.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
			
			private boolean isPropertySame() throws Exception{
				String currentPropertyValue = unipi.get(device, circuit, property)[0][1];
				if(currentPropertyValue.contentEquals(getProperty().getValue()))
					return true;
				getProperty().setValue(currentPropertyValue);
				return false;
			}
			
		}, 0, millis);
		_listeners.add(listener);
	}
	
	/**
	 * Remove already set listener
	 * @param listener listener to remove
	 */
	public synchronized void removeListener(PropertyChangeListener listener){
		if(_listeners.indexOf(listener) == -1)
			return;
		_timers.remove(_listeners.indexOf(listener));
	    _listeners.remove(listener);
	 }
	
	/**
	 * Get current interval of thermometer
	 * @return
	 * @throws IOException
	 */
	public double getInterval() throws Exception{
		String[][] data = unipi.get(device, circuit, "interval");
		return Double.parseDouble(data[0][1]);
	}
	
	/**
	 * Get current temperature
	 * @return current temperature
	 * @throws IOException
	 */
	public Double getValue() throws Exception{
		return unipi.getDoubleValue(device, circuit);
	}
	
	/**
	 * Get if sensor is on
	 * @return boolean status of sensor
	 * @throws IOException
	 */
	public boolean isLost() throws Exception{
		String value = unipi.get(device, circuit, "lost")[0][1];
		if(value.contentEquals("true"))
			return true;
		return false;
	}
	
	/**
	 * Get circuit number of this part
	 * @return circuit number
	 * @throws IOException 
	 */
	public String getAddress() throws Exception{
		return unipi.get(device, circuit, "address")[0][1];
	}
	
	/**
	 * Get circuit number of this part
	 * @return circuit number
	 */
	public String getCircuit(){
		return circuit;
	}
	
	/**
	 * Get device name of this part
	 * @return device number
	 */
	public UniPart getDevice(){
		return device;
	}
	
	/**
	 * Not same as getDevice. Get device returns UniPiart, this method return dev string
	 * You can see difference in UniPart.SENSOR: getDevice() returns SENSOR, but this method returns for example "temp" as thermometer
	 * @return Dev String from UniPiAPI
	 * @throws IOException
	 */
	public String getDev() throws IOException{
		return dev;
	}
	
	/**
	 * Get time value of sensor
	 * @return time status of sensor
	 * @throws IOException
	 */
	public Double getTime() throws Exception{
		String[][] data = unipi.get(device, circuit, "time");
		return Double.parseDouble(data[0][1]);
	}
	
	/**
	 * Get all data about this part in string array
	 * @return all data about this part
	 * @throws IOException
	 */
	public String[][] getAllData() throws Exception{
		return unipi.get(device, circuit);
	}
}
