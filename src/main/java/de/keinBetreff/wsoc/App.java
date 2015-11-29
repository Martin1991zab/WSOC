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
    	int port = 8000;
    	if(args.length == 1)
    		port = Integer.parseInt(args[0]);
    	System.out.println("Start");
        Endpoint.publish("http://localhost:" + port + "/services", new WSOC());
    }
}
