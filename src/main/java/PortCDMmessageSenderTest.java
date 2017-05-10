import eu.portcdm.amss.client.ApiClient;
import eu.portcdm.amss.client.ApiException;
import eu.portcdm.amss.client.StateupdateApi;
import eu.portcdm.dto.LocationTimeSequence;
import eu.portcdm.messaging.*;
import se.viktoria.stm.portcdm.connector.common.util.DateFormatter;
import se.viktoria.stm.portcdm.connector.common.util.PortCallMessageBuilder;
import se.viktoria.stm.portcdm.connector.common.util.StateWrapper;

import java.util.UUID;


public class PortCDMmessageSenderTest {


    public PortCDMmessageSenderTest() {
    }

    public static void main( String[] args ) {
        new PortCDMmessageSenderTest().runUpdates();
    }

    public void runUpdates() {
        PortCallMessage portCallMessage;

        // * 1. Setup ApiClient and connection to PortCDM
        ApiClient apiClient;

        apiClient = new ApiClient();

        // Base path = URL to PortCDM
        // Virtual Machine: http://192.168.56.101:8080/amss
        // PortCDM Sandbox: http://dev.portcdm.eu:8080/
        apiClient.setBasePath( "http://192.168.56.101:8080/amss" );

        // Authenticate with headers
        // Virtual Machine: "X-PortCDM-UserId", "porter" | "X-PortCDM-Password", "porter" | "X-PortCDM-ApiKey", "tobbesnyckel" |
        // PortCDM: "X-PortCDM-UserId", "viktoria" | "X-PortCDM-Password", "vik123" | "X-PortCDM-ApiKey", "eeee"
        apiClient.addDefaultHeader( "X-PortCDM-UserId", "porter" );
        apiClient.addDefaultHeader( "X-PortCDM-Password", "porter" );
        apiClient.addDefaultHeader( "X-PortCDM-ApiKey", "tobbesnyckel" );
        System.out.println(apiClient.getDateFormat());

        StateupdateApi stateupdateApi = new StateupdateApi( apiClient );

        // * 3. Fetch new message
        portCallMessage = getExampleMessage();
        // * 4. Send message to PortCDM
        try {
            stateupdateApi.sendMessage("porter", "porter", "porter", portCallMessage);
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
    }
    /**
     * Example of creating a PortCallMessage using the common package util classes.
     * There are more versions of the StateWrapper constructor, this one is for creating a LocationState.
     * We do not want to send a message with both localJobId and localPortCallId, but both are filled in to show the format.
     *
     * @return
     */

    private PortCallMessage getExampleMessage2() {
        PortCallMessage portCallMessage = new PortCallMessage();
        LocationState locationState = new LocationState();
        LocationState.ArrivalLocation arrivalLocation = new LocationState.ArrivalLocation();
        LocationState.DepartureLocation departureLocation = new LocationState.DepartureLocation();
        portCallMessage.setPortCallId("urn:x-mrn:stm:portcdm:local_port_call:SEGOT:DHC:52723");
        portCallMessage.setComment(null);
        portCallMessage.setMessageId("urn:x-mrn:stm:portcdm:message:" + UUID.randomUUID().toString());
        locationState.setArrivalLocation(arrivalLocation);
        locationState.setDepartureLocation(departureLocation);
        locationState.setReferenceObject(LocationReferenceObject.VESSEL);
        portCallMessage.setLocationState(locationState);
        portCallMessage.setReportedAt(DateFormatter.toGregorianXML("2016-09-02T10:00:00Z"));
        return portCallMessage;
    }

    /** Skapa ett korrekt meddelande. Olika variabler skall antingen bestämmas genom meddelandet eller skapas. Kan finnas
     * jobb ID vilka antingen skall sättas automatiskt eller sllkapas..
     */

    private PortCallMessage getExampleMessage() {
        StateWrapper stateWrapper = new StateWrapper(
                LocationReferenceObject.VESSEL, //referenceObject
                LocationTimeSequence.ARRIVAL_TO, //ARRIVAL_TO or DEPARTURE_FROM
                LogicalLocation.BERTH, //Type of required location
                53.50, //Latitude of required location
                53.50, //Longitude of required location
                "Skarvik Harbour 518", //Name of required location
                LogicalLocation.ANCHORING_AREA, //Type of optional location
                52.50, //Latitude of optional location
                52.50, //Longitude of optional location
                "Dana Fjord D1" );//Name of optional location
        //Change dates from 2017-03-23 06:40:00 to 2017-03-23T06:40:00Z
        PortCallMessage portCallMessage = PortCallMessageBuilder.build(
                "urn:x-mrn:stm:portcdm:local_port_call:SEGOT:DHC:52724", //localPortCallId
                "urn:x-mrn:stm:portcdm:local_job:FENIX_SMA:990198126", //localJobId
                stateWrapper, //StateWrapper created above
                "2017-03-23T06:40:01Z", //Message's time
                TimeType.ESTIMATED, //Message's timeType
                "urn:x-mrn:stm:vessel:IMO:9259501", //vesselId
                "2017-03-23T06:38:57Z", //reportedAt (optional)
                "Viktoria", //reportedBy (optional)
                "urn:x-mrn:stm:portcdm:message:5eadbb1c-6be7-4cf2-bd6d-f0af5a0c35dc", //groupWith (optional), messageId of the message to group with.
                "example comment" //comment (optional)
        );
        return portCallMessage;
    }
}


 // OLd way to construct messages

/*
    private PortCallMessage testMessage(String portCallId) {
        PortCallMessage portCallMessage = new PortCallMessage();
        LocationState locationState = new LocationState();
        LocationState.ArrivalLocation arrivalLocation = new LocationState.ArrivalLocation();
        LocationState.DepartureLocation departureLocation = new LocationState.DepartureLocation();
        portCallMessage.setPortCallId(portCallId);
        portCallMessage.setComment(null);
        portCallMessage.setMessageId("urn:x-mrn:stm:portcdm:message:" + UUID.randomUUID().toString());
        locationState.setArrivalLocation(arrivalLocation);
        locationState.setDepartureLocation(departureLocation);
        locationState.setReferenceObject(LocationReferenceObject.VESSEL);
        portCallMessage.setLocationState(locationState);
        portCallMessage.setReportedAt(DateFormatter.toGregorianXML("2016-09-02T10:00:00Z"));
        return portCallMessage;
    }
 */