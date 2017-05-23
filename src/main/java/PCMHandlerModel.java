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
    List<PortCallMessage> latestFetchBatch = new ArrayList<PortCallMessage>();
    int selectedPCMIndex;
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

    public int getSelectedPCMIndex() {
        return selectedPCMIndex;
    }

    public void setSelectedPCMIndex(int index) {
        selectedPCMIndex = index;
    }


    // Skapar och svarar på ett meddelande utifrån ett föregående meddelande
    public boolean respondToMessageWithStatement(String text, ServiceTimeSequence serviceTimeSequence) {
        TimeStampHelper timeStampHelper = new TimeStampHelper();
        PortCallMessage message = messageList.get(selectedPCMIndex);
        //message.setMessageId(null);
        message.setReportedBy("VTS");
        message.getServiceState().setTimeSequence(serviceTimeSequence);
        message.setReportedAt(timeStampHelper.getTimeGregorian());
        if (text == null) {
            message.setComment(text);
        } else
            message.setComment(text);
        senderModel.sendMessage(message);
        return true;
    }

    public boolean respondToMessage(String text) {
        TimeStampHelper timeStampHelper = new TimeStampHelper();
        PortCallMessage message = messageList.get(selectedPCMIndex);
        message.setMessageId(null);
        message.setReportedBy("VTS");
        message.setReportedAt(timeStampHelper.getTimeGregorian());
        if (!(text == null)) {
            message.setComment(text);
            senderModel.sendMessage(message);
            return true;
        }
        return false;
    }

    //Från en lista med alla PCM sorterar ut de som har relevant Service Object och returnerar de
    public List<PortCallMessage> getRelevantPCMs(List<PortCallMessage> messageList) {

        List<PortCallMessage> relevantPCMs = new ArrayList<>();
        for (PortCallMessage portCallMessage : messageList) {
            if (isRelevant(portCallMessage)) {
                relevantPCMs.add(portCallMessage);
            }
        }
        return relevantPCMs;
    }

    public boolean isRelevant(PortCallMessage portCallMessage) {

        ServiceState servState = portCallMessage.getServiceState();
        LocationState locState = portCallMessage.getLocationState();

        try {
            if (servState.getServiceObject().toString().equals("ARRIVAL_VTSAREA")) {
                return true;
            } else if (servState.getServiceObject().toString().equals("DEPARTURE_VTSAREA")) {
                return true;
            } else if (servState.getServiceObject().toString().equals("ARRIVAL_BERTH")) {
                return true;
            } else if (servState.getServiceObject().toString().equals("DEPARTURE_BERTH")) {
                return true;
            } else if (servState.getServiceObject().toString().equals("ARRIVAL_ANCHORING_OPERATION")) {
                return true;
            } else if (servState.getServiceObject().toString().equals("DEPARTURE_ANCHORING_OPERATION")) {
                return true;
            } else if (servState.getServiceObject().toString().equals("ANCHORING")) {
                return true;
            } else if (servState.getServiceObject().toString().equals("TOWAGE")) {
                return true;
            } else if (servState.getServiceObject().toString().equals("ESCORT_TOWAGE")) {
                return true;
            }
                /*else if(locState.getReferenceObject().equals("TUG")){
                    System.out.print("TUG");
                    relevantPCM.add(portCallMessage);
                }
                else if(locState.getReferenceObject().toString().equals("ESCORT_TUG")){
                    System.out.print("ESCORT_TUG");
                    relevantPCM.add(portCallMessage);
                } */
        } catch (NullPointerException e) {
        }
        return false;
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

    public void getMessagesBetweenTimes(String startdate, String enddate) {
        latestFetchBatch = new ArrayList<PortCallMessage>();
        for (PortCallMessage pcm : fetcherModel.fetchMessagesBetweenTimes(startdate, enddate)) {
            latestFetchBatch.add(pcm);
            if (isRelevant(pcm))
                messageList.add(pcm);
        }
    }

    public List<PortCallMessage> getLatestFetchBatch(){
        return latestFetchBatch;
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
                "Port Call ID: " + portCallMessage.getPortCallId() + "\n" +
                "Message ID: " + portCallMessage.getMessageId() + "\n" +
                "Reported at: " + strYear + "-" + strMonth + "-" + strDay + " " + strHour + ":" + strSec + "\n" +
                "\n" +
                "Time Type: " + timeType + "\n" +
                "Time Sequence: "+ timeSequence +  "\n" +
                "Service Object: " + serviceObj + "\n" +
                "\n" +
                "Comment: " + portCallMessage.getComment();


        return string;
    }

    // En main som testar getRelevantPCMs-metod genom att skapa en lista med pcm som har olika ServiceObjects som sedan körs metoden
    public static void main(String[] args) {
        PCMHandlerModel pcmHandler = new PCMHandlerModel();

        List<PortCallMessage> listofPCM = new ArrayList<>();

        ServiceState servState = new ServiceState();
        ServiceState servState2 = new ServiceState();
        // LocationState locState = new LocationState();

        ServiceObject servObj = ServiceObject.ARRIVAL_VTSAREA;
        ServiceObject servObj2 = ServiceObject.DEPARTURE_VTSAREA;
        //LocationReferenceObject refObj = LocationReferenceObject.TUG; -- LYCKAS INTE FILTRERA PÅ DESSA

        PortCallMessage message1 = pcmHandler.senderModel.createMessage();
        servState.setServiceObject(servObj);
        message1.setServiceState(servState);
        listofPCM.add(message1);

        PortCallMessage message2 = pcmHandler.senderModel.createMessage();
        listofPCM.add(message2);

        PortCallMessage message3 = pcmHandler.senderModel.createMessage();
        listofPCM.add(message3);

        PortCallMessage message4 = pcmHandler.senderModel.createMessage();
        servState2.setServiceObject(servObj2);
        message4.setServiceState(servState2);
        listofPCM.add(message4);

        //PortCallMessage message5 = pcmHandler.senderModel.createMessage();
        // locState.setReferenceObject(refObj);
        //message5.setLocationState(locState);
        // listofPCM.add(message5);

        pcmHandler.getRelevantPCMs(listofPCM);
    }


    /*
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
    */

}