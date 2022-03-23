package it.unibo.radarSystem22.actors.proxy;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;


public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
	public static final  IApplMessage sonarActivate     = new ApplMessage("msg( cmd, dispatch, system, sonar, activate,    0)");
	public static final  IApplMessage sonarDeactivate   = new ApplMessage("msg( cmd, dispatch, system, sonar, deactivate,  0)");
	public static final  IApplMessage getDistance       = new ApplMessage("msg( cmd, request,  system, sonar, distance,    0)");
	public static final  IApplMessage isActive          = new ApplMessage("msg( cmd, request,  system, sonar, isActive,    0)");
 	
	public SonarProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, CommSystemConfig.protcolType);
	}
	public SonarProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name,  host,  entry, protocol );
 	}
 	@Override
	public void activate() {
 		if( CommSystemConfig.protcolType == ProtocolType.tcp   )  
 			sendCommandOnConnection( sonarActivate.toString());
//  		else if( CommSystemConfig.protcolType == ProtocolType.mqtt)  
//  			sendCommandOnConnection( sonarActivate.toString());
// 		else if( CommSystemConfig.protcolType == ProtocolType.coap) 
//   			sendCommandOnConnection("activate");  //PUT
 		else //CASO DI DEFAULT
 			sendCommandOnConnection("activate");		
	}

	@Override
	public void deactivate() {
 		if( CommSystemConfig.protcolType == ProtocolType.tcp   )  
 			sendCommandOnConnection( sonarDeactivate.toString() );
//  		else if( RadarSystemConfig.protcolType == ProtocolType.mqtt)  
//  			sendCommandOnConnection(Utils.sonarDeactivate.toString());
//		else if( RadarSystemConfig.protcolType == ProtocolType.coap) 
// 			sendCommandOnConnection("deactivate");
 		else //CASO DI DEFAULT
 			sendCommandOnConnection("deactivate");		
	}

	@Override
	public IDistance getDistance() {
		String answer="";
		//Colors.out( name + " | getDistance ", Colors.ANSI_PURPLE);
		if( CommSystemConfig.protcolType == ProtocolType.tcp   ) 
			answer = sendRequestOnConnection(  getDistance.toString() ) ;
//  		else if( CommSystemConfig.protcolType == ProtocolType.mqtt)  
//  			answer = sendRequestOnConnection(Utils.getDistance.toString().replace("system", name)) ;
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
//		if( CommSystemConfig.withContext )  answer = sendRequestOnConnection( isActive.toString()) ;
////  		else if( CommSystemConfig.protcolType == ProtocolType.mqtt)  
////  			answer = sendRequestOnConnection(Utils.isActive.toString().replace("system", name)) ;
//		else   
			 answer=sendRequestOnConnection("isActive");
		//ColorsOut.out( name + " | isActive-answer=" + answer, Colors.ANSI_PURPLE);
		try {
			IApplMessage reply = new ApplMessage(answer);
			return reply.msgContent().equals( "true" );
		}catch( Exception e) {return answer.equals( "true" );}
	}
 	
 }

 

