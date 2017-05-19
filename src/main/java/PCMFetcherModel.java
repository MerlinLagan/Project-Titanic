import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.messaging.PortCallMessage;
import java.util.List;

// Den här klassen ska hämta meddelanden från PortCDM enl specifikations

public class PCMFetcherModel {
    // 4567 kunde aron och anton skicka till
    // 8080/dmp kan de hämta ifrån
    // från amss assistant message service fick man xml-test
    // comment

    public String baseurl = "http://sandbox-5.portcdm.eu:8080/mb/mqs";
    // http://192.168.56.101:8080/dmp
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
        connectorClient.addDefaultHeader("X-PortCDM-UserId", "test16");
        connectorClient.addDefaultHeader("X-PortCDM-Password", "test123");
        connectorClient.addDefaultHeader("X-PortCDM-APIKey", "Testkonto16");
        connectorClient.setBasePath(baseurl);
        stateUpdateApi = new StateupdateApi(connectorClient);
        return stateUpdateApi;
    }

// använd /mb/mqs messagebroker gör en post sedan sparar ned istället vi går runt, polla för att få ut meddelanden sedan sist
    // Get messages between blir redundant
    // skapa en kö och använd denna som nyckel
    public List<PortCallMessage> fetchMessagesBetweenTimes(/*String fromtime, String totime*/) {
        try {
            return stateUpdateApi.getPortCallMessages("test16", "test123", "Testkonto16", 5);
            // return stateUpdateApi.getMessagesBetween(fromtime, totime, "test16", "test123","Testkonto16");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main( String[] args )
    {
        PCMFetcherModel app = new PCMFetcherModel();
        app.initiateStateupdateAPI();
        System.out.println("ran main method");
        //List<PortCallMessage> messageList = app.fetchMessagesBetweenTimes();
        List<PortCallMessage> messageList = app.fetchMessagesBetweenTimes();
        for (PortCallMessage pcm : messageList) {
            String printString = pcm.getComment().toString();
            System.out.println(printString);
        }
      //  app.GetPortCalls();
    }
}