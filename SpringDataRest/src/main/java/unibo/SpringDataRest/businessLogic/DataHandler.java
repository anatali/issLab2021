package unibo.SpringDataRest.businessLogic;

import org.springframework.beans.factory.annotation.Autowired;
import unibo.SpringDataRest.communication.Mailer;
import unibo.SpringDataRest.model.Person;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class DataHandler {
    private static Vector<Person> userDataList = new Vector<Person>();
    private static int id = 0;



    private static void addNewPerson(){
        Person userData = new Person();
        userData.setId(id);
        userData.setFirstName("dummy");
        userData.setLastName("dummy");
        System.out.println(" --- DataHandler addNewPerson id=" + id);
        userDataList.add(userData);
        id++;
    }
    public static void addPerson(Person p){
        System.out.println(" --- DataHandler addPerson  " +p );
        userDataList.add(p);
        //Added for mailing
        Mailer.sendAMail("antonio.natali@unibo.it", p.toString());
    }
    public static void removePerson(Person p){
        System.out.println(" --- DataHandler removePerson  " +p );
        userDataList.remove(p);
    }
    public static List<Person> getAllPersons(){
        //if( userDataList.isEmpty()) addNewPerson();
        return userDataList;
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
        //System.out.println(" --- DataHandler getPersonWithLastName lastName=" + lastName);
        String pFound = "person not found";
        Iterator<Person> iter = userDataList.iterator();
        while( iter.hasNext() ){
            Person p = iter.next();
            if(p.getLastName().equals(lastName)) {
                pFound = p.toString();
                break;
            }
        }
        System.out.println(" --- DataHandler getPersonWithLastName="
                + lastName + " result: " + ! pFound.contains("not found"));
        return pFound;
    }
    public static Person getThePersonWithLastName(String lastName){
        //System.out.println(" --- DataHandler getPersonWithLastName lastName=" + lastName);
        Person pFound = null;
        Iterator<Person> iter = userDataList.iterator();
        while( iter.hasNext() ){
            Person p = iter.next();
            if(p.getLastName().equals(lastName)) {
                pFound = p;
                break;
            }
        }
        System.out.println(" --- DataHandler getPersonWithLastName="
                + lastName + " result: " + pFound);
        return pFound;
    }
}
