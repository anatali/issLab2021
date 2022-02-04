package it.unibo.enablerCleanArch.main.remotedisplay;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.RadarApplHandler;
import it.unibo.radar.interfaces.IRadar;

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
		setup(configFileName);
		executeDisplayOnPc();
	}
	
	public void executeDisplayOnPc() {
 	   IRadarDisplay radar     = DeviceFactory.createRadarGui();
 	   IApplMsgHandler handler = new RadarApplHandler("radarH", radar);
 	   TcpServer serverRadar = new TcpServer( "ServerTcp", RadarSystemConfig.radarGuiPort,  handler );
 	   serverRadar.start();				
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
