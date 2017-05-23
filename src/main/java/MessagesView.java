/**
 * Created by oscarsands on 05/05/2017.
 */

import javax.swing.*;
import java.awt.*;

/*
 * The Client with its GUI
 */
public class MessagesView extends JFrame {


    private static final long serialVersionUID = 1L;
    public JButton previousMessageButton, nextMessageButton, updateLogButton;
    private JTextArea loggArea;

    MessagesView(Controller controller) {

        super("Logg");

        JPanel centerPanel = new JPanel(new GridLayout());

        // The content for the centerPanel
        loggArea = new JTextArea();
        loggArea.setBackground(Color.WHITE);
        loggArea.setSize(15, this.getWidth());
        loggArea.setEditable(false);
        JScrollPane scrollPanel = new JScrollPane(loggArea);

        // Fills the centerPanel with content and adds it to the frame
        centerPanel.add(scrollPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();

        // The content for the southPanel
        String newerMessages = "0";
        previousMessageButton = new JButton("Previous Message (0)");
        previousMessageButton.setEnabled(false);
        previousMessageButton.addActionListener(controller);
        nextMessageButton = new JButton("Next Message (0)");
        nextMessageButton.setEnabled(false);
        nextMessageButton.addActionListener(controller);
        updateLogButton = new JButton ("Update Log");
        updateLogButton.addActionListener(controller);


        // Fills the southPanel with content and adds it to the frame
        southPanel.add(previousMessageButton);
        southPanel.add(nextMessageButton);
        southPanel.add(updateLogButton);
        add(southPanel, BorderLayout.SOUTH);

        //templates = new HashMap<String, String>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(false);
    }

    public void changePositionInfo(int[] positionInListInfo){
        int nextMessagesCount = positionInListInfo[0]-positionInListInfo[1];
        int previousMessagesCount = positionInListInfo[0]-nextMessagesCount;
        previousMessageButton.setText("Previous Message (" + previousMessagesCount + ")");
        nextMessageButton.setText("Next Message (" + (nextMessagesCount-1) + ")");
        if (previousMessagesCount == 0)
            previousMessageButton.setEnabled(false);
        else previousMessageButton.setEnabled(true);
        if (nextMessagesCount-1 == 0)
            nextMessageButton.setEnabled(false);
        else nextMessageButton.setEnabled(true);

    }
    public void clearLog(){
        loggArea.setText("");
    }

    // Append text in the TextArea
    public void append(String str) {
        clearLog();
        loggArea.append(str + "\n");
        loggArea.setCaretPosition(loggArea.getText().length() - 1);
    }

    public void appendWithOneBreak(String str) {
        clearLog();
        loggArea.append(str + "\n");
        loggArea.setCaretPosition(loggArea.getText().length() - 1);
    }

}