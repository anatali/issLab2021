package it.unibo.enablerCleanArch.domain;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.io.FileUtils;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

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
	
	public static void takePhoto(String fName) {  
 		try {
 	        Colors.out("WebCamRasp | takePhoto in:" + fName);
			//Runtime.getRuntime().exec("fswebcam -r 320×240 " + fName);
 	        /*
 	        new Thread() {
 	        	public void run() {
 	        		try {
						Runtime.getRuntime().exec("fswebcam -r &"+ fName );
					} catch (IOException e) {
						Colors.outerr("WebCamRasp | thread takePhoto ERROR " + e.getMessage() );					}
 	        	}
 	        }.start();
 	        */
 	       Runtime.getRuntime().exec("fswebcam -r "+ fName + " &");
 	       Utils.delay(2000);
 	       /*
 	        Colors.out("WebCamRasp | takePhoto launched "  );
 	        File photoFile = new File( fName );
			while( ! photoFile.isFile() ) {
				Colors.out( "WebCamRasp | waiting for file ... " + fName  );
				Utils.delay(500); 
				photoFile = new File( fName );
			}
 	        */
 	        
       	}catch( Exception e) {
       		Colors.outerr("WebCamRasp | takePhoto ERROR " + e.getMessage() );
    	}
		
	}
	
	public static String getImage(String fName) {  
		//https://www.baeldung.com/java-base64-image-string
		try {
			fName = "/home/pi/nat/it.unibo.enablerCleanArch-1.0/bin/"+fName;
			Colors.out("WebCamRasp | getImage from:" + fName);
			File imgFile = new File(fName);
			//Colors.out("WebCamRasp | getImage imgFile:" + imgFile);
			byte[] fileContent   = FileUtils.readFileToByteArray( imgFile );
			Colors.out("WebCamRasp | getImage fileContent:" + fileContent.length);
			String encodedString = Base64.getEncoder().encodeToString(fileContent);
			Colors.out("WebCamRasp | getImage encodedString=" + encodedString.length());
 			return encodedString;
		} catch (Exception e) {
 			Colors.outerr("RadarSystemMainOnPcCoapBase | getImage " + e.getMessage());
 			return "";
		}
		
	}
}
