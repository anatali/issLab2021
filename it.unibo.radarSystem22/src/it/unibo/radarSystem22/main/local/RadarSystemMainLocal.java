package it.unibo.radarSystem22.main.local;
 
import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.ActionFunction;
import it.unibo.radarSystem22.domain.Controller;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.interfaces.*;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


/*
 *  
 */

public class RadarSystemMainLocal implements IApplication{
private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	@Override
	public String getName() {	 
		return "RadarSystemMainLocal";
	}

	public void setup( String configFile )  {
		if( configFile != null ) DomainSystemConfig.setTheConfiguration(configFile);
		else {
  			DomainSystemConfig.testing      		= false;			
			DomainSystemConfig.sonarDelay        = 200;
			//Su PC
			DomainSystemConfig.simulation   		= true;
			DomainSystemConfig.DLIMIT      		= 40;  
			DomainSystemConfig.ledGui            = true;
			DomainSystemConfig.RadarGuiRemote    = false;
		//Su Raspberry (nel file di configurazione)
//			DomainSystemConfig.simulation   		= false;
//			DomainSystemConfig.DLIMIT      		= 12;  
//			DomainSystemConfig.ledGui            = false;
//			DomainSystemConfig.RadarGuiRemote    = true;
		}
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
	    radar      = DomainSystemConfig.RadarGuiRemote ? null : DeviceFactory.createRadarGui();
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
		new RadarSystemMainLocal().doJob(null);
 	}

}
