package it.unibo.enablerCleanArch.enablers.devices;
import it.unibo.enablerCleanArch.domain.Distance;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Colors;
 

public class SonarProxyAsClient extends EnablerAsClient implements ISonar{
 	
	public SonarProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name,  host,  entry, protocol );
 	}
 	@Override
	public void activate() {
 		sendCommandOnConnection("activate");		
	}

	@Override
	public void deactivate() {
		sendCommandOnConnection("deactivate");		
	}

	@Override
	public IDistance getDistance() {
		Colors.out( name + " | getDistance ", Colors.ANSI_PURPLE);
		String answer = sendRequestOnConnection("getDistance");
		Colors.out( name + " | getDistance answer="+answer, Colors.ANSI_PURPLE);
		return new Distance( Integer.parseInt(answer) );
	}

	@Override
	public boolean isActive() {
		String answer = sendRequestOnConnection("isActive");
		Colors.out( name + " | isActive-answer=" + answer, Colors.ANSI_PURPLE);
		return answer.equals( "true" );
	}
 
	
 }

 
