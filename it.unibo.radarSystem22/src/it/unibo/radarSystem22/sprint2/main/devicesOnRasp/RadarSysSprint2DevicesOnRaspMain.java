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
 
 
/*
 * Attiva il TCPServer.
 * 
 */
public class RadarSysSprint2DevicesOnRaspMain implements IApplication{
	private ISonar sonar;
	private ILed  led ;
	private TcpServer server ;

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

		RadarSysConfigSprint2.RadarGuiRemote    = true;		
		RadarSysConfigSprint2.serverPort        = 8023;		
		RadarSysConfigSprint2.hostAddr          = "localhost";

	}
	protected void configure() {		
 	    sonar      = DeviceFactory.createSonar();
 	    led        = DeviceFactory.createLed();

 	   IApplMsgHandler ledh = LedApplHandler.create("ledh", led);
	   int port   = RadarSysConfigSprint2.serverPort;
	   server     = new TcpServer("raspServer",port,ledh );
	
	}
	
	protected void execute() {
		server.activate();
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
