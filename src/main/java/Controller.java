import eu.portcdm.messaging.ServiceTimeSequence;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jakob on 15/05/17.
 */

public class Controller implements ActionListener {

    LogModel logModel;
    TemplatesHandlerModel tmpltsModel;
    MessengerView msgrView;
    MessagesView msgsView;
    PCMHandlerModel pcmHandler;
    PCMSenderModel pcmSender;
    PCMFetcherModel pcmFetcher;
    VesselLocationModel vsllocModel;
    VesselLocationView vsllocView;
    Timer timer;


    public void initialize(){
        tmpltsModel.loadTemplatesMap("data.properties");
        msgrView.loadTemplatesFromMap(tmpltsModel.getTemplates());
        updateFullVesselLocationView();
        timer = new Timer(5000, this);
        timer.setInitialDelay(0);
        timer.start();
        applyNewMessages();
        msgsView.setVisible(true);
        msgrView.setVisible(true);
        vsllocView.setVisible(true);
    }

    public void addViews(MessengerView msgrView, MessagesView msgsView, VesselLocationView vsllocView){
        this.msgsView = msgsView;
        this.msgrView = msgrView;
        this.vsllocView = vsllocView;
    }

    public void addModels(TemplatesHandlerModel tmpltsModel, LogModel msgsModel,
                          PCMHandlerModel pcmHandler, VesselLocationModel vsllocModel){
        this.tmpltsModel = tmpltsModel;
        this.logModel = msgsModel;
        this.pcmHandler = pcmHandler;
        this.vsllocModel = vsllocModel;
    }

    String newTime = "2016-01-01T00:00:01Z";
    String oldTime;
    TimeStampHelper timeStampHelper = new TimeStampHelper();

    public void getNewMessages(){
        this.oldTime = newTime;
        this.newTime = TimeStampHelper.getCurrentTimeStamp();
        pcmHandler.getMessagesBetweenTimes(oldTime, newTime);
    }



    public void applyNewMessages(){
        for (String str : pcmHandler.getPortCallMessagesAsStrings(pcmHandler.getRelevantPCMs(pcmHandler.getLatestFetchBatch()))) {
            logModel.addMessage(str);
        }
        int[] currentPositionInfo = logModel.getMessagePositions();
        msgsView.changePositionInfo(currentPositionInfo);
        vesselUpdateAction();
    }

    public void vesselUpdateAction(){
        for (ArrayList<String> vesselInfo : pcmHandler.getMultipleVesselsTravelinfo(pcmHandler.getLatestFetchBatch())) {
            vsllocModel.addInfo(vesselInfo.get(0), vesselInfo.get(1), vesselInfo.get(2));
        }
        updateFullVesselLocationView();
    }

    public void updateFullVesselLocationView(){
        String overview = "";
        for (PolygonWithBoats polygon : vsllocModel.getPolygons()){
            overview = overview + "Polygon: " + polygon.getID().toString() + "\n " +
                    "Current Vessels: " + polygon.countConfirmedVessels() + "\n " +
                    "Estimated Vessels: " + polygon.countEstimatedVessels() + "\n " +
                    "\n " +
                    "\n ";
        }
        vsllocView.updateVesselLocs(overview);
    }

    public void templateChanged(){
        msgrView.enableDeleteButton();
        String templateKey = msgrView.getSelectedTemplate();
        String templateValue = tmpltsModel.getTemplateFromKey(templateKey);
        msgrView.setMessageBoxText(templateValue);
        msgrView.labelField.removeAll();
        logModel.updateLabelList(templateValue);
        String[] labels = logModel.getLabels();
        for (String str : labels) {
            msgrView.addLabel(str);
            msgrView.labelField.repaint();
        }
        if (templateKey.equals("")){
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == msgrView.templateMenu) {
            templateChanged();
        }

        if(o == msgrView.sendButton) {
            logModel.applyLabelsToMessage(msgrView.tThree.getText(), msgrView.getLabels());
            pcmHandler.respondToMessage(logModel.getMessage());
            msgsView.append(logModel.getMessage());
            logModel.setCurrentMessageAsAnswered();
            msgrView.disableSendButtons();
        }

        if(o == msgrView.confirmButton){
            logModel.applyLabelsToMessage(msgrView.tThree.getText(), msgrView.getLabels());
            pcmHandler.respondToMessageWithStatement(logModel.getMessage(), ServiceTimeSequence.CONFIRMED);
            logModel.addInfoToCurrentMessage("SENT MESSAGE BELOW" + "\n" +
                    "\n" + logModel.getMessage() + "\n" + "\n" + "CONFIRMED");
            msgsView.append(logModel.getLogMessage());
            logModel.setCurrentMessageAsAnswered();
            msgrView.disableSendButtons();
        }
        if(o == msgrView.denyButton){
            logModel.applyLabelsToMessage(msgrView.tThree.getText(), msgrView.getLabels());
            pcmHandler.respondToMessageWithStatement(logModel.getMessage(), ServiceTimeSequence.DENIED);
            logModel.addInfoToCurrentMessage("SENT MESSAGE BELOW" + "\n" +
                    "\n" + logModel.getMessage() + "\n" + "\n" + "DENIED");
            msgsView.append(logModel.getLogMessage());
            logModel.setCurrentMessageAsAnswered();
            msgrView.disableSendButtons();
        }
        if(o == msgrView.newTemplateButton) {
            msgrView.createTemplateName();
            String templateKey = msgrView.getLastCreatedTemplateName();
            String templateValue = msgrView.getMessageBoxText();
            if (templateKey != null && templateValue != null) {
                tmpltsModel.addTemplate(templateKey, templateValue);
                msgrView.addSingleTemplate(templateKey);
            }
            tmpltsModel.saveTemplatesMap();
            return;
        }
        if(o == msgrView.deleteMessageButton) {
            try {
                tmpltsModel.removeTemplate(msgrView.getSelectedTemplate());
                msgrView.removeSelectedTemplateFromMenu();
            }
            catch (NullPointerException exception) {
            }
        }
        if (o == timer) {
            getNewMessages();
            applyNewMessages();
        }

        if (o == msgsView.previousMessageButton) {

            try {
                logModel.goToPreviousMessage();
                msgsView.append(logModel.getLogMessage());
                int[] currentPositionInfo = logModel.getMessagePositions();
                msgsView.changePositionInfo(currentPositionInfo);
                msgrView.enableSendButtons();
                pcmHandler.setSelectedPCMIndex(logModel.getSelectedLogMessageIndex());
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
            msgrView.goToEmptyTemplate();
            templateChanged();
            if (logModel.isAnsweredTo()){
                msgrView.disableSendButtons();
            }
        }

        if (o == msgsView.nextMessageButton) {
            try {
                logModel.goToNextMessage();
                msgsView.append(logModel.getLogMessage());
                int[] currentPositionInfo = logModel.getMessagePositions();
                msgsView.changePositionInfo(currentPositionInfo);
                msgrView.enableSendButtons();
                pcmHandler.setSelectedPCMIndex(logModel.getSelectedLogMessageIndex());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            msgrView.goToEmptyTemplate();
            templateChanged();
            if (logModel.isAnsweredTo()){
                msgrView.disableSendButtons();
            }
        }
        if (o == msgsView.updateLogButton) {
            try {
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}