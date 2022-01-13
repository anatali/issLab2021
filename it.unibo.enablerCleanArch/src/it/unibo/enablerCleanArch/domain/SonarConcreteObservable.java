package it.unibo.enablerCleanArch.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public class SonarConcreteObservable extends SonarModelObservable   {
	private int numData           = 5; 
	private int dataCounter       = 1;
	private  BufferedReader reader ;
	private int lastSonarVal      = 0;
	private Process p             = null;
	 
	@Override
	protected void sonarSetUp() {
		super.sonarSetUp();
 		try { 
 			if( p == null ) {
		        Colors.out("SonarConcreteObservable | sonarSetUp ");
				p          = Runtime.getRuntime().exec("sudo ./SonarAlone");
		        reader     = new BufferedReader( new InputStreamReader(p.getInputStream()));	
 		}
       	}catch( Exception e) {
       		Colors.outerr("SonarConcreteObservable | sonarSetUp ERROR " + e.getMessage() );
    	}
	} 	
	
	@Override
	public void activate() {
		Colors.out("SonarConcreteObservable | activate ");
 		try { 
 			if( p == null ) {
		        Colors.out("SonarConcreteObservable | sonarSetUp ");
				p          = Runtime.getRuntime().exec("sudo ./SonarAlone");
		        reader     = new BufferedReader( new InputStreamReader(p.getInputStream()));	
 		}
       	}catch( Exception e) {
       		Colors.outerr("SonarConcreteObservable | sonarSetUp ERROR " + e.getMessage() );
    	}
 		super.activate();
 	}


	//Identical to SonarConcrete
	@Override
	protected void sonarProduce( ) {
        try {
			String data = reader.readLine();
			if( data == null ) return;
			dataCounter++;
			
			if( dataCounter % numData == 0 ) { //every numData ...				
				int v = Integer.parseInt(data);
				//Colors.out("SonarConcreteObservable | v=" + v );
				if( lastSonarVal != v && v < RadarSystemConfig.sonarDistanceMax) {	
					//Eliminiamo dati del tipo 3430 //TODO: filtri in sottosistremia stream
 					Colors.out("SonarConcreteObservable call updateDistance | v=" + v );
 					lastSonarVal = v;
 	 				updateDistance( v );
				}
 				//Utils.delay(RadarSystemConfig.sonarDelay);
			}
       }catch( Exception e) {
       		Colors.outerr("SonarConcreteObservable | sonarProduce: " + e.getMessage() );
       }		
	}

	@Override
	public void deactivate() {
		Colors.out("SonarConcreteObservable | deactivate", Colors.GREEN);
		if( p != null ) {
			p.destroy();  //Block the runtime process
			p=null;
		}
		super.deactivate();
 	}
  
}
