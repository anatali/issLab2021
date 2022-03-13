package it.unibo.radarSystem22.sprint2.main.devicesOnRasp;

 
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.tcp.TcpServer;
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.sprint2.RadarSysConfigSprint2;
import it.unibo.radarSystem22.sprint2.handlers.LedApplHandler;
import it.unibo.radarSystem22.sprint2.handlers.SonarApplHandler;
 
 
/*
 * Attiva il TCPServer.
 * 
 */
public class RadarSysSprint2DevicesOnRaspMain implements IApplication{
	private ISonar sonar;
	private ILed  led ;
	private TcpServer ledServer ;
	private TcpServer sonarServer ;

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
    	DomainSystemConfig.ledGui      = true;		//se siamo su PC	

		RadarSysConfigSprint2.tracing           = false;		
		RadarSysConfigSprint2.RadarGuiRemote    = true;		
 
	}
	protected void configure() {		
 	   led        = DeviceFactory.createLed();
 	   IApplMsgHandler ledh = LedApplHandler.create("ledh", led);
 	   ledServer     = new TcpServer("ledServer",RadarSysConfigSprint2.ledPort,ledh );

	   sonar      = DeviceFactory.createSonar();
 	   IApplMsgHandler sonarh = SonarApplHandler.create("sonarh", sonar);
 	   sonarServer   = new TcpServer("sonarServer",RadarSysConfigSprint2.sonarPort,sonarh );

 	   
	}
	
	protected void execute() {		
		ledServer.activate();
		sonarServer.activate();
	}
	
	@Override
	public String getName() {
		return this.getClass().getName() ;  
	}

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with NO CONFIG files| ");
		new RadarSysSprint2DevicesOnRaspMain().doJob(null,null);
  	}
}
