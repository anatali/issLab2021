package it.unibo.enablerCleanArch.domain;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SonarConcrete extends SonarAbstract implements ISonar{
	private int numData           = 5; 
	private int dataCounter       = 1;
	private  BufferedReader reader ;
	
	@Override
	protected void sonarSetUp() {
		curVal = 0;		
		try {
			Process p  = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        reader     = new BufferedReader( new InputStreamReader(p.getInputStream()));	
       	}catch( Exception e) {
    		System.out.println("SonarConcrete | activate ERROR " + e.getMessage() );
    	}
	}
	protected void sonarBehavior() {
         try {
 			String data = reader.readLine();
			dataCounter++;
			if( dataCounter % numData == 0 ) { //every numData ...
				System.out.println("SonarConcrete | data=" + data );
				curVal = Integer.parseInt(data);
				if( curVal < 100 ) setVal( );   //inutile inviare dati che non si possono visualizzare
			 }
        }catch( Exception e) {
        		System.out.println("SonarConcrete | ERROR " + e.getMessage() );
        }
 	}
 }