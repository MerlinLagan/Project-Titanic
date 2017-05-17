import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.dto.LocationTimeSequence;
import eu.portcdm.messaging.*;
import se.viktoria.stm.portcdm.connector.common.util.DateFormatter;
import se.viktoria.stm.portcdm.connector.common.util.PortCallMessageBuilder;
import se.viktoria.stm.portcdm.connector.common.util.StateWrapper;

import java.util.UUID;


public class PCMSenderModel {
    // comment

    public String baseurl = "http://192.168.56.101:8080/dmp";
    public String userId = "porter";
    public String password = "porter";
    public eu.portcdm.client.service.StateupdateApi stateUpdateApi;
    public eu.portcdm.client.ApiClient connectorClient;
    public PortcallsApi portCallsApi;


    public PCMSenderModel() {
        initiateStateupdateAPI();
    }

    public boolean sendMessage(PortCallMessage message){
            try {
                stateUpdateApi.sendMessage( "porter", "porter", "porter", message );
                return true;
            } catch (eu.portcdm.client.ApiException e) {
                e.printStackTrace();
                return false;
            }

    }
    private eu.portcdm.client.service.StateupdateApi initiateStateupdateAPI() {
        connectorClient = new eu.portcdm.client.ApiClient();
        connectorClient.setConnectTimeout(15);
        connectorClient.addDefaultHeader("X-PortCDM-UserId", "porter");
        connectorClient.addDefaultHeader("X-PortCDM-Password", "porter");
        connectorClient.addDefaultHeader("X-PortCDM-APIKey", "eeee");
        connectorClient.setBasePath(baseurl);
        stateUpdateApi = new eu.portcdm.client.service.StateupdateApi(connectorClient);
        return stateUpdateApi;
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
    private PortCallMessage createMessage2(PortCallMessage portCallMessage, String text){
        PortCallMessage message = portCallMessage;
        message.setReportedBy("VTS");
        message.setComment(text);
        // TODO add xml gregorian calender to line under
        //message.setReportedAt();
        return message;
    }

    private PortCallMessage createMessage(LocationTimeSequence locationTimeSequence, LogicalLocation logicalLocation) {
        StateWrapper stateWrapper = new StateWrapper(
                LocationReferenceObject.VESSEL, //referenceObject
                locationTimeSequence, //ARRIVAL_TO or DEPARTURE_FROM
                logicalLocation, //Type of required location
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
