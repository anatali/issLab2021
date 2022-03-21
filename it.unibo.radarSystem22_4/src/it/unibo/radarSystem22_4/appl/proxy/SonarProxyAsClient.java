package it.unibo.radarSystem22_4.appl.proxy;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.ProtocolType;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.proxy.ProxyAsClient;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;


public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
	public static    IApplMessage sonarActivate   ; 
	public static    IApplMessage sonarDeactivate ;  
	public static    IApplMessage getDistance  ; 
	public static    IApplMessage isActive     ; 
  	
	public SonarProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, ProtocolType.tcp);
	}
	public SonarProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name,  host,  entry, protocol );
		sonarActivate   = CommUtils.buildDispatch(name,"cmd", "activate",   "sonar");
		sonarDeactivate = CommUtils.buildDispatch(name,"cmd", "deactivate", "sonar");
		getDistance     = CommUtils.buildRequest(name, "req", "getDistance","sonar");
		isActive        = CommUtils.buildRequest(name, "req", "isActive",   "sonar");
 	}
 	@Override
	public void activate() {
	     if( protocol == ProtocolType.tcp  ) {
 			sendCommandOnConnection(sonarActivate.toString());	
	     }
	   //ALTRI PROTOCOLLI ...	
	}

	@Override
	public void deactivate() {
	     if( protocol == ProtocolType.tcp   ) {
 			sendCommandOnConnection(sonarDeactivate.toString());		
	     }
	}

	@Override
	public IDistance getDistance() {
		String answer = "0";
	    if( protocol == ProtocolType.tcp  ) {
			answer = sendRequestOnConnection(getDistance.toString());
			ColorsOut.out( name + " | getDistance answer="+answer, ColorsOut.ANSI_PURPLE);
	     }
 		return new Distance( Integer.parseInt(answer) );
	}

	@Override
	public boolean isActive() {
		String answer = "false";
	    if( protocol == ProtocolType.tcp  ) {
			answer = sendRequestOnConnection(isActive.toString());
			ColorsOut.out( name + " | isActive-answer=" + answer, ColorsOut.ANSI_PURPLE);
	    }
		return answer.equals( "true" );
	}
 	
 }

 

