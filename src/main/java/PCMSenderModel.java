import eu.portcdm.amss.client.ApiClient;
import eu.portcdm.amss.client.StateupdateApi;
import eu.portcdm.dto.LocationTimeSequence;
import eu.portcdm.messaging.*;
import se.viktoria.stm.portcdm.connector.common.util.PortCallMessageBuilder;
import se.viktoria.stm.portcdm.connector.common.util.StateWrapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PCMSenderModel {

    public StateupdateApi stateUpdateApi;
    public ApiClient apiClient;

    String baseUrl, userID, userPW, apiKey;

    String  BASEURL_SANDBOX = "http://sandbox-5.portcdm.eu:8080/amss",
            BASEURL_VIRTUALBOX = "http://192.168.56.101:8080/amss";
    String  userIDVBox = "porter", userPWVBox = "porter", apiKeyVBox = "eeee",
            userIDSBox = "test13", userPWSBox = "test123", apiKeySBox = "Testkonto 13";

    public PCMSenderModel(String boxtype) {
        initiateStateupdateAPI(boxtype);
    }

    private StateupdateApi initiateStateupdateAPI(String boxtype) {
        apiClient = new ApiClient();
        if (boxtype.equals("sandbox")) {
            baseUrl = BASEURL_SANDBOX;
            userID = userIDSBox;
            userPW = userPWSBox;
            apiKey = apiKeySBox;
            System.out.println("PCMSenderModel initiated in sandbox mode.");

        }
        else if (boxtype.equals("virtualbox")) {
            baseUrl = BASEURL_VIRTUALBOX;
            userID = userIDVBox;
            userPW = userPWVBox;
            apiKey = apiKeyVBox;
            System.out.println("PCMSenderModel initiated in virtualbox mode.");
        }

        apiClient.addDefaultHeader("X-PortCDM-UserId", userID);
        apiClient.addDefaultHeader("X-PortCDM-Password", userPW);
        apiClient.addDefaultHeader("X-PortCDM-ApiKey", apiKey);
        apiClient.setBasePath(baseUrl);
        stateUpdateApi = new StateupdateApi(apiClient);

        return stateUpdateApi;
    }

    public PortCallMessage createNewMessage(LocationTimeSequence locationTimeSequence, LogicalLocation logicalLocation, double reqLat,
                                             double reqLong, String reqName, LogicalLocation logicalOptionalLocation, double reqOptLat,
                                             double reqOptLong, String reqOptName, String localPCID, String localJID, String time, TimeType
                                                     timeType, String vesselID, String reportedAt, String reportedBy, String groupWith, String comment) {
        StateWrapper stateWrapper = new StateWrapper(
                LocationReferenceObject.VESSEL, //referenceObject
                locationTimeSequence, //ARRIVAL_TO or DEPARTURE_FROM
                logicalLocation, //Type of required location
                reqLat, //Latitude of required location
                reqLong, //Longitude of required location
                reqName, //Name of required location
                logicalOptionalLocation, //Type of optional location
                reqOptLat, //Latitude of optional location
                reqOptLong, //Longitude of optional location
                reqOptName);//Name of optional location
        //Change dates from 2017-03-23 06:40:00 to 2017-03-23T06:40:00Z
        PortCallMessage portCallMessage = PortCallMessageBuilder.build(
                localPCID, //localPortCallId
                localJID, //localJobId
                stateWrapper, //StateWrapper created above
                time, //Message's time
                timeType, //Message's timeType
                vesselID, //vesselId
                reportedAt, //reportedAt (optional)
                reportedBy, //reportedBy (optional)
                groupWith, //groupWith (optional), messageId of the message to group with.
                comment //comment (optional)
        );
        return portCallMessage;

    }

    public void sendMessage(PortCallMessage message){
        try {
            stateUpdateApi.sendMessage(message);
        } catch (eu.portcdm.amss.client.ApiException e) {
            e.printStackTrace();
        }
    }

    public PortCallMessage getExampleMessage() {
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
                "2017-01-01T00:00:06Z", //Message's time
                TimeType.ESTIMATED, //Message's timeType
                "urn:x-mrn:stm:vessel:IMO:9259501", //vesselId
                "2017-01-01T00:00:05Z", //reportedAt (optional)
                "Viktoria", //reportedBy (optional)
                "urn:x-mrn:stm:portcdm:message:5eadbb1c-6be7-4cf2-bd6d-f0af5a0c35dc", //groupWith (optional), messageId of the message to group with.
                "exampletest" //comment (optional)
        );
        return portCallMessage;
    }

    public PortCallMessage createMessage(){

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
                "urn:x-mrn:stm:portcdm:local_port_call:SEGOT:DHC:52723", //localPortCallId
                "urn:x-mrn:stm:portcdm:local_job:FENIX_SMA:990198125", //localJobId
                stateWrapper, //StateWrapper created above
                "2017-01-01T00:00:05Z", //Message's time
                TimeType.ESTIMATED, //Message's timeType
                "urn:x-mrn:stm:vessel:IMO:9259501", //vesselId
                null, //reportedAt (optional)
                null, //reportedBy (optional)
                null, //groupWith (optional), messageId of the message to group with.
                "Hello World" //comment (optional)
        );
        return portCallMessage;
    }

    public PortCallMessage createGenericMessage(LocationTimeSequence locationTimeSequence, LogicalLocation logicalLocation) {
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
                "2017-01-01T00:00:05Z", //Message's time
                TimeType.ESTIMATED, //Message's timeType
                "urn:x-mrn:stm:vessel:IMO:9259501", //vesselId
                "2017-01-01T00:00:05Z", //reportedAt (optional)
                "Viktoria", //reportedBy (optional)
                "urn:x-mrn:stm:portcdm:message:5eadbb1c-6be7-4cf2-bd6d-f0af5a0c35dc", //groupWith (optional), messageId of the message to group with.
                "example comment" //comment (optional)
        );
        return portCallMessage;
    }
}