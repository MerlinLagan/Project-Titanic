import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.messaging.PortCallMessage;

import java.util.List;


public class PortCDMmessageFetcherTest {
    // 4567 kunde aron och anton skicka till
    // 8080/dmp kan de hämta ifrån
    // från amss assistant message service fick man xml-test

    public String baseurl = "http://192.168.56.101:8080/dmp";
    public String userId = "porter";
    public String password = "porter";
    public StateupdateApi stateUpdateApi;
    public ApiClient connectorClient;
    public PortcallsApi portCallsApi;

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

    private  List<PortCallMessage> GetPortCalls() {

        try {
           // List<PortCallSummary> portCallSummaries = portcallsApi.getAllPortCalls(8);
            return stateUpdateApi.getMessagesBetween("2015-05-03T06:30:00Z","2017-05-05T19:50:00Z", "porter", "porter", "porter");

           // System.out.println(portCallSummaries.size());
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
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

    /* Hämtar och returnerar en lista med ett givet antal PortCallMessages
    public List<PortCallMessage> getMessages(){
        try {
            //return stateupdateApi.getMessagesBetween("2017-05-03T06:30:00Z","2017-05-05T06:50:00Z");
            return stateUpdateApi.getPortCallMessages(10);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }
*/

    public static void main( String[] args )
    {
        PortCDMmessageFetcherTest app = new PortCDMmessageFetcherTest();
        app.initiateStateupdateAPI();
        System.out.println("ran main method");
        List<PortCallMessage> messageList = app.getMessages();

        for (PortCallMessage pcm : messageList) {
            String printString = pcm.getReportedAt().toString();
            System.out.println(printString);
        }
      //  app.GetPortCalls();
    }
}