import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    public JButton sendButton, newTemplateButton, deleteMessageButton;
    public JComboBox<String> templateMenu;
    public HashMap<String, String> templates;
    public JPanel labelField;
    public JPanel northPanel;
    public String lastCreatedTemplateName;

    // Constructor connection receiving a socket number
    MessengerView(Controller controller) {

        super("Chat Client");

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
        newTemplateButton = new JButton("New Template");
        newTemplateButton.addActionListener(controller);
        deleteMessageButton = new JButton("Delete Template");
        deleteMessageButton.addActionListener(controller);

        // Fills the southPanel with content and adds it to the frame
        southPanel.add(sendButton);
        southPanel.add(newTemplateButton);
        southPanel.add(deleteMessageButton);
        add(southPanel, BorderLayout.SOUTH);

        templates = new HashMap<String, String>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        tThree.requestFocus();

        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                // templatesHandlerModel.saveTemplatesMap();
            }
        });
    }

    public void addLabel(String str){
        JTextField jtf = new JTextField(str);
        labelField.add(jtf);
        revalidate();
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


    public void createTemplateName(){
        lastCreatedTemplateName = JOptionPane.showInputDialog(null, "Ange namn för standardmeddelandet");
    }

    public String getLastCreatedTemplateName(){
        return lastCreatedTemplateName;
    }
}