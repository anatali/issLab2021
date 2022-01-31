package it.unibo.enablerCleanArch.domain;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.io.FileUtils;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class WebCamRasp {

	public static void startWebCamRasp() {
		
	}
	public static void startWebCamStream( ) {  
 		try {
       		ColorsOut.out("WebCamRasp | startStream  "  );
			//String cmd = "/usr/local/bin/mjpg_streamer -i \"input_uvc.so -r 1280x720  -d /dev/video0 -f 30\" -o \"output_http.so -p 8085  -w /usr/local/share/mjpg-streamer/www\" ";			
 			//Runtime.getRuntime().exec( cmd );
 			Runtime.getRuntime().exec("sudo bash startWebCamStreamer.sh");

       		ColorsOut.out("WebCamRasp | startStream done "  );
    	}catch( Exception e) {
       		ColorsOut.outerr("WebCamRasp | startStream ERROR " + e.getMessage() );
    	}
	}
	public static void stopWebCamStream( ) {  
 		try {
       		ColorsOut.out("WebCamRasp | stopStream  "  );
  			Runtime.getRuntime().exec("sudo killall mjpg_streamer");
       		ColorsOut.out("WebCamRasp | stopStream done "  );
    	}catch( Exception e) {
       		ColorsOut.outerr("WebCamRasp | stopStream ERROR " + e.getMessage() );
    	}
	}
	
	public static void takePhoto(String fName) {  
 		try {
 	        ColorsOut.out("WebCamRasp | takePhoto in:" + fName);
			//Runtime.getRuntime().exec("fswebcam -r 320×240 " + fName);
  	        Runtime.getRuntime().exec("fswebcam "+ fName ); 	        
       	}catch( Exception e) {
       		ColorsOut.outerr("WebCamRasp | takePhoto ERROR " + e.getMessage() );
    	}
		
	}
	
	public static String getImage(String fName) {  
		//https://www.baeldung.com/java-base64-image-string
		try {
			fName = "/home/pi/nat/it.unibo.enablerCleanArch-1.0/bin/"+fName;
			ColorsOut.out("WebCamRasp | getImage from:" + fName);
			File imgFile = new File(fName);
			//Colors.out("WebCamRasp | getImage imgFile:" + imgFile);
			byte[] fileContent   = FileUtils.readFileToByteArray( imgFile );
			ColorsOut.out("WebCamRasp | getImage fileContent:" + fileContent.length);
			String encodedString = Base64.getEncoder().encodeToString(fileContent);
			ColorsOut.out("WebCamRasp | getImage encodedString=" + encodedString.length());
 			return encodedString;
		} catch (Exception e) {
 			ColorsOut.outerr("RadarSystemMainOnPcCoapBase | getImage " + e.getMessage());
 			return "";
		}
		
	}
}
