
// har ett objekt med alla vessels samt deras locations, baserat på Actual locations & estimates
// Sker kontinuerligt genom att fetchas från varje nytt port call message där föregående skrivs över

// Initieras genom:
// Fetcha alla messages från väldigt långt tillbaka
// Gå igenom nyast - äldst och om VesselID:t inte registrerats än så tar man currentloc esimatedloc och lägger in
// om ESTIMATED är nyare i tiden än ACTUALLOCATION vet vi att det även finns en estimate om framtiden

import java.util.ArrayList;

/**
 * Created by Jakob on 17/05/17.
 */

public class VesselLocationModel {

    String confirmed = "CONFIRMED";
    String estimated = "ESTIMATED";

    ArrayList<PolygonWithBoats> polygons;

    public VesselLocationModel() {
        polygons = new ArrayList<PolygonWithBoats>();
    }

    public ArrayList<PolygonWithBoats> getPolygons(){
        return polygons;
    }

    public void addInfo(String polygonID, String vesselID, String reportState) {
        PolygonWithBoats newPolygon = new PolygonWithBoats(polygonID);
        if (!polygons.contains(newPolygon)) {
            if (reportState.equals(confirmed)){
                for (PolygonWithBoats e : polygons) {
                    e.removeConfirmedBoat(vesselID);
                    e.removeEstimatedBoat(vesselID);
                }
                newPolygon.addConfirmedBoat(vesselID);
            }
            else
                newPolygon.addEstimatedBoat(vesselID);
            polygons.add(newPolygon);
        } else if (reportState.equals(confirmed)) {
            for (PolygonWithBoats e : polygons) {
                e.removeConfirmedBoat(vesselID);
                e.removeEstimatedBoat(vesselID);
                if (e.getID().equals(polygonID))
                    e.addConfirmedBoat(vesselID);
            }
        } else if (reportState.equals(estimated)) {
            for (PolygonWithBoats e : polygons) {
                e.removeEstimatedBoat(vesselID);
                if (e.getID().equals(polygonID))
                    e.addEstimatedBoat(vesselID);
            }
        }
    }
}