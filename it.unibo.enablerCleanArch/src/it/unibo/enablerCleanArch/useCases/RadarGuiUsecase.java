package it.unibo.enablerCleanArch.useCases;

import it.unibo.enablerCleanArch.domain.*;
 

public class RadarGuiUsecase {
private static boolean started = false;

 
	public static void doUseCase( IRadarGui radar, int d ) {
		/*
		if( ! started ) {
 			started = true;
 			radarPojo.radarSupport.setUpRadarGui();  //Naive ....
 		}
 		*/
		System.out.println("RadarGuiUsecase | sonar data=" + d);
		radar.update(""+d, "90");
  	}	
	/*
	public static void activate( ISonar sonar) {
		new Thread() {
			public void run() {
				try {
 					while( true ) {
						int d = sonar.getVal();	//ricevo dato dal sonar
						System.out.println("RadarGui | sonar data=" + d);
						radarPojo.radarSupport.update(""+d, "90");
 						Thread.sleep(1000);
					}
				} catch (Exception e) {
		 			e.printStackTrace();
				}					
			}
		}.start();
	}*/

}
