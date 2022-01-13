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
	 
	@Override
	protected void sonarSetUp() {
		super.sonarSetUp();
 		try {
			Process p  = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        reader     = new BufferedReader( new InputStreamReader(p.getInputStream()));	
	        Colors.out("SonarConcreteObservable | sonarSetUp done");
       	}catch( Exception e) {
       		Colors.outerr("SonarConcreteObservable | sonarSetUp ERROR " + e.getMessage() );
    	}
	} 	

	@Override
	protected void sonarProduce( ) {
        try {
			String data = reader.readLine();
			if( data == null ) return;
			dataCounter++;
			
			if( dataCounter % numData == 0 ) { //every numData ...				
				int v = Integer.parseInt(data);
				//Colors.out("SonarConcrete | v=" + v );
				if( lastSonarVal != v && v < RadarSystemConfig.sonarDistanceMax) {	
					//Eliminiamo dati del tipo 3430 //TODO: filtri in sottosistremia stream
 					Colors.out("SonarConcrete updateDistance | v=" + v );
 					lastSonarVal = v;
 	 				updateDistance( v );
				}
 				//Utils.delay(RadarSystemConfig.sonarDelay);
			}
       }catch( Exception e) {
       		Colors.outerr("SonarConcrete | ERROR " + e.getMessage() );
       }		
	}

  
}
