package it.unibo.enablerCleanArch.domain;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SonarConcrete implements ISonar{
private String data = "";

	public static ISonar create() {
		SonarConcrete sonar = new SonarConcrete();
		sonar.activate();
		return sonar;
	}
	 
	 
	private  void activate() {
	  new Thread() {
         public void run() {
        	try {
	    		Process p             = Runtime.getRuntime().exec("sudo ./SonarAlone");
	            BufferedReader reader = new BufferedReader( new InputStreamReader(p.getInputStream()));	
	            int numData           = 5;
	            int dataCounter       = 1;
		        while( true ){
			        data = reader.readLine();
			        dataCounter++;
			        if( dataCounter % numData == 0 ) { //every numData ...
				        System.out.println("SonarConcrete | data=" + data );
				        //c_caller.forward( data );
			        }
		        }//while
        	}catch( Exception e) {
        		e.printStackTrace();
        	}
        }//run        
	  }.start();
		
	}

	@Override
	public int getVal() {
		int v = Integer.parseInt(data);
		return v;
	}

}
