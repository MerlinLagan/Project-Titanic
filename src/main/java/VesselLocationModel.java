
// har ett objekt med alla vessels samt deras locations, baserat på Actual locations & estimates
// Sker kontinuerligt genom att fetchas från varje nytt port call message där föregående skrivs över

// Initieras genom:
// Fetcha alla messages från väldigt långt tillbaka
// Gå igenom nyast - äldst och om VesselID:t inte registrerats än så tar man currentloc esimatedloc och lägger in
// om ESTIMATED är nyare i tiden än ACTUALLOCATION vet vi att det även finns en estimate om framtiden


import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Jakob on 17/05/17.
 */

public class VesselLocationModel {

    TreeMap boatPosMap = new TreeMap();

    public VesselLocationModel() {
       boatPosMap = new TreeMap();
    }


    public void updateLocations(String berth, String vesselID, String reportState){
        BoatWithLocation boat = new BoatWithLocation(vesselID);
        boatPosMap.put(boat.getID(), boat);
    }

    public static void main(String[] args){
        VesselLocationModel vslloc = new VesselLocationModel();
        vslloc.updateLocations("Lindholmen", "1", "CONFIRMED");
        vslloc.updateLocations("Lindholmen", "1", "CONFIRMED");
    }
}
