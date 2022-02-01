package it.unibo.enablerCleanArch.domain;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
//import it.unibo.enablerCleanArch.supports.Utils;


/*
 * CurVal è usata come valore della distanza corrente misurata
 */
public class SonarConcrete extends SonarModel implements ISonar{
	private  BufferedReader reader ;
	private int lastSonarVal      = 0;
	private Process p ;
	
	@Override
	protected void sonarSetUp() {
 		try {
			p       = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        reader  = new BufferedReader( new InputStreamReader(p.getInputStream()));
	        ColorsOut.out("SonarConcrete | sonarSetUp done");
       	}catch( Exception e) {
       		ColorsOut.outerr("SonarConcrete | sonarSetUp ERROR " + e.getMessage() );
    	}
	}


	@Override
	public void activate() {
		ColorsOut.out("SonarConcrete | activate ");
 		if( p == null ) { sonarSetUp();  }
 		super.activate();
 	}
	

	@Override
	protected void sonarProduce( ) {
        try {
			String data = reader.readLine();
			if( data == null ) return;
			int v = Integer.parseInt(data);
			//Colors.out("SonarConcrete | v=" + v );
			if( lastSonarVal != v && v < RadarSystemConfig.sonarDistanceMax) {	
				//Eliminiamo dati del tipo 3430 //TODO: filtri in sottosistemi a stream
 				lastSonarVal = v;
 	 			updateDistance( v );
			}
       }catch( Exception e) {
       		ColorsOut.outerr("SonarConcrete | ERROR " + e.getMessage() );
       }		
	}
 
	@Override
	public void deactivate() {
		ColorsOut.out("SonarConcrete | deactivate", ColorsOut.GREEN);
	    lastSonarVal      = 0;
	    curVal            = new Distance(90);
		if( p != null ) {
			p.destroy();  //Block the runtime process
			p=null;
		}
		super.deactivate();
 	}

 }
