package com.shepherd.javaunipi;

import java.io.IOException;

public class UniPi {

    public static final int HTTP = 0;
    public static final int HTTPS = 1;

    private final String hostname;
    private int port = 80;
    private int protocol = HTTP;
    private final UniPiHttp http;

    public UniPi(String hostname, int port, int protocol, UniPiHttp http) {
        this.hostname = hostname;
        this.port = port;
        this.protocol = protocol;
        this.http = http;
    }

    public UniPi(String hostname, int port, UniPiHttp http) {
        this.hostname = hostname;
        this.port = port;
        this.http = http;
    }
    
    public UniPi (String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.http = new UniPiJavaxHttp(protocol);
    }

    public UniPi(String hostname) {
        this.hostname = hostname;
        this.http = new UniPiJavaxHttp(protocol);
    }

    /**
     * Return value as String array from selected device and circuit
     *
     * @param device Target device (Relay, etc...)
     * @param circuit Number of device
     * @return current value of selected component
     * @throws IOException
     */
    public String[][] getValue(UniPart device, Integer circuit) throws IOException, Exception {
        return getValue(device, circuit.toString());
    }

    /**
     * Return value as String array from selected device and circuit
     *
     * @param device Target device (Relay, etc...)
     * @param circuit Number of device
     * @return current value of selected component
     * @throws IOException
     */
    public String[][] getValue(UniPart device, String circuit) throws IOException, Exception {
        return get(device, circuit, "value");
    }

    /**
     * Return value as int from selected device and circuit
     *
     * @param device device Target device (Relay, etc...)
     * @param circuit Number of device
     * @return current value of selected component
     * @throws IOException
     */
    public int getIntValue(UniPart device, String circuit) throws IOException, Exception {
        String[][] returnValue = getValue(device, circuit.toString());
        int returnInteger = Integer.parseInt(returnValue[0][1]);
        return returnInteger;
    }

    /**
     * Return value as int from selected device and circuit
     *
     * @param device device Target device (Relay, etc...)
     * @param circuit Number of device
     * @return current value of selected component
     * @throws IOException
     */
    public int getIntValue(UniPart device, Integer circuit) throws IOException, Exception {
        return getIntValue(device, circuit.toString());
    }

    /**
     * Return value as double from selected device and circuit
     *
     * @param device device Target device (Relay, etc...)
     * @param circuit number of device
     * @return current value of selected component
     * @throws IOException
     */
    public double getDoubleValue(UniPart device, Integer circuit) throws IOException, Exception {
        return getDoubleValue(device, circuit.toString());
    }

    /**
     * Return value as double from selected device and circuit
     *
     * @param device device Target device (Relay, etc...)
     * @param circuit number of device
     * @return current value of selected component
     * @throws IOException
     */
    public double getDoubleValue(UniPart device, String circuit) throws IOException, Exception {
        String[][] returnValue = getValue(device, circuit);
        double returnDouble = Double.parseDouble(returnValue[0][1]);
        return returnDouble;
    }

    /**
     * Returns all properties of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @return all properties of selected device and circuit
     * @throws IOException
     */
    public String[][] get(UniPart device, Integer circuit) throws IOException, Exception {
        return get(device, circuit.toString());
    }

    /**
     * Returns all properties of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @return all properties of selected device and circuit
     * @throws IOException
     */
    public String[][] get(UniPart device, String circuit) throws IOException, Exception {
        return get(device, circuit, "ALL");
    }

    /**
     * Returns all information of all configured devices as String array's
     *
     * @return all information of all configured devices
     * @throws IOException
     */
    public String[][] getAll() throws IOException, Exception {
        String returnedData = http.getData("://" + hostname + ":" + port + "/rest/all");
        returnedData = returnedData.substring(1, returnedData.length() - 2);
        returnedData = returnedData.replaceAll("\\{", "");
        returnedData = returnedData.replaceAll("\"", "");
        returnedData = returnedData.replaceAll(":", ",");
        returnedData = returnedData.replaceAll(" ", "");
        String[] predataArray = returnedData.split("},");
        String[][] dataArray = new String[predataArray.length][];
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = predataArray[i].split(",");
        }
        return dataArray;
    }

    /**
     * Returns selected property of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number or name of device
     * @param property property to find value of (mainly located on [0][1])
     * @return selected property of selected device and circuit
     * @throws IOException
     */
    public String[][] get(UniPart device, String circuit, String property) throws IOException, Exception {
        if (device == UniPart.ALL) {
            return getAll();
        }
        String deviceName = null;
        switch (device) {
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
        if ((property.length() > 0) && (!property.contentEquals("ALL"))) {
            returnedData = http.getData("://" + hostname + ":" + port + "/rest/" + deviceName + "/" + circuit + "/" + property);
        } else {
            returnedData = http.getData("://" + hostname + ":" + port + "/rest/" + deviceName + "/" + circuit);
        }
        returnedData = returnedData.substring(1, returnedData.length() - 1);
        returnedData = returnedData.replaceAll("\\{", "");
        returnedData = returnedData.replaceAll("\"", "");
        returnedData = returnedData.replaceAll(":", ",");
        returnedData = returnedData.replaceAll(" ", "");
        String[] predataArray = returnedData.split("},");
        String[][] dataArray = new String[predataArray.length][];
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = predataArray[i].split(",");
        }
        return dataArray;
    }

    /**
     * Returns selected property of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param property property to find value of (mainly located on [0][1])
     * @return selected property of selected device and circuit
     * @throws IOException
     */
    public String[][] get(UniPart device, Integer circuit, String property) throws IOException, Exception {
        return get(device, circuit.toString(), property);
    }

    /**
     * Sets property of selected device and circuit to selected value
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param property property to set
     * @param value value to set
     * @throws IOException
     */
    public void set(UniPart device, Integer circuit, String property, Double value) throws IOException, Exception {
        set(device, circuit.toString(), property, value.toString());
    }

    /**
     * Sets property of selected device and circuit to selected value
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param property property to set
     * @param value value to set
     * @throws IOException
     */
    public void set(UniPart device, String circuit, String property, Double value) throws IOException, Exception {
        set(device, circuit, property, value.toString());
    }

    /**
     * Sets property of selected device and circuit to selected value
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param property property to set
     * @param value value to set
     * @throws IOException
     */
    public void set(UniPart device, Integer circuit, String property, Integer value) throws IOException, Exception {
        set(device, circuit.toString(), property, value.toString());
    }

    /**
     * Sets property of selected device and circuit to selected value
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param property property to set
     * @param value value to set
     * @throws IOException
     */
    public void set(UniPart device, String circuit, String property, Integer value) throws IOException, Exception {
        set(device, circuit, property, value.toString());
    }

    /**
     * Sets property of selected device and circuit to selected value
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param prop UniPiProperty to set
     * @throws IOException
     */
    public void set(UniPart device, String circuit, UniProperty prop) throws IOException, Exception {
        set(device, circuit, prop.getPropertyName(), prop.getValue());
    }

    /**
     * Sets value of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param value value to set
     * @throws IOException
     */
    public void setValue(UniPart device, Integer circuit, Integer value) throws IOException, Exception {
        set(device, circuit.toString(), "value", value.toString());
    }

    /**
     * Sets value of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param value value to set
     * @throws IOException
     */
    public void setValue(UniPart device, String circuit, Integer value) throws IOException, Exception {
        set(device, circuit, "value", value.toString());
    }

    /**
     * Sets value of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param value value to set
     * @throws IOException
     */
    public void setValue(UniPart device, String circuit, Double value) throws IOException, Exception {
        set(device, circuit, "value", value.toString());
    }

    /**
     * Sets value of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param value value to set
     * @throws IOException
     */
    public void setValue(UniPart device, Integer circuit, Double value) throws IOException, Exception {
        set(device, circuit.toString(), "value", value.toString());
    }

    /**
     * Sets value of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param value value to set
     * @throws IOException
     */
    public void setValue(UniPart device, Integer circuit, String value) throws IOException, Exception {
        set(device, circuit.toString(), "value", value);
    }

    /**
     * Sets value of selected device and circuit
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param value value to set
     * @throws IOException
     */
    public void setValue(UniPart device, String circuit, String value) throws IOException, Exception {
        set(device, circuit, "value", value);
    }

    /**
     * Sets property of selected device and circuit to selected value
     *
     * @param device device device Target device (Relay, etc...)
     * @param circuit number of device
     * @param property property to set
     * @param value value to set
     * @throws IOException
     */
    public void set(UniPart device, String circuit, String property, String value) throws IOException, Exception {
        String deviceName = null;
        switch (device) {
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
        http.sendPost("://" + hostname + ":" + port + "/rest/" + deviceName + "/" + circuit, property + "=" + value);
    }

    /**
     * Parse dev name to UniPart
     *
     * @param dev
     * @return
     */
    public static UniPart parseUniPart(String dev) {
        if (dev.contentEquals("relay")) {
            return UniPart.RELAY;
        }
        if (dev.contentEquals("sensor")) {
            return UniPart.SENSOR;
        }
        if ((dev.contentEquals("di")) || (dev.contentEquals("input"))) {
            return UniPart.DIGITAL_INPUT;
        }
        if ((dev.contentEquals("ai")) || (dev.contentEquals("analoginput"))) {
            return UniPart.ANALOG_INPUT;
        }
        if ((dev.contentEquals("ao")) || (dev.contentEquals("analogoutput"))) {
            return UniPart.ANALOG_OUTPUT;
        }
        return UniPart.ALL;
    }
}
