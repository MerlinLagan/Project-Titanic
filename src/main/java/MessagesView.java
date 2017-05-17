/**
 * Created by oscarsands on 05/05/2017.
 */

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/*
 * The Client with its GUI
 */
public class MessagesView extends JFrame {


    private static final long serialVersionUID = 1L;
    // to Logout and get the list of the users
    public JButton previousMessageButton, nextMessageButton;
    // for the chat room
    private JTextArea loggArea;
    // the default port numb    er


    MessagesView(Controller controller) {

        super("Logg");


        JPanel centerPanel = new JPanel(new GridLayout());

        // The content for the centerPanel
        loggArea = new JTextArea("SampleThree");
        loggArea.setBackground(Color.WHITE);
        loggArea.setSize(15, this.getWidth());

        // Fills the centerPanel with content and adds it to the frame
        centerPanel.add(loggArea);
        add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();

        // The content for the southPanel
        String newerMessages = "0";
        previousMessageButton = new JButton("Previous Message");
        previousMessageButton.addActionListener(controller);
        nextMessageButton = new JButton("Next Message (" + newerMessages + ")");
        nextMessageButton.addActionListener(controller);

        // Fills the southPanel with content and adds it to the frame
        southPanel.add(previousMessageButton);
        southPanel.add(nextMessageButton);
        add(southPanel, BorderLayout.SOUTH);

        //templates = new HashMap<String, String>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
    }

    // Append text in the TextArea
    void append(String str) {
        loggArea.append(str + "\n");
        loggArea.setCaretPosition(loggArea.getText().length() - 1);
    }

    // called by the GUI is the connection failed
    // we reset our buttons, label, textfield
    void connectionFailed() {
        // metod som kallades på om connection misslyckades
        System.out.println("CONNECTION FAILED");
        append("shit failed");
    }

}