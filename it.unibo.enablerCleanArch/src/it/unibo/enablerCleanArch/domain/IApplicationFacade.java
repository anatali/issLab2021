package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.supports.Utils;

public interface IApplicationFacade extends IApplication{
	//public void doJob(String configFileName);
	//public String getName();
	public void setUp( String configFile );
	public void ledActivate( boolean v );	
	public String ledState(   );
	public void sonarActivate(   );
	public boolean sonarIsactive(   );
	public void sonarDectivate(   );
	public String sonarDistance(   );	
	public ISonarObservable getSonar();
	public void doLedBlink();
	public void stopLedBlink();
}
