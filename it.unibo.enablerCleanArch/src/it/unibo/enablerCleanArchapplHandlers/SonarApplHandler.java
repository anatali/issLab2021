package it.unibo.enablerCleanArchapplHandlers;

 
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IApplInterpreter;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarApplInterpreter;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
 

public class SonarApplHandler extends ApplMsgHandler  {
 
private IApplInterpreter sonarIntepr;

public static IApplMsgHandler create(String name, ISonar sonar) {
	if( Utils.isCoap() ) {
		return new SonarResourceCoap("sonar",  new SonarApplInterpreter(sonar) );
	}else {
		return new SonarApplHandler(name, sonar);
	}
}
		public SonarApplHandler(String name, ISonar sonar) {
			super(name);
			sonarIntepr = new SonarApplInterpreter(sonar);
			ColorsOut.out(name+ " | SonarApplHandler CREATED with sonar= " + sonar, ColorsOut.BLUE);
	 	}
 
		@Override
		public void elaborate( ApplMessage message, Interaction2021 conn ) {
			ColorsOut.out(name+ " | elaborate ApplMessage " + message + " conn=" + conn, ColorsOut.BLUE);
			String payload = message.msgContent();
			if( message.isRequest() ) {
				String answer = sonarIntepr.elaborate(message);
				if( Utils.isMqtt() ) sendAnswerToClient( answer  );
				else sendMsgToClient( answer, conn );
			}else sonarIntepr.elaborate( message.msgContent() ); //non devo inviare risposta
		}
		
 			@Override
			public void elaborate(String message, Interaction2021 conn) {
 				ColorsOut.out(name+ " | elaborate " + message + " conn=" + conn, ColorsOut.BLUE);
 				if( message.equals("getDistance") || message.equals("isActive")  ) {
 					sendMsgToClient( sonarIntepr.elaborate(message), conn );
   				}else sonarIntepr.elaborate(message);
 			}
}
