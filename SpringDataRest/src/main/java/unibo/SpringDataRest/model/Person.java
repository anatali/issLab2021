package unibo.SpringDataRest.model;

import lombok.Data;

@Data
public class Person {
    private long id;
    private String firstName;
    private String lastName;

    public String toString(){
        return "person id="+id+" firstName="+firstName + " lastName="+lastName;
    }
}
