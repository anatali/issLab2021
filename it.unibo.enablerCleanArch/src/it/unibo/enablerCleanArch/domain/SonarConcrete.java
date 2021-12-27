package it.unibo.enablerCleanArch.domain;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import it.unibo.enablerCleanArch.supports.Colors;


/*
 * CurVal è usata come valore della distanza corrente misurata
 */
public class SonarConcrete extends SonarModel implements ISonar{
	private int numData           = 3; 
	private int dataCounter       = 1;
	private  BufferedReader reader ;
	
	@Override
	protected void sonarSetUp() {
 		try {
			Process p  = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        reader     = new BufferedReader( new InputStreamReader(p.getInputStream()));	
       	}catch( Exception e) {
       		Colors.outerr("SonarConcrete | activate ERROR " + e.getMessage() );
    	}
	}
	@Override
	protected void updateDistance( int d ) {
 		curVal = new Distance( d );
  	}	
	@Override
	public IDistance getDistance() {
		return curVal;
	}

	@Override
	protected void sonarProduce( ) {
        try {
			String data = reader.readLine();
			dataCounter++;
			if( dataCounter % numData == 0 ) { //every numData ...
 				Colors.out("SonarConcrete | data=" + data );
 				updateDistance( Integer.parseInt(data));
 				//Utils.delay(RadarSystemConfig.sonarDelay);
			}
       }catch( Exception e) {
       	Colors.outerr("SonarConcrete | ERROR " + e.getMessage() );
       }		
	}
 

 }
