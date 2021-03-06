// Class used to get portCDM messages from server

import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.messaging.PortCallMessage;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;


public class PCMFetcherModel {

    eu.portcdm.client.service.StateupdateApi stateUpdateApi;
    eu.portcdm.client.ApiClient connectorClient;

    String baseUrl, userID, userPW, apiKey;

    String  BASEURL_SANDBOX ="http://sandbox-5.portcdm.eu:8080/dmp",
            BASEURL_VIRTUALBOX = "http://192.168.56.101:8080/dmp";
    String  userIDVBox = "porter", userPWVBox = "porter", apiKeyVBox = "none",
            userIDSBox = "test13", userPWSBox = "test123", apiKeySBox = "Testkonto 13";

    //Konstruktor för klassen som anropar stateupdateAPI
    public PCMFetcherModel(String boxtype){
        initiateStateupdateAPI(boxtype);
    }

    public eu.portcdm.client.service.StateupdateApi initiateStateupdateAPI(String boxtype) {
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
        connectorClient.addDefaultHeader("X-PortCDM-ApiKey", apiKey);
        connectorClient.setBasePath(baseUrl);
        stateUpdateApi = new StateupdateApi(connectorClient);
        return stateUpdateApi;
    }

    public List<PortCallMessage> fetchMessagesBetweenTimes(String startdate, String enddate) {
        TimeStampHelper timeStampHelper = new TimeStampHelper();
        try {
            return stateUpdateApi.getMessagesBetween(startdate, enddate);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}