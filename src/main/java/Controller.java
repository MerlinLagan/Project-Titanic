import eu.portcdm.messaging.ServiceTimeSequence;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Jakob on 15/05/17.
 */


// Controller class - MVC Pattern



public class Controller implements ActionListener {

    // Declare models and views variables

    MessagesViewsModels messagesViewsModels;
    TemplatesHandlerModel tmpltsModel;
    MessengerView msgrView;
    MessagesView msgsView;
    PCMHandlerModel pcmHandler;
    PCMSenderModel pcmSender;
    PCMFetcherModel pcmFetcher;
    VesselLocationModel vsllocModel;
    VesselLocationView vsllocView;
    Timer timer;


    // Initialize application
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

    // Adding views
    public void addViews(MessengerView msgrView, MessagesView msgsView, VesselLocationView vsllocView){
        this.msgsView = msgsView;
        this.msgrView = msgrView;
        this.vsllocView = vsllocView;
    }

    // Adding models
    public void addModels(TemplatesHandlerModel tmpltsModel, MessagesViewsModels msgsModel,
                          PCMHandlerModel pcmHandler, VesselLocationModel vsllocModel){
        this.tmpltsModel = tmpltsModel;
        this.messagesViewsModels = msgsModel;
        this.pcmHandler = pcmHandler;
        this.vsllocModel = vsllocModel;
    }

    String startTime = TimeStampHelper.getCurrentTimeStampMinusOneSec();
    String newTime = startTime;
    String oldTime;


    // Tell model to get PCmessages since last time method was called upon
    public void getNewMessages(){
        this.oldTime = newTime;
        this.newTime = TimeStampHelper.getCurrentTimeStamp();
        pcmHandler.getMessagesBetweenTimes(oldTime, newTime);
    }

    // Applies latest message fetch-batch from model to view
    public void applyNewMessages(){
        for (String str : pcmHandler.getPortCallMessagesAsStrings(pcmHandler.getRelevantPCMs(pcmHandler.getLatestFetchBatch()))) {
            messagesViewsModels.addMessage(str);
        }
        int[] currentPositionInfo = messagesViewsModels.getMessagePositions();
        msgsView.changePositionInfo(currentPositionInfo);
        vesselUpdateAction();
    }

    // Tells vessel location model to update itself based on new info from PCMHandlerModel's fetched messages
    public void vesselUpdateAction(){
        for (ArrayList<String> vesselInfo : pcmHandler.getMultipleVesselsTravelinfo(pcmHandler.getLatestFetchBatch())) {
            vsllocModel.addInfo(vesselInfo.get(0), vesselInfo.get(1), vesselInfo.get(2));
        }
        updateFullVesselLocationView();
    }

    // Update view based on vessel location model
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

    // Update messengerview based on new template
    public void templateChanged(){
        msgrView.enableDeleteButton();
        String templateKey = msgrView.getSelectedTemplate();
        String templateValue = tmpltsModel.getTemplateFromKey(templateKey);
        msgrView.setMessageBoxText(templateValue);
        msgrView.labelField.removeAll();
        messagesViewsModels.updateLabelList(templateValue);
        String[] labels = messagesViewsModels.getLabels();
        for (String str : labels) {
            msgrView.addLabel(str);
            msgrView.labelField.repaint();
        }
        if (templateKey.equals("")){
        }
    }

    // Actionlisteners for buttons in various panels
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == msgrView.templateMenu) {
            templateChanged();
        }

        if(o == msgrView.sendButton) {
            messagesViewsModels.applyLabelsToMessage(msgrView.tThree.getText(), msgrView.getLabels());
            pcmHandler.respondToMessage(messagesViewsModels.getMessage());
            msgsView.append(messagesViewsModels.getMessage());
            messagesViewsModels.setCurrentMessageAsAnswered();
            msgrView.disableSendButtons();
        }

        if(o == msgrView.confirmButton){
            messagesViewsModels.applyLabelsToMessage(msgrView.tThree.getText(), msgrView.getLabels());
            pcmHandler.respondToMessageWithStatement(messagesViewsModels.getMessage(), ServiceTimeSequence.CONFIRMED);
            messagesViewsModels.addInfoToCurrentMessage("SENT MESSAGE BELOW" + "\n" +
                    "\n" + messagesViewsModels.getMessage() + "\n" + "\n" + "CONFIRMED");
            msgsView.append(messagesViewsModels.getLogMessage());
            messagesViewsModels.setCurrentMessageAsAnswered();
            msgrView.disableSendButtons();
        }
        if(o == msgrView.denyButton){
            messagesViewsModels.applyLabelsToMessage(msgrView.tThree.getText(), msgrView.getLabels());
            pcmHandler.respondToMessageWithStatement(messagesViewsModels.getMessage(), ServiceTimeSequence.DENIED);
            messagesViewsModels.addInfoToCurrentMessage("SENT MESSAGE BELOW" + "\n" +
                    "\n" + messagesViewsModels.getMessage() + "\n" + "\n" + "DENIED");
            msgsView.append(messagesViewsModels.getLogMessage());
            messagesViewsModels.setCurrentMessageAsAnswered();
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
                messagesViewsModels.goToPreviousMessage();
                msgsView.append(messagesViewsModels.getLogMessage());
                int[] currentPositionInfo = messagesViewsModels.getMessagePositions();
                msgsView.changePositionInfo(currentPositionInfo);
                msgrView.enableSendButtons();
                pcmHandler.setSelectedPCMIndex(messagesViewsModels.getSelectedLogMessageIndex());
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
            msgrView.goToEmptyTemplate();
            templateChanged();
            if (messagesViewsModels.isAnsweredTo()){
                msgrView.disableSendButtons();
            }
        }

        if (o == msgsView.nextMessageButton) {
            try {
                messagesViewsModels.goToNextMessage();
                msgsView.append(messagesViewsModels.getLogMessage());
                int[] currentPositionInfo = messagesViewsModels.getMessagePositions();
                msgsView.changePositionInfo(currentPositionInfo);
                msgrView.enableSendButtons();
                pcmHandler.setSelectedPCMIndex(messagesViewsModels.getSelectedLogMessageIndex());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            msgrView.goToEmptyTemplate();
            templateChanged();
            if (messagesViewsModels.isAnsweredTo()) {
                msgrView.disableSendButtons();
            }
        }
    }
}