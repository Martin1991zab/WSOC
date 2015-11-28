package de.keinBetreff.wsoc;

import javax.xml.ws.Endpoint;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	System.out.println("Start");
        Endpoint ep = Endpoint.publish("http://localhost:8080/services", new WSOC());
        //Thread.sleep(1000 * 30);
        //ep.stop();
        //System.out.println("Ende");
    }
}
