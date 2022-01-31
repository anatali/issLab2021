package it.unibo.enablerCleanArchapplHandlers;

 
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarApplHandler extends ApplMsgHandler  {
ISonar sonar;
  
		public SonarApplHandler(String name, ISonar sonar) {
			super(name);
			this.sonar = sonar;
	 	}
 
		@Override
		public void elaborate( ApplMessage message, Interaction2021 conn ) {
			ColorsOut.out(name+ " | elaborate ApplMessage " + message + " conn=" + conn, ColorsOut.BLUE);
			String payload = message.msgContent();
			if( message.isRequest() ) {
				if(payload.equals("getDistance") ) {
					String vs = ""+sonar.getDistance().getVal();
					ApplMessage reply = prepareReply( message, vs);  //Utils.buildReply("sonar", "distance", vs, message.msgSender()) ;
					sendAnswerToClient(reply.toString());

				}else if(payload.equals("isActive") ) {
 					String sonarState = ""+sonar.isActive();
					ApplMessage reply = prepareReply( message, sonarState); //Utils.buildReply("sonar", "sonarstate", sonarState, message.msgSender()) ;
  					sendAnswerToClient(reply.toString());
				}
			}else elaborate(payload, conn);			
		}
		
 			@Override
			public void elaborate(String message, Interaction2021 conn) {
 				ColorsOut.out(name+ " | elaborate " + message + " conn=" + conn, ColorsOut.BLUE);
 				if( message.equals("getDistance")) {
 	 				//Colors.out(name+ " | elaborate getDistance="  , Colors.BLUE);
					String vs = ""+sonar.getDistance().getVal();
 	 				//Colors.out(name+ " | elaborate vs=" + vs, Colors.BLUE);
					this.sendMsgToClient(vs, conn);
 				}else if( message.equals("activate")) {
 					sonar.activate();
 				}else if( message.equals("deactivate")) {
 					sonar.deactivate();
 				}else if( message.equals("isActive")) {
 					String sonarState = ""+sonar.isActive();
 					//this.sendMsgToClient(sonarState, conn);					 
  				}
 			}
}
