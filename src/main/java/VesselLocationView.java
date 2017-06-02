// View for vessel locations in VTS area

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jakob on 17/05/17.
 */
public class VesselLocationView extends JFrame {

    JTextArea vesselLocationArea;
    JScrollPane jps;

    VesselLocationView(Controller controller) {

        super("VesselLocations");

        JPanel centerPanel = new JPanel(new GridLayout());


        // The content for the centerPanel
        vesselLocationArea = new JTextArea("");
        vesselLocationArea.setEditable(false);
        vesselLocationArea.setBackground(Color.WHITE);
        vesselLocationArea.setSize(15, this.getWidth());

        JScrollPane jps = new JScrollPane(vesselLocationArea);

        centerPanel.add(jps);
        add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(false);
    }

    public void updateVesselLocs(String str){
        vesselLocationArea.setText(str);
    }
}