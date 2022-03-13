package it.unibo.radarSystem22.sprint2.main.devicesOnRasp;


import it.unibo.comm2022.ProtocolType;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint1.ActionFunction;
import it.unibo.radarSystem22.sprint1.Controller;
import it.unibo.radarSystem22.sprint2.RadarSysConfigSprint2;
import it.unibo.radarSystem22.sprint2.proxy.LedProxyAsClient;
import it.unibo.radarSystem22.sprint2.proxy.SonarProxyAsClient;

/*
 * Attiva il Controller (vedi sprint1) e il RadarDisplay (vedi domain)
 * e due proxy al Led e al Sonar.
 * 
 */
public class RadarSysSprint2ControllerOnPcMain implements IApplication{
	private IRadarDisplay radar;
	private ISonar sonar;
	private ILed  led ;
	private Controller controller;
	
	@Override
	public void doJob(String domainConfig, String systemConfig ) {
		setup( );
		configure();
		//start
	    ActionFunction endFun = (n) -> { 
	    	System.out.println(n); 
	    	terminate(); 
	    };
		controller.start(endFun, 30);		
	}
	
	public void setup(  )  {	
		DomainSystemConfig.testing      	= false;			
		DomainSystemConfig.sonarDelay       = 200;
		//Su PC
		DomainSystemConfig.simulation   	= true;
		DomainSystemConfig.DLIMIT      		= 40;  
		DomainSystemConfig.ledGui           = true;
		
		RadarSysConfigSprint2.RadarGuiRemote    = false;		
		RadarSysConfigSprint2.serverPort  = 8023;		
		RadarSysConfigSprint2.hostAddr    = "localhost";
	}
	
	public void configure(  )  {	
		String host = RadarSysConfigSprint2.hostAddr;
		String port = ""+RadarSysConfigSprint2.serverPort;
		ProtocolType protocol = ProtocolType.tcp;
		
 		led    		= new LedProxyAsClient("ledPxy",     host, port, protocol );
  		sonar  		= new SonarProxyAsClient("sonarPxy", host, port, protocol );
  		radar  		= DeviceFactory.createRadarGui();
 
	    //Controller
	    controller = Controller.create(led, sonar, radar);	 		
	}
	public void terminate() {
 		BasicUtils.aboutThreads("Before deactivation | ");
		//sonar.deactivate();
		System.exit(0);
	}	
	
	@Override
	public String getName() {
		return this.getClass().getName() ; //"RadarSystemSprint2OnPcMain";
	}

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with NO CONFIG files| ");
		new RadarSysSprint2ControllerOnPcMain().doJob( null,null );
  	}	
}
