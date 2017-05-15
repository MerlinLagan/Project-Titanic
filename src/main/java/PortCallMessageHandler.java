/**
 * Created by maxlundstrom on 15/05/17.
 */
import eu.portcdm.client.ApiClient;
import eu.portcdm.client.ApiException;
import eu.portcdm.client.service.PortcallsApi;
import eu.portcdm.client.service.StateupdateApi;
import eu.portcdm.dto.PortCall;
import eu.portcdm.dto.PortCallSummary;

import java.util.List;
    public class PortCallMessageHandler {
        public StateupdateApi stateUpdateApi;

        // Här lagras APIn och callen.
        public ApiClient connectorClient;
        PortcallsApi portcallsApi;
        PortCall currentCall;
        public String baseurl = "http://192.168.56.101:8080/dmp";
        List<PortCallSummary> summaries;

        // Konstruktor som anropar initiateStateupdateAPI och hämtare nuvarande portcall
        public PortCallMessageHandler(){
            initiateStateupdateAPI();
            summaries = getSummaries();
          // TODO   här skall vi hänvisa till portcallIDet (kolla vad summaries är)
            currentCall = getPortCall(0);
        }

        // Uppdaterar listan med PortCalls
        public boolean updateCalls(){
            summaries = getSummaries();
            if (summaries == null){
                return false;
            }
            else {
                return true;
            }
        }

        // Hämtar den nuvarande callen
        public PortCall getCurrentCall(){
            return currentCall;
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


        // Hämtar ett antal Summaries
        private List<PortCallSummary> getSummaries(){
            try {
                return portcallsApi.getAllPortCalls("porter", "porter"," porter", 30);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Hämtar ett PortCall
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

