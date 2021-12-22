package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.RadarDisplay;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.RadarGuiClient;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.RadarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class TcpContextServerExampleMain {
private TcpContextServer contextServer;
 
private ApplMessage fardistance  = new ApplMessage("msg( distance, dispatch, main, sonar, 36, 0 )");
private ApplMessage neardistance = new ApplMessage("msg( distance, dispatch, main, sonar, 10, 1 )");
private ApplMessage turnOnLed    = new ApplMessage("msg( turn, dispatch, main, led, on, 2 )");
private ApplMessage turnOffLed   = new ApplMessage("msg( turn, dispatch, main, led, off, 3 )");

private ApplMessage sonarActivate= new ApplMessage("msg( sonarcmd, dispatch, main, sonar, activate, 4 )");
private ApplMessage sonarStop    = new ApplMessage("msg( sonarcmd, dispatch, main, sonar, deactivate, 4 )");
private ApplMessage getDistance  = new ApplMessage("msg( sonarcmd, request,  main, sonar, getDistance,   5 )");
private ApplMessage getLedState  = new ApplMessage("msg( ledcmd,   request,  main, led,   getState, 6 )");

private ApplMessage radarUpdate  = new ApplMessage("msg( update, request,  main, radar, DISTANCE, 7 )");

private Interaction2021 conn; 
private ISonar sonar;
private ILed led;

private ProxyAsClient sonarCaller;

	public void configureTheSystem() {
		RadarSystemConfig.simulation 		= true;    
		RadarSystemConfig.testing    		= false;    		
		RadarSystemConfig.ControllerRemote	= false;    		
//		RadarSystemConfig.LedRemote  		= false;    		
//		RadarSystemConfig.SonareRemote  	= false;    		
//		RadarSystemConfig.RadarGuieRemote  	= false;    	
		RadarSystemConfig.pcHostAddr        = "localhost";
		RadarSystemConfig.ctxServerPort     = 8048;
		RadarSystemConfig.DLIMIT            = 60;

		//Creazione del server di contesto
		 
		contextServer =  new TcpContextServer("TcpContextServer", RadarSystemConfig.ctxServerPort );
		sonar = DeviceFactory.createSonar();
		led   = DeviceFactory.createLed();
		//Registrazione dei componenti presso il contesto	
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
		IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led);
		IApplMsgHandler radarHandler = new RadarApplHandler("radarH");
		
 		contextServer.addComponent("sonar", sonarHandler);
		contextServer.addComponent("led",   ledHandler);	
		contextServer.addComponent("radar", radarHandler);	
	}
	
	
	public void execute() throws Exception{
		//sonar.activate();	//NO: bisogna inviare un msg
		sonarCaller = 
				new ProxyAsClient("client","localhost", ""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp);
		contextServer.activate();
//		simulateDistance(   );
		simulateController();
 	}
	
	/*
	 * Metodo che usa conn in modo diretto: da evitare
	 */
	protected void simulateDistance(   ) throws Exception {
		conn = sonarCaller.getConn();
		conn.forward( fardistance.toString() );  
		conn.forward( neardistance.toString() );  
	}
	
	protected void simulateController(    )  { 
		RadarSystemConfig.sonarDelay  = 100;
		RadarSystemConfig.DLIMIT      = 40;
		
//		ACallerClient sonarCaller  = 
//				new ACallerClient("sonarCaller", "localhost",  ""+RadarSystemConfig.ctxServerPort);
		ProxyAsClient ledCaller    = 
				new ProxyAsClient("ledCaller","localhost",""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp);
/*
RadarGuiClient radarCaller = 
				new RadarGuiClient("radarCaller","localhost",  ""+RadarSystemConfig.ctxServerPort, 
						RadarSystemConfig.protcolType);
*/
		IRadarDisplay radar = RadarDisplay.getRadarDisplay();
		
		sonarCaller.sendCommandOnConnection(sonarActivate.toString());
		for( int i=1; i<=40; i++) {
			String answer = sonarCaller.sendRequestOnConnection(getDistance.toString());
			//System.out.println("simulateController sonar answer = " + answer);
	 		int v = Integer.parseInt(answer);
			//System.out.println("simulateController sonar value = " + v);
	 		//radarCaller.sendCommandOnConnection(radarUpdate.toString().replace("DISTANCE",answer));
	 		radar.update(answer, "90");
			if( v < RadarSystemConfig.DLIMIT ) 
				ledCaller.sendCommandOnConnection(turnOnLed.toString());
			else ledCaller.sendCommandOnConnection(turnOffLed.toString());  
			String ledState = ledCaller.sendRequestOnConnection(getLedState.toString());
			Colors.outappl("simulateController ledState=" + ledState + " for distance=" + v + " i="+i, Colors.ANSI_PURPLE);
			//Utils.delay(100);
		}
		sonarCaller.sendCommandOnConnection(sonarStop.toString());
	}
	
	
	
	public static void main( String[] args) throws Exception {		
		TcpContextServerExampleMain sys = new TcpContextServerExampleMain();
		sys.configureTheSystem();
		sys.execute();		
	}

}
