/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shepherd.javaunipi;

/**
 *
 * @author Joe
 * TODO - Use some kind of mocking framework
 */
public class UniPiMock implements UniPiHttp{

    @Override
    public void sendPost(String url, String params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getData(String url) throws Exception {
        if (url.contains("/rest/sensor/28020DA908000083")) {
            return "{\"interval\": 15, \"value\": 33.8, \"circuit\": \"28020DA908000083\", \"address\": \"28020DA908000083\", \"time\": 1497022433.141432, \"typ\": \"DS18B20\", \"lost\": false, \"dev\": \"temp\"}";
        }
        
        if (url.contains("/rest/di/1_01")) {
            return "{\"circuit\": \"1_01\", \"debounce\": 50, \"counter\": 0, \"value\": 0, \"dev\": \"input\", \"counter_mode\": \"disabled\"}";
        }
        
        if (url.contains("/rest/ai/1_01")) {
            return "{\"value\": 4.195585864388563, \"circuit\": \"1_01\", \"dev\": \"ai\"}";
        }
        return null;
    }
    
}
