package it.unibo.enablerCleanArch.domain;
import it.unibo.enablerCleanArch.supports.Colors;

public class WebCamRasp {

	public static void startWebCamRasp() {
		
	}
	
	public static void getPhoto(String fName) { //image.jpg
 		try {
 			//cd /home/pi/nat/mjpg-streamer/mjpg-streamer-experimental
			Runtime.getRuntime().exec("cd /home/pi/nat/mjpg-streamer/mjpg-streamer-experimental");
			Runtime.getRuntime().exec("fswebcam -r 320×240 " + fName);
 	        Colors.out("WebCamRasp | getPhoto done");
       	}catch( Exception e) {
       		Colors.outerr("WebCamRasp | getPhoto ERROR " + e.getMessage() );
    	}
		
	}
}
