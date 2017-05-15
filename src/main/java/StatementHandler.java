import eu.portcdm.dto.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by maxlundstrom on 15/05/17.
 */
public class StatementHandler  {

    // Här lagras statements samt den senaste callen.
    HashMap<String,String> latestStatements;
    PortCall activeCall;

    // Konstruktor, kräver en call och hämtar statements därifrån direkt.
    public StatementHandler(PortCall call) {
        activeCall = call;
        latestStatements = getAllLatestStatements(activeCall);
    }

    // Ändrar vilket call vi hämtar samt uppdaterar senaste statements
    public boolean setActiveCall(PortCall call) {
        if (call == null){
            return false;
        }
        activeCall = call;
        latestStatements = getAllLatestStatements(activeCall);
        return true;
    }

    // Hämtar en statement utefter sitt ID. Fullständig lista är ej sammanställd.
    public String getStatement(String id) {
        return latestStatements.get(id);
    }

    // Lägger in alla senaste statements i en HashMap med statens ID som key och timeType + timeStatement som value.
    private HashMap<String, String> getAllLatestStatements(PortCall call){

        // Skapar ny hashmap
        HashMap<String, String> output = new HashMap<String, String>();

        // Tar ut alla ProcessSteps från callen
        List<ProcessStep> steps = call.getProcessSteps();

        for (ProcessStep step : steps) {

            //System.out.println("Step: " + step.getDefinitionName());

            // Tar ut alla SubProcesses från ProcessStepen
            List<SubProcess> substeps = step.getSubProcesses();

            for (SubProcess substep : substeps){

                //System.out.println("\tSubstep: " + substep.getDefinitionName());

                // Tar ut alla Events från SubProcessen
                List<Event> events = substep.getEvents();

                for (Event event : events){

                    //System.out.println("\t\tEvent: " + event.getDefinitionName());

                    // Tar ut alla States från SubProcessen
                    List<State> states = event.getStates();

                    for (State state : states) {

                        //System.out.println("\t\t\tState: " + state.getDefinitionName() + ", id: " + state.getStateDefinitionId());

                        String id = state.getStateDefinitionId();

                        // Tar ut alla Statements från Staten
                        List<Statement> statements = state.getStatements();

                        // Kollar om de finns
                        if (!statements.isEmpty()) {

                            // Tar fram senaste uppdateringen
                            Statement statement = statements.get(statements.size() - 1);

                            // Kombinerar timeType och timeStatement till en metod
                            String time = statement.getTimeType().toString() + " " + statement.getTimeStatement();

                            // Lägger in den i hashmapen
                            output.put(id, time);
                        }
                    }
                }
            }
        }

        // Returnerar alla statements som hittades
        return output;
    }


}
