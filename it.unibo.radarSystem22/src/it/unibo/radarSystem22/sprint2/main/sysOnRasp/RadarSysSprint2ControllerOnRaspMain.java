package it.unibo.radarSystem22.sprint2.main.sysOnRasp;

 
import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.tcp.TcpServer;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.ActionFunction;
import it.unibo.radarSystem22.sprint1.Controller;
import it.unibo.radarSystem22.sprint2.RadarSysConfigSprint2;
import it.unibo.radarSystem22.sprint2.handlers.LedApplHandler;
import it.unibo.radarSystem22.sprint2.proxy.RadarGuiProxyAsClient;
 
 
/*
 * Attiva il sistema su Raspberry, Controller compreso.
 * Accede al RadarDisplay su PC tramite proxy.
 * 
 */
public class RadarSysSprint2ControllerOnRaspMain implements IApplication{
	private ISonar sonar;
	private ILed  led ;
	private TcpServer server ;
	private IRadarDisplay radar;
	private Controller controller;

	@Override
	public void doJob(String domainConfig, String systemConfig) {
		setup(domainConfig,   systemConfig);
		configure();
		execute();
	}
	
	public void setup( String domainConfig, String systemConfig )  {
		DomainSystemConfig.simulation  = true;
    	DomainSystemConfig.testing     = false;			
    	DomainSystemConfig.tracing     = false;			
		DomainSystemConfig.sonarDelay  = 200;
    	DomainSystemConfig.ledGui      = true;			
    	DomainSystemConfig.DLIMIT      = 75;
    	
		RadarSysConfigSprint2.RadarGuiRemote    = true;		
		RadarSysConfigSprint2.serverPort        = 8023;		
		RadarSysConfigSprint2.hostAddr          = "localhost";
	}
	protected void configure() {		
 	    sonar      = DeviceFactory.createSonar();
 	    led        = DeviceFactory.createLed();
	    radar      = new  RadarGuiProxyAsClient("radarPxy", 
	    		      RadarSysConfigSprint2.hostAddr, 
		              ""+RadarSysConfigSprint2.serverPort, 
		              ProtocolType.tcp);
	    //Controller
	    controller = Controller.create(led, sonar, radar);	 		
 	
	}
	
	protected void execute() {
		//start
	    ActionFunction endFun = (n) -> { 
	    	System.out.println(n); 
	    	terminate(); 
	    };
		controller.start(endFun, 30);		
	}
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		System.exit(0);
	}	
	@Override
	public String getName() {
		return this.getClass().getName() ;  
	}

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with NO CONFIG files| ");
		new RadarSysSprint2ControllerOnRaspMain().doJob(null,null);
  	}
}
