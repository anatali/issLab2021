package unibo.basicRobot22;

/*
 * BasicRobot riceve comandi aril  
 * Comando aril: 
 */

import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import org.json.JSONObject;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22comm.utils.ColorsOut;
 
 


public class BasicRobot22 extends QakActor22FsmAnnot{
protected  RobotAdapter22 robotAdapter;
protected  IApplMessage curRequest;
 	
	public BasicRobot22(String name) {
		super(name);
		robotAdapter = new RobotAdapter22(getName());
	}

	@State( name = "activate", initial=true)
	@Transition( state = "work", msgId=ApplData.robotCmdId )   
	protected void activate( IApplMessage msg ) {
		outInfo(""+msg);
  	}
 
	@State( name = "work" )
  	@Transition( state = "handleOk", msgId="endMoveOk"  )
 	@Transition( state = "handleKo", msgId="endMoveKo"  )
	protected void work( IApplMessage msg ) {
		outInfo(""+msg);  //msg(move,request,...,basicrobot,w,3)
		curRequest = msg;
		String cmd = msg.msgContent().replace("'","");
 		robotAdapter.robotMove(cmd);
 	}
 
	@State( name = "handleOk" )
	@Transition( state = "work", msgId=ApplData.robotCmdId  )
 	protected void handleOk( IApplMessage msg ) {
		outInfo(""+msg);
		this.updateResourceRep( ""+msg );
		IApplMessage reply = MsgUtil.buildReply(getName(), msg.msgId(), msg.msgContent(), curRequest.msgSender());
		ColorsOut.outappl(reply.toString(), ColorsOut.WHITE_BACKGROUND);
		this.sendReply( curRequest, reply );
  	}
   	
	@State( name = "handleKo" )
	@Transition( state = "work", msgId="move"  )
	protected void handleKo( IApplMessage msg ) {
		outInfo(""+msg);

		JSONObject json = new JSONObject( msg.msgContent().replace("'",""));
		String move  = json.getString("move");
		int duration = json.getInt("duration") ;
		this.updateResourceRep(move + " failed after:"+duration);

		IApplMessage reply = MsgUtil.buildReply(getName(), msg.msgId(), ""+duration, curRequest.msgSender());
		ColorsOut.outappl(reply.toString(), ColorsOut.YELLOW_BACKGROUND);
		this.sendReply( curRequest, reply );

		//this.updateResourceRep( ""+msg );
 	}
 
	
	@State( name = "endJob" )
	protected void endJob( IApplMessage msg ) {
		outInfo(""+msg);
   	}
	
 
	
}

 