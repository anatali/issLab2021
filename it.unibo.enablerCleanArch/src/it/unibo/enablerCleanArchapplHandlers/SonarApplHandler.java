package it.unibo.enablerCleanArchapplHandlers;

 
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarApplLogic;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
 

public class SonarApplHandler extends ApplMsgHandler  {
private ISonar sonar;

public static IApplMsgHandler create(String name, ISonar sonar) {
	if( Utils.isCoap() ) {
		return new SonarResourceCoap("led",  new SonarApplLogic(sonar) );
	}else {
		return new SonarApplHandler(name, sonar);
	}
}

  
		public SonarApplHandler(String name, ISonar sonar) {
			super(name);
			this.sonar = sonar;
			ColorsOut.out(name+ " | SonarApplHandler CREATED with sonar= " + sonar, ColorsOut.BLUE);
	 	}
 
		@Override
		public void elaborate( ApplMessage message, Interaction2021 conn ) {
			ColorsOut.out(name+ " | elaborate ApplMessage " + message + " conn=" + conn, ColorsOut.BLUE);
			String payload = message.msgContent();
			if( message.isRequest() ) {
				if(payload.equals("getDistance") ) {
					String vs = ""+sonar.getDistance().getVal();
					ApplMessage reply = Utils.prepareReply( message, vs);  //Utils.buildReply("sonar", "distance", vs, message.msgSender()) ;
					//sendAnswerToClient(reply.toString());
					sendMsgToClient( reply.toString(), conn );

				}else if(payload.equals("isActive") ) {
 					String sonarState = ""+sonar.isActive();
					ApplMessage reply = Utils.prepareReply( message, sonarState); //Utils.buildReply("sonar", "sonarstate", sonarState, message.msgSender()) ;
  					//sendAnswerToClient(reply.toString());
					sendMsgToClient( reply.toString(), conn );
				}
			}else elaborate(payload, conn);			
		}
		
 			@Override
			public void elaborate(String message, Interaction2021 conn) {
 				ColorsOut.out(name+ " | elaborate " + message + " conn=" + conn, ColorsOut.BLUE);
 				if( message.equals("getDistance")) {
 	 				//ColorsOut.out(name+ " | elaborate getDistance="  , ColorsOut.BLUE);
					String vs = ""+sonar.getDistance().getVal();
 	 				ColorsOut.out(name+ " | elaborate vs=" + vs, ColorsOut.BLUE);
					sendMsgToClient(vs, conn);
 				}else if( message.equals("activate")) {
 					ColorsOut.out(name+ " | activate sonar="+sonar , ColorsOut.BLUE);
 					sonar.activate();
 				}else if( message.equals("deactivate")) {
 					sonar.deactivate();
 				}else if( message.equals("isActive")) {
 					String sonarState = ""+sonar.isActive();
 					sendMsgToClient(sonarState, conn);					 
  				}
 			}
}
