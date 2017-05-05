package main.java;

import eu.portcdm.client.ApiClient;
import eu.portcdm.client.service.PortcallsApi;

/**
 * Created by maxlundstrom on 04/05/17.
 */
public class main {

    public static void main(String [] args)
    public static void main( String[] args )
    {
        App app = new App();
        app.initiateStateUpdateAPI();
        PortCallMessage testPCM = app.testMessage("portcallone");
        app.sendPCM(testPCM);
        System.out.println("ran main method");
        app.GetPortCalls();
    }
}

