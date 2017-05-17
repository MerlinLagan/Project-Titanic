import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.messaging.PortCallMessage;

import java.util.List;


public class PCMFetcherModel {
    // 4567 kunde aron och anton skicka till
    // 8080/dmp kan de hämta ifrån
    // från amss assistant message service fick man xml-test
    // comment

    public String baseurl = "http://192.168.56.101:8080/dmp";
    public String userId = "porter";
    public String password = "porter";
    public StateupdateApi stateUpdateApi;
    public ApiClient connectorClient;
    public PortcallsApi portCallsApi;

    //Konstruktor för klassen som anropar stateupdateAPI
    public PCMFetcherModel(){
        initiateStateupdateAPI();

    }

    private StateupdateApi initiateStateupdateAPI() {
        connectorClient = new ApiClient();
        connectorClient.setConnectTimeout(15);
        connectorClient.addDefaultHeader("X-PortCDM-UserId", "porter");
        connectorClient.addDefaultHeader("X-PortCDM-Password", "porter");
        connectorClient.addDefaultHeader("X-PortCDM-APIKey", "eeee");
        connectorClient.setBasePath(baseurl);
        stateUpdateApi = new StateupdateApi(connectorClient);
        return stateUpdateApi;
    }



    public List<PortCallMessage> getMessages() {
        try {
            stateUpdateApi.getPortCallMessages("porter", "porter", "porter", 5);
            return stateUpdateApi.getMessagesBetween("2017-05-05T06:30:00Z","2017-05-05T17:50:00Z", "porter", "porter"," porter");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

/*
    public static void main( String[] args )
    {
        PCMFetcherModel app = new PCMFetcherModel();
        app.initiateStateupdateAPI();
        System.out.println("ran main method");
        List<PortCallMessage> messageList = app.getMessages();

        for (PortCallMessage pcm : messageList) {
            String printString = pcm.getReportedAt().toString();
            System.out.println(printString);
        }
      //  app.GetPortCalls();
    }*/
}