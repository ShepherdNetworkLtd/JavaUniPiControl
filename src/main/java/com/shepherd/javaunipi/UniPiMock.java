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
            return "{\"value\": 34.5}";
        }
        
        if (url.contains("/rest/di/1_01")) {
            return "{\"value\": 1}";
        }
        
        if (url.contains("/rest/ai/1_01")) {
            return "{\"value\": 4.203413449956452}";
        }
        return null;
    }
    
}
