package it.unibo.radarSystem22.distrib.intepreters;
import it.unibo.comm2022.interfaces.IApplInterpreter;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.utils.CommUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.interfaces.ISonar;

public class SonarApplInterpreter implements IApplInterpreter{
private	ISonar sonar;

	public SonarApplInterpreter(ISonar sonar) {
		this.sonar = sonar;
	}

	@Override
	public String elaborate( IApplMessage message) {
		ColorsOut.out( "SonarApplInterpreter | elaborate ApplMessage " + message  , ColorsOut.BLUE);		 
		String payload = message.msgContent();
		if( message.isRequest() ) {
			if(payload.equals("getDistance") ) {
				String vs = ""+sonar.getDistance().getVal();
				IApplMessage reply = CommUtils.prepareReply( message, vs);  //Utils.buildReply("sonar", "distance", vs, message.msgSender()) ;
				//sendAnswerToClient(reply.toString());
				//sendMsgToClient( reply.toString(), conn );
				return reply.toString();
			}else if(payload.equals("isActive") ) {
					String sonarState = ""+sonar.isActive();
				IApplMessage reply = CommUtils.prepareReply( message, sonarState); //Utils.buildReply("sonar", "sonarstate", sonarState, message.msgSender()) ;
					//sendAnswerToClient(reply.toString());
				//sendMsgToClient( reply.toString(), conn );
				return reply.toString();
			}else return "request_unknown";
		}else return elaborate( payload );			
	}

	@Override
	public String elaborate(String message) {
		ColorsOut.out("SonarApplInterpreter | elaborate " + message, ColorsOut.BLUE);
			if( message.equals("getDistance")) {
				//ColorsOut.out(name+ " | elaborate getDistance="  , ColorsOut.BLUE);
				return ""+sonar.getDistance().getVal();
 			}else if( message.equals("activate")) {
				ColorsOut.out("SonarApplInterpreter | activate sonar="+sonar , ColorsOut.BLUE);
				sonar.activate();
			}else if( message.equals("deactivate")) {
				sonar.deactivate();
			}else if( message.equals("isActive")) {
				return ""+sonar.isActive();
 			}
 		return message+"_done";
	}
	

}
