import eu.portcdm.dto.LocationTimeSequence;
import eu.portcdm.messaging.*;
import se.viktoria.stm.portcdm.connector.common.util.DateFormatter;
import se.viktoria.stm.portcdm.connector.common.util.PortCallMessageBuilder;
import se.viktoria.stm.portcdm.connector.common.util.StateWrapper;

import java.util.UUID;

/**
 * Created by maxlundstrom on 17/05/17.
 */
public class PCMCreatorModel {

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
    }
}
