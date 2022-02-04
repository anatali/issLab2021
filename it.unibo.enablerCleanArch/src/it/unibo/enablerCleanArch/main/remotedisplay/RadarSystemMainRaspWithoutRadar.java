package it.unibo.enablerCleanArch.main.remotedisplay;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.RadarGuiProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
 

/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRasp
 */

public class RadarSystemMainRaspWithoutRadar implements IApplication{
private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	@Override
	public String getName() {	 
		return "RadarSystemMainRaspWithoutRadar";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
   			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.sonarDelay        = 200;
 			RadarSystemConfig.simulation   		= false;
			RadarSystemConfig.DLIMIT      		= 12;  
			RadarSystemConfig.radarGuiPort      = 8014;
		configure();
 	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		//start
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
		controller.start(endFun, 30);
	}
	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
	    ColorsOut.out("Creating radar proxy for port:"+RadarSystemConfig.radarGuiPort);
	    radar      = new  RadarGuiProxyAsClient("radarPxy", 
				          RadarSystemConfig.pcHostAddr, 
				          ""+RadarSystemConfig.radarGuiPort, 
				          ProtocolType.tcp);
	    //Controller
	    controller = Controller.create(led, sonar, radar);	 
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() { return radar; }
 	public ILed getLed() { return led; }
 	public ISonar getSonar() { return sonar; }
 	public Controller getController() { return controller; }
	
	public static void main( String[] args) throws Exception {
		//ColorsOut.out("Please set RadarSystemConfig.pcHostAddr in RadarSystemConfig.json");
		new RadarSystemMainRaspWithoutRadar().doJob(null);
 	}

}
