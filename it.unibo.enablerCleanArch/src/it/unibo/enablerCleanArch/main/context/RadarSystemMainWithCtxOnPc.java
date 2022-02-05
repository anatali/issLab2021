package it.unibo.enablerCleanArch.main.context;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
import it.unibo.enablerCleanArch.supports.TcpServer;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.RadarApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;

/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRasp
 */

public class RadarSystemMainWithCtxOnPc implements IApplication{
private TcpContextServer contextServer;
private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainControllerWithContextOnPc";
	}

	public void setup( String configFile )  {
 	      RadarSystemConfig.protcolType       = ProtocolType.tcp;
 		  RadarSystemConfig.testing           = false;
		  RadarSystemConfig.raspHostAddr      = "192.168.1.9";
		  RadarSystemConfig.ctxServerPort     = 8048;
		  RadarSystemConfig.sonarDelay        = 1500;
		  RadarSystemConfig.withContext       = true;		
		  RadarSystemConfig.tracing           = true;
	
	}
	
	protected void configure() {
		String host           = RadarSystemConfig.raspHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String ctxport        = ""+RadarSystemConfig.ctxServerPort;
		led    		= new LedProxyAsClient("ledPxy", host, ctxport, protocol );
  		sonar  		= new SonarProxyAsClient("sonarPxy", host, ctxport, protocol );
  		radar  		= DeviceFactory.createRadarGui();
  		controller 	= Controller.create(led, sonar, radar);
	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		execute();
	}
	
	public void execute() {
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
		controller.start(endFun, 30);
 	}
	
 
	public void terminate() {
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainWithCtxOnPc().doJob(null);
 	}

}
