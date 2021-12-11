package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.concur.ACallerClient;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
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
private ApplMessage getSonarval  = new ApplMessage("msg( sonarcmd, request, main, sonar, getVal, 5 )");

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
		RadarSystemConfig.sonarDelay        = 50;

		int ledPort	       = 0;	//dont'care	
		int sonarPort      = 0;	//dont'care		
		
		ProtocolType protocol               = null;
		//Creazione del server di contesto
		ContextMsgHandler ctxH = new ContextMsgHandler("ctxH");
		contextServer          = new TcpServer("TcpContxtServer", RadarSystemConfig.ctxServerPort, ctxH);
		
		//Creazione del sonar
		//sonar = new EnablerAsServer("sonar",  sonarPort, protocol);
		
		//Creazione del led
		//ILed led = DeviceFactory.createLed();		
		//LedEnablerAsServerHandler ledServer = new LedEnablerAsServerHandler(  "led", ledPort, protocol, led  );
 		
		//Registrazione dei componenti presso il server handler		
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH");
		IApplMsgHandler ledHandler   = new LedApplHandler("ledH");
		ctxH.addComponent("sonar", sonarHandler);
		ctxH.addComponent("led",   ledHandler);	
	}
	
	
	public void execute() throws Exception{
		contextServer.activate();
		ACallerClient client = new ACallerClient("client","localhost", RadarSystemConfig.ctxServerPort);
		conn = client.getConn();
		simulateDistance( client, true );
		//simulateDistance( false );
 	}
	
	protected void simulateDistance( ACallerClient client, boolean far ) throws Exception {
		if( far ) conn.forward( fardistance.toString() );  
		else  conn.forward( neardistance.toString() );  
		// client --> contextServer --> sonar.valueUpdated( ) --> produced=true
		//int v = sonar.getVal();
		client.sendRequestOnConnection(sonarActivate.toString());
		String answer = client.sendRequestOnConnection(getSonarval.toString());
		System.out.println("simulateDistance sonar value = " + answer);
		/*
		System.out.println("simulateDistance sonar value = " + v);
		if( v < RadarSystemConfig.DLIMIT ) conn.forward(turnOnLed.toString());  
		else conn.forward(turnOffLed.toString());  		*/
	}
	
	public static void main( String[] args) throws Exception {
		
		TcpContextServerExampleMain sys = new TcpContextServerExampleMain();
		sys.configureTheSystem();
		sys.execute();
		
	}

}
