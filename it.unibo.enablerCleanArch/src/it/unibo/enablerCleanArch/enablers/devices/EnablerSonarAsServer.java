package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;

public class EnablerSonarAsServer extends EnablerAsServer{
private ISonar sonar;

	public EnablerSonarAsServer(String name, int port, ProtocolType protocol, IApplMsgHandler handler, ISonar sonar) {
		super(name, port, protocol, handler);
		this.sonar = sonar;
	}
 	public boolean isActive() {
 		return sonar.isActive();
 	}
 	public void stop() {
 		//Colors.out(name+" |  EnablerSonarAsServer deactivate  "  );
 		super.stop();
 		sonar.deactivate();
  	}
 	public void start() {
 		//Colors.out(name+" |  EnablerSonarAsServer activate  "  );
 		super.start();
 		sonar.activate();
  	}

}
