package unibo.radarSystem22.actors;
 
import it.unibo.kactor.IApplMessage;
import unibo.actor22.*;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.radarSystem22.actors.main.RadarSystemConfig;

/*
 * Il controller conosce SOLO I NOMI dei dispositivi 
 * (non ha riferimenti ai dispositivi-attori)
 */
public class ControllerActorAsObserver extends QakActor22{
protected int numIter = 1;
protected IApplMessage getStateRequest ;
protected boolean on = true;

	public ControllerActorAsObserver(String name  ) {
		super(name);
		getStateRequest  = Qak22Util.buildRequest(name,"ask", ApplData.reqLedState, ApplData.ledName);
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {  
		if( msg.isEvent() ) elabEvent(msg);
		else  elabCmd(msg) ;	
 	}
	
	protected void elabEvent(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabEvent=" + msg, ColorsOut.GREEN);
		if( msg.isEvent()  ) {  //defensive
			String dstr = msg.msgContent();
			int d       = Integer.parseInt(dstr);
			if( d <  RadarSystemConfig.DLIMIT ) {
				forward(ApplData.turnOnLed); 		
				forward(ApplData.deactivateSonar);
			}
			else {
				forward(ApplData.turnOffLed); 
			}
		}
	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd, ColorsOut.GREEN);
		switch( msgCmd ) {
			case ApplData.cmdActivate : {
				sendMsg( ApplData.activateSonar);
 	 			break;
			}
			default:break;
		}		
	}

}
