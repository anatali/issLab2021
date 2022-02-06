package it.unibo.enablerCleanArch.main.remotedisplay;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.tcp.TcpServer;
import it.unibo.enablerCleanArchapplHandlers.RadarApplHandler;

/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRasp
 */

public class RadarSystemMainDisplayOnPc implements IApplication{
private IRadarDisplay radar = null;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainDisplayOnPc";
	}

	public void setup( String configFile )  {
		RadarSystemConfig.radarGuiPort = 8014;
 	}
	
 	
	@Override
	public void doJob(String configFileName) {
		RadarSystemConfig.tracing = true;
		setup(configFileName);
		executeDisplayOnPc();
	}
	
	public void executeDisplayOnPc() {
 	   IRadarDisplay radar     = DeviceFactory.createRadarGui();
 	   IApplMsgHandler handler = new RadarApplHandler("radarH", radar);
 	   TcpServer serverRadar   = new TcpServer( "ServerTcp", RadarSystemConfig.radarGuiPort,  handler );
 	   serverRadar.activate();				
 	}
	
 
	public void terminate() {
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainDisplayOnPc().doJob(null);
 	}

}
