package main.java;

import eu.portcdm.client.ApiClient;
import eu.portcdm.client.service.PortcallsApi;

/**
 * Created by maxlundstrom on 04/05/17.
 */
public class main {

    public static void main(String [] args)
    {
        System.out.println("Sushi är Max");
        System.out.println("Max Sushi, (Oscar was here today)");
        System.out.print("Hej hej hej Max är bara fjorton år");

        ApiClient client = new ApiClient();

        PortcallsApi api = new PortcallsApi(client);

        


    }

}
