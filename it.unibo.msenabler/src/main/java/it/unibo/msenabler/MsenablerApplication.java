package it.unibo.msenabler;

import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;
import it.unibo.enablerCleanArch.main.RadarSystemMainOnPcMqtt;
import it.unibo.enablerCleanArch.supports.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsenablerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsenablerApplication.class, args);
		new RadarSystemDevicesOnRaspMqtt().doJob("RadarSystemConfig.json");
		//Utils.delay(1000);
		//HumanEnablerController.sysClient = new RadarSystemMainOnPcMqtt();
	}

}
