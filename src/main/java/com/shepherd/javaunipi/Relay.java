package com.shepherd.javaunipi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Relay {
	private final UniPart device = UniPart.RELAY;
	private int circuit;
	private UniPi unipi;
	
	private List<PropertyChangeListener> _listeners = new ArrayList<PropertyChangeListener>();
	private List<Timer> _timers = new ArrayList<Timer>();
	
	/**
	 * Part of the UniPiAPI
	 * @param circuit number of relay
	 * @param unipi instance of UniPiAPI to send data
	 */
	public Relay(UniPi unipi, int circuit){
		this.circuit = circuit;
		this.unipi = unipi;
	}
	
	/**
	 * Add listener for changing value property witch watch for change every 500 milliseconds
	 * @param listener listener for notify
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener) throws IOException{
		addListener(listener, "value", 500);
	}
	
	/**
	 * Add listener for changing value property
	 * @param listener listener for notify
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, int millis) throws IOException{
		addListener(listener, "value", millis);
	}
	
	/**
	 * Add listener for changing value of some property witch watch for change every 500 milliseconds
	 * @param listener listener for notify
	 * @param property property to watch value for
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, String property) throws IOException{
		addListener(listener, property, 500);
	}
	
	/**
	 * Add listener for changing value of some property witch watch for change every 500 milliseconds
	 * @param listener listener for notify
	 * @param property property to watch value for
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, UniProperty property) throws IOException{
		addListener(listener, property.getPropertyName(), 500);
	}
	
	/**
	 * Add listener for changing value of some property
	 * @param listener listener for notify
	 * @param property property to watch value for
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, UniProperty property, int millis) throws IOException{
		addListener(listener, property.getPropertyName(), millis);
	}
	
	/**
	 * Add listener for changing value of some property
	 * @param listener listener for notify
	 * @param property property to watch value for
	 * @param milis how often look for change
	 * @throws IOException
	 */
	public void addListener(PropertyChangeListener listener, final String property, int millis) throws IOException{
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
				} catch (IOException e) {}
			}
			
			private boolean isPropertySame() throws IOException{
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
	 * Sets on or off relay
	 * @param on true = on, false = off
	 * @throws IOException
	 */
	public void setOn(boolean on) throws Exception{
		int value = 1;
		if(!on)
			value = 0;
		unipi.setValue(device, circuit, value);
	}
	
	/**
	 * Sets on or off relay
	 * @param on 0 = off, else = on
	 * @throws IOException
	 */
	public void setValue(int on) throws Exception{
		if(on == 0)
			setOn(false);
		else
			setOn(true);
	}
	
	/**
	 * Sets on or off relay
	 * @param on 0 = off, else = on
	 * @throws IOException
	 */
	public void setValue(char on) throws Exception{
		if(on == 0)
			setOn(false);
		else
			setOn(true);
	}
	
	/**
	 * Get current status of relay
	 * @return 1 or 0 if relay is on or off
	 * @throws IOException
	 */
	public int getValue() throws IOException{
		return unipi.getIntValue(device, circuit);
	}
	
	/**
	 * Get if relay is on
	 * @return boolean status of relay
	 * @throws IOException
	 */
	public boolean isOn() throws IOException{
		int value = getValue();
		if(value == 1)
			return true;
		return false;
	}
	
	/**
	 * Get circuit number of this part
	 * @return circuit number
	 */
	public int getCircuit(){
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
		String[][] data = unipi.get(device, circuit, "dev");
		return data[0][1];
	}
	
	/**
	 * Get pending status of relay
	 * @return pending status of relay
	 * @throws IOException
	 */
	public boolean getPending() throws IOException{
		String[][] data = unipi.get(device, circuit, "pending");
		return Boolean.parseBoolean(data[0][1]);
	}
	
	/**
	 * Toggle status of relay
	 * @throws IOException 
	 */
	public void toggle() throws Exception{
		setOn(!isOn());
	}
	
	/**
	 * Get all data about this part in string array
	 * @return all data about this part
	 * @throws IOException
	 */
	public String[][] getAllData() throws IOException{
		return unipi.get(device, circuit);
	}
}
