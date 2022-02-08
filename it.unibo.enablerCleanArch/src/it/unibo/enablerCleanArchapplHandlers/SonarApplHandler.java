package it.unibo.enablerCleanArchapplHandlers;

 
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IApplLogic;
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
 
private IApplLogic sonarLogic;

public static IApplMsgHandler create(String name, ISonar sonar) {
	if( Utils.isCoap() ) {
		return new SonarResourceCoap("sonar",  new SonarApplLogic(sonar) );
	}else {
		return new SonarApplHandler(name, sonar);
	}
}

  
		public SonarApplHandler(String name, ISonar sonar) {
			super(name);
			sonarLogic = new SonarApplLogic(sonar);
			ColorsOut.out(name+ " | SonarApplHandler CREATED with sonar= " + sonar, ColorsOut.BLUE);
	 	}
 
		@Override
		public void elaborate( ApplMessage message, Interaction2021 conn ) {
			ColorsOut.out(name+ " | elaborate ApplMessage " + message + " conn=" + conn, ColorsOut.BLUE);
			String payload = message.msgContent();
			if( message.isRequest() ) {
				String answer = sonarLogic.elaborate(message);
				if( Utils.isMqtt() ) sendAnswerToClient( answer  );
				else sendMsgToClient( answer, conn );
			}else sonarLogic.elaborate( message.msgContent() ); //non devo inviare risposta
		}
		
 			@Override
			public void elaborate(String message, Interaction2021 conn) {
 				ColorsOut.out(name+ " | elaborate " + message + " conn=" + conn, ColorsOut.BLUE);
 				if( message.equals("getDistance") || message.equals("isActive")  ) {
   				}else sonarLogic.elaborate(message);
 			}
}
