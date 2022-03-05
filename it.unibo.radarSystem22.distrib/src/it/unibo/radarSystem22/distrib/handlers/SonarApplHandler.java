package it.unibo.radarSystem22.distrib.handlers;

 
import it.unibo.actorComm.ApplMsgHandler;
import it.unibo.actorComm.interfaces.IApplInterpreter;
import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.radarSystem22.distrib.intepreters.SonarApplInterpreter;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.interfaces.ISonar;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;
 

public class SonarApplHandler extends ApplMsgHandler  {
 
private IApplInterpreter sonarIntepr;

public static IApplMsgHandler create(String name, ISonar sonar) {
//	if( CommUtils.isCoap() ) {
//		return new SonarResourceCoap("sonar",  new SonarApplInterpreter(sonar) );
//	}else  
		return new SonarApplHandler(name, sonar);
	 
}
		public SonarApplHandler(String name, ISonar sonar) {
			super(name);
			sonarIntepr = new SonarApplInterpreter(sonar);
			ColorsOut.out(name+ " | SonarApplHandler CREATED with sonar= " + sonar, ColorsOut.BLUE);
	 	}
 
		@Override
		public void elaborate( IApplMessage message, Interaction2021 conn ) {
			ColorsOut.out(name+ " | elaborate ApplMessage " + message + " conn=" + conn, ColorsOut.BLUE);
			String payload = message.msgContent();
			if( message.isRequest() ) {
				String answer = sonarIntepr.elaborate(message);
				if( CommUtils.isMqtt() ) sendAnswerToClient( answer  );
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
