package it.unibo.radarSystem22_4.appl.handler;

 
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22_4.appl.interpreter.SonarApplInterpreter;
import it.unibo.radarSystem22_4.comm.ApplMsgHandler;
import it.unibo.radarSystem22_4.comm.interfaces.IApplInterpreter;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMsgHandler;
import it.unibo.radarSystem22_4.comm.interfaces.Interaction2021;

 

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
		public void elaborate(IApplMessage message, Interaction2021 conn) {
 			ColorsOut.out(name+ " | elaborate " + message + " conn=" + conn, ColorsOut.BLUE);
 			if( message.equals("getDistance") || message.equals("isActive")  ) {
 				sendMsgToClient( sonarIntepr.elaborate(message), conn );
   			}else sonarIntepr.elaborate(message);
 		}
}
