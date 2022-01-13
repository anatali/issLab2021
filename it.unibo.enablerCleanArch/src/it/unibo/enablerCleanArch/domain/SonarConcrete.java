package it.unibo.enablerCleanArch.domain;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;


/*
 * CurVal è usata come valore della distanza corrente misurata
 */
public class SonarConcrete extends SonarModel implements ISonar{
	private int numData           = 3; 
	private int dataCounter       = 1;
	private  BufferedReader reader ;
	private int lastSonarVal      = 0;
	
	@Override
	protected void sonarSetUp() {
 		try {
			Process p  = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        reader     = new BufferedReader( new InputStreamReader(p.getInputStream()));
	        Colors.out("SonarConcrete | sonarSetUp done");
       	}catch( Exception e) {
       		Colors.outerr("SonarConcrete | sonarSetUp ERROR " + e.getMessage() );
    	}
	}
//	@Override
//	protected void updateDistance( int d ) {
// 		curVal = new Distance( d );
//  	}	
//	@Override
//	public IDistance getDistance() {
//		return curVal;
//	}

	//Identical to SonarConcrete

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
