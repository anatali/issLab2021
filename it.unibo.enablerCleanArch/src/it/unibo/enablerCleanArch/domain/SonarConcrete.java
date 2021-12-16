package it.unibo.enablerCleanArch.domain;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import it.unibo.enablerCleanArch.supports.Colors;

public class SonarConcrete extends SonarModel implements ISonar{
	protected  IDistance curVal ;
	private int numData           = 5; 
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
	public IDistance getDistance() {
 		return curVal ;
	}
	
	@Override
	protected void sonarProduce(BlockingQueue<IDistance> queue) {
        try {
			String data = reader.readLine();
			dataCounter++;
			if( dataCounter % numData == 0 ) { //every numData ...
				//curVal.setVal( Integer.parseInt(data) );
				Colors.out("SonarConcrete | data=" + data );
				queue.add( new Distance(data) );
			 }
       }catch( Exception e) {
       	Colors.outerr("SonarConcrete | ERROR " + e.getMessage() );
       }
		
	}
/*	
	protected void sonarProduce() {
         try {
 			String data = reader.readLine();
			dataCounter++;
			if( dataCounter % numData == 0 ) { //every numData ...
				curVal.setVal( Integer.parseInt(data) );
 				Colors.out("SonarConcrete | data=" + data );
				valueUpdated( );    
 			 }
        }catch( Exception e) {
        	Colors.outerr("SonarConcrete | ERROR " + e.getMessage() );
        }
 	}*/

 }
