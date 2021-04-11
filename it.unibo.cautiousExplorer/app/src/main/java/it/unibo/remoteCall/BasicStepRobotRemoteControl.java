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

    @Override
    protected void msgDriven(JSONObject infoJson) {
        try {
            System.out.println("BasicStepRobotRemoteControl | " + infoJson);
            if (infoJson.has("start")) {
                doStep();
            }else if (infoJson.has("stepDone") ) {
                moves.updateMovesRep("w");
                moves.showMap();
                String direction = moves.getDirection();
                if( direction.equals("DOWN") ) doStep();
            }else if (infoJson.has("stepFail") ) {
                turn180("stepRobot");
                moves.showMap();
                doStep();
            }else if (infoJson.has("sonarInfo") ) {
                //avoid to show data
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
