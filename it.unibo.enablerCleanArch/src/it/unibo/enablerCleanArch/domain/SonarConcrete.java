package it.unibo.enablerCleanArch.domain;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SonarConcrete implements ISonar{
private int curVal       = -1;

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
			        String data = reader.readLine();
			        dataCounter++;
			        if( dataCounter % numData == 0 ) { //every numData ...
				        System.out.println("SonarConcrete | data=" + data );
				    int d = Integer.parseInt(data);
				    setVal(d);
			        }
		        }//while
        	}catch( Exception e) {
        		System.out.println("SonarConcrete | activate ERROR " + e.getMessage() );
        	}
        }//run        
	  }.start();
		
	}
	
	synchronized void setVal(int d){
		curVal = d;
		this.notify();
	}

	@Override
	public int getVal() {
		waitForUpdatedVal();
 		int v  = curVal;
 		curVal = -1;
		return v;
	}

	private synchronized void waitForUpdatedVal() {
		try {
			while( curVal < 0 ) wait();
 		} catch (InterruptedException e) {
 			System.out.println("SonarConcrete | waitForUpdatedVal ERROR " + e.getMessage() );
		}		
	}
}
