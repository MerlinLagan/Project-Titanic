import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jakob on 15/05/17.
 */

public class Controller implements ActionListener {

    MessagesHandlerModel msgsModel;
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
        vesselViewTester();
        updateFullVesselLocationView();
        timer = new Timer(5000, this);
        timer.setInitialDelay(0);
        timer.start();
    }

    public void addViews(MessengerView msgrView, MessagesView msgsView, VesselLocationView vsllocView){
        this.msgsView = msgsView;
        this.msgrView = msgrView;
        this.vsllocView = vsllocView;
    }

    public void addModels(TemplatesHandlerModel tmpltsModel, MessagesHandlerModel msgsModel,
                          PCMHandlerModel pcmHandler, PCMFetcherModel pcmFetcher, PCMSenderModel pcmSender,
                          VesselLocationModel vsllocModel){
        this.tmpltsModel = tmpltsModel;
        this.msgsModel = msgsModel;
        this.pcmHandler = pcmHandler;
        this.pcmFetcher = pcmFetcher;
        this.pcmSender = pcmSender;
        this.vsllocModel = vsllocModel;
    }

    String newTime = "";
    String oldTime;
    TimeStampHelper timeStampHelper = new TimeStampHelper();

    public void getMessages(){
        System.out.println("haj");
        oldTime = newTime;
        newTime = TimeStampHelper.getCurrentTimeStamp();
       // pcmFetcher.fetchMessagesBetweenTimes(oldTime, newTime);
        System.out.println(oldTime);
        System.out.println(newTime);
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public void vesselViewTester(){
        vsllocModel.addInfo("Lindholmen", "1", "CONFIRMED");
        vsllocModel.addInfo("Lindholmen", "1", "CONFIRMED");
        vsllocModel.addInfo("Arendal", "7", "CONFIRMED");
        vsllocModel.addInfo("Arendal", "1", "ESTIMATED");
        vsllocModel.addInfo("Arendal", "2", "ESTIMATED");
        vsllocModel.addInfo("Arendal", "3", "ESTIMATED");
        vsllocModel.addInfo("Arendal", "4", "ESTIMATED");
        vsllocModel.addInfo("EriksBerg", "1", "CONFIRMED");
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


    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == msgrView.templateMenu) {
            String templateKey = msgrView.getSelectedTemplate();
            String templateValue = tmpltsModel.getTemplateFromKey(templateKey);
            msgrView.setMessageBoxText(templateValue);
            msgrView.labelField.removeAll();
            msgsModel.updateLabelList(templateValue);
            String[] labels = msgsModel.getLabels();
            for (String str : labels) {
                msgrView.addLabel(str);
                msgrView.labelField.repaint();
            }
        }

        if(o == msgrView.sendButton) {
            msgsModel.applyLabelsToMessage(msgrView.tThree.getText(), msgrView.getLabels());
            msgsView.append(msgsModel.getMessage());
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
                System.out.println("list is already empty");
            }
        }
        if (o == timer) {
            msgsModel.addMessageTest();
            int[] currentPositionInfo = msgsModel.getMessagePositions();
            msgsView.changePositionInfo(currentPositionInfo);
        }

        if (o == msgsView.previousMessageButton) {
            try {
                msgsModel.goToPreviousMessage();
                msgsView.append(msgsModel.getLogMessage());
                int[] currentPositionInfo = msgsModel.getMessagePositions();
                msgsView.changePositionInfo(currentPositionInfo);
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }

        if (o == msgsView.nextMessageButton) {
            try {
                msgsModel.goToNextMessage();
                msgsView.append(msgsModel.getLogMessage());
                int[] currentPositionInfo = msgsModel.getMessagePositions();
                msgsView.changePositionInfo(currentPositionInfo);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (o == msgsView.clearLogButton) {
            try {
                msgsView.clearLog();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}