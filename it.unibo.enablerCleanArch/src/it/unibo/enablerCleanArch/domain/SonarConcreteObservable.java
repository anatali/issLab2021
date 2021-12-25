package it.unibo.enablerCleanArch.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unibo.enablerCleanArch.supports.Colors;

public class SonarConcreteObservable extends SonarModelObservable   {
	private int numData           = 5; 
	private int dataCounter       = 1;
	private  BufferedReader reader ;
	 
	@Override
	protected void sonarSetUp() {
		super.sonarSetUp();
 		try {
			Process p  = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        reader     = new BufferedReader( new InputStreamReader(p.getInputStream()));	
       	}catch( Exception e) {
       		Colors.outerr("SonarConcrete | activate ERROR " + e.getMessage() );
    	}
	} 	

	@Override
	protected void sonarProduce( ) {
        try {
			String data = reader.readLine();
			dataCounter++;
			if( dataCounter % numData == 0 ) { //every numData ...
 				Colors.out("SonarConcrete | data=" + data );
 				updateDistance( Integer.parseInt(data));
			 }
       }catch( Exception e) {
       	Colors.outerr("SonarConcrete | ERROR " + e.getMessage() );
       }		
	}

  
}
