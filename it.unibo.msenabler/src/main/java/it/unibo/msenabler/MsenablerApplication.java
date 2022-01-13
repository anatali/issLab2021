package it.unibo.msenabler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

@SpringBootApplication
public class MsenablerApplication {
public static RadarSystemDevicesOnRaspMqtt sys ;

	public static void main(String[] args) {
		SpringApplication.run(MsenablerApplication.class, args);
		sys = new RadarSystemDevicesOnRaspMqtt();
        WebSocketHandler h     = WebSocketHandler.getWebSocketHandler();
        sys.doJob("RadarSystemConfig.json");
        
        ISonarObservable sonar = sys.getSonar();
        while( sonar == null ) {
        	Colors.out("MsenablerApplication | sonar is null ... ");
        	Utils.delay(250);
        	sonar = sys.getSonar();
        }
        sonar.register(h);
 		
	}

}
