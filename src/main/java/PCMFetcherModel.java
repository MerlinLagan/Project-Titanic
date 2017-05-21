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

    //public String baseurl = "http://sandbox-5.portcdm.eu:8080/mb/mqs/";
    public String baseurl = "http://dev.portcdm.eu:8080/mb/mqs/";
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
        connectorClient.addDefaultHeader("X-PortCDM-UserId", "viktoria");
        connectorClient.addDefaultHeader("X-PortCDM-Password", "vik123");
        connectorClient.addDefaultHeader("X-PortCDM-APIKey", "eeee");
        //connectorClient.addDefaultHeader("X-PortCDM-xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        connectorClient.setBasePath(baseurl);
        stateUpdateApi = new StateupdateApi(connectorClient);
        return stateUpdateApi;
    }

    List<eu.portcdm.messaging.PortCallMessage> messageList;
// använd /mb/mqs messagebroker gör en post sedan sparar ned istället vi går runt, polla för att få ut meddelanden sedan sist
    // Get messages between blir redundant
    // skapa en kö och använd denna som nyckel
    public /*List<eu.portcdm.messaging.PortCallMessage>*/ void fetchMessagesBetweenTimes(/*String fromtime, String totime*/) {
        try {
            List<eu.portcdm.messaging.PortCallMessage> messageList = stateUpdateApi.getPortCallMessages("viktoria", "vik123", "eeee", 5);
            // return stateUpdateApi.getMessagesBetween(fromtime, totime, "test16", "test123","Testkonto16");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    public static void main( String[] args )
    {
        PCMFetcherModel app = new PCMFetcherModel();
        app.initiateStateupdateAPI();
        System.out.println("ran main method");
        //List<PortCallMessage> messageList = app.fetchMessagesBetweenTimes();
        //List<PortCallMessage> messageList = app.fetchMessagesBetweenTimes();
        app.fetchMessagesBetweenTimes();
        System.out.println(app.messageList);
      //  for (PortCallMessage pcm : messageList) {
      //      String printString = pcm.getComment().toString();
      //      System.out.println(printString);
      //  }
      //  app.GetPortCalls();
    }
}