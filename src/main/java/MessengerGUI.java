//https://github.com/jakoblrssn/Project_Estonia.git

// susanna.winther@hiq.se

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/*
 * The Client with its GUI
 */

// Mooring vill ha när fartyg passerar rappunkt 1.
// Agenten vill ha när fartyg passerar rappunkt 1.
// Skjuter in tidsstämplar när fartyg går in i området.
// Hör med backengänget om de kan få till en linje i Kartan där man kan
// automatgenerera
//

public class MessengerGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel label;
    public JTextArea tThree;
    private JButton sendButton, newMessageButton, deleteMessageButton;
    private JComboBox<String> templateMenu;
    public MessagesGUI messagesGui;
    // private App app;
    private HashMap<String, String> templates;
    public JPanel labelField;
    public JPanel northPanel;
    public TemplatesHandler templatesHandler;

    // Constructor connection receiving a socket number
    MessengerGUI() {

        super("Chat Client");
        messagesGui = new MessagesGUI();
        templatesHandler = new TemplatesHandler();

        northPanel = new JPanel(new GridLayout(3,1));

        // The content for the northPanel
        labelField = new JPanel(new GridLayout(1,5, 1, 3));
        templateMenu = new JComboBox();
        templateMenu.addActionListener(this);
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
        sendButton.addActionListener(this);
        newMessageButton = new JButton("New Template");
        newMessageButton.addActionListener(this);
        deleteMessageButton = new JButton("Delete Template");
        deleteMessageButton.addActionListener(this);

        // Fills the southPanel with content and adds it to the frame
        southPanel.add(sendButton);
        southPanel.add(newMessageButton);
        southPanel.add(deleteMessageButton);
        add(southPanel, BorderLayout.SOUTH);

        templates = new HashMap<String, String>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        tThree.requestFocus();

        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                templatesHandler.saveTemplatesMap();
            }
        });
    }

    public void addLabel(String str){
        JTextField jtf = new JTextField(str);
        labelField.add(jtf);
        revalidate();
    }

    public void loadTemplatesMap(String mapName){
        templatesHandler.loadTemplatesMap(mapName);
        templates = templatesHandler.getTemplates();
        applyTemplates(templates);
    }

    public void removeTemplatesFromMenu(){
        templateMenu.removeAllItems();
    }

    public void applyTemplates(HashMap<String, String> templates){
        removeTemplatesFromMenu();
        for (String key : templates.keySet()) {
            templateMenu.addItem(key);
        }
    }

    public String applyLabelsToMessage(String messageText) {

        int[] LabelPositions = LabelHandler.getLabelPositions(messageText);

        if (LabelPositions.length == 0){
            return messageText;
        }

        String newString = "";
        Boolean firstiteration = true;

        Component[] JTexts = labelField.getComponents();

        for (int i = 1; i <= JTexts.length; i++)
        {
            JTextField currentTextField = (JTextField)JTexts[i-1];
            String currentLabelEntry = currentTextField.getText();
            System.out.println(currentLabelEntry);
            int leftBracketPos = LabelPositions[(2*(i-1))];
            int rightBracketPos = LabelPositions[(2*(i)-1)];

            if (firstiteration == false) {
                int previousRightBracketPos = LabelPositions[(2*(i-2)+1)];
                System.out.println("prev right bracket pos = " + previousRightBracketPos);
                newString = newString + messageText.substring(previousRightBracketPos, leftBracketPos-1) +
                        currentLabelEntry;
                if (i == JTexts.length) {
                    newString = newString + messageText.substring(rightBracketPos, messageText.length());
                }
            }
            else {
                newString = messageText.substring(0, leftBracketPos - 1) + currentLabelEntry;
                System.out.println(newString);
                 }
            firstiteration = false;
        }
        return newString;
    }

    public void setMessageBox(String str){
        tThree.setText(str);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == templateMenu) {
            JComboBox cb = (JComboBox)e.getSource();
            String chosenTemplate = (String)cb.getSelectedItem();
            templatesHandler.action(chosenTemplate, this);
        }

        if(o == sendButton) {
            messagesGui.append(applyLabelsToMessage(tThree.getText()));

			/*
			if (app == null)
				app = new App();
			App.main(null);
			return;
			*/

        }
        if(o == newMessageButton) {
            String templateName = JOptionPane.showInputDialog(null, "Ange namn för standardmeddelandet");
            String templateContent = tThree.getText();
            if (templateName != null && templateContent != null) {
                templates.put(templateName, templateContent);
                templateMenu.addItem(templateName);
            }
            return;
        }
        if(o == deleteMessageButton) {
            try {
                templates.remove(templateMenu.getSelectedItem().toString());
                templateMenu.removeItem(templateMenu.getSelectedItem());
            }
            catch (NullPointerException exception) {
                System.out.println("list is already empty");
            }
        }
    }
}