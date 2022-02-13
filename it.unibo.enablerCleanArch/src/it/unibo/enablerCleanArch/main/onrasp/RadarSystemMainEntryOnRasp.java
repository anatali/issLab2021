package it.unibo.enablerCleanArch.main.onrasp;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.Utils;

/*
 * Applicazione usata da Spring quando gira su Rasp
 * Non ha bisogno di ContextServer 
 * Fa la configurazione nel setUp
 * aggiunge doBlink e i metodi usati da per HIController
 */

public class RadarSystemMainEntryOnRasp implements IApplicationFacade{
	private ISonar sonar;
	private ILed  led ;
// 	private IContext ctx;
 	private boolean ledblinking        = false;
  
 	@Override
	public void setUp(String configFile) {
 		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else { 
				//Configurazione cabalata nel programma	         
			}
 		configure();
	  }	
	 	
	protected void configure() {
		//Dispositivi di Input
		if( RadarSystemConfig.sonarObservable ) {
			sonar  = DeviceFactory.createSonarObservable();		
//			IObserver sonarObs  = new SonarObserver( "sonarObs" ) ;
//			((ISonarObservable)sonar).register( sonarObs );			
		}else{
			sonar = DeviceFactory.createSonar();
		}
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
	    //led.turnOff();
/*	    
		//Creazione del server di contesto
	    String id 	 = "";
	    String entry = "";
		switch( RadarSystemConfig.protcolType ) {
		case tcp :  { id="rasp";  entry=""+RadarSystemConfig.ctxServerPort; break; }
		case coap : {  break; }
		case mqtt : { id="rasp";  entry=MqttConnection.topicInput; break; }
		default:
		};
	    ctx  = Context2021.create(id,entry);
  		//Registrazione dei componenti presso il contesto
 		  IApplMsgHandler sonarHandler = SonarApplHandler.create("sonarH",sonar); 
		  IApplMsgHandler ledHandler   = LedApplHandler.create("ledH",led);		  
 		  ctx.addComponent("sonar", sonarHandler);	    //sonar NAME mandatory
		  ctx.addComponent("led",   ledHandler);		//led NAME mandatory
*/
	    }
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		//ctx.deactivate();
		System.exit(0);
	}
	
//	public void doJob( String configFileName ) {
//		setUp( configFileName );
//		configure();
// 	}	
//	@Override
//	public String getName() {	 
//		return "RadarSystemMainEntryOnPc";
//	}		
 	@Override
	public void ledActivate(boolean v) {
 		if( v ) led.turnOn();else led.turnOff();
	} 	
	@Override
	public String ledState() {
 		return ""+led.getState();//coapLedSup.request("ledState"); //payload don't care
	}	
	@Override
	public void doLedBlink() {
		new Thread() {
			public void run() {
				ledblinking = true;
				while( ledblinking ) {
					ledActivate(true);
					Utils.delay(500);
					ledActivate(false);
					Utils.delay(500);
				}
			}
		}.start();				
	}
		
	@Override
	public void stopLedBlink() {
		ledblinking = false;		
	}	
	@Override
	public void sonarActivate() {
		ColorsOut.out("RadarSystemMainOnPcCoapBase | sonarActivate");
 		sonar.activate();		
	}
	@Override
	public boolean sonarIsactive() {
		return sonar.isActive();
	}
	@Override
	public void sonarDectivate() {
		sonar.deactivate();
	}
	@Override
	public String sonarDistance() {
 		return ""+sonar.getDistance().getVal();
	}
	
	
	public static void main( String[] args) throws Exception {
// 		new RadarSystemMainEntryOnRasp().doJob( "RadarSystemConfig.json" );
 	}

}
