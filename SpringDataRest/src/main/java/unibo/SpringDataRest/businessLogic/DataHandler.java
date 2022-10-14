package unibo.SpringDataRest.businessLogic;

import unibo.SpringDataRest.model.Person;

import java.util.Iterator;
import java.util.Vector;

public class DataHandler {
    private static Vector<Person> userDataList = new Vector<Person>();
    private static int id = 0;

    private static void addNewPerson(){
        Person userData = new Person();
        userData.setId(id);
        userData.setFirstName("dummy");
        userData.setLastName("dummy");
        System.out.println(" --- DataHandler createUserData id=" + id);
        userDataList.add(userData);
        id++;
    }
    public static void addPerson(Person userData){
        System.out.println(" --- DataHandler addUserData id=" + id);
        userDataList.add(userData);
    }
    public static Person getLast(){
        if( userDataList.isEmpty()) addNewPerson();
        return userDataList.lastElement();
    }
    public static Person getFirst(){
        if( userDataList.isEmpty()) addNewPerson();
        return userDataList.firstElement();
    }
    public static String getPersonWithLastName(String lastName){
        System.out.println(" --- DataHandler getPersonWithLastName lastName=" + lastName);
        String pFound = "person not found";
        Iterator<Person> iter = userDataList.iterator();
        while( iter.hasNext() ){
            Person p = iter.next();
            if(p.getLastName().equals(lastName)) {
                pFound = p.toString();
                break;
            }
        }
        //userDataList.forEach( p -> { if(p.getLastName().equals(lastName)) p0=p;} );
        return pFound;
    }
}
