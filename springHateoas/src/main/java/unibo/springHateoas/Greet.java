package unibo.springHateoas;
import org.springframework.hateoas.RepresentationModel;

public class Greet extends RepresentationModel<Greet>{
	private String message;
	
	public Greet() {
		 
	}
	
	public Greet(String message) {
        this.message = message;
    }
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString(){
		return message;
	}


}
