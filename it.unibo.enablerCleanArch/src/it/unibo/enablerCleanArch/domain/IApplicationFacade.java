package it.unibo.enablerCleanArch.domain;
  

public interface IApplicationFacade { //extends IApplication
	//public void doJob(String configFileName);
	public String getName();
	public void setUp( String configFile );
	public void ledActivate( boolean v );	
	public String ledState(   );
	public void sonarActivate(   );
	public boolean sonarIsactive(   );
	public void sonarDectivate(   );
	public String sonarDistance(   );	
	public void takePhoto( String fName  );	
	public void startWebCamStream(   );	

//	public ISonarObservable getSonar();
//	public void doLedBlink();
//	public void stopLedBlink();
}
