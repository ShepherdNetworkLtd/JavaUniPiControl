
package com.shepherd.javaunipi;


/**
 *
 * @author Joe
 * //TODO can use proxy-common HttpUtils
 */
public interface UniPiHttp {
    
    public void sendPost(String url, String params) throws Exception;
    
    public String getData(String url) throws Exception;
    
}
