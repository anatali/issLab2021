/*
===============================================================
ClientBoundaryWebsockBasicAsynch.java
Use the cril language and the support specified in the
configuration file WebsocketBasicConfig.txt

GOAL: use request instead of requestSynch
and handle the information sent by WEnv over the cmdSocket-8091
===============================================================
*/
package it.unibo.wenv;
import it.unibo.annotations.IssProtocolSpec;
import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports.IssCommsSupportFactory;
import org.json.JSONObject;

@IssProtocolSpec( configFile ="WebsocketBasicConfig.txt" )
public class ClientBoundaryWebsockBasicAsynch {
    private IssOperations support;

    //Factory method
    public static ClientBoundaryWebsockBasicAsynch create(){
        ClientBoundaryWebsockBasicAsynch obj = new ClientBoundaryWebsockBasicAsynch();
        IssOperations support                = new IssCommsSupportFactory().create( obj  );
        obj.setCommSupport(support);
        support.registerObserver( new RobotObserver() );    //!!!!
        return obj;
    }

    protected void setCommSupport(IssOperations support){
        this.support = support;
    }

    protected void doRobotAsynchMove(String jsonMoveStr) {
        //"{\"robotmove\":\"...\", \"time\": ...}";
        JSONObject jsonObj = new JSONObject(jsonMoveStr);
        int time = Integer.parseInt( jsonObj.get("time").toString() );
        support.forward( jsonMoveStr );
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
        //We not not know the answer
    }

    public String boundary( ) {
        return doBoundary(1,"",support);
    }

    private String doBoundary(int stepNum, String journey, IssOperations rs) {
        if (stepNum > 4) {
            return journey;
        }
        String answer = rs.requestSynch( MsgRobotUtil.forwardMsg );
        while( answer.equals("true") ){
            journey = journey + "w";
            answer = rs.requestSynch( MsgRobotUtil.forwardMsg );
        }
        //collision
        rs.requestSynch(MsgRobotUtil.turnLeftMsg);
        return doBoundary(stepNum + 1, journey + "l", rs);
    }


    public static void main(String args[]){
        ClientBoundaryWebsockBasicAsynch appl = ClientBoundaryWebsockBasicAsynch.create();
        //String trip = appl.boundary();
        //System.out.println("trip="+trip);
        appl.doRobotAsynchMove( MsgRobotUtil.forwardMsg );
    }
}
