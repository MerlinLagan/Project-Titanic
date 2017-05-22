import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/*
 * The Client with its GUI
 */

// Mooring vill ha när fartyg passerar rappunkt 1.
// Agenten vill ha när fartyg passerar rappunkt 1.
// Skjuter in tidsstämplar när fartyg går in i området.
// Hör med backengänget om de kan få till en linje i Kartan där man kan
// automatgenerera

public class MessengerView extends JFrame{

    private static final long serialVersionUID = 1L;
    public JLabel label;
    public JTextArea tThree;
    public JButton sendButton, confirmButton, denyButton, newTemplateButton, deleteMessageButton;
    public JComboBox<String> templateMenu;
    public HashMap<String, String> templates;
    public JPanel labelField;
    public JPanel northPanel;
    public String lastCreatedTemplateName;

    // Constructor connection receiving a socket number
    MessengerView(Controller controller) {

        super("Messenger");

        northPanel = new JPanel(new GridLayout(3,1));

        // The content for the northPanel
        labelField = new JPanel(new GridLayout(1,5, 1, 3));
        templateMenu = new JComboBox();
        templateMenu.addActionListener(controller);
        label = new JLabel("Enter Message below", SwingConstants.CENTER);

        // Fills the northPanel with content and adds it to the frame
        northPanel.add(labelField);
        northPanel.add(templateMenu);
        northPanel.add(label);
        add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout());

        // The content for the centerPanel
        tThree = new JTextArea("SampleThree");
        tThree.setBackground(Color.WHITE);
        tThree.setSize(15, this.getWidth());

        // Fills the centerPanel with content and adds it to the frame
        centerPanel.add(tThree);
        add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();

        // The content for the southPanel
        sendButton = new JButton("Send");
        sendButton.addActionListener(controller);
        confirmButton = new JButton("confirm");
        confirmButton.addActionListener(controller);
        denyButton = new JButton("deny");
        denyButton.addActionListener(controller);
        newTemplateButton = new JButton("New Template");
        newTemplateButton.addActionListener(controller);
        deleteMessageButton = new JButton("Delete Template");
        deleteMessageButton.addActionListener(controller);
        deleteMessageButton.setEnabled(false);

        // Fills the southPanel with content and adds it to the frame
        southPanel.add(sendButton);
        southPanel.add(confirmButton);
        southPanel.add(denyButton);
        southPanel.add(newTemplateButton);
        southPanel.add(deleteMessageButton);
        add(southPanel, BorderLayout.SOUTH);

        templates = new HashMap<String, String>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        tThree.requestFocus();
    }

    public void addLabel(String str){
        JTextField jtf = new JTextField(str);
        labelField.add(jtf);
        revalidate();
    }

    public String[] getLabels() {

        String[] labels = new String[labelField.getComponentCount()];
        Component[] jTexts = labelField.getComponents();

        for (int i=0; i<labelField.getComponentCount(); i++) {

            labels[i] = ((JTextField)jTexts[i]).getText();

        }
        return labels;
    }

    public void loadTemplatesFromMap(HashMap<String, String> map){
        applyTemplates(map);
    }

    public void removeAllTemplatesFromMenu(){
        templateMenu.removeAllItems();
    }

    public void removeSelectedTemplateFromMenu(){
        templateMenu.removeItem(templateMenu.getSelectedItem());
    }

    public void addSingleTemplate(String key){
        templateMenu.addItem(key);
    }

    public void applyTemplates(HashMap<String, String> templates){
        removeAllTemplatesFromMenu();
        for (String key : templates.keySet()) {
            templateMenu.addItem(key);
        }
    }

    public void setMessageBoxText(String str){
        tThree.setText(str);
    }

    public String getMessageBoxText(){
        return tThree.getText();
    }

    public String getSelectedTemplate(){
        return (String)templateMenu.getSelectedItem();
    }

    public void disableDeleteButton(){
        deleteMessageButton.setEnabled(false);
    }

    public void enableDeleteButton(){
        deleteMessageButton.setEnabled(true);
    }

    public void disableSendButtons(){
        sendButton.setEnabled(false);
        confirmButton.setEnabled(false);
        denyButton.setEnabled(false);
    }

    public void enableSendButtons(){
        sendButton.setEnabled(true);
        confirmButton.setEnabled(true);
        denyButton.setEnabled(true);
    }

    public void createTemplateName(){
        lastCreatedTemplateName = JOptionPane.showInputDialog(null, "Ange namn för standardmeddelandet");
    }

    public void goToEmptyTemplate(){
        templateMenu.setSelectedIndex(0);
    }

    public String getLastCreatedTemplateName(){
        return lastCreatedTemplateName;
    }
}