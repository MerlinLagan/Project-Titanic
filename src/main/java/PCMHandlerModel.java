/**
 * Created by maxlundstrom on 15/05/17.
 */

import eu.portcdm.dto.LocationTimeSequence;
import eu.portcdm.messaging.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;
import java.util.ArrayList;

public class PCMHandlerModel {

    List<PortCallMessage> messageList = new ArrayList<PortCallMessage>();
    int selectedMessageIndex;
    PCMFetcherModel fetcherModel;
    PCMSenderModel senderModel;

    // Konstruktor som anropar initiateStateupdateAPI och hämtare nuvarande portcall
    public PCMHandlerModel() {
        this.fetcherModel = new PCMFetcherModel("virtualbox");
        this.senderModel = new PCMSenderModel("virtualbox");
    }

    public PCMHandlerModel(PCMFetcherModel fetcherModel, PCMSenderModel senderModel) {
        this.fetcherModel = fetcherModel;
        this.senderModel = senderModel;
    }

    public int getSelectedMessageIndex(){
        return selectedMessageIndex;
    }

    public void setSelectedMessageIndex(int index){
        selectedMessageIndex = index;
        System.out.println("Logmodel index " + selectedMessageIndex);
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

    //Från en lista med alla PCM sorterar ut de som har relevant service state och returnerar de
    public List<PortCallMessage> checkServiceState(List<PortCallMessage> messageList) {
        List<PortCallMessage> relevantPCM = new ArrayList<>();
        for (PortCallMessage portCallMessage : messageList) {
            if (portCallMessage.getLocalPortCallId().equals("urn:x-mrn:stm:portcdm:local_port_call:SEGOT:DHC:52724")) {
                if (portCallMessage.getServiceState().toString().equals("VTSAreaEntry_Requested")) {
                    relevantPCM.add(portCallMessage);
                }
            }
        }
        System.out.print(relevantPCM.toString());
        return null;
    }

    //Samma uppbyggnad som checkServiceState men används för att testa i main
    public static List<PortCallMessage> checkComment(List<PortCallMessage> messageList) {
        List<PortCallMessage> relevantPCM = new ArrayList<>();
        System.out.print("A");
        for (PortCallMessage portCallMessage : messageList) {
            System.out.print("B");
            if (portCallMessage.getLocalPortCallId().equals("urn:x-mrn:stm:portcdm:local_port_call:SEGOT:DHC:52724")) {
                System.out.print("C");
                if (portCallMessage.getComment().equals("Oscar")) {
                    System.out.print("D");
                    relevantPCM.add(portCallMessage);
                }
            }
        }
        System.out.print("E");
        System.out.print(relevantPCM.toString());
        return null;
    }

    public List<PortCallMessage> getPortCallMessages() {
        return messageList;
    }

    /*
    Takes arraylist with pcms and turns into arraylist with strings for log
    */
    public List<String> getPortCallMessagesAsStrings(List<PortCallMessage> pcmList) {
        ArrayList<String> pcmStringList = new ArrayList<String>();
        for (PortCallMessage pcm : pcmList)
            pcmStringList.add(formatMessageForLog(pcm));
        return pcmStringList;
    }

    public ArrayList<String> getVesselTravelInfo(PortCallMessage message) {
        ArrayList<String> vesselInfo = new ArrayList<String>();
        String location;
        String vesselID;
        String timeType;
        try {
            //timetypes: ACTUAL, CANCELLED, ESTIMATED, RECOMMENDED, TARGET
            location = message.getLocationState().getArrivalLocation().getTo().getLocationType().toString();
            vesselID = message.getVesselId().toString();
            timeType = message.getLocationState().getTimeType().value();

            if (location != null && vesselID != null &&
                    (timeType.equals("ACTUAL") || timeType.equals("ESTIMATED") ||timeType.equals("TARGET"))) {
                if (timeType.equals("ACTUAL")) {

                    vesselInfo.add(0, location);
                    vesselInfo.add(1, vesselID);
                    vesselInfo.add(2, "ACTUAL");
                }
                if (timeType.equals("ESTIMATED") || timeType.equals("TARGET")) {
                    vesselInfo.add(0, location);
                    vesselInfo.add(1, vesselID);
                    vesselInfo.add(2, "ESTIMATED");
                }
                System.out.println("actually returning vesselInfo where info is: "  + location + "   "  + vesselID + "   "  + timeType);
                return vesselInfo;
            }
        }
        catch (Exception NullPointerException) {
        }
        return null;
    }

    public ArrayList<ArrayList<String>> getMultipleVesselsTravelinfo(List<PortCallMessage> pcmList) {
        ArrayList<ArrayList<String>> travelInfoList = new ArrayList<ArrayList<String>>();
        ArrayList<String> singleVesselInfo;
        for (PortCallMessage pcm : pcmList) {
            singleVesselInfo = getVesselTravelInfo(pcm);
            if (singleVesselInfo != null) {
                travelInfoList.add(singleVesselInfo);
            }
        }
        return travelInfoList;
    }

    private PortCallMessage createGenericMessage(LocationTimeSequence locationTimeSequence, LogicalLocation logicalLocation) {
        return senderModel.createGenericMessage(locationTimeSequence, logicalLocation);
    }

    private void sendMessage(PortCallMessage pcm) {
        senderModel.sendMessage(pcm);
    }

    public void getMessagesBetweenTimes(String startdate, String enddate) {
        for (PortCallMessage pcm : fetcherModel.fetchMessagesBetweenTimes("hh", "hh"))
        this.messageList.add(pcm);
    }

    public String formatMessageForLog(PortCallMessage portCallMessage) {

        XMLGregorianCalendar time = portCallMessage.getReportedAt();
        Integer intYear = time.getYear();
        String strYear = intYear.toString();
        Integer intMonth = time.getMonth();
        String strMonth = intMonth.toString();
        Integer intDay = time.getDay();
        String strDay = intDay.toString();
        Integer intHour = time.getHour();
        String strHour = intHour.toString();
        Integer intSec = time.getSecond();
        String strSec = intSec.toString();
        String timeType = "null", timeSequence = "null", serviceObj = "null";

        ServiceState servState = portCallMessage.getServiceState();
        try {
            timeType = servState.getTimeType().toString(); // ACTUAL, RECOMMENDED, ESTIMATED mm
        } catch (Exception e) {
        }
        try {
            timeSequence = servState.getTimeSequence().toString(); //ARRIVAL_TO, DEPARTURE_FROM, REQUESTED, COMMENCED, COMPLETED, CONFIRMED, DENIED , REQUEST_RECEIVED
        } catch (Exception e) {
        }
        try {
            serviceObj = servState.getServiceObject().toString(); // ARRIVAL_VTSAREA mm
        } catch (Exception e) {
        }



        String string = "Port Call Message Information" + "\n " +
                "\n " +
                "ReportedBy: " + portCallMessage.getReportedBy() +  "\n " +
                "Regarding Vessel ID: " + portCallMessage.getVesselId() + "\n" +
                "Message ID: " + portCallMessage.getMessageId() + "\n" +
                "Local Job ID: " + portCallMessage.getLocalJobId() + "\n" +
                "Reported at: " + strYear + "-" + strMonth + "-" + strDay + " " + strHour + ":" + strSec + "\n" +
                "\n" +
                "Time Type: " + timeType + "\n" +
                "Time Sequence: "+ timeSequence +  "\n" +
                "Service Object: " + serviceObj + "\n" +
                "\n" +
                "Comment: " + portCallMessage.getComment();


        return string;
    }

    public static void main(String[] args) {
        PCMHandlerModel pcmHandler = new PCMHandlerModel();
        PortCallMessage message1 = pcmHandler.senderModel.createMessage();
        message1.setComment("Oscar");
        pcmHandler.sendMessage(message1);

        pcmHandler.getMessagesBetweenTimes("asd", "asd");
        System.out.println(pcmHandler.messageList);

        for (PortCallMessage pcm : pcmHandler.messageList) {
            //System.out.println(pcm.getComment());

            if (!(pcm.getComment() == null)) {
                System.out.println("is not null2");
                if (pcm.getComment().equals("exampletest")) {
                    System.out.println(pcm.getComment() + "yeo");
                }
            }
            //checkComment(pcmHandler.messageList);
        }
    }
}