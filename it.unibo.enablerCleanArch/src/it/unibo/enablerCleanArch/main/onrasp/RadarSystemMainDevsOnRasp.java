package it.unibo.enablerCleanArch.main.onrasp;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.main.all.SonarObserver;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.mqtt.MqttConnection;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;
import it.unibo.enablerCleanArch.supports.context.Context2021; 

/*
 * Applicazione che va in coppia con RadarSystemMainUasgeOnPc
 */

public class RadarSystemMainDevsOnRasp implements IApplication{
	private ISonar sonar;
	private ILed  led ;
 	private IContext ctx;

	@Override
	public String getName() {	 
		return "RadarSystemMainDevsOnPc";
	}

	  
	  public void setUp(String configFile) {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else { 
				//Configurazione cabalata nel programma	         
			}
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
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		ctx.deactivate();
		System.exit(0);
	}

	public void doJob( String configFileName ) {
		setUp( configFileName );
		configure();
 	}
	
	public static void main( String[] args) throws Exception {
 		new RadarSystemMainDevsOnRasp().doJob( "RadarSystemConfig.json" );
 	}

}
