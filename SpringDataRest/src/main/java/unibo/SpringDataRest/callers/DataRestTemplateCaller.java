package unibo.SpringDataRest.callers;
import com.squareup.okhttp.*;
import com.squareup.okhttp.MediaType;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import unibo.SpringDataRest.model.Person;

import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.StringReader;

/*
RestTemplate should be used for Rest interactions
 */


public class DataRestTemplateCaller  {
    final protected String BASE_URL   = "http://localhost:8080/RestApi";

    private void readTheHtmlPage(String htmlString, String elementID){
        try {
            HTMLEditorKit htmlEditKit = new HTMLEditorKit();
            HTMLDocument htmlDocument = new HTMLDocument();
            try {
                htmlEditKit.read(new StringReader( htmlString ), htmlDocument, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Element foundField  = htmlDocument.getElement(elementID);
            int start  = foundField.getStartOffset();
            int length = foundField.getEndOffset() - start;
            System.out.println("foundField:"  + start + " " + length);
            String s   = foundField.getDocument().getText(start,length);
            System.out.println( s );
            //System.out.println("title="+htmlDocument.getProperty("title")); //

        } catch( Exception e){
            System.out.println( "readTheHtmlPage ERROR:"+e.getMessage());
            //e.printStackTrace();
        }
    }
    public void runGet(String lastName){
        System.out.println("--------- runGet");
        String response =  doGet(BASE_URL +"/getAPerson?lastName="+lastName);
        //System.out.println(response);   //Visualizza la pagina
        readTheHtmlPage(response,"FOUND");
    }
    public void runGetAll( ){
        System.out.println("--------- runGetAll");
        String response =  doGet(BASE_URL +"/getAllPersons");
        //System.out.println(response);   //Visualizza la pagina
        readTheHtmlPage(response,"ALLPERSONS");
    }

    //https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
    public void runPost() {
        Person p0       = new Person();
        p0.setId(1);
        p0.setFirstName("Giovanni");
        p0.setLastName("Verga");
        HttpEntity<Person> request = new HttpEntity<Person>(p0);
        int respCode = doPost( BASE_URL +"/createPersonWithModel", request);
        //System.out.println("runPost response="+response);
        if( respCode == 200 || respCode == 201) System.out.println("runPost Verga ok" );
        else System.out.println("WARNING: runPost problem:" + respCode);


    }


    protected int doPost(String urlStr, HttpEntity<Person> request)  {
        try{
            RestTemplate rt = new RestTemplate( );

            System.out.println("doPost urlStr=" + urlStr);

            //HttpEntity<Person> request = new HttpEntity<Person>(p0);
            //ResponseEntity<Person> foo = rt.postForEntity(urlStr,request,Person.class);         //OK
            Person foo = rt.postForObject(urlStr, request, Person.class);                         //OK

            //String foo = rt.postForObject(BASE_URL +"/createPerson", request, String.class);
            System.out.println("foo:" + foo  ); //Tutta la pagina
            if( foo != null && foo.getId()==1 ) return 201; else return 0;
                //return foo.getStatusCode().value();
/*
            //ResponseEntity<Person> createResponse  = rt.postForEntity(BASE_URL +"/createPerson", p0, Person.class);
            ResponseEntity<Person> createResponse  = rt.postForEntity(BASE_URL +"/createPersonWithModel", p0, Person.class);
            HttpStatus code = createResponse.getStatusCode();

            System.out.println("response.getStatusCode=" + code.value() );
            //System.out.println("response.getBody      =" + createResponse.getBody() );
            return code.value() ;

 */
        }catch(Exception e){
            System.out.println("response.ERROR =" + e.getMessage() );
            return 0;
        }
    }



    protected String doGet(String url)  {
        try{
            RestTemplate rt = new RestTemplate( );
            ResponseEntity<String> response = rt.getForEntity( url, String.class);
            //System.out.println("response:" + response );
            //System.out.println("response body:" + response.getBody() );
            return response.getBody().toString();
        }catch(Exception e){
            return "error: " +e.getMessage();
        }
    }

    public static void main(String[] args)  {
        DataRestTemplateCaller appl = new DataRestTemplateCaller();
        //appl.runGetAll();
        appl.runPost();
      }
}
