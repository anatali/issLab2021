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
            //System.out.println("BasicRobotControl | msgDriven  " + infoJson);
            if (infoJson.has("start")) {
                this.engageBasicRobot(true);
                doMove("l");
                doMove("r");
                //this.engageBasicRobot(false);
            }else if (infoJson.has("endmove") ) {
                String moveResult = infoJson.getString("endmove");
                System.out.println("BasicRobotControl | endmove " + moveResult );
                if( moveResult.equals("true")) doMove("w");
                else{
                    System.out.println("BasicRobotControl | BYE  "  );
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
        System.out.println("BasicRobotControl | main "  ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
        //Configure the system
        BasicRobotRemoteControl controller = new BasicRobotRemoteControl("controller");
        controller.send( startDefaultMsg );

    }
}
