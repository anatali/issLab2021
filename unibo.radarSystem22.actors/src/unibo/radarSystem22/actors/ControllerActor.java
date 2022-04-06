package unibo.radarSystem22.actors;
 
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import unibo.actor22.*;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.radarSystem22.actors.main.RadarSystemConfig;

/*
 * Il controller conosce SOLO I NOMI dei dispositivi 
 * (non ha riferimenti ai dispositivi-attori)
 */
public class ControllerActor extends QakActor22{
protected int numIter = 1;
protected IApplMessage getStateRequest ;
protected IRadarDisplay radar;
protected boolean on = true;

	public ControllerActor(String name  ) {
		super(name);
		radar = DeviceFactory.createRadarGui();
		if( ! RadarSystemConfig.sonarObservable) 
			getStateRequest  = Qak22Util.buildRequest(name,"ask", ApplData.reqLedState, ApplData.ledName);
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {  
		if( msg.isEvent() )        elabEvent(msg);
		else if( msg.isDispatch()) elabCmd(msg) ;	
		else if( msg.isReply() )   elabReply(msg) ;	
 	}
	

	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd + " obs=" + RadarSystemConfig.sonarObservable, ColorsOut.GREEN);
		switch( msgCmd ) {
			case ApplData.cmdActivate : {
				sendMsg( ApplData.activateSonar);
				doControllerWork();
 	 			break;
			}
			default:break;
		}		
	}
	
	protected void elabReply(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabReply=" + msg, ColorsOut.GREEN);
		//if( msg.msgId().equals(ApplData.reqDistance ))
		String dStr = msg.msgContent();
		int d = Integer.parseInt(dStr);
		//Radar
		radar.update(dStr, "60");
		//LedUse case
		if( d <  RadarSystemConfig.DLIMIT ) {
			forward(ApplData.turnOnLed); 		
			forward(ApplData.deactivateSonar);
			CommUtils.delay(2000);
			System.exit(0);
		}
		else {
			forward(ApplData.turnOffLed); 
	    	CommUtils.delay(500);
	    	doControllerWork();
		}
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

    protected void doControllerWork() {
		CommUtils.aboutThreads(getName()  + " |  Before doControllerWork " + RadarSystemConfig.sonarObservable );
    	if( ! RadarSystemConfig.sonarObservable)  request( ApplData.askDistance );
	}	

}
