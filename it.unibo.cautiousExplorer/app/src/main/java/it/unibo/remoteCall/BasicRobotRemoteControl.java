/*
============================================================
BasicRobotRemoteControl
Sends commands over TCP-8010
============================================================
 */
package it.unibo.remoteCall;
import it.unibo.actor0.sysUtil;
import it.unibo.interaction.IUniboActor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;


public class BasicRobotRemoteControl extends AbstractRobotRemote {

    public BasicRobotRemoteControl(String name) {
        super(name);
    }

    @Override
    protected void msgDriven(JSONObject infoJson) {
        try {
            //System.out.println("BasicRobotControl | msgDriven  " + infoJson);
            if (infoJson.has("start")) {
                this.engageBasicRobot(true);
                doMove("l", "basicRobot");
                doMove("r", "basicRobot");

            }else if (infoJson.has("endmove") ) {
                String moveResult = infoJson.getString("endmove");
                System.out.println("BasicRobotControl | endmove " + moveResult );

                if( moveResult.equals("true")) doMove("w", "basicRobot");
                else{
                    System.out.println("BasicRobotControl | BYE  "  );
                    this.turn180("basicRobot");
                    doMove("w", "basicRobot");
                    this.engageBasicRobot(false);
                    terminate();
                    //System.exit(1);
                }
            }else if (infoJson.has("sonarInfo") ) {
                //avoid to show data
            }

        }catch( Exception e){
            System.out.println("BasicRobotControl ERROR:" + e.getMessage());
        }

    }

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("BasicRobotRemoteControl | main " + sysUtil.aboutThreads("main") ); //
        System.out.println("================================================================");
        //Configure the system
        BasicRobotRemoteControl controller = new BasicRobotRemoteControl("controller");
        ObserverRemoteNaive  obs           = new ObserverRemoteNaive("obs");
        controller.send( startDefaultMsg );

    }

    @Override
    public void registerActor(@NotNull IUniboActor iUniboActor) {

    }

    @Override
    public void removeActor(@NotNull IUniboActor iUniboActor) {

    }
}
