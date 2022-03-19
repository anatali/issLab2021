package it.unibo.radarSystem22_4.appl.proxy;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.ApplMessage;
import it.unibo.radarSystem22_4.comm.ProtocolType;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.proxy.ProxyAsClient;
import it.unibo.radarSystem22_4.comm.utils.CommSystemConfig;


public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
	public static final  IApplMessage sonarActivate     = new ApplMessage("msg( sonarcmd, dispatch, system, sonar, activate,    0)");
	public static final  IApplMessage sonarDeactivate   = new ApplMessage("msg( sonarcmd, dispatch, system, sonar, deactivate,  0)");
	public static final  IApplMessage getDistance       = new ApplMessage("msg( sonarcmd, request,  system, sonar, getDistance, 0)");
	public static final  IApplMessage isActive          = new ApplMessage("msg( sonarcmd, request,  system, sonar, isActive,    0)");
  	
	public SonarProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, ProtocolType.tcp);
	}
	public SonarProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name,  host,  entry, protocol );
 	}
 	@Override
	public void activate() {
	     if( CommSystemConfig.protcolType==ProtocolType.tcp   ) {
 			sendCommandOnConnection(sonarActivate.toString());	
	     }
	}

	@Override
	public void deactivate() {
	     if( CommSystemConfig.protcolType==ProtocolType.tcp   ) {
 			sendCommandOnConnection(sonarDeactivate.toString());		
	     }
	}

	@Override
	public IDistance getDistance() {
		String answer = "0";
	    if( CommSystemConfig.protcolType==ProtocolType.tcp   ) {
			answer = sendRequestOnConnection(getDistance.toString());
			ColorsOut.out( name + " | getDistance answer="+answer, ColorsOut.ANSI_PURPLE);
	     }
 		return new Distance( Integer.parseInt(answer) );
	}

	@Override
	public boolean isActive() {
		String answer = "false";
	    if( CommSystemConfig.protcolType==ProtocolType.tcp   ) {
			answer = sendRequestOnConnection(isActive.toString());
			ColorsOut.out( name + " | isActive-answer=" + answer, ColorsOut.ANSI_PURPLE);
	    }
		return answer.equals( "true" );
	}
 	
 }

 

