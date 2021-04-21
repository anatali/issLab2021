/*
============================================================
BasicRobotRemoteControl
Sends commands over TCP-8010
============================================================
 */
package it.unibo.remoteCall;
import it.unibo.actor0.sysUtil;
import org.json.JSONObject;


public class BasicStepRobotRemoteControl extends AbstractRobotRemote {

    public BasicStepRobotRemoteControl(String name) {
        super(name);
    }

    protected void performBasicMoves(){
        doMove("l", "stepRobot");
        moves.updateMovesRep("l");
        doMove("r", "stepRobot");
        moves.updateMovesRep("r");
        doMove("w", "stepRobot");   //non control over result?
        moves.updateMovesRep("w");
    }

    protected void performStepMoves(){
        doStep();
    }

    @Override
    protected void msgDriven(JSONObject infoJson) {
        try {
            System.out.println("BasicStepRobotRemoteControl | " + infoJson);
            if (infoJson.has("start")) {
                //performBasicMoves();
                doStep();
            }else if (infoJson.has("stepDone") ) {
                moves.updateMovesRep("w");
                moves.showMap();
                String direction = moves.getDirection();
                if( direction.equals("DOWN") ) doStep();
            }else if (infoJson.has("stepFail") ) {
                /*
                turn180("stepRobot");
                moves.showMap();
                doStep();*/
            }else if (infoJson.has("endMove") ) {
                String result = infoJson.getString("endMove");
                System.out.println("BasicStepRobotRemoteControl | result=" + result);
            }

        }catch( Exception e){
            System.out.println("BasicStepRobotRemoteControl ERROR:" + e.getMessage());
        }

    }

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("BasicRobotRemoteControl | main " + sysUtil.aboutThreads("main") ); //
        System.out.println("================================================================");
        //Configure the system
        BasicStepRobotRemoteControl controller = new BasicStepRobotRemoteControl("controller");
        ObserverRemoteNaive  obs               = new ObserverRemoteNaive("obs");
        controller.send( startDefaultMsg );

    }
}
