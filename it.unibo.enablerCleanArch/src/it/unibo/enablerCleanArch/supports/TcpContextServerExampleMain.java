package it.unibo.enablerCleanArch.supports;

 
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.RadarDisplay;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.RadarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.RadarGuiProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class TcpContextServerExampleMain {
private TcpContextServer contextServer;
private Interaction2021 conn; 
private ISonar sonar;
private ILed led;
private SonarProxyAsClient sonarCaller;

	public void configureTheSystem() {
		RadarSystemConfig.simulation 		= true;    
		RadarSystemConfig.testing    		= false;    		
//		RadarSystemConfig.ControllerRemote	= false;    		
//		RadarSystemConfig.LedRemote  		= false;    		
//		RadarSystemConfig.SonareRemote  	= false;    		
//		RadarSystemConfig.RadarGuieRemote  	= false;    	
		RadarSystemConfig.pcHostAddr        = "localhost";
		RadarSystemConfig.ctxServerPort     = 8048;
		RadarSystemConfig.DLIMIT            = 60;
		RadarSystemConfig.withContext       = true;
		
		//Creazione del server di contesto
		 
		contextServer =  new TcpContextServer("TcpContextServer", RadarSystemConfig.ctxServerPort );
		sonar = DeviceFactory.createSonar();
		led   = DeviceFactory.createLed();
		//Registrazione dei componenti presso il contesto	
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
		IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led);
		IApplMsgHandler radarHandler = new RadarApplHandler("radarH", RadarDisplay.getRadarDisplay());
		
 		contextServer.addComponent("sonar", sonarHandler);
		contextServer.addComponent("led",   ledHandler);	
		contextServer.addComponent("radar", radarHandler);	
	}
	
	
	public void execute() throws Exception{
		sonarCaller = 
			new SonarProxyAsClient("client","localhost", ""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp);
		contextServer.activate();
  		simulateController();
 	}
	
 	protected void simulateController(    )  { 
		RadarSystemConfig.sonarDelay  = 50;
		RadarSystemConfig.DLIMIT      = 60;
		IRadarDisplay radar           = RadarDisplay.getRadarDisplay();		
		ProxyAsClient ledCaller       = new ProxyAsClient("ledCaller","localhost",
				""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp);
/*
RadarGuiClient radarCaller = 
				new RadarGuiClient("radarCaller","localhost",  ""+RadarSystemConfig.ctxServerPort, 
						RadarSystemConfig.protcolType);
*/
		
		
		sonarCaller.sendCommandOnConnection(Utils.sonarActivate.toString());
		for( int i=1; i<=40; i++) {
			String answer = sonarCaller.sendRequestOnConnection(Utils.getDistance.toString());
			//System.out.println("simulateController sonar answer = " + answer);
	 		int v = Integer.parseInt(answer);
			System.out.println("simulateController sonar value = " + v);
	 		//radarCaller.sendCommandOnConnection(radarUpdate.toString().replace("DISTANCE",answer));
	 		radar.update(answer, "90");
			if( v < RadarSystemConfig.DLIMIT ) 
				ledCaller.sendCommandOnConnection(Utils.turnOnLed.toString());
			else ledCaller.sendCommandOnConnection(Utils.turnOffLed.toString());  
			String ledState = ledCaller.sendRequestOnConnection(Utils.getLedState.toString());
			Colors.outappl("simulateController ledState=" + ledState + " for distance=" + v + " i="+i, Colors.ANSI_PURPLE);
			//Utils.delay(100);
		}
		sonarCaller.sendCommandOnConnection(Utils.sonarDeactivate.toString());
	}
	
	
	
	public static void main( String[] args) throws Exception {		
		TcpContextServerExampleMain sys = new TcpContextServerExampleMain();
		sys.configureTheSystem();
		sys.execute();		
	}

}
