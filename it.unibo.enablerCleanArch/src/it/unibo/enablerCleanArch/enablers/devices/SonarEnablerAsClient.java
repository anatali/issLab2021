package it.unibo.enablerCleanArch.enablers.devices;
import it.unibo.enablerCleanArch.domain.Distance;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Colors;

  

public class SonarEnablerAsClient extends EnablerAsClient implements ISonar{
 	
	public SonarEnablerAsClient( String name, String host, int port, ProtocolType protocol ) {
		super( name,  host,  port, protocol );
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
		Colors.out( name + " | getVal ", Colors.ANSI_PURPLE);
		String answer = sendRequestOnConnection("getVal");
		return new Distance( Integer.parseInt(answer) );
	}

	@Override
	public boolean isActive() {
		String answer = sendRequestOnConnection("isActive");
		Colors.out( name + " | isActive-answer=" + answer, Colors.ANSI_PURPLE);
		return answer.equals( "true" );
	}
 
	
 }

 

