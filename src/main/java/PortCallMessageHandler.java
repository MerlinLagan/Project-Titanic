/**
 * Created by maxlundstrom on 15/05/17.
 */
import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.dto.Port;
import eu.portcdm.dto.PortCall;
import eu.portcdm.dto.PortCallSummary;

import java.util.List;
    public class PortCallMessageHandler {
        public StateupdateApi stateUpdateApi;

        // Här lagras APIn och callen.
        public ApiClient connectorClient;
        PortcallsApi portcallsApi;
        PortCall activeCall;
        public String baseurl = "http://192.168.56.101:8080/dmp";
        List<PortCallSummary> summaries;

        // Konstruktor, skapar ett api och hämtar senaste callen.
        public PortCallMessageHandler(){
            initiateStateupdateAPI();
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

        private StateupdateApi initiateStateupdateAPI() {
            connectorClient = new ApiClient();
            connectorClient.setConnectTimeout(15);
            connectorClient.addDefaultHeader("X-PortCDM-UserId", "porter");
            connectorClient.addDefaultHeader("X-PortCDM-Password", "porter");
            connectorClient.addDefaultHeader("X-PortCDM-APIKey", "eeee");
            connectorClient.setBasePath(baseurl);
            stateUpdateApi = new StateupdateApi(connectorClient);
            return stateUpdateApi;
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

