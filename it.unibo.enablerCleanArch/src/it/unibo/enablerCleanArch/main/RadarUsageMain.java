package it.unibo.enablerCleanArch.main;

import radarPojo.radarSupport;

public class RadarUsageMain {
 	
	protected void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
 			e.printStackTrace();
		}		
	}
	public void doJob() {
		System.out.println("start");
		radarSupport.setUpRadarGui();
		delay(2000);
		radarSupport.update( "40", "60");
		delay(2000);
	}
	public static void main(String[] args) {
		new RadarUsageMain().doJob();
	}
}
