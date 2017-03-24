package com.shepherd.javaunipi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UniPi {
	
	public static final int HTTP = 0;
	public static final int HTTPS = 1;
	
	private String hostname;
	private int port = 80;
	private int protocol = HTTP;
	
	public UniPi(String hostname, int port, int protocol){
		this.hostname = hostname;
		this.port = port;
		this.protocol = protocol;
	}
	
	public UniPi(String hostname, int port){
		this.hostname = hostname;
		this.port = port;
	}
	
	public UniPi(String hostname){
		this.hostname = hostname;
	}
	
	
	/**
	 * Return value as String array from selected device and circuit
	 * @param device Target device (Relay, etc...)
	 * @param circuit Number of device
	 * @return current value of selected component
	 * @throws IOException
	 */
	public String[][] getValue(UniPart device, Integer circuit) throws IOException{
		return getValue(device, circuit.toString());
	}
	
	/**
	 * Return value as String array from selected device and circuit
	 * @param device Target device (Relay, etc...)
	 * @param circuit Number of device
	 * @return current value of selected component
	 * @throws IOException
	 */
	public String[][] getValue(UniPart device, String circuit) throws IOException{
		return get(device, circuit, "value");
	}
	
	/**
	 * Return value as int from selected device and circuit
	 * @param device device Target device (Relay, etc...)
	 * @param circuit Number of device
	 * @return current value of selected component
	 * @throws IOException
	 */
	public int getIntValue(UniPart device, String circuit) throws IOException{
		String[][] returnValue = getValue(device, circuit.toString());
		int returnInteger = Integer.parseInt(returnValue[0][1]);
		return returnInteger;
	}
	
	/**
	 * Return value as int from selected device and circuit
	 * @param device device Target device (Relay, etc...)
	 * @param circuit Number of device
	 * @return current value of selected component
	 * @throws IOException
	 */
	public int getIntValue(UniPart device, Integer circuit) throws IOException{
		return getIntValue(device, circuit.toString());
	}
	
	/**
	 * Return value as double from selected device and circuit
	 * @param device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @return current value of selected component
	 * @throws IOException
	 */
	public double getDoubleValue(UniPart device, Integer circuit) throws IOException{
		return getDoubleValue(device, circuit.toString());
	}
	
	/**
	 * Return value as double from selected device and circuit
	 * @param device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @return current value of selected component
	 * @throws IOException
	 */
	public double getDoubleValue(UniPart device, String circuit) throws IOException{
		String[][] returnValue = getValue(device, circuit);
		double returnDouble = Double.parseDouble(returnValue[0][1]);
		return returnDouble;
	}
	
	/**
	 * Returns all properties of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @return all properties of selected device and circuit
	 * @throws IOException
	 */
	public String[][] get(UniPart device, Integer circuit) throws IOException{
		return get(device, circuit.toString());
	}
	
	/**
	 * Returns all properties of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @return all properties of selected device and circuit
	 * @throws IOException
	 */
	public String[][] get(UniPart device, String circuit) throws IOException{
		return get(device, circuit, "ALL");
	}
	
	/**
	 * Returns all information of all configured devices as String array's
	 * @return all information of all configured devices
	 * @throws IOException
	 */
	public String[][] getAll() throws IOException{
		String returnedData = getData("://" + hostname + ":" + port + "/rest/all");
		returnedData = returnedData.substring(1, returnedData.length() - 2);
		returnedData = returnedData.replaceAll("\\{", "");
		returnedData = returnedData.replaceAll("\"", "");
		returnedData = returnedData.replaceAll(":", ",");
		returnedData = returnedData.replaceAll(" ", "");
		String[] predataArray = returnedData.split("},");
		String[][] dataArray = new String[predataArray.length][];
		for(int i = 0; i < dataArray.length; i++)
			dataArray[i] = predataArray[i].split(",");
		return dataArray;
	}
	
	/**
	 * Returns selected property of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number or name of device
	 * @param property property to find value of (mainly located on [0][1])
	 * @return selected property of selected device and circuit
	 * @throws IOException
	 */
	public String[][] get(UniPart device, String circuit, String property) throws IOException{
		if(device == UniPart.ALL)
			return getAll();
		String deviceName = null;
		switch(device){
		case RELAY:
			deviceName = "relay";
			break;
		case DIGITAL_INPUT:
			deviceName = "di";
			break;
		case ANALOG_INPUT:
			deviceName = "ai";
			break;
		case ANALOG_OUTPUT:
			deviceName = "ao";
			break;
		case SENSOR:
			deviceName = "sensor";
			break;
		default:
			return null;
		}
		String returnedData = null;
		if((property.length() > 0) && (!property.contentEquals("ALL")))
			returnedData = getData("://" + hostname + ":" + port + "/rest/" + deviceName + "/" + circuit + "/" + property);
		else{
			returnedData = getData("://" + hostname + ":" + port + "/rest/" + deviceName + "/" + circuit);
		}
		returnedData = returnedData.substring(1, returnedData.length() - 1);
		returnedData = returnedData.replaceAll("\\{", "");
		returnedData = returnedData.replaceAll("\"", "");
		returnedData = returnedData.replaceAll(":", ",");
		returnedData = returnedData.replaceAll(" ", "");
		String[] predataArray = returnedData.split("},");
		String[][] dataArray = new String[predataArray.length][];
		for(int i = 0; i < dataArray.length; i++)
			dataArray[i] = predataArray[i].split(",");
		return dataArray;
	}
	
	/**
	 * Returns selected property of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param property property to find value of (mainly located on [0][1])
	 * @return selected property of selected device and circuit
	 * @throws IOException
	 */
	public String[][] get(UniPart device, Integer circuit, String property) throws IOException{
		return get(device, circuit.toString(), property);
	}
	
	
	/**
	 * Sets property of selected device and circuit to selected value
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param property property to set
	 * @param value value to set
	 * @throws IOException
	 */
	public void set(UniPart device, Integer circuit, String property, Double value) throws IOException{
		set(device, circuit.toString(), property, value.toString());
	}
	
	/**
	 * Sets property of selected device and circuit to selected value
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param property property to set
	 * @param value value to set
	 * @throws IOException
	 */
	public void set(UniPart device, String circuit, String property, Double value) throws IOException{
		set(device, circuit, property, value.toString());
	}
	
	/**
	 * Sets property of selected device and circuit to selected value
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param property property to set
	 * @param value value to set
	 * @throws IOException
	 */
	public void set(UniPart device, Integer circuit, String property, Integer value) throws IOException{
		set(device, circuit.toString(), property, value.toString());
	}
	
	/**
	 * Sets property of selected device and circuit to selected value
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param property property to set
	 * @param value value to set
	 * @throws IOException
	 */
	public void set(UniPart device, String circuit, String property, Integer value) throws IOException{
		set(device, circuit, property, value.toString());
	}
	
	/**
	 * Sets property of selected device and circuit to selected value
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param prop UniPiProperty to set
	 * @throws IOException
	 */
	public void set(UniPart device, String circuit, UniProperty prop) throws IOException{
		set(device, circuit, prop.getPropertyName(), prop.getValue());
	}
	
	/**
	 * Sets value of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param value value to set
	 * @throws IOException
	 */
	public void setValue(UniPart device, Integer circuit, Integer value) throws IOException{
		set(device, circuit.toString(), "value", value.toString());
	}
	
	/**
	 * Sets value of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param value value to set
	 * @throws IOException
	 */
	public void setValue(UniPart device, String circuit, Integer value) throws IOException{
		set(device, circuit, "value", value.toString());
	}
	
	/**
	 * Sets value of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param value value to set
	 * @throws IOException
	 */
	public void setValue(UniPart device, String circuit, Double value) throws IOException{
		set(device, circuit, "value", value.toString());
	}
	
	/**
	 * Sets value of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param value value to set
	 * @throws IOException
	 */
	public void setValue(UniPart device, Integer circuit, Double value) throws IOException{
		set(device, circuit.toString(), "value", value.toString());
	}
	
	/**
	 * Sets value of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param value value to set
	 * @throws IOException
	 */
	public void setValue(UniPart device, Integer circuit, String value) throws IOException{
		set(device, circuit.toString(), "value", value);
	}
	

	/**
	 * Sets value of selected device and circuit
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param value value to set
	 * @throws IOException
	 */
	public void setValue(UniPart device, String circuit, String value) throws IOException{
		set(device, circuit, "value", value);
	}
	
	/**
	 * Sets property of selected device and circuit to selected value
	 * @param device device device Target device (Relay, etc...)
	 * @param circuit number of device
	 * @param property property to set
	 * @param value value to set
	 * @throws IOException
	 */
	public void set(UniPart device, String circuit, String property, String value) throws IOException{
		String deviceName = null;
		switch(device){
		case RELAY:
			deviceName = "relay";
			break;
		case DIGITAL_INPUT:
			deviceName = "di";
			break;
		case ANALOG_INPUT:
			deviceName = "ai";
			break;
		case ANALOG_OUTPUT:
			deviceName = "ao";
			break;
		case SENSOR:
			deviceName = "sensor";
			break;
		default:
			return;
		}
		sendPost("://" + hostname + ":" + port + "/rest/" + deviceName + "/" + circuit.toString(), property + "=" + value);
	}
	
	/**
	 * Parse dev name to UniPart
	 * @param dev
	 * @return
	 */
	public static UniPart parseUniPart(String dev){
		if(dev.contentEquals("relay"))
			return UniPart.RELAY;
		if(dev.contentEquals("sensor"))
			return UniPart.SENSOR;
		if((dev.contentEquals("di")) || (dev.contentEquals("input")))
			return UniPart.DIGITAL_INPUT;
		if((dev.contentEquals("ai")) || (dev.contentEquals("analoginput")))
			return UniPart.ANALOG_INPUT;
		if((dev.contentEquals("ao")) || (dev.contentEquals("analogoutput")))
			return UniPart.ANALOG_OUTPUT;
		return UniPart.ALL;
	}
	
	/**
	 * Get data from RPi
	 * @param url URL to obtain data from
	 * @return data from RPi
	 * @throws IOException
	 */
	private String getData(String url) throws IOException{
		StringBuffer response = new StringBuffer();
		if(protocol == HTTPS){
			 url = "https" +  url;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				
			con.setRequestMethod("GET");
		 
			BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
			String inputLine;
		 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}
		else{
			url = "http" +  url;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("GET");
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}
 
		return response.toString();
	}
	
	/**
	 * Send data to RPi
	 * @param url target URL for desired action
	 * @param params parameters eg. properties and values to set
	 * @throws IOException
	 */
	private void sendPost(String url, String params) throws IOException{
		if(protocol == HTTPS){
			url = "https" + url;
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	 
			con.setRequestMethod("POST");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
	 
			con.getResponseCode();
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}else{
			url = "http" + url;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
	 
			con.getResponseCode();
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}
	}
}
