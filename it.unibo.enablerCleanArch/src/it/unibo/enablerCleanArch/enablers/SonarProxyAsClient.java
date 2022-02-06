package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.Distance;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
 	
	public SonarProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name,  host,  entry, protocol );
 	}
 	@Override
	public void activate() {
 		if( RadarSystemConfig.protcolType == ProtocolType.tcp && RadarSystemConfig.withContext )  
 			sendCommandOnConnection(Utils.sonarActivate.toString());
  		else if( RadarSystemConfig.protcolType == ProtocolType.mqtt)  
  			sendCommandOnConnection(Utils.sonarActivate.toString());
 		else if( RadarSystemConfig.protcolType == ProtocolType.coap) 
 			sendRequestOnConnection("activate");
 		else //CASO DI DEFAULT
 			sendCommandOnConnection("activate");		
	}

	@Override
	public void deactivate() {
 		if( RadarSystemConfig.protcolType == ProtocolType.tcp && RadarSystemConfig.withContext )  
 			sendCommandOnConnection(Utils.sonarDeactivate.toString());
  		else if( RadarSystemConfig.protcolType == ProtocolType.mqtt)  
  			sendCommandOnConnection(Utils.sonarDeactivate.toString());
		else if( RadarSystemConfig.protcolType == ProtocolType.coap) 
 			sendRequestOnConnection("deactivate");
 		else //CASO DI DEFAULT
 			sendCommandOnConnection("deactivate");		
	}

	@Override
	public IDistance getDistance() {
		String answer="";
		//Colors.out( name + " | getDistance ", Colors.ANSI_PURPLE);
		if( RadarSystemConfig.protcolType == ProtocolType.tcp && RadarSystemConfig.withContext ) 
			answer = sendRequestOnConnection( Utils.getDistance.toString() ) ;
  		else if( RadarSystemConfig.protcolType == ProtocolType.mqtt)  
  			answer = sendRequestOnConnection(Utils.getDistance.toString().replace("system", name)) ;
		else  //CASO DI DEFAULT
			answer = sendRequestOnConnection("getDistance");
		ColorsOut.out( name + " | getDistance answer="+answer, ColorsOut.ANSI_PURPLE);
		if( answer.startsWith("msg")){ //structured msg
			ApplMessage ma = new ApplMessage(answer);
			answer = ma.msgContent();
		}
		return new Distance( Integer.parseInt(answer) );
	}

	@Override
	public boolean isActive() {
		String answer = "";
		if( RadarSystemConfig.withContext )  answer = sendRequestOnConnection(Utils.isActive.toString()) ;
  		else if( RadarSystemConfig.protcolType == ProtocolType.mqtt)  
  			answer = sendRequestOnConnection(Utils.isActive.toString().replace("system", name)) ;
		else   answer=sendRequestOnConnection("isActive");
		//ColorsOut.out( name + " | isActive-answer=" + answer, Colors.ANSI_PURPLE);
		return answer.equals( "true" );
	}
 	
 }

 

