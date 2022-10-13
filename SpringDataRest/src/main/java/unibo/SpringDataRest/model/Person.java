package unibo.SpringDataRest.model;

import org.springframework.context.annotation.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //Hybernate
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	private String firstName;
	private String lastName;
	//public long getId() {return id;}
	//public void setId(long id) {this.id=id;}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String toString(){
		return "id="+id+ " firstName="+firstName + " lastName="+lastName;
	}
}
