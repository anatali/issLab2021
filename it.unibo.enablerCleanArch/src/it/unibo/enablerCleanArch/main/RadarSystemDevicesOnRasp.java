package it.unibo.enablerCleanArch.main;


import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.RadarGuiProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
import it.unibo.enablerCleanArch.supports.Utils;


/*
 * Applicazione che va in coppia con RadarSystemMainOnPc
 */
public class RadarSystemDevicesOnRasp implements IApplication{
private ISonar sonar          		= null;
private ILed led              		= null;
private TcpContextServer ctxServer  = null;
 

	public void setUp( String configFile )   {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
			RadarSystemConfig.simulation   		= true;
			RadarSystemConfig.SonareRemote 		= false;
			RadarSystemConfig.LedRemote    		= false;
			RadarSystemConfig.ledGui    		= true;
			RadarSystemConfig.ControllerRemote  = false; 
			RadarSystemConfig.sonarPort    		= 8012;
			RadarSystemConfig.ledPort      		= 8010;
			RadarSystemConfig.radarGuiPort    	= 8014;
			RadarSystemConfig.ctxServerPort     = 8018;
			RadarSystemConfig.DLIMIT   			= 55;
			RadarSystemConfig.sonarDelay   		= 100;
			RadarSystemConfig.withContext  		= true; 
			RadarSystemConfig.testing      		= false;			
		}
	}
	
	protected void createSonarEnabler( ) {
		sonar = SonarModel.create();		
		EnablerAsServer sonarServer  = 
				new EnablerAsServer("sonarServer",RadarSystemConfig.sonarPort, 
						ProtocolType.tcp, new SonarApplHandler("sonarH",sonar) );
		sonarServer.start();
		Colors.out("createSonarEnabler DONE",Colors.ANSI_YELLOW);
 	}
	
	protected void createLedEnabler( ) {
		led = LedModel.create();
		EnablerAsServer ledServer  = 
				new EnablerAsServer("LedEnablerAsServer",RadarSystemConfig.ledPort, 
						ProtocolType.tcp,  new LedApplHandler("ledH", led) );
		ledServer.start();
		Colors.out("createLedEnable DONE", Colors.ANSI_YELLOW);
 	}
 
	protected void addSonarToContext( ) {
		sonar = SonarModel.create();		
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
		ctxServer.addComponent("sonar", sonarHandler);		
	}
	protected void addLedToContext( ) {
		led = LedModel.create();
		IApplMsgHandler ledHandler = new LedApplHandler("ledH",led);
		ctxServer.addComponent("led", ledHandler);				
	}
	
	protected void configure() {
		if( RadarSystemConfig.withContext ) {
			ctxServer  = new TcpContextServer("TcpApplServer",RadarSystemConfig.ctxServerPort);
			addSonarToContext( ); 
			addLedToContext( );
		}else {
			createSonarEnabler( ) ;
			createLedEnabler();
		}
		if( ! RadarSystemConfig.ControllerRemote ) {
			Utils.delay(1000); //Give time to the LedGui ...
			IRadarDisplay radarClient = new RadarGuiProxyAsClient("radarClient",
					RadarSystemConfig.pcHostAddr,""+RadarSystemConfig.radarGuiPort, ProtocolType.tcp);			
			Controller.activate(led, sonar, radarClient);  //accede direttamente ai devices
		} 
 	}
	
	protected void execute() {
		if( ctxServer != null ) ctxServer.activate();
		//sonar.activate();	//NO activate the sonar is done by the Controller
 	}
	
 

  
	@Override
	public void doJob(String configFileName) {
 		setUp(configFileName);
		configure();
		execute();
	}

	@Override
	public String getName() {
		return "RadarSystemDevicesOnRasp";
	}

 	
	public static void main( String[] args) throws Exception {
		new RadarSystemDevicesOnRasp().doJob(null); //"RadarSystemConfig.json"
 	}

}
