/**
 * Created by maxlundstrom on 15/05/17.
 */

import eu.portcdm.dto.LocationTimeSequence;
import eu.portcdm.messaging.*;
import java.util.List;
import java.util.ArrayList;

public class PCMHandlerModel {

    List<PortCallMessage> messageList;
    PortCallMessage selectedMessage;
    PCMFetcherModel fetcherModel;
    PCMSenderModel senderModel;


    // Konstruktor som anropar initiateStateupdateAPI och hämtare nuvarande portcall
    public PCMHandlerModel() {
        messageList = new ArrayList<>();
        this.fetcherModel = new PCMFetcherModel("virtualbox");
        this.senderModel = new PCMSenderModel("virtualbox");

        //  PCMSenderModel senderModel = new PCMSenderModel("virtualbox");
    }
    public PCMHandlerModel(PCMFetcherModel fetcherModel, PCMSenderModel senderModel) {
        messageList = new ArrayList<>();
        this.fetcherModel = fetcherModel;
        this.senderModel = senderModel;

        //  PCMSenderModel senderModel = new PCMSenderModel("virtualbox");
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
    public List<PortCallMessage> checkServiceState (List<PortCallMessage> messageList) {
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
    public static List<PortCallMessage> checkComment (List<PortCallMessage> messageList) {
        List<PortCallMessage> relevantPCM= new ArrayList<>();
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

    public List<String> getVesselTravelInfo(PortCallMessage message){
        List<String> vesselInfo = new ArrayList<>(); ;
        vesselInfo.add(message.getLocationState().getArrivalLocation().getTo().getLocationType().toString());
        vesselInfo.add(message.getVesselId());
        vesselInfo.add(message.getLocationState().getTimeType().toString());
        return vesselInfo;
    }

    private PortCallMessage createGenericMessage(LocationTimeSequence locationTimeSequence, LogicalLocation logicalLocation) {
        return senderModel.createGenericMessage(locationTimeSequence, logicalLocation);
    }

    private void sendMessage(PortCallMessage pcm){
        senderModel.sendMessage(pcm);
    }

    private List<PortCallMessage> getMessagesBetweenTimes(){
        return fetcherModel.fetchMessagesBetweenTimes("", "");
    }

    public static void main(String[] args) {
        PCMHandlerModel pcmHandler = new PCMHandlerModel();
        PortCallMessage message1 = pcmHandler.createGenericMessage(LocationTimeSequence.ARRIVAL_TO, LogicalLocation.ANCHORING_AREA);
        PortCallMessage message2 = pcmHandler.createGenericMessage(LocationTimeSequence.ARRIVAL_TO, LogicalLocation.BERTH);
        message1.setComment("Oscar");
        message2.setComment("Sands");

        pcmHandler.sendMessage(message1);
        pcmHandler.sendMessage(message2);

        pcmHandler.messageList.add(message1);
        pcmHandler.messageList.add(message2);

        pcmHandler.messageList = pcmHandler.getMessagesBetweenTimes();

        for (PortCallMessage pcm : pcmHandler.messageList){
            System.out.println(pcm.getComment());
        }

        //checkComment(pcmHandler.messageList);


    }
}