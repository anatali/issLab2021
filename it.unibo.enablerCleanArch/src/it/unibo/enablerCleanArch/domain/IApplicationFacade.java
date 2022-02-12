package it.unibo.enablerCleanArch.domain;
  

public interface IApplicationFacade {  
	public void setUp( String configFile );
	public String getName();
	public void ledActivate( boolean v );	
	public String ledState(   );
	public void sonarActivate(   );
	public boolean sonarIsactive(   );
	public void sonarDectivate(   );
	public String sonarDistance(   );	
 	public void doLedBlink();
 	public void stopLedBlink();
}
