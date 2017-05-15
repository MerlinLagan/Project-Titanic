import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Jakob on 15/05/17.
 */

public class Controller implements ActionListener {

    MessagesHandlerModel msgsModel;
    TemplatesHandlerModel tmpltsModel;
    MessengerView msgrView;
    MessagesView msgsView;

    public void initialize(){
        tmpltsModel.loadTemplatesMap("data.properties");
        msgrView.loadTemplatesFromMap(tmpltsModel.getTemplates());
    }

    public void addViews(MessengerView msgrView, MessagesView msgsView){
        this.msgsView = msgsView;
        this.msgrView = msgrView;
    }

    public void addModels(TemplatesHandlerModel tmpltsModel, MessagesHandlerModel msgsModel){
        this.tmpltsModel = tmpltsModel;
        this.msgsModel = msgsModel;
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
                msgsModel.applyLabelsToMessage(msgrView.tThree.getText());
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
        }
    }