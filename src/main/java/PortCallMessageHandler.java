/**
 * Created by maxlundstrom on 15/05/17.
 */
import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.dto.Port;
import eu.portcdm.dto.PortCall;
import eu.portcdm.dto.PortCallSummary;

import java.util.List;
public class PortCallMessageHandler {
    public class PortCallMesseageHandler {

        // Här lagras APIn och callen.
        PortcallsApi portcallsApi;
        PortCall activeCall;
        List<PortCallSummary> summaries;

        // Konstruktor, skapar ett api och hämtar senaste callen.
        public PortCallMesseageHandler(){
            setupApi();
            summaries = getSummaries();
            activeCall = getPortCall(0);
        }

        // Uppdaterar listan med PortCalls
        public boolean refreshCalls(){
            summaries = getSummaries();
            if (summaries == null){
                return false;
            }
            else {
                return true;
            }
        }

        // Hämtar den aktuella callen
        public PortCall getActiveCall(){
            return activeCall;
        }

        // Ansluter till backenden
        private void setupApi(){

            ApiClient apiClient = new ApiClient();

            // Adress till virtualboxens PortCDM Services
            apiClient.setBasePath( "http://192.168.56.101:8080/dmp");

            // Inlogg till servern
            apiClient.addDefaultHeader( "X-PortCDM-UserId", "porter" );
            apiClient.addDefaultHeader( "X-PortCDM-Password", "porter" );

            // API-key, används inte idag men måste finnas
            apiClient.addDefaultHeader( "X-PortCDM-ApiKey", "Fenix-SMA" );

            portcallsApi = new PortcallsApi(apiClient);
        }

        // Hämtar ett givet antal PortCallSummaries
        private List<PortCallSummary> getSummaries(){
            try {
                return portcallsApi.getAllPortCalls("porter", "porter"," porter", 30);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Hämtar ett givet PortCall
        public PortCall getPortCall(int id){
            PortCallSummary summary = summaries.get(id);
            try {
                return portcallsApi.getPortCall(summary.getId(), "porter", "porter"," porter");
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
