package it.unibo.webspring.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@Controller
class HumanInterfaceController {
    @Value("\${human.logo}")
    var appName: String? = null

    var applicationModelRep = "waiting"


    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName)
        println("HumanInterfaceController | entry model=$model")
        return "naiveGui"
    }

    /*
    Spring provides a Model object which can be passed into the controller.
    You can configure this model object via the addAttribute(key, value) method.
     */

    @GetMapping("/model")
    @ResponseBody   //With this annotation, the String returned by the methods is sent to the browser as plain text.
    fun  homePage( model: Model) : String{
        model.addAttribute("arg", appName)
        println("HumanInterfaceController | homePage model=$model")
        return String.format("HumanInterfaceController text normal state= $applicationModelRep"  );
    }

    //@RequestMapping methods assume @ResponseBody semantics by default.
    //https://springframework.guru/spring-requestmapping-annotation/

    //@RequestMapping( "/w") //, method = RequestMethod.POST,  MediaType.APPLICATION_FORM_URLENCODED_VALUE
    @PostMapping("/w")  //signals that this method handles POST requests
    @ResponseBody
    fun doMove( model: Model, @RequestParam(name = "move") move:String ) : String {
        println("HumanInterfaceController | doMove  $move")
        applicationModelRep = "doing $move"
        return  "doMove Current Robot State: $applicationModelRep";
    }

    /*

	@PostMapping( path = "/move" ) 
	public String doMove( 
		@RequestParam(name="move", required=false, defaultValue="h") 
		//binds the value of the query string parameter name into the moveName parameter of the  method
		String moveName, Model viewmodel) {
		if( robotMoves.contains(moveName) ) {
			applicationModelRep = moveName;
			viewmodel.addAttribute("arg", "Current Robot State:"+applicationModelRep);
		}else {
			viewmodel.addAttribute("arg", "Sorry: move unknown - Current Robot State:"+applicationModelRep );
		}		
		return "guiMoves";
	}	
    
	@PostMapping( path = "/movegui" ) 
	public String doHalt( Model viewmodel ) {
 		viewmodel.addAttribute("arg", "Current Robot State:" + applicationModelRep);
		return "guiMoves";		
	}
	

    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"HumanInterfaceController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }
*/

    init {
        //robotMoves.addAll(Arrays.asList(*arrayOf("w", "s", "h", "r", "l", "z", "x")))
    }
}