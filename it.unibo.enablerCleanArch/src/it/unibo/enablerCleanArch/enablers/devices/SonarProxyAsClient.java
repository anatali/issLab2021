package it.unibo.enablerCleanArch.enablers.devices;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.Distance;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
 	
	public SonarProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name,  host,  entry, protocol );
 	}
 	@Override
	public void activate() {
 		if( RadarSystemConfig.withContext )  sendCommandOnConnection(Utils.sonarActivate.toString());
 		else sendCommandOnConnection("activate");		
	}

	@Override
	public void deactivate() {
 		if( RadarSystemConfig.withContext )  sendCommandOnConnection(Utils.sonarDeactivate.toString());
 		else sendCommandOnConnection("deactivate");		
	}

	@Override
	public IDistance getDistance() {
		String answer="";
		//Colors.out( name + " | getDistance ", Colors.ANSI_PURPLE);
		if( RadarSystemConfig.withContext )  answer = sendRequestOnConnection(Utils.getDistance.toString()) ;
		else  answer = sendRequestOnConnection("getDistance");
		//Colors.out( name + " | getDistance answer="+answer, Colors.ANSI_PURPLE);
		return new Distance( Integer.parseInt(answer) );
	}

	@Override
	public boolean isActive() {
		String answer = "";
		if( RadarSystemConfig.withContext )  answer = sendRequestOnConnection(Utils.isActive.toString()) ;
		else   sendRequestOnConnection("isActive");
		//Colors.out( name + " | isActive-answer=" + answer, Colors.ANSI_PURPLE);
		return answer.equals( "true" );
	}
 	
 }

 

