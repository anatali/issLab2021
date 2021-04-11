/*
============================================================
BoundaryWalkerRemoteControl
    Use BasicStepRobotActor  that is able to execute commands
    - to perform basic moves
    - to perform a step
============================================================
 */
package it.unibo.remoteCall;

import it.unibo.actor0.ApplMessage;
import org.json.JSONObject;

public class BoundaryWalkerRemoteControl extends AbstractRobotRemote { //
    private int count = 1;

    public BoundaryWalkerRemoteControl(String name) {
        super(name);
    }

    protected void nextMove(String answerJsonStr) throws Exception {
        //{"stepDone":"ok" } {"stepFail":"163" }
        System.out.println("BoundaryWalkerRemoteControl | nextMove - answerJsonStr=" + answerJsonStr );
        JSONObject answer = new JSONObject(answerJsonStr);
        if( answer.has("stepDone")){
            moves.updateMovesRep("w");
            moves.showMap();
            //ActorBasicJava.delay(500);
            doStep();
        }else if( answer.has("stepFail")){
                //moves.setObstacle();      //AVOID in order to exclude ERROR:Index -1 out of bounds
                String pathSoFar = moves.getJourney();
                System.out.println("BoundaryWalkerRemoteControl | obstacle - pathSoFar=" + pathSoFar + " count=" + count);
                 moves.showMap();
                 doMove("l", "stepRobot");
                if( count > 3 ) {
                     terminate();
                }else{
                    count++;
                    doStep();
                }
            moves.showMap();
        }else {
            System.out.println("BoundaryWalkerRemoteControl | todo ... ");
        }
    }



    protected String reverse( String s  ){
        if( s.length() <= 1 )  return s;
        else return reverse( s.substring(1) ) + s.charAt(0) ;
    }
    @Override
    protected void handleInput(String s) {
        if( ! s.contains("sonarInfo")) System.out.println("BoundaryWalkerRemoteControl handleInput:" + s);
        ApplMessage msg = ApplMessage.create(s);
        String msgId    = msg.getMsgId();
        try {
            //System.out.println("BoundaryWalkerRemoteControl handleInput msg=" + msgId);
            if (msgId.equals("start")) {
                //startConn();      //already done by AbstractRobotRemote
                //doMove("l");    //to avoid sonar
                moves.showMap();
                doStep();
            }else if(msgId.equals("stepAnswer")){
                nextMove(msg.getMsgContent());
            }
          }catch( Exception e){
            String direction = moves.getDirection();
            System.out.println("BoundaryWalkerRemoteControl ERROR:" + e.getMessage());
            System.out.println("BoundaryWalkerRemoteControl direction:" + direction);
            if( direction.equals("UP")) {
                try {
                    nextMove(msg.getMsgContent());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void msgDriven(JSONObject infoJson) {

    }

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("BoundaryWalkerRemoteControl | main "  ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
        //Configure the system
        BoundaryWalkerRemoteControl walker = new BoundaryWalkerRemoteControl("walker");
        ObserverRemoteNaive  obs           = new ObserverRemoteNaive("obs");
        walker.send( startDefaultMsg );

    }
}
