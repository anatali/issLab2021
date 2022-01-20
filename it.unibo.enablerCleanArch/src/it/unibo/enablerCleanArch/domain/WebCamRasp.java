package it.unibo.enablerCleanArch.domain;
import it.unibo.enablerCleanArch.supports.Colors;

public class WebCamRasp {

	public static void startWebCamRasp() {
		
	}
	public static void startWebCamStream( ) {  
 		try {
       		Colors.out("WebCamRasp | startStream  "  );
			//String cmd = "/usr/local/bin/mjpg_streamer -i \"input_uvc.so -r 1280x720  -d /dev/video0 -f 30\" -o \"output_http.so -p 8085  -w /usr/local/share/mjpg-streamer/www\" ";			
 			//Runtime.getRuntime().exec( cmd );
 			Runtime.getRuntime().exec("sudo bash startWebCamStreamer.sh");

       		Colors.out("WebCamRasp | startStream done "  );
    	}catch( Exception e) {
       		Colors.outerr("WebCamRasp | startStream ERROR " + e.getMessage() );
    	}
	}
	public static void stopWebCamStream( ) {  
 		try {
       		Colors.out("WebCamRasp | stopStream  "  );
  			Runtime.getRuntime().exec("sudo killall mjpg_streamer");
       		Colors.out("WebCamRasp | stopStream done "  );
    	}catch( Exception e) {
       		Colors.outerr("WebCamRasp | stopStream ERROR " + e.getMessage() );
    	}
	}
	
	public static void getPhoto(String fName) {  
 		try {
 	        Colors.out("WebCamRasp | getPhoto in:" + fName);
			//cd /home/pi/nat/mjpg-streamer/mjpg-streamer-experimental
			//Runtime.getRuntime().exec("cd /home/pi/nat/mjpg-streamer/mjpg-streamer-experimental");
			//Runtime.getRuntime().exec("fswebcam -r 320×240 " + fName);
 	        Runtime.getRuntime().exec("fswebcam "+ fName );
 	        Colors.out("WebCamRasp | getPhoto done");
       	}catch( Exception e) {
       		Colors.outerr("WebCamRasp | getPhoto ERROR " + e.getMessage() );
    	}
		
	}
}
