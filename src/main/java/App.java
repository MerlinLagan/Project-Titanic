import java.util.List;
import java.util.UUID;

import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.dto.PortCallSummary;
import eu.portcdm.messaging.LocationReferenceObject;
import eu.portcdm.messaging.LocationState;
import eu.portcdm.messaging.PortCallMessage;
import eu.portcdm.messaging.TimeType;
import se.viktoria.stm.portcdm.connector.common.util.DateFormatter;


public class App {
    // 4567 kunde aron och anton skicka till
    // 8080/dmp kan de hämta ifrån
    // från amss assistant message service fick man xml-test

    public String baseurl = "http://192.168.56.101:1337/mb";
    public String userId = "porter";
    public String password = "porter";
    public StateupdateApi stateUpdateApi;
    public ApiClient connectorClient;


//	private StateupdateApi initiateStateUpdateAPI2() {
//		int timeout = 5;
//		connectorClient = new ApiClient();
//		connectorClient.setBasePath(baseurl);
//		connectorClient.setConnectTimeout(timeout);
//		//	connectorClient.setApiKey(apiKey);
//		//	connectorClient.setUsername(userId);
//		//	connectorClient.setPassword(password);
//		stateUpdateApi = new StateupdateApi(connectorClient);
//		return stateUpdateApi;
//	}

    private StateupdateApi initiateStateUpdateAPI() {
        connectorClient = new ApiClient();
        connectorClient.setConnectTimeout(5);
        connectorClient.addDefaultHeader("X-PortCDM-UserId", "porter");
        connectorClient.addDefaultHeader("X-PortCDM-Password", "porter");
        connectorClient.addDefaultHeader("X-PortCDM-APIKey", "eeee");
        connectorClient.setBasePath("http://192.168.56.101:1337/mb");
        stateUpdateApi = new StateupdateApi(connectorClient);
        return stateUpdateApi;
    }

    private PortCallMessage testMessage(String portCallId){
        PortCallMessage portCallMessage = new PortCallMessage();
        LocationState locationState = new LocationState();
        LocationState.ArrivalLocation arrivalLocation = new LocationState.ArrivalLocation();
        LocationState.DepartureLocation departureLocation = new LocationState.DepartureLocation();


        portCallMessage.setPortCallId(portCallId);
        portCallMessage.setComment(null);
        //		portCallMessage.setMessageId("urn:x-mrn:stm:portcdm:message:" + UUID.randomUUID().toString());
        portCallMessage.setMessageId("urn:x-mrn:stm:portcdm:message:" + "kalle");
        locationState.setArrivalLocation(arrivalLocation);
        locationState.setDepartureLocation(departureLocation);
        locationState.setReferenceObject(LocationReferenceObject.VESSEL);
        portCallMessage.setLocationState(locationState);
        portCallMessage.setReportedAt(DateFormatter.toGregorianXML("2016-09-02T10:00:00Z"));

        return portCallMessage;
    }

    private void sendPCM(PortCallMessage message){
        try {
            stateUpdateApi.sendMessage(message);
        } catch (ApiException e){
            e.printStackTrace();
        }
    }

    private void GetPortCalls(){
        ApiClient client = new ApiClient();
        client.addDefaultHeader("X-PortCDM-UserId", "porter");
        client.addDefaultHeader("X-PortCDM-Password", "porter");
        client.addDefaultHeader("X-PortCDM-APIKey", "eeee");
        client.setBasePath("http://192.168.56.101:8080/dmp");
        PortcallsApi portcallsApi = new PortcallsApi(client);

        try {
            List<PortCallSummary> portCallSummaries = portcallsApi.getAllPortCalls(30);
            System.out.println(portCallSummaries.size());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }



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