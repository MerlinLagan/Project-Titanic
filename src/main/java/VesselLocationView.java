import javax.swing.*;
import java.awt.*;

/**
 * Created by Jakob on 17/05/17.
 */
public class VesselLocationView extends JFrame {

    JTextArea vesselLocationArea;

    VesselLocationView(Controller controller) {

        super("VesselLocations");

        JPanel centerPanel = new JPanel(new GridLayout());

        // The content for the centerPanel
        vesselLocationArea = new JTextArea("");
        vesselLocationArea.setEditable(false);
        vesselLocationArea.setBackground(Color.WHITE);
        vesselLocationArea.setSize(15, this.getWidth());

        // Fills the centerPanel with content and adds it to the frame
        centerPanel.add(vesselLocationArea);
        add(centerPanel, BorderLayout.CENTER);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
    }

    public void updateVesselLocs(String str){
        vesselLocationArea.setText(str);
    }

}
