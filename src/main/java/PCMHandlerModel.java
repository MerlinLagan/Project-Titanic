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
import eu.portcdm.messaging.LocationReferenceObject;
import eu.portcdm.messaging.LogicalLocation;
import eu.portcdm.messaging.PortCallMessage;
import eu.portcdm.messaging.TimeType;
import se.viktoria.stm.portcdm.connector.common.util.PortCallMessageBuilder;
import se.viktoria.stm.portcdm.connector.common.util.StateWrapper;

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
        public PCMHandlerModel(){
            initiateStateupdateAPI();
//            summaries = getSummaries();
          // TODO   här skall vi hänvisa till portcallIDet (kolla vad summaries är)
//            currentCall = getPortCall(0);
        }

        // Uppdaterar summaries
        public boolean updateCalls(){
            summaries = getSummaries();
            if (summaries == null){
                return false;
            }
            else {
                return true;
            }
        }

        // Hämtar den nuvarande callen
        public PortCall getCurrentCall(){
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
        public PortCall getPortCall(int id){
            PortCallSummary summary = summaries.get(id);
            try {
                return portcallsApi.getPortCall(summary.getId(), "porter", "porter"," porter");
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        // Hämtar ett antal Summaries
        private List<PortCallSummary> getSummaries(){
            try {
                return portcallsApi.getAllPortCalls("porter", "porter"," porter", 30);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }
        // Skapa ett meddelande utifrån ett föregående meddelande
        private PortCallMessage updateRecievedMessage(PortCallMessage portCallMessage, String text){
            PortCallMessage message = portCallMessage;
            message.setReportedBy("VTS");
            message.setComment(text);
            // TODO add xml gregorian calender to line under
            //message.setReportedAt(TimeStampHelper.getCurrentTimeStamp());
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


            public List<PortCallMessage> checkServiceState(List<PortCallMessage>){
                    //plocka ut en pmc, kolla om det är det xxx lägg in i ny lista "relavent list" reapat
                return null;

        }
    }

