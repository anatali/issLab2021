package it.unibo.enablerCleanArch.enablers.devices;

 
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class SonarApplHandler extends ApplMsgHandler  {
ISonar sonar;
  
		public SonarApplHandler(String name) {
			super(name);
			sonar = it.unibo.enablerCleanArch.domain.SonarModel.createSonarMock();
	 	}
  			
 			@Override
			public void elaborate(String message, Interaction2021 conn) {
 				//Colors.out(name+ " | elaborate " + message, Colors.ANSI_YELLOW);
 				if( message.equals("getVal")) {
 					String vs = ""+sonar.getVal();
 					this.sendMsgToClient(vs, conn);
 				}else if( message.equals("activate")) {
 					sonar.activate();
 				}else if( message.equals("activate")) {
 					sonar.deactivate();
 				}else if( message.equals("isActive")) {
 					String sonarState = ""+sonar.isActive();
 					this.sendMsgToClient(sonarState, conn);
 				}
 			}

 			
		
}
