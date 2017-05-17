/**
 * Created by Jakob on 17/05/17.
 */
public class BoatWithLocation {
    String vesselID;
    String estimatedLocation;
    String currentLocation;

    public BoatWithLocation(String vesselID){
        this.vesselID = vesselID;
    }

    public void setCurrentLocation(String currentLocation){
        this.currentLocation = currentLocation;
    }

    public void setEstimatedLocation(String estimatedLocation){
        this.estimatedLocation = estimatedLocation;
    }

    public String getCurrentLocation(){
        return this.currentLocation;
    }

    public String getID(){
        return this.vesselID;
    }

    @Override
    public boolean equals(Object object)
    {
        System.out.println("hej");
        if (object instanceof BoatWithLocation) {
            BoatWithLocation boat = (BoatWithLocation) object;
            return this.vesselID.equals(boat.vesselID);
        }
        return false;
    }
    @Override
    public int hashCode(){
        return Integer.parseInt(vesselID);
    }
}
