/*
============================================================
BasicRobotRemoteControl
Sends commands over TCP-8010
============================================================
 */
package it.unibo.stepServiceCaller;
import org.json.JSONObject;


public class BasicRobotRemoteControl extends AbstractRobotRemote {

 
    public BasicRobotRemoteControl(String name) {
        super(name);
    }


    @Override
    protected void msgDriven(JSONObject infoJson) {
        try {
            System.out.println("BasicRobotControl msgDriven  " + infoJson);

            if (infoJson.has("start")) {
                //startConn();
                for( int i=1; i<=2; i++) {
                    delay(500);
                    moves.showMap();
                    doMove("w");
                    moves.updateMovesRep("w");
                }
                moves.showMap();
                moves.showJourney();
                //conn.closeConnection();
                terminate();
                //System.exit(1);
            }

        }catch( Exception e){
            System.out.println("BasicRobotControl ERROR:" + e.getMessage());
        }

    }

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("BasicRobotControl | main "  ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
        //Configure the system
        BasicRobotRemoteControl controller = new BasicRobotRemoteControl("controller");
        controller.send( startDefaultMsg );

    }
}
