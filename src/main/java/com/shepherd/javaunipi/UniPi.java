package com.shepherd.javaunipi;

import java.io.IOException;

/**
 *
 * @author Joe
 */
public interface UniPi {

    public String[][] getValue(UniPart device, Integer circuit) throws IOException;

    public String[][] getValue(UniPart device, String circuit) throws IOException;

    public int getIntValue(UniPart device, String circuit) throws IOException;

    public int getIntValue(UniPart device, Integer circuit) throws IOException;

    public double getDoubleValue(UniPart device, Integer circuit) throws IOException;

    public double getDoubleValue(UniPart device, String circuit) throws IOException;

    public String[][] get(UniPart device, Integer circuit) throws IOException;

    public String[][] get(UniPart device, String circuit) throws IOException;

    public String[][] getAll() throws IOException;

    public String[][] get(UniPart device, String circuit, String property) throws IOException;

    public String[][] get(UniPart device, Integer circuit, String property) throws IOException;

    public void set(UniPart device, Integer circuit, String property, Double value) throws IOException;

    public void set(UniPart device, String circuit, String property, Double value) throws IOException;

    public void set(UniPart device, Integer circuit, String property, Integer value) throws IOException;

    public void set(UniPart device, String circuit, String property, Integer value) throws IOException;

    public void set(UniPart device, String circuit, UniProperty prop) throws IOException;

    public void setValue(UniPart device, Integer circuit, Integer value) throws Exception;

    public void setValue(UniPart device, String circuit, Integer value) throws Exception;

    public void setValue(UniPart device, String circuit, Double value) throws Exception;

    public void setValue(UniPart device, Integer circuit, Double value) throws Exception;

    public void setValue(UniPart device, Integer circuit, String value) throws Exception;

    public void setValue(UniPart device, String circuit, String value) throws Exception;

    public void set(UniPart device, String circuit, String property, String value) throws Exception;

}
