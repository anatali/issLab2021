package it.unibo.enablerCleanArch.domain;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
//import it.unibo.enablerCleanArch.supports.Utils;


/*
 * CurVal è usata come valore della distanza corrente misurata
 */
public class SonarConcrete extends SonarModel implements ISonar{
//	private int numData           = 3; 
//	private int dataCounter       = 1;
	private  BufferedReader reader ;
	private int lastSonarVal      = 0;
	private Process p ;
	
	@Override
	protected void sonarSetUp() {
 		try {
			p       = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        reader  = new BufferedReader( new InputStreamReader(p.getInputStream()));
	        Colors.out("SonarConcrete | sonarSetUp done");
       	}catch( Exception e) {
       		Colors.outerr("SonarConcrete | sonarSetUp ERROR " + e.getMessage() );
    	}
	}
	 
//	@Override
//	protected void updateDistance( int d ) {
//		Colors.out("SonarConcrete  | updateDistance d=" + d  );
// 		if( RadarSystemConfig.protcolType  == ProtocolType.mqtt) {
// 			super.updateDistance(d);
// 			return;
// 		}
////			try {
////				blockingQueue.put( curVal );
////			} catch (InterruptedException e) {
////				Colors.outerr("SonarConcrete  | updateDistance ERROR:" + e.getMessage() );;
////			}
// 		curVal = new Distance( d );
//  	}	
	
//	@Override
//	public IDistance getDistance() {
//		if( RadarSystemConfig.protcolType  == ProtocolType.mqtt) {
//			return super.getDistance();
//		}
//		Colors.out("SonarConcrete getDistance | curVal=" + curVal  );
//		return curVal;
//	}

	@Override
	public void activate() {
		Colors.out("SonarConcrete | activate ");
 		try { 
 			if( p == null ) {
		        Colors.out("SonarConcrete | sonarSetUp ");
				p          = Runtime.getRuntime().exec("sudo ./SonarAlone");
		        reader     = new BufferedReader( new InputStreamReader(p.getInputStream()));	
 		}
       	}catch( Exception e) {
       		Colors.outerr("SonarConcrete | sonarSetUp ERROR " + e.getMessage() );
    	}
 		super.activate();
 	}
	

	@Override
	protected void sonarProduce( ) {
        try {
			String data = reader.readLine();
			if( data == null ) return;
			//dataCounter++;
			
			//if( dataCounter % numData == 0 ) { //every numData ...				
				int v = Integer.parseInt(data);
				//Colors.out("SonarConcrete | v=" + v );
				if( lastSonarVal != v && v < RadarSystemConfig.sonarDistanceMax) {	
					//Eliminiamo dati del tipo 3430 //TODO: filtri in sottosistemi a stream
 					lastSonarVal = v;
 	 				updateDistance( v );
				}
 				//Utils.delay(RadarSystemConfig.sonarDelay);
			//}
       }catch( Exception e) {
       		Colors.outerr("SonarConcrete | ERROR " + e.getMessage() );
       }		
	}
 
	@Override
	public void deactivate() {
		Colors.out("SonarConcrete | deactivate", Colors.GREEN);
	    lastSonarVal      = 0;
	    curVal            = new Distance(90);
		if( p != null ) {
			p.destroy();  //Block the runtime process
			p=null;
		}
		super.deactivate();
 	}

 }
