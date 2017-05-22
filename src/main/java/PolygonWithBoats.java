import java.util.HashMap;

/**
 * Created by Jakob on 17/05/17.
 */
public class PolygonWithBoats {
    String polygonID;
    String estimatedLocation;
    String confirmedLocation;
    int numberOfEstimates;
    int numberOfConfirmed;

    HashMap<String, Integer> estimatesToPolygon;
    HashMap<String, String> confirmedToPolygon;

    public PolygonWithBoats(String polygonID) {
        this.polygonID = polygonID;
        estimatesToPolygon = new HashMap<String, Integer>();
        confirmedToPolygon = new HashMap<String, String>();
    }

    public void addEstimatedBoat(String vesselID) {
        estimatesToPolygon.put(vesselID, /* minutes left til estimate */ 10);
    }

    public void addConfirmedBoat(String vesselID) {
        confirmedToPolygon.put(vesselID, "time arrived");
    }

    public void removeConfirmedBoat(String vesselID) {
        confirmedToPolygon.remove(vesselID);
        numberOfConfirmed = confirmedToPolygon.size();
    }

    public void removeEstimatedBoat(String vesselID) {
        estimatesToPolygon.remove(vesselID);
        numberOfEstimates = estimatesToPolygon.size();
    }

    public HashMap<String, Integer> getEstimatedVessels() {
        return estimatesToPolygon;
    }

    public HashMap<String, String> getConfirmedVessels() {
        return confirmedToPolygon;
    }

    public int countEstimatedVessels() {
        return estimatesToPolygon.size();
    }

    public int countConfirmedVessels() {
        return confirmedToPolygon.size();
    }

    public String getID() {
        return this.polygonID;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PolygonWithBoats) {
            PolygonWithBoats polygon = (PolygonWithBoats) object;
            boolean x = (this.hashCode() == polygon.hashCode());
            return x;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return polygonID.hashCode();
    }
}