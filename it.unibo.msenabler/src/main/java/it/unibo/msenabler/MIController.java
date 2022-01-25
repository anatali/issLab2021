package it.unibo.msenabler;

import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

//See https://www.baeldung.com/rest-template
@RestController
public class MIController {
    private WebSocketHandler wshandler = WebSocketHandler.getWebSocketHandler();

    @PostMapping( "/photo" )
    public String elaborate( @RequestParam(name="request") String  msg,
                             @RequestParam("image") MultipartFile multipartFile){
        String s         = multipartFile.getContentType();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try {
            byte[] imgContent = multipartFile.getBytes();
            Colors.out("MachineEnablerController | elaborate " + msg
                    + " ContentType:" + multipartFile.getContentType()
                    + " filename:" + fileName
                    + " content length=" + imgContent.length
                    + " wshandler=" + wshandler
            );

            //storeImage(multipartFile.getBytes(),fileName);
             
            //Oltre a memorizzare su file lo invio sulla ws
            propagatePhoto(imgContent, fileName);
            
        }catch(Exception e){
            Colors.outerr("elaborate ERROR:"+ e.getMessage());
        }
        return ("Going to manage photo:" + msg + " " + fileName  );
    }

    
    protected void propagatePhoto(byte[] imgContent, String fileName ) {
        new Thread() {
        	public void run() {
        		try {
        			Colors.out(" --- STORE THE PHOTO  in " + fileName );
        			storeImage(imgContent,fileName);
        			Utils.delay(1000);
        			Colors.out(" --- PROPAGATE THE PHOTO AFTER SOME TIME --- ");
					wshandler.sendBinaryToAll( imgContent   );
				} catch (IOException e) {
					Colors.outerr("elaborate propagate photo ERROR:"+ e.getMessage());
				}
        	} 
        }.start();    	
    }
    public void storeImage(byte[] decodedBytes, String fName) {
        try {
            //byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            Colors.out("MachineEnablerController | storeImage decodedBytes.length=" + decodedBytes.length);
            FileUtils.writeByteArrayToFile(new File(fName), decodedBytes);
        } catch (Exception e) {
            Colors.outerr("RadarSystemMainOnPcCoapBase | storeImage " + e.getMessage());
        }
    }
}
//curl -d request="todo" localhost:8081/photo