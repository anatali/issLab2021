package it.unibo.radarSystem22.distrib.proxy;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.interfaces.IDistance;
import it.unibo.radarSystem22.interfaces.ISonar;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;


public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
	public static final  IApplMessage sonarActivate     = new ApplMessage("msg( sonarcmd, dispatch, system, sonar, activate,    0)");
	public static final  IApplMessage sonarDeactivate   = new ApplMessage("msg( sonarcmd, dispatch, system, sonar, deactivate,  0)");
	public static final  IApplMessage getDistance       = new ApplMessage("msg( sonarcmd, request,  system, sonar, getDistance, 0)");
	public static final  IApplMessage isActive          = new ApplMessage("msg( sonarcmd, request,  system, sonar, isActive,    0)");
 	
	public SonarProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, CommSystemConfig.protcolType);
	}
	public SonarProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name,  host,  entry, protocol );
 	}
 	@Override
	public void activate() {
 		if( CommSystemConfig.protcolType == ProtocolType.tcp && CommSystemConfig.withContext )  
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
 		if( CommSystemConfig.protcolType == ProtocolType.tcp && CommSystemConfig.withContext )  
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
		if( CommSystemConfig.protcolType == ProtocolType.tcp && CommSystemConfig.withContext ) 
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
		if( CommSystemConfig.withContext )  answer = sendRequestOnConnection( isActive.toString()) ;
//  		else if( CommSystemConfig.protcolType == ProtocolType.mqtt)  
//  			answer = sendRequestOnConnection(Utils.isActive.toString().replace("system", name)) ;
		else   answer=sendRequestOnConnection("isActive");
		//ColorsOut.out( name + " | isActive-answer=" + answer, Colors.ANSI_PURPLE);
		return answer.equals( "true" );
	}
 	
 }

 

