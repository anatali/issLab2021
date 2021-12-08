package it.unibo.enablerCleanArch.concur;


import it.unibo.enablerCleanArch.adapters.SonarAdapterEnablerAsServer;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.LedEnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpContextServer;

public class TcpContextServerExampleMain {
private TcpContextServer contextServer;
private ISonar sonar;
private ApplMessage fardistance  = new ApplMessage("msg( distance, dispatch, main, sonar, 36, 0 )");
private ApplMessage neardistance = new ApplMessage("msg( distance, dispatch, main, sonar, 10, 1 )");
private ApplMessage turnOnLed    = new ApplMessage("msg( turn, dispatch, main, led, on, 2 )");
private ApplMessage turnOffLed   = new ApplMessage("msg( turn, dispatch, main, led, off, 3 )");
private Interaction2021 conn; 

	public void configureTheSystem() {
		RadarSystemConfig.simulation 		= true;    
		RadarSystemConfig.testing    		= true;    		
		RadarSystemConfig.ControllerRemote	= false;    		
		RadarSystemConfig.LedRemote  		= false;    		
		RadarSystemConfig.SonareRemote  	= false;    		
		RadarSystemConfig.RadarGuieRemote  	= false;    	
		RadarSystemConfig.pcHostAddr        = "localhost";
		RadarSystemConfig.ledPort			= 8010;		
		RadarSystemConfig.sonarPort			= 8012;		
		RadarSystemConfig.ctxServerPort     = 8048;
		
		//Creazione del server di contesto
		contextServer  = new TcpContextServer("TcpApplServer", RadarSystemConfig.ctxServerPort);
		
		//Creazione del sonar
		sonar = new SonarAdapterEnablerAsServer("sonar",  RadarSystemConfig.sonarPort, ProtocolType.tcp);
		
		//Creazione del led
		ILed led = DeviceFactory.createLed();		
		LedEnablerAsServer ledServer = 
				new LedEnablerAsServer(  "led", RadarSystemConfig.ledPort, ProtocolType.tcp, led  );
 		
		//Registrazione dei componenti presso il server
		contextServer.addComponent("sonar",(ApplMessageHandler) sonar);
		contextServer.addComponent("led",ledServer);	
	}
	
	
	public void execute() throws Exception{
		contextServer.activate();
		ACallerClient client = new ACallerClient("client","localhost",RadarSystemConfig.ctxServerPort);
		conn = client.getConn();
		simulateDistance( true );
		simulateDistance( false );
 	}
	
	protected void simulateDistance( boolean far ) throws Exception {
		if( far ) conn.forward( fardistance.toString() );  
		else  conn.forward( neardistance.toString() );  
		// client --> contextServer --> sonar.valueUpdated( ) --> produced=true
		int v = sonar.getVal();
		System.out.println("simulateDistance sonar value = " + v);
		if( v < RadarSystemConfig.DLIMIT ) conn.forward(turnOnLed.toString());  
		else conn.forward(turnOffLed.toString());  		
	}
	
	public static void main( String[] args) throws Exception {
		
		TcpContextServerExampleMain sys = new TcpContextServerExampleMain();
		sys.configureTheSystem();
		sys.execute();
		
	}

}
