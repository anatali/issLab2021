package it.unibo.webspring.intro;
//See https://curl.haxx.se/docs/manual.html  

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

 
//@RestController 	//annotates all the methods with @ResponseBody that embeds the return value in the body of HTTP answer
public class MachineInterfaceController { 
    @Value("${machine.logo}")
    String appName;
    
    Set<String> robotMoves = new HashSet<String>(); 
    
    public MachineInterfaceController() {
        robotMoves.addAll( Arrays.asList(new String[] {"w","s","h","r","l","z","x"}) );        	
    }

  String applicationModelRep="waiting";
/* 
 @GetMapping("/") 		 
  public String entry(Model model) {
	  System.out.println("------------------- MachineInterfaceController homePage " + model  );
      model.addAttribute("arg", appName);
      return "gui";	//just a string, no more a view ...
  } 
*/   
  
  @GetMapping("/model")  
  public String applmodel(Model model) {
	  model.addAttribute("arg", appName);
      return String.format("MachineInterfaceController applmodel = " + applicationModelRep );      
  }     
	
  //curl -d move=w localhost:8080/w									//OK	data urlencoded
  //curl -d move=w -H 'Content-type:text/plain' localhost:8080/w	//OK
  //curl -X POST -H 'Content-type:text/plain' localhost:8080/w		//KO
  //curl -X POST  localhost:8080/w									//KO
   @RequestMapping(value = "/w", 
    		method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody String doMove( String move ) {
		applicationModelRep = "w";
        return  "doMove APPLICATION_FORM_URLENCODED_VALUE move="+move+ " Current Robot State:"+applicationModelRep; 
    }    

   //curl -d move=r --header 'Content-type:text/plain' localhost:8080/r   	//KO
   //curl -d move=r localhost:8080/r										//KO
   @RequestMapping(value = "/r", 
   		method = RequestMethod.POST,
   		consumes = MediaType.TEXT_PLAIN_VALUE)
   public @ResponseBody String doMoveR( String move ) {
		applicationModelRep = "r";
       return  "doMoveR   move="+move+ " Current Robot State:"+applicationModelRep; 
   }    
   
   //curl --request POST --header 'Content-type:text/plain' localhost:8080/s	//OK
   //curl -d move=s -H 'Content-type:text/plain' localhost:8080/s   			//OK
   //curl -d move=s localhost:8080/s											//OK
	@PostMapping( path = "/s" ) 
	public String doMoveS(String move) { //@RequestParam(name="move", required=false, defaultValue="h") 
	  applicationModelRep = move;
	  return String.format("doMoveS move=" + applicationModelRep  );   
	}	

	//curl -d move=z localhost:8080/move									//OK
	@PostMapping( path = "/move" ) 
	public String doMove( 
		@RequestParam(name="move", required=false, defaultValue="h") 
		//binds the value of the query string parameter name into the moveName parameter of the  method
		String moveName, Model viewmodel) {
		System.out.println("------------------- MachineInterfaceController doMove=" + moveName  );
		if( robotMoves.contains(moveName) ) {	//NO MORE view model since no more view ...
			applicationModelRep = moveName;
			//viewmodel.addAttribute("arg", "Current Robot State:"+applicationModelRep);
			return "guiMoves move=" + applicationModelRep;	
		}else {
 			//viewmodel.addAttribute("arg", "Sorry: move unknown - Current Robot State:"+applicationModelRep );
			return "Sorry: the move " + moveName + " is unknown ";		 
		}		
	}	
    

    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"MachineInterfaceController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }
 
}

