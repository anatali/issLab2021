package it.unibo.enablerCleanArch.enablers.devices;

 
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class SonarApplHandler extends ApplMsgHandler  {
ISonar sonar;
  
		public SonarApplHandler(String name, ISonar sonar) {
			super(name);
			this.sonar = sonar;
	 	}
  			
 			@Override
			public void elaborate(String message, Interaction2021 conn) {
 				//Colors.out(name+ " | elaborate " + message + " conn=" + conn, Colors.ANSI_YELLOW);
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
