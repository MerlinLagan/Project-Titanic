import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.messaging.PortCallMessage;
import java.util.List;


public class PCMFetcherModel {

    eu.portcdm.client.service.StateupdateApi stateUpdateApi;
    eu.portcdm.client.ApiClient connectorClient;

    String baseUrl, userID, userPW, apiKey;

    String  BASEURL_SANDBOX ="http://sandbox-5.portcdm.eu:8080/mb/mqs",
            BASEURL_VIRTUALBOX = "http://192.168.56.101:8080/mb/mqs";
    String  userIDVBox = "porter", userPWVBox = "porter", apiKeyVBox = "none",
            userIDSBox = "test13", userPWSBox = "test123", apiKeySBox = "Testkonto 13";

    //Konstruktor för klassen som anropar stateupdateAPI
    public PCMFetcherModel(String boxtype){
        initiateStateupdateAPI(boxtype);
    }

    private eu.portcdm.client.service.StateupdateApi initiateStateupdateAPI(String boxtype) {
        connectorClient = new ApiClient();
        connectorClient.setConnectTimeout(15);
        if (boxtype.equals("sandbox")) {
            baseUrl = BASEURL_SANDBOX;
            userID = userIDSBox;
            userPW = userPWSBox;
            apiKey = apiKeySBox;

        }
        if (boxtype.equals("virtualbox")) {
            baseUrl = BASEURL_VIRTUALBOX;
            userID = userIDVBox;
            userPW = userPWVBox;
            apiKey = apiKeyVBox;
        }
        connectorClient.addDefaultHeader("X-PortCDM-UserId", userID);
        connectorClient.addDefaultHeader("X-PortCDM-Password", userPW);
        connectorClient.addDefaultHeader("X-PortCDM-APIKey", apiKey);
        connectorClient.setBasePath(baseUrl);
        stateUpdateApi = new StateupdateApi(connectorClient);
        return stateUpdateApi;
    }

    public List<PortCallMessage> fetchMessagesBetweenTimes(String fromtime, String totime) {
        try {
           // return stateUpdateApi.getPortCallMessages(userIDvbox, userPWvbox, apiKeyvbox, 5);
             return stateUpdateApi.getMessagesBetween("2017-03-23T06:40:01Z","2017-05-23T06:40:01Z");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
          // Hämtar ett specifikt PortCall
        public PortCall getPortCall(int id) {
            PortCallSummary summary = summaries.get(id);
            try {
                return portcallsApi.getPortCall(summary.getId(), "porter", "porter", " porter");
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }
     */

    public static void main( String[] args )
    {
        PCMFetcherModel app = new PCMFetcherModel("sandbox");
        app.initiateStateupdateAPI("virtualbox");
        System.out.println("ran main method");
        List<PortCallMessage> messageList = app.fetchMessagesBetweenTimes("abc", "abc");
        for (PortCallMessage pcm : messageList) {
            String printString = pcm.getComment();
            System.out.println(printString);
        }
    }
}