package unibo.radarSystem22.actors;

import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.QakActor22;
import unibo.actor22comm.utils.ColorsOut;

public class SonarActor extends QakActor22{
ISonar sonar;

//	public static ISonar createSonar(String name) {
//		if( DomainSystemConfig.simulation )  return createSonarMock( name );
//		else  return createSonarConcrete(name);		
//	}
//
//	public static ISonar createSonarMock( ) {
// 		return new SonarMockForActor( );
//	}	
//	public static ISonar createSonarConcrete(String name) {
// 		return new SonarConcreteForActor(name);
//	}

	public SonarActor(String name) {
		super(name);
		if( DomainSystemConfig.simulation )  sonar = new SonarMockForActor( );
		else new SonarConcreteForActor(name);
	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		ColorsOut.out( getName()  + " | handleMsg " + msg, ColorsOut.CYAN);
		if( msg.isRequest() ) elabRequest(msg);
		else elabCmd(msg);
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
 		switch( msgCmd ) {
			case ApplData.cmdActivate  : sonar.activate();break;
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgCmd);
		}
	}
 
	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		//ColorsOut.out( getName()  + " | elabRequest " + msgCmd, ColorsOut.CYAN);
		switch( msgReq ) {
			case ApplData.reqDistance  :{
				int d = sonar.getDistance().getVal();
				IApplMessage reply = MsgUtil.buildReply(getName(), ApplData.reqDistance, ""+d, msg.msgSender());
				ColorsOut.out( getName()  + " | reply= " + reply, ColorsOut.CYAN);
 				sendReply(msg, reply );				
				break;
			}
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgReq);
		}
	}

		
 
	

}
