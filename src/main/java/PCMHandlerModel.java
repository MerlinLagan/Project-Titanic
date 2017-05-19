/**
 * Created by maxlundstrom on 15/05/17.
 */
import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.dto.LocationTimeSequence;
import eu.portcdm.dto.PortCall;
import eu.portcdm.dto.PortCallSummary;
import eu.portcdm.messaging.*;
import se.viktoria.stm.portcdm.connector.common.util.PortCallMessageBuilder;
import se.viktoria.stm.portcdm.connector.common.util.StateWrapper;

import java.util.List;
import java.util.ArrayList;

import eu.portcdm.dto.LocationTimeSequence;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;
    public class PCMHandlerModel {
        // comment
        public StateupdateApi stateUpdateApi;

        // Här lagras APIn och callen.
        public ApiClient connectorClient;
        PortcallsApi portcallsApi;
        PortCall currentCall;
        public String baseurl = "http://192.168.56.101:8080/dmp";
        List<PortCallSummary> summaries;

        // Konstruktor som anropar initiateStateupdateAPI och hämtare nuvarande portcall
        public PCMHandlerModel() {
            initiateStateupdateAPI();
//            summaries = getSummaries();
            // TODO   här skall vi hänvisa till portcallIDet (kolla vad summaries är)
//            currentCall = getPortCall(0);
        }

        // Uppdaterar summaries
        public boolean updateCalls() {
            summaries = getSummaries();
            if (summaries == null) {
                return false;
            } else {
                return true;
            }
        }

        // Hämtar den nuvarande callen
        public PortCall getCurrentCall() {
            return currentCall;
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

        //TODO En metod som hanterar listan från fetch och skriver ut dessa


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

        // Hämtar ett antal Summaries
        private List<PortCallSummary> getSummaries() {
            try {
                return portcallsApi.getAllPortCalls("porter", "porter", " porter", 30);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Skapa ett meddelande utifrån ett föregående meddelande
        private PortCallMessage updateRecievedMessage(PortCallMessage portCallMessage, String text, ServiceState serviceState) {
            TimeStampHelper timeStampHelper = new TimeStampHelper();
            PortCallMessage message = portCallMessage;
            message.setReportedBy("VTS");
            message.setComment(text);
            message.setServiceState(serviceState);
            message.setReportedAt(timeStampHelper.getTimeGregorian());
            return message;
        }

        private PortCallMessage createNewMessage(LocationTimeSequence locationTimeSequence, LogicalLocation logicalLocation, double reqLat,
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

        //Från en lista med alla PCM sorterar ut de som har relevant service state och returnerar de
        public List<PortCallMessage> checkServiceState (List<PortCallMessage> messageList) {
            List<PortCallMessage> relevantPCM = new ArrayList<>();

            for (PortCallMessage portCallMessage : messageList) {
                if (portCallMessage.getLocalPortCallId() == "urn:x-mrn:stm:portcdm:local_port_call:SEGOT:DHC:52724") {
                    if (portCallMessage.getServiceState().toString() == "VTSAreaEntry_Requested") {
                        relevantPCM.add(portCallMessage);
                    }
                }
            }
            System.out.print(relevantPCM.toString());
            return null;

        }

        //Samma uppbyggnad som checkServiceState men används för att testa i main
         public static List<PortCallMessage> checkComment (List<PortCallMessage> messageList) {
             List<PortCallMessage> relevantPCM= new ArrayList<>();
             System.out.print("A");
             for (PortCallMessage portCallMessage : messageList) {
                 System.out.print("B");
                 if (portCallMessage.getLocalPortCallId() == "urn:x-mrn:stm:portcdm:local_port_call:SEGOT:DHC:52724") {
                     System.out.print("C");
                     if (portCallMessage.getComment() == "Oscar") {
                         System.out.print("D");
                         relevantPCM.add(portCallMessage);
                     }
                 }
             }         
             System.out.print("E");
             System.out.print(relevantPCM.toString());
             return null;

         }

        public List<String> getVesselTravelInfo(PortCallMessage message){
            List<String> vesselInfo = new ArrayList<>(); ;

            vesselInfo.add(message.getLocationState().getArrivalLocation().getTo().getLocationType().toString());
            vesselInfo.add(message.getVesselId());
            vesselInfo.add(message.getLocationState().getTimeType().toString());
            return vesselInfo;
        }



        private PortCallMessage createGenericMessage(LocationTimeSequence locationTimeSequence, LogicalLocation logicalLocation) {
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

        


        //main för att testa checkComment som kommer fungerar typ som checkServiceState i framtiden
        public static void main(String[] args) {

            List<PortCallMessage> messageList = new ArrayList<>();

            PortCallMessage message1 = new PCMHandlerModel().createGenericMessage(LocationTimeSequence.ARRIVAL_TO, LogicalLocation.ANCHORING_AREA);
            message1.setComment("Oscar");
            messageList.add(message1);

            PortCallMessage message2 = new PCMHandlerModel().createGenericMessage(LocationTimeSequence.ARRIVAL_TO, LogicalLocation.BERTH);
            messageList.add(message2);

            checkComment(messageList);

        }
    }


