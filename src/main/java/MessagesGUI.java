/**
 * Created by oscarsands on 05/05/2017.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * The Client with its GUI
 */
public class MessagesGUI extends JFrame implements ActionListener {


    private static final long serialVersionUID = 1L;
    // to Logout and get the list of the users
    private JButton buttonOne, buttonTwo, buttonThree;
    // for the chat room
    private JTextArea ta;
    // the default port number

    //TODO Lägg in hållare för API-uppkopplingsnycklar

    // Constructor connection receiving a socket number
    MessagesGUI() {

        super();

        ta = new JTextArea("Welcome to the VTS log \n \n \n", 80, 80);
        JPanel centerPanel = new JPanel(new GridLayout(1,1));
        centerPanel.add(new JScrollPane(ta));
        ta.setEditable(false);
        this.add(centerPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);

    }

    // Append text in the TextArea
    void append(String str) {
        ta.append(str + "\n");
        ta.setCaretPosition(ta.getText().length() - 1);
    }

    // called by the GUI is the connection failed
    // we reset our buttons, label, textfield
    void connectionFailed() {
        // metod som kallades på om connection misslyckades
        System.out.println("CONNECTION FAILED");
        append("shit failed");
    }

    /*
     * Button or JTextField clicked
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        // if it is the buttonTwo
        if(o == buttonOne) {
            return;
        }
        // if it the who is in button
        if(o == buttonTwo) {
            return;
        }

        if(o == buttonThree) {
            // ok it is a connection request
            // tf.getText().trim();
            // TILL EXEMPEL KAN DENNA KNAPP DISABLEA KNAPPAR FRAM OCH
            // TILLBAKA FÖR ATT MAN NU ÄR CONNECTAD MOT EN AKTÖR
            // OCH INTE SKA KUNNA T.EX CONNECTA MOT EN NY UTAN ATT DISCONNECTA FÖRST
        }

    }

    // to start the whole thing the server
    public static void main(String[] args) {
    }

}