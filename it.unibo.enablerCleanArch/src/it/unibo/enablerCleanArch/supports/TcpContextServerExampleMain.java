package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.concur.ACallerClient;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.RadarGuiClient;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.RadarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class TcpContextServerExampleMain {
private TcpServer contextServer;
private ISonar sonar;
private ApplMessage fardistance  = new ApplMessage("msg( distance, dispatch, main, sonar, 36, 0 )");
private ApplMessage neardistance = new ApplMessage("msg( distance, dispatch, main, sonar, 10, 1 )");
private ApplMessage turnOnLed    = new ApplMessage("msg( turn, dispatch, main, led, on, 2 )");
private ApplMessage turnOffLed   = new ApplMessage("msg( turn, dispatch, main, led, off, 3 )");

private ApplMessage sonarActivate= new ApplMessage("msg( sonarcmd, dispatch, main, sonar, activate, 4 )");
private ApplMessage getSonarval  = new ApplMessage("msg( sonarcmd, request,  main, sonar, getVal,   5 )");
private ApplMessage getLedState  = new ApplMessage("msg( ledcmd,   request,  main, led,   getState, 6 )");

private ApplMessage radarUpdate  = new ApplMessage("msg( update, request,  main, radar, DISTANCE, 7 )");

private Interaction2021 conn; 

	public void configureTheSystem() {
		RadarSystemConfig.simulation 		= true;    
		RadarSystemConfig.testing    		= false;    		
		RadarSystemConfig.ControllerRemote	= false;    		
		RadarSystemConfig.LedRemote  		= false;    		
		RadarSystemConfig.SonareRemote  	= false;    		
		RadarSystemConfig.RadarGuieRemote  	= false;    	
		RadarSystemConfig.pcHostAddr        = "localhost";
		RadarSystemConfig.ctxServerPort     = 8048;
		RadarSystemConfig.sonarDelay        = 1500;
		

		//Creazione del server di contesto
		ContextMsgHandler ctxH = new ContextMsgHandler("ctxH");
		contextServer          = new TcpServer("TcpContextServer", RadarSystemConfig.ctxServerPort, ctxH);
 		
		//Registrazione dei componenti presso il contesto	
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH");
		IApplMsgHandler ledHandler   = new LedApplHandler("ledH");
		IApplMsgHandler radarHandler = new RadarApplHandler("radarH");
		
		ctxH.addComponent("sonar", sonarHandler);
		ctxH.addComponent("led",   ledHandler);	
		ctxH.addComponent("radar", radarHandler);	
	}
	
	
	public void execute() throws Exception{
		contextServer.activate();
//		simulateDistance(   );
		simulateController();
 	}
	
	/*
	 * Metodo che usa conn in modo diretto: da evitare
	 */
	protected void simulateDistance(   ) throws Exception {
		ACallerClient serverCaller = new ACallerClient("client","localhost", RadarSystemConfig.ctxServerPort);
		conn = serverCaller.getConn();
		 conn.forward( fardistance.toString() );  
		 conn.forward( neardistance.toString() );  
	}
	
	protected void simulateController(    )  {
		// client --> contextServer --> sonar.valueUpdated( ) --> produced=true

		RadarSystemConfig.sonarDelay        = 50;
		RadarSystemConfig.DLIMIT            = 40;
		
		ACallerClient sonarCaller  = new ACallerClient("sonarCaller", "localhost",  RadarSystemConfig.ctxServerPort);
		ACallerClient ledCaller    = new ACallerClient("ledCaller",   "localhost",  RadarSystemConfig.ctxServerPort);
		RadarGuiClient radarCaller = new RadarGuiClient("radarCaller","localhost",  RadarSystemConfig.ctxServerPort, ProtocolType.tcp);
		
		sonarCaller.sendCommandOnConnection(sonarActivate.toString());
		for( int i=1; i<= 10; i++) {
			String answer = sonarCaller.sendRequestOnConnection(getSonarval.toString());
			//System.out.println("simulateController sonar answer = " + answer);
	 		int v = Integer.parseInt(answer);
			//System.out.println("simulateController sonar value = " + v);
	 		radarCaller.sendCommandOnConnection(radarUpdate.toString().replace("DISTANCE",answer));
			if( v < RadarSystemConfig.DLIMIT ) 
				ledCaller.sendCommandOnConnection(turnOnLed.toString());
			else ledCaller.sendCommandOnConnection(turnOffLed.toString());  
			String ledState = ledCaller.sendRequestOnConnection(getLedState.toString());
			System.out.println("simulateController ledState=" + ledState + " for distance=" + v);
			Colors.delay(500);
		}
	}
	
	
	
	public static void main( String[] args) throws Exception {		
		TcpContextServerExampleMain sys = new TcpContextServerExampleMain();
		sys.configureTheSystem();
		sys.execute();		
	}

}
