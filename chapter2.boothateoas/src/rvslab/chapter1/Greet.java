package rvslab.chapter1;

public class Greet extends ResourceSupport{
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
