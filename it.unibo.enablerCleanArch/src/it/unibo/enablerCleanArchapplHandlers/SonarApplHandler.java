package it.unibo.enablerCleanArchapplHandlers;

 
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
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
			Colors.out(name+ " | elaborate ApplMessage " + message + " conn=" + conn, Colors.ANSI_YELLOW);
			String payload = message.msgContent();
			if( message.isRequest() ) {
				if(payload.equals("getDistance") ) {
					String vs = ""+sonar.getDistance().getVal();
					ApplMessage reply = Utils.buildReply("sonar", "distance", vs, message.msgSender()) ;
					sendMsgToClient(reply, conn ); 
				}else if(payload.equals("isActive") ) {
 					String sonarState = ""+sonar.isActive();
					ApplMessage reply = Utils.buildReply("sonar", "sonarstate", sonarState, message.msgSender()) ;
					sendMsgToClient(reply, conn ); 					
				}
			}else elaborate(payload, conn);			
		}
		
 			@Override
			public void elaborate(String message, Interaction2021 conn) {
 				Colors.out(name+ " | elaborate " + message + " conn=" + conn, Colors.ANSI_YELLOW);
 				if( message.equals("getDistance")) {
 	 				//Colors.out(name+ " | elaborate getDistance="  , Colors.ANSI_YELLOW);
					String vs = ""+sonar.getDistance().getVal();
 	 				//Colors.out(name+ " | elaborate vs=" + vs, Colors.ANSI_YELLOW);
					this.sendMsgToClient(vs, conn);
 				}else if( message.equals("activate")) {
 					sonar.activate();
 				}else if( message.equals("deactivate")) {
 					sonar.deactivate();
 				}else if( message.equals("isActive")) {
 					String sonarState = ""+sonar.isActive();
 					this.sendMsgToClient(sonarState, conn);
 				}
 			}
}
