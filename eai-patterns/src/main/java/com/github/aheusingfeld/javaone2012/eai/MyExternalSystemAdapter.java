package com.github.aheusingfeld.javaone2012.eai;

/**
 * This is just a dummy implementation to show how simple it is to create a custom implementation of an adapter for
 * Spring Integration.
 * @see
 */
public class MyExternalSystemAdapter
{
    /**
     * A simple method withoud any dependency on Spring Integration taking a String - which will be the message body.
     * @param data - the data to be send
     */
    public void sendData(String data)
    {
        // as it's a dummy implementation representing your custom code you can do whatever you like in here
        System.out.println(System.currentTimeMillis() + " - Data send: " + data);
    }

}
