package it.unibo.radarSystem22.sprint2.handlers;

 
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
 
  

public class SonarApplHandler extends ApplMsgHandler  {
  
public static IApplMsgHandler create(String name, ISonar sonar) {
 		return new SonarApplHandler(name, sonar); 
}
		public SonarApplHandler(String name, ISonar sonar) {
			super(name);
 			ColorsOut.out(name+ " | SonarApplHandler CREATED with sonar= " + sonar, ColorsOut.BLUE);
	 	}
 
		@Override
		public void elaborate( IApplMessage message, Interaction2021 conn ) {
 		}
		
 		@Override
		public void elaborate(String message, Interaction2021 conn) {
 			ColorsOut.out(name+ " | elaborate " + message + " conn=" + conn, ColorsOut.BLUE);
 			if( message.equals("getDistance") || message.equals("isActive")  ) {
 					sendMsgToClient( message, conn );
   			}else {} 
 		}
}
