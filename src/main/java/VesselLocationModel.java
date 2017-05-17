/**
 * Created by Jakob on 17/05/17.
 */
public class VesselLocationModel {

    // har ett objekt med alla vessels samt deras locations, baserat på Actual locations & estimates
    // Sker kontinuerligt genom att fetchas från varje nytt port call message där föregående skrivs över

    // Initieras genom:
    // Fetcha alla messages från väldigt långt tillbaka
    // Gå igenom nyast - äldst och om VesselID:t inte registrerats än så tar man currentloc esimatedloc och lägger in
    // om ESTIMATED är nyare i tiden än ACTUALLOCATION vet vi att det även finns en estimate om framtiden

}
