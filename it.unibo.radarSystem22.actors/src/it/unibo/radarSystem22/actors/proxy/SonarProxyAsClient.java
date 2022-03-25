package it.unibo.radarSystem22.actors.proxy;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;


public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
	public static final  IApplMessage sonarActivate     = new ApplMessage("msg( cmd, dispatch, system, sonar, activate,    0)");
	public static final  IApplMessage sonarDeactivate   = new ApplMessage("msg( cmd, dispatch, system, sonar, deactivate,  0)");
	public static final  IApplMessage getDistance       = new ApplMessage("msg( cmd, request,  system, sonar, getDistance, 0)");
	public static final  IApplMessage isActive          = new ApplMessage("msg( cmd, request,  system, sonar, isActive,    0)");
 	
	public SonarProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, CommSystemConfig.protcolType);
	}
	public SonarProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name,  host,  entry, protocol );
 	}
 	@Override
	public void activate() {
 		if( CommUtils.isTcp() || CommUtils.isUdp() )  
 			sendCommandOnConnection( sonarActivate.toString());
 	}

	@Override
	public void deactivate() {
 		if( CommUtils.isTcp() || CommUtils.isUdp()   )  
 			sendCommandOnConnection( sonarDeactivate.toString() );
 	}

	@Override
	public IDistance getDistance() {
		String answer="";
		ColorsOut.outappl( name + " | getDistance ", ColorsOut.ANSI_PURPLE);
		if( CommUtils.isTcp() || CommUtils.isUdp() ) 
			answer = sendRequestOnConnection(  getDistance.toString() ) ;
 		if( answer.startsWith("msg")){ //structured msg
			ApplMessage ma = new ApplMessage(answer);
			answer = ma.msgContent();
		}
		return new Distance( Integer.parseInt(answer) );
	}

	@Override
	public boolean isActive() {
		String answer = "";
 		if( CommUtils.isTcp() || CommUtils.isUdp() ) ;
			 answer=sendRequestOnConnection(isActive.toString());
		//ColorsOut.out( name + " | isActive-answer=" + answer, Colors.ANSI_PURPLE);
		try {
			IApplMessage reply = new ApplMessage(answer);
			return reply.msgContent().equals( "true" );
		}catch( Exception e) {return answer.equals( "true" );}
	}
 	
 }

 

