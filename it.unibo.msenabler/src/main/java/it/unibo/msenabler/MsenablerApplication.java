package it.unibo.msenabler;

import it.unibo.enablerCleanArch.domain.IApplicationFacade;
import it.unibo.enablerCleanArch.main.onpc.RadarSystemMainEntryOnPc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;


@SpringBootApplication
public class MsenablerApplication {
//public static RadarSystemDevicesOnRaspMqtt sysMqtt ;
//public static RadarSystemMainOnPcCoapBase sysCoap ;
//public static RadarSystemMainEntryOnPc sysAppl;
public static IApplicationFacade sysAppl;
public static final boolean allOnRasp = false;   //when true, this appl must run on Raspberry
/*
 * This operation is called when the application runs over Raspberry
 */
//    public static IApplicationFacade startSystemMqtt() {
//		sysMqtt = new RadarSystemDevicesOnRaspMqtt();
//        WebSocketHandler h     = WebSocketHandler.getWebSocketHandler();
//        sysMqtt.doJob("RadarSystemConfig.json");
//        
//        ISonarObservable sonar = sysMqtt.getSonar();
//        while( sonar == null ) {
//        	ColorsOut.out("MsenablerApplication | sonar is null ... ");
//        	Utils.delay(250);
//        	sonar = sysMqtt.getSonar();
//        }
//        sonar.register(h);
//    	return sysMqtt;
//    }
    
    /*
     * This operation is called when the application runs on PC
     */
    public static IApplicationFacade startSystem(String raspAddr) {
    	sysAppl = new RadarSystemMainEntryOnPc(raspAddr,"RadarSystemConfig.json"); 
    	//sysAppl.doJob("RadarSystemConfig.json");
		//sysAppl.setUp("RadarSystemConfig.json");
        WebSocketHandler h  = WebSocketHandler.getWebSocketHandler();
    	//CoapConnection sonarCoapSupport = sysCoap.getSonarCoapConnection();
    	//sonarCoapSupport.observeResource(new SonarDataObserver(h) );
    	//RadarSystemConfig.raspHostAddr = "192.168.1.112";
		return sysAppl;
    }
    
	public static void main(String[] args) {
		SpringApplication.run(MsenablerApplication.class, args);
		//if( allOnRasp ) startSystemMqtt(); else startSystemCoap();
	}

}
