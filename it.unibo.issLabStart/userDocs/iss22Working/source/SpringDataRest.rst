.. role:: red 
.. role:: blue 
.. role:: brown 
.. role:: remark
.. role:: worktodo

=======================================
SpringDataRest
=======================================

-------------------------------------
SpringDataRest - requisiti
-------------------------------------

Costruire una applicazione (un sistema software) che permette la gestione CRUD di informazioni
relative a persone.
L'applicazione deve essere resa disponibile in rete attraverso una API Restful.


+++++++++++++++++++++++++++++++++++
SpringDataRest: user story
+++++++++++++++++++++++++++++++++++

Come utente del servizio mi aspetto che,  ...

---------------------------------
SpringDataRest: analisi
---------------------------------

Dopo interazione con il committente,  le informazioni relative a persone possono essere
formalmente espresse dalla seguente classe Java:

+++++++++++++++++++++++++++++
Person
+++++++++++++++++++++++++++++

.. code:: Java

 
    public class Person {
      private long id;
      private String firstName;
      public String toString(){
        return "person id="+id+" firstName="+firstName + " lastName="+lastName;
    }  

I metodi get/set delle proprietà sono omessi perchè qui non essenziali.


++++++++++++++++++++++++++++
Gestore CRUD
++++++++++++++++++++++++++++

Il gestore logico dei dati richiesto dal committente è molto semplice: 
realizza un elenco di :ref:`Person` e 
offre operazioni per aggiungere/eliminare elementi dall'elenco e cercare/ottenere elementi.

- parte CREATE: *addPerson*
- parte READ: *getFirst, getLast, getAllPersons, getPersonWithLastName*
- parte UPDATE: todo
- parte DELETE: *removePerson*

Il gestore implicato dai requisiti può essere formalizzato dalla seguente classe Java:

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
DataHandler
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

  public class DataHandler {
      private static Vector<Person> userDataList = new Vector<Person>();
      private static int id = 0;

      private static void addNewPerson(){
          Person userData = new Person();
          userData.setId(id);
          userData.setFirstName("dummy");
          userData.setLastName("dummy");
           userDataList.add(userData);
          id++;
      }
      public static void addPerson(Person userData){
          userDataList.add(userData);
      }
      public static List<Person> getAllPersons(){
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
          String pFound = "person not found";
          //Scandisce userDataList cercando la prima persona con userDataList
          ...
          return pFound;
      }
      public static void removePerson(Person p){
        userDataList.remove(p);
      }
  }

+++++++++++++++++++++++++++++++
SpringDataRest: API Restful
+++++++++++++++++++++++++++++++
Al momento questo requisito suggerisce l'uso di un framework specializzato come SprinBoot.

- :blue:`HATEOAS` sta per *Hypermedia as the Engine of Application State*.
- :blue:`HAL` (*Hypertext Application Language*)  fornisce un formato coerente  per il collegamento 
  ipertestuale tra le risorse.

.. Buone spiegazioni in https://spring.io/guides/gs/accessing-data-rest/ Accessing JPA Data with REST

+++++++++++++++++++++++++++++++++
SpringDataRest: piano di lavoro
+++++++++++++++++++++++++++++++++

#. Creazione di un progetto SpringBoot  con 
   interazioni *HumanMachine* (:blue:`hm`) e *MachineToMachine* (:blue:`m2m`)
#. Testing con RestTemplate ()
#. Swagger


.. Creazione di un database usando H2 :  `Progetto SpringDataRest - database`
.. Testing con :ref:`MockMvc`  
.. Progetto SpringDataRest - servizi (e controller)
.. SpringDataRest - HAL browser





--------------------------------------------
SpringDataRest: impostazione del workspace
--------------------------------------------

Il codice completo del progetto si trova in **progetto  SpringDataRest**.

+++++++++++++++++++++++++++++++++++++++++++
SpringDataRest - build.gradle iniziale
+++++++++++++++++++++++++++++++++++++++++++

Il progetto inizia con le seguenti dipendenze nel file *build.gradle*:

.. code:: 

  dependencies {
   implementation 'org.springframework.boot:spring-boot-starter-data-rest'
   implementation 'org.springframework.boot:spring-boot-starter-web'
   testImplementation 'org.springframework.boot:spring-boot-starter-test'
  //Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
  //For java HTTP caller
	  implementation 'com.squareup.okhttp:okhttp:2.7.5'
  //Human-machine interface
    implementation "org.springframework.boot:spring-boot-starter-thymeleaf"
  }

++++++++++++++++++++++++++++++++++++++++++++++
Progetto SpringDataRest - business logic
++++++++++++++++++++++++++++++++++++++++++++++
Il codice che definisce la business logic della applicazione è organizzato su due classi:

- La classe :ref:`Person` che rappresenta i dati
- La classe :ref:`DataHandler` che rappresenta il gestore logico dei dati
 
 


+++++++++++++++++++++++++++
PersonGuiNaive
+++++++++++++++++++++++++++

.. code:: html

  <!DOCTYPE html>
  <html xmlns="http://www.w3.org/1999/xhtml"
        xmlns:th="http://www.thymeleaf.org">
  <head>
      <title>PersonGuiNaive</title>
      <style> ...  </style>
  </head>
  <body>
  <h1>UserData Gui</h1>

  <h2>Last Person inserted</h2>
  <div  ID="LASTPERSON">
    id=<span th:text="${lastperson.id}">Replaceable text</span >
    firstName=<span th:text="${lastperson.firstName}">Replace text</span >
    lastName=<span th:text="${lastperson.lastName}">Replace text</span >
  </div>

  <h2>Insert a new Person</h2>
  <form method="POST" action="/Api/createPerson"  th:object="${personmodel}">
      <label for="id">ID : </label>
      <input type="text" th:field="*{id}"><br/>

      <label for="firstName">FIRSTNAME : </label>
      <input type="text" th:field="*{firstName}"><br/>

      <label for="lastName">LASTNAME : </label>
      <input type="text" th:field="*{lastName}">
      <input type="submit" value="submit">
  </form>

  <h3>Answer to Api/getAPerson?lastName=... </h3>
  <div id="FOUND" th:text="${personfound}"> Replaceable text </div>

  <h3>Answer to Api/getAllPersons</h3>
  <table id="ALLPERSONS">
      <tr th:each="person: ${persons}">
          <td th:text="${person.id}" />
          <td th:text="${person.firstName}" />
          <td th:text="${person.lastName}" />
      </tr>
  </table>
  </body>
  </html> 


+++++++++++++++++++++++++++++++++++++++++++
SpringDataRest - HIController 
+++++++++++++++++++++++++++++++++++++++++++

Il Controller Spring *HIController* realizza il comportamento di un controllore Human-machine  
che restituisce una pagina HTML elaborata da TheamLeaf.
Il path inizia sempre con :blue:`Api`.

.. code:: Java

  @Controller
  @RequestMapping("/Api")
  public class HIController { ...

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
HIController API
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
.. code:: Java

      private void updateTheModel(Model model, Person lastPerson, String foundPerson){
          model.addAttribute("personmodel", new Person());
          model.addAttribute("lastperson",  lastPerson);
          model.addAttribute("personfound", foundPerson );
      }

      @GetMapping
      public String get(Model model){
          updateTheModel(model, DataHandler.getLast(), "todo");
          return "PersonGuiNaive"; //Rendered by TheamLeaf
      }
      @GetMapping("/getAPerson") //getAPerson?lastName=Foscolo
      public String getAPerson(
            Model model, @RequestParam( "lastName" ) String lastName){
          String ps = DataHandler.getPersonWithLastName(lastName);
          updateTheModel(model, DataHandler.getLast(), ps);
          return "PersonGuiNaive";
      }
      @GetMapping("/getAllPersons")
       public String getAllPersons( Model model ){
       List<Person> lp = DataHandler.getAllPersons( );
        updateTheModel(model, DataHandler.getLast(), "todo");
        model.addAttribute("persons", lp ); //Further info in page
        return "PersonGuiNaive";
    }
      @PostMapping("/createPerson")
      public String post(
        @ModelAttribute("personmodel") Person userData, Model model) {
          DataHandler.addPerson(userData);
          updateTheModel(model, DataHandler.getLast(), "todo");
          return "PersonGuiNaive";
      }
  }

:remark:`La interazione con HIController riceve come risposta una String (la pagina HTML)` 
  
+++++++++++++++++++++++++++++
SpringDataRest - esecuzione
+++++++++++++++++++++++++++++

Eseguiamo l'applicazione con il comando:

.. code::

    gradlew bootrun

+++++++++++++++++++++++++++++++++++++++++++++
SpringDataRest: accesso a HI con browser
+++++++++++++++++++++++++++++++++++++++++++++


.. list-table:: 
  :width: 100%

  * - :blue:`http://localhost:8080/` 
  * - Restituisce dati JSON relativi al top level service.
  
      La risposta utilizza il formato HAL per l'output JSON e 
      indica che il server offre un  collegamento situato a http://localhost:8080/

      .. code::

        {
          "_links": {
            "profile": {
              "href": "http://localhost:8080/profile"
            }
          }
        }


.. list-table:: 
  :width: 100%

  * - :blue:`http://localhost:8080/Api` 
  * - Restituisce la pagina generata da  :ref:`PersonGuiNaive` mediante Thymeleaf.
  
      .. image:: ./_static/img/SpringDataRest/SpringDataRestGuiInit.png 
         :align: center
         :width: 40%

+++++++++++++++++++++++++++++++++++++++++++++
SpringDataRest: accesso a HI con curl
+++++++++++++++++++++++++++++++++++++++++++++
.. list-table:: 
  :width: 90%

  * - Creare dati (POST)
  * -   
      .. code::

        curl -d "id=1&firstName=Alessando&lastName=Manzoni" -H 
             "Content-Type: application/x-www-form-urlencoded" 
             -X POST http://localhost:8080/Api/createPerson
  * - Cercare un dato (GET)
  * -   
      .. code::

        curl http://localhost:8080/Api/getAPerson?lastName=Manzoni
  * - Cercare tutti dati (GET)
  * -   
      .. code::

        curl http://localhost:8080/Api/getAllPersons 

+++++++++++++++++++++++++++++++++++++++++++++
SpringDataRest: accesso a HI con Java
+++++++++++++++++++++++++++++++++++++++++++++

Nella classe *unibo.SpringDataRest.callers.DataHttpCaller* del progetto *SpringDataRest* 
eseguiamo chiamate HTTP usando
la libreria *com.squareup.okhttp.OkHttpClient* (si veda https://www.baeldung.com/guide-to-okhttp).

.. code:: Java

  public class DataHIHttpCaller {
    final private OkHttpClient client = new OkHttpClient();
    final private String BASE_URL     = "http://localhost:8080/Api";
 
    public void runGet(String lastName){
        String response =  
           doGet(BASE_URL +"/getAPerson?lastName="+lastName);
        //System.out.println(response);   //Visualizza la pagina: prolisso
        //Visualizzimamo l'elemento della pagina che contiene la risposta
        PageUtil.readTheHtmlPage(response,"FOUND");  
    }
    public void runGetAll( ){
        String response =  doGet(BASE_URL +"/getAllPersons");
        //System.out.println(response);   //Visualizza la pagina: prolisso
        //Visualizzimamo l'elemento della pagina che contiene la risposta
        PageUtil.readTheHtmlPage(response,"ALLPERSONS"); 
    }
    public void runCreate(String id,String firstName,String lastName){
      String personData = "id=ID&firstName=FN&lastName=LN"
          .replace("ID",id).replace("FN",firstName).replace("LN",lastName);
      RequestBody body   = RequestBody.create(
        MediaType.parse("application/x-www-form-urlencoded"), personData);
      int respCode = doPost(BASE_URL + "/createPerson", body);
      if( respCode == 200 ) System.out.println("runCreate ok" );
      else System.out.println("WARNING: runCreate problem:" + respCode);
    }

    //get, post in Java ...
     
      public static void main(String[] args)  {
        //IPOTESI: applicazione attivata
        DataHttpCaller appl = new DataHttpCaller();
          appl.runGetAll();        
          appl.runGet("Foscolo");  //person not found
          appl.runCreate("2","Alessandro","Foscolo");           
          appl.runGet("Foscolo");
      }
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
get, post in Java con OkHttpClient
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

     private String doGet(String url)  {
      Request request = new Request.Builder()
          .url(url)
          .build();
      try{
        Response response = client.newCall(request).execute();
        return response.body().string();
      }catch(Exception e){...}
    }
    private int doPost(String urlStr, RequestBody body)  {
        try{
            Request request = new Request.Builder()
                .url(urlStr)
                .post(body)
                .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            return( response.code()   )  ;
        }catch(Exception e){ return 0; }
    }    

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
PageUtil.readTheHtmlPage
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Utilizza la classe *javax.swing.text.html.HTMLEditorKit* per ricavare  
dalla String che rappresenta una pagina HTML 
le informazioni relative all'elemento HTML con 'id=elementID', che poi visualizza
su *System.out*.

.. code:: Java

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
            String s   = foundField.getDocument().getText(start,length);
            System.out.println( s );
        } catch( Exception e){
             e.printStackTrace();
        }
    }

+++++++++++++++++++++++++++++++++++++++++++++++
RestTemplate
+++++++++++++++++++++++++++++++++++++++++++++++
La classe RestTemplate (https://www.baeldung.com/rest-template) costituisce un client sincrono, 
progettata per chiamare i servizi REST. 
I suoi metodi primari, qui di seguito elencati, 
sono strettamente legati ai metodi del protocollo HTTP HEAD , GET , POST , PUT , DELETE e OPTIONS.

- :blue:`getForEntity()`: executes a GET request and returns an object of ResponseEntity class 
  that contains both the status code  and the resource as an object.
- :blue:`getForObject()` : similar to getForEntity(), but returns the resource directly.
- :blue:`exchange()`: executes a specified HTTP method, such as GET, POST, PUT, etc, and returns a ResponseEntity 
  containing both the HTTP status code and the resource as an object.
- :blue:`execute()` : similar to the exchange() method, but takes additional parameters: 
  RequestCallback and ResultSetExtractor.
- :blue:`headForHeaders()`: executes a HEAD request and returns all HTTP headers for the specified URL.
- :blue:`optionsForAllow()`: executes an OPTIONS request and uses the Allow header to return the HTTP methods 
  that are allowed under the specified URL.
- :blue:`delete()`: deletes the resources at the given URL using the HTTP DELETE method.
- :blue:`put()`: updates a resource for a given URL using the HTTP PUT method.

Per interagire con il server, occorre creare un'istanza di RestTemplate, eseguire la richiesta, 
interpretare la risposta, mappare la risposta agli oggetti di dominio e anche gestire le eccezioni. 

Le informazioni consegnate al cliente possono essere in diversi formati, 
come ad esempio JSON, XML, HTML, PHP, text, etc.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
RestTemplate vs. WebClient
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

RestTemplate sarà deprecato nelle versioni future di Spring a favore di 
WebClient (https://www.baeldung.com/spring-5-webclient) che fornisce un'API sincrona tradizionale, 
ma supporta anche un efficiente approccio reattivo, non bloccante e asincrono, 
che funziona tramite il protocollo HTTP/1.1.

+++++++++++++++++++++++++++++++++++++++++++++++
SpringDataRest: accesso a HI con RestTemplate
+++++++++++++++++++++++++++++++++++++++++++++++
RestTemplate può essere usato anche al posto di OkHttpClient per interagire con  
:ref:`SpringDataRest - HIcontroller`. Ad esempio (il codice che segue si trova 
in *unibo.SpringDataRest.callers.RestTemplateApiCaller* del *progetto  SpringDataRest*)
utilizza le seguenti classi per:

- *org.springframework.http.HttpEntity<String>*  (si veda: https://www.demo2s.com/java/spring-httpentity-httpentity-t-body.html)
- *org.springframework.http.ResponseEntity<String>* (si veda: https://www.demo2s.com/java/java-org-springframework-http-responseentity.html)

+++++++++++++++++++++++++++++++++++++++++++++
SpringDataRest: testing con RestTemplate
+++++++++++++++++++++++++++++++++++++++++++++

Il codice precedente può essere riusato all'interno di un 
Impostiamo una test JUnit che 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
RestTemplateApiUtil before/after
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Prima dei test lanciamo l'applicazione, che viene chiusa al termine degli stessi.

.. code:: Java

    public class HITestWithRestTemplate {

    @BeforeAll
    public static void start() throws Exception {
        SpringDataRestApplication.main( new String[]{});
        rtUtil = new RestTemplateApiUtil("http://localhost:8080/Api");
    }

    @AfterAll
    public static void end(){
        SpringDataRestApplication.closeAppl();
    }
    

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
BasicTestWithRestTemplate: i test
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Riportiamo un test di esempio che effettua i seguenti passi:

#. Verifica che la persona con *lastName="Foscolo"* non esiste (la lista dei dati è inizialmente composta
   da un persona con *lastName="dummy"* )
#. Crea la persona *Ugo Fosoolo*
#. Verifica che la persona con *lastName="Foscolo"* ora esiste
#. Elimina la persona *Ugo Fosoolo* appena creata in modo da lasicare la lista dei dati
   nell sua configurazione iniziale.

.. code:: Java

    private static RestTemplateApiUtil rtUtil;
    @Test
    public void testGetFoscoloAfterCreate(){
        System.out.println("=== testGetFoscoloAfterCreate"  );
        ckeckPerson("Foscolo","person not found" );
        //CREATE
        ResponseEntity<String> response =
                rtUtil.createPerson("1","Ugo","Foscolo");
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        //CHECK
        ckeckPerson("Foscolo","lastName=Foscolo" );
        //DELETE
        response = rtUtil.deletePerson("1","Ugo","Foscolo");
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        //CHECK
        ckeckPerson("Foscolo","person not found" );
    }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
ckeckPerson
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

L'operazione che verifica l'esistenza di una persona è così definita:

.. code:: Java

    private void ckeckPerson( String lastName, String expected){
        ResponseEntity<String> response =  rtUtil.getAPerson(lastName);
        String answer = PageUtil.readTheHtmlPage(response.getBody(), "FOUND"); 
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        assertTrue( answer.contains(expected));
    }

Per determinare i valori inclusi da :ref:`SpringDataRest - HIController`  nella pagina di risposta
abbiamo riusato :ref:`PageUtil.readTheHtmlPage`.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
RestTemplateApiUtil
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La classe *RestTemplateApiUtil* usata dalle procedure di testing trasforma una operazione 'di business'
in una chiamata al :ref:`SpringDataRest - HIController` che usa 
:ref:`SpringDataRest: accesso a HI con RestTemplate`.

.. code:: Java
  
  public class RestTemplateApiUtil {

     protected String BASE_URL ;
     public RestTemplateApiUtil(String BASE_URL){
         this.BASE_URL = BASE_URL;
     }
    public ResponseEntity<String> getLastPerson( ){
         return  doGet(BASE_URL +"/");
    }
     public ResponseEntity<String> getAPerson(String lastName){
          return  doGet(BASE_URL +"/getAPerson?lastName="+lastName);
    }
    public ResponseEntity<String> getAllPersons( ){
         return  doGet(BASE_URL +"/getAllPersons");
    }
    public ResponseEntity<String> createPerson(String id, String firstName, String lastName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String personData  = "id=ID&firstName=FN&lastName=LN".replace("ID",id)
                .replace("FN",firstName).replace("LN",lastName);
        HttpEntity<String> entity = new HttpEntity<String>(personData,headers);
        return doPost(BASE_URL +"/createPerson",entity);
     }
    public ResponseEntity<String> deletePerson(String id, String firstName, String lastName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String personData  = "id=ID&firstName=FN&lastName=LN".replace("ID",id)
                .replace("FN",firstName).replace("LN",lastName);
        HttpEntity<String> entity = new HttpEntity<String>(personData,headers);
        return doDelete(BASE_URL +"/deletePerson",entity);
    }
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
RestTemplateApiUtil: basic ops
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Le operazioni che  usano :ref:`SpringDataRest: accesso a HI con RestTemplate`
per realizzare le chiamate REST sono così definite:

.. code:: Java    

    protected ResponseEntity<String> doGet(String url)  {
        RestTemplate rt = new RestTemplate( );
        ResponseEntity<String> response = rt.getForEntity( url, String.class);
        return response;
    }

    protected ResponseEntity<String> doPost(String urlStr, HttpEntity<String> entity)  {
        RestTemplate rt = new RestTemplate( );
         ResponseEntity<String> response = rt
                .exchange(urlStr, HttpMethod.POST, entity, String.class);
        return response;
    }
    protected ResponseEntity<String> doDelete(String urlStr, HttpEntity<String> entity)  {
        RestTemplate rt = new RestTemplate( );
        ResponseEntity<String> response = rt
                .exchange(urlStr, HttpMethod.DELETE, entity, String.class);
        return response;
    }

++++++++++++++++++++++++++++++++++++++++++++++++++
SpringDataRest - M2MController
++++++++++++++++++++++++++++++++++++++++++++++++++

Il Controller Spring *M2MController* realizza il comportamento di un controllore Machine-to-machine 
che restituisce dati in formato JSON.
Il path inizia sempre con :blue:`/RestApi`. 

.. code:: Java

  @RestController
  @RequestMapping(path = "/RestApi", produces = "application/json")
  @CrossOrigin(origins = "*")

  public class M2MController {  ...

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
M2MController API
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

   @GetMapping("/getLastPerson")
    public Person getLastPerson() {
        return DataHandler.getLast();  //Restituice un oggetto Java di class Person
        //poichè produce "application/json" i dati sono convertiti in Json
        //Ad esempio:{"id":2,"firstName":"Alessando","lastName":"Manzoni"}
    }
    @GetMapping("/getAllPersons")
    public List<Person> getAllPersons() {
        return DataHandler.getAllPersons();
    }

    @PostMapping("/createPersonWithModel")
    public ResponseEntity<Person> createPersonWithModel(@RequestBody Person p) {
        HttpHeaders headers = new HttpHeaders();
        DataHandler.addPerson(p);
        return new ResponseEntity<Person>(p, headers, HttpStatus.CREATED);
    }

    @PostMapping("/createPerson")
    public String createPersonWithParams(@RequestParam( "id" ) String id,
                               @RequestParam( "firstName" ) String firstName,
                               @RequestParam( "lastName" ) String lastName, Model model) {
        Person p = new Person();
        p.setId(Long.valueOf(id));
        p.setFirstName(firstName);
        p.setLastName(lastName);
        DataHandler.addPerson(p);
        return "";
    }

  }

:remark:`La interazione con RestApiController riceve come risposta una String Json` 

+++++++++++++++++++++++++++++++++++++++++++++
DataOnly REST: testing con RestTemplate
+++++++++++++++++++++++++++++++++++++++++++++

Il test è simile a :ref:`SpringDataRest: testing con RestTemplate`, con òe seguenti modifiche:

.. code::

    public class M2MTestWithRestTemplate {
    private static RestTemplateApiUtil rtUtil;

    @BeforeAll
    public static void start() throws Exception {
        SpringDataRestApplication.main( new String[]{});
        rtUtil = new RestTemplateApiUtil("http://localhost:8080/RestApi");
    }

    private void ckeckPersonRestApi( String lastName, boolean expected){
        ResponseEntity<String> response =  rtUtil.getAPerson(lastName);
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        if( expected ) assertTrue( response.getBody() != null );
        else assertTrue( response.getBody() == null );
    }

Nel caso la persona esista, il valore di *response.getBody()* è una stringa JSon che rappresenta i 
dati della persona trovata; ad esempio:

.. code::

   {"id":1,"firstName":"Ugo","lastName":"Foscolo"}

++++++++++++++++++++++++++++++
SpringDataRest - descrizione
++++++++++++++++++++++++++++++

OpenAPI è una specifica, cioè una descrizione astratta che non è legata a un’implementazione 
particolare. Fino alla versione 2.0, questa specifica era chiamata Swagger ed è stata rinominata 
in seguito specifica OpenAPI. Tuttavia, i tool forniti dalla società di sviluppo originale, 
SmartBear, continuano a esistere con il nome di Swagger.


Aggiungiamo in :ref:`SpringDataRest - build.gradle iniziale` la dipendenza alla libreria springdoc-openapi 
che sostituisce la libreria SpringFox, non più mantenuta. Questa libreria
esamina a runtime  l'applicazione, per inferirne la API semantics basata sulla configurazione Spring,
sulla struttura delle classi e sulle annotwzioni.

.. code:: 

  //Aggiungere in build.gradle
	implementation 'org.springdoc:springdoc-openapi-ui:1.6.11'

  //Aggiungere in application.properties
	spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER  


  https://editor.swagger.io/#
  
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
swagger-ui/index.html
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code::

  http://localhost:8080/swagger-ui/index.html

Fornisce la gui che segue:

.. image:: ./_static/img/SpringDataRest/SpringDataRestSwaggerUi.png 
    :align: center
    :width: 80%

L'uso di questa GUI permette l'esecuzione delle operazioni disponibili, fornendo anche le chianate in curl.
Ad esempio:

.. code::

   curl -X 'GET' \
     'http://localhost:8080/RestApi/getAllPersons' \
     -H 'accept: application/json'
 
 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
v3/api-docs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code::

  http://localhost:8080/swagger-ui/index.html

Fornisce le informazioni sulle operazioni in Json.

 
.. Spring Fox 3.0.0 not supporting new PathPattern Based Path Matching Strategy for Spring MVC which is now the new default from spring-boot 2.6.0.

.. https://springdoc.org/#Introduction
.. https://www.youtube.com/watch?v=utRxyPfFlDw
.. SpringFox hasn't been updated for a year or so, so I would prefer remove it completely from a project  and replace it with maintained springdoc-openapi library.


---------------------------------------
SpringDataRest:  interfaces
---------------------------------------
A partire da *Spring Framewrk 5.0* è possibile descrivere API RESTful mediante interfacce Java.

Supponiamo ad esempio dei definire:

.. code::

  public interface PersonService {
    @GetMapping(value="/person/{personId}", produces ="application/json")
    Person getPerson(@PathVariable int personId, 
                     @RequestParam(required = false)String arg);
  }

Questa specifica include le seguenti informazioni:

 - :blue:`@GetMapping`: specifica che il metodo *getPerson* è mappato a una richiesta
   HTTP GET con URL-path= :brown:`/person/{personId}?arg=...` e che il formato della risposta è JSON.
 - :blue:`@PathVariable`: specifica che il path variabile name :brown:`{personId}` della GET
   è mappato all'argomento *personId* del metodo.

   Ad esempio, una HTTP *GET /person*:brown:`/3?arg` si traduce in una chiamata a *getPerson(3,"")*.
 - :blue:`@RequestParam`: specifica che i parametri di query devono essere mappati nell'argomento 
   *arg* del metodo.

   Ad esempio, una HTTP *GET /person/3*:brown:`?arg=abc` si traduce in una chiamata a *getPerson(3,"abc")*.
 - Il metodo *getPerson* restituisce un POJO di tipo Person, che costituisce il 
   :blue:`DTO` (*Data Transfer Object*) usato per trasferire i dati di risposta al caller.


+++++++++++++++++++++++++++++++++++++++++++
SpringDataRest: interface implementation
+++++++++++++++++++++++++++++++++++++++++++

.. code::

  @RestController
  public class M2MController implements PersonService {

    @Override
    public Person getPerson(int personId) {
        Person p = DataHandler.getPersonWithId( Long.valueOf(personId) );
        return p;
    }
    ...

Indicazione

.. code::

  curl http://localhost:8080/RestApi/person?personId=1
  curl http://localhost:8080/RestApi/person/1
  curl http://localhost:8080/RestApi/getLastPerson





---------------------------------------
Actuator - JConsole
---------------------------------------

.. code::

  implementation 'org.springframework.boot:spring-boot-starter-actuator:2.7.4'

Actuator viene attualmente fornito con la maggior parte degli endpoint disabilitati.
Per abilitarli:

.. code::

  management.endpoints.web.exposure.include=* 

Actuator espone i suoi endpoints come :blue:`MBean` che possono essere visualizzati e gestiti con strumenti
(tra cui la JConsole) conformi alla specifica JMX (Java Management Extensions).

.. code::

  localhost:8080/actuator
  //open the Links sections


.. image:: ./_static/img/SpringDataRest/SpringDataRestActuator.png 
    :align: center
    :width: 80%  
 
See https://www.oracle.com/technical-resources/articles/java/jconsole.html

L'interfaccia utente grafica di JConsole è uno strumento di monitoraggio 
conforme alla specifica JMX (Java Management Extensions).

JMX fornisce strumenti per la gestione e il monitoraggio di applicazioni, oggetti di sistema, 
dispositivi (come le stampanti) e reti orientate ai servizi. 
Tali risorse sono rappresentate da oggetti chiamati Managed Bean (o :blue:`MBean`).

JConsole utilizza l'ampia strumentazione della Java Virtual Machine (JVM) per fornire informazioni sulle prestazioni 
e sul consumo di risorse delle applicazioni in esecuzione sulla piattaforma Java.

L' eseguibile JConsole si trova in *JDK_HOME /bin* , dove JDK_HOME è la directory in cui è installato Java Development Kit (JDK). 

L'avvio di JConsole senza alcun argomento rileverà automaticamente tutte le applicazioni Java locali 
e visualizzerà una finestra di dialogo che consente di selezionare l'applicazione che si desidera monitorare.

L'utilizzo di JConsole per monitorare un'applicazione locale è utile per lo sviluppo e per la creazione di prototipi, 
ma non è consigliato per gli ambienti di produzione, poiché JConsole stessa consuma notevoli risorse di sistema. 
Si consiglia il monitoraggio remoto per isolare l'applicazione JConsole dalla piattaforma monitorata.

---------------------------------------
SpringDataRest: invio mail
---------------------------------------

+++++++++++++++++++++++++++++
Mail: requisito mail
+++++++++++++++++++++++++++++
Inviare una mail a un destinatario precisato dal committente, quando un elemento viene aggiunto ai dati.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
SpringDataRest-Mail: user story
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Come utente del servizio, mi aspetto che una mail venga inviata al destinatario specificato
in un file di configurazione dell'applicazione:

- se effettuo un `SpringDataRest: accesso a HI con curl` - Creare dati (POST)
- se eseguio un programma che invoca (con successo) l'operazione *createPerson* di 
  :ref:`SpringDataRest - M2MController`

+++++++++++++++++++++++++++++
SpringDataRest-Mail: analisi
+++++++++++++++++++++++++++++
Dal punto di vista tecnologico, il problema implica l'utilizzo di un mail server e di una libreria Java 
di supporto al mailing; ad esempio:

- mail server: :blue:`gmail` (*smtp.gmail.com*) oppure il testing tool :blue:`mailtrap` (*smtp.mailtrap.io*)  
- libreria Java: *javax.mail*

Dal punto di vista logico, si pone il seguente problema:

:remark:`Quale compoenente del sistema ha la responsabilità di iniviare la mail?`

#. I controller :ref:`SpringDataRest - M2MController` e :ref:`SpringDataRest - HIController`.
#. Il :ref:`DataHandler`.
#. Un nuovo componente che fa 'polling' su :ref:`DataHandler`, invocandone il metodo *getLast* 
   e invia una mail quando trova questa info cmodificata.
#. Un nuovo componente, che capisce quando il :ref:`DataHandler` ha terminato l'operazione *addPerson*.

Notiamo subito che:

- Affidare il compito ai controller significa non includere questa parte nel 'core' della business logic.
- Introdurre un ente attivo che fa polling significa sprecare risorse e non avere sicurezza che 
  venga inviata una mail ad ogni inserzione (si pensi al caso di una inserzione seguita da eliminazione
  e una frequenza di polling cui sfugge il cambiamento).


Il punto essenziale è che la business logic venga riorganizzata in modo che una mail vemga inviata
ogni volta che il :ref:`DataHandler` ha terminato l'operazione *addPerson*.
Questo comportamento  costituisce una estensione delle funzionalità del gestore logico dei dati,
che può essere tecnicamente ottenuta in diversi modi:

#. definire una classe specializzazione di :ref:`DataHandler`. Qui però va ricordato il principio di Occam.
#. modificare il codice *addPerson* di :ref:`DataHandler` in modo che realizzi l'invio della mail,
   avvaledosi di una utility di supporto
#. impostare il :ref:`DataHandler` come un **oggetto osservabile**, che aggiorna gli observer al termine
   dell'operazione  *addPerson*. Un observer che viene registrato presso il :ref:`DataHandler` 
   rappresenta il nuovo componente, che 'capisce' quando è il momento di inviare la mail.

Il terzo tipo di riorganizzazione architetturale sembra più complesso ma è sicuramente più flssibile che
non cablare nuovi possibili comportamenti 'marginali' nel codice del gestore.
Si pensi ad esempio all'aggiunta di nuovi requisiti come:

- registare in un file di log il completamento con successo di ogni operazione;
- emettere un suono di allarme quando un elemento viene eliminato;
- accendere un led quando il :ref:`DataHandler` è in esecuzione;
- utilizzare un protocollo *publish-subscribe* per inviare informazioni al mondo esterno  
  o per aprire nuovi canali di richiesta;
- ... 

Inoltre, in accordo al :blue:`principio di singola resposabilità`,
l'invio della  mail è incapsulato nel codice dell'observer, liberando il :ref:`DataHandler`
da questo ulteriore compito.

Per realizzare la nuova funzionalità conviene impostare un nuovo SPRINT.

++++++++++++++++++++++++++++++++++
SpringDataRest: sprint 1 (mail)
++++++++++++++++++++++++++++++++++

Aggiungiamo dunque in *build.gradle* la dipendenza a *javax.mail*:

  .. code::
  
     implementation 'com.sun.mail:javax.mail:1.5.5'

La logica per creare la sessione differisce in base al tipo di server SMTP; 
ad esempio, se il server SMTP non richiede alcuna autenticazione, possiamo 
creare l'oggetto Session con alcune semplici proprietà mentre se richiede 
l'autenticazione TLS o SSL, la logica da creare sarà diversa . 

Il protocollo :blue:`SSL` ed il suo successore :blue:`TLS` permettono una comunicazione sicura dal sorgente 
al destinatario (end-to-end) su reti TCP/IP offrendo autenticazione, integrità dei dati 
e cifratura operando al di sopra del livello di trasporto.


++++++++++++++++++++++++++++++++++
SpringDataRest-Mail: progetto
++++++++++++++++++++++++++++++++++


Introduciamo la classe *EmailService* come utility per l'invio dei messaggi di mail
capace di usare due diversi mail servers:

- :blue:`gmail` (*smtp.gmail.com*) per inviare messaggi in modo reale
- :blue:`mailtrap` (*smtp.mailtrap.io*) per scopi di testing

Il programma Java per inviare e-mail contiene i seguenti passaggi:

- Creazione di un oggetto *javax.mail.Session*
- Creando un oggetto *javax.mail.internet.MimeMessage*, dobbiamo impostare diverse proprietà in questo oggetto 
  come l'indirizzo e-mail del destinatario, l'oggetto dell'e-mail, l'e-mail di risposta, 
  il corpo dell'e-mail, gli allegati ecc.
- Utilizzo di *javax.mail.Transport* per inviare il messaggio di posta elettronica.



Il :blue:`principio di singola resposabilità`, induce a delegare l'invio della mail a un componente
specializzato, come ad esempio:

%%%%%%%%%%%%%%%%%%%%%%%%%%%
EmailService
%%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code:: Java

  public interface Mailer{
    public void sendMail( String msg, String destination ) throws Exception;
  }

  public class EmailService implements Mailer{
  private String userName="";
  private String userPswd="";
  private boolean useTrueMail = false;

    public EmailService() {
        readUserData(  );
    }

    protected void readUserData(  )   {
    //legge da file i valori userName e userPswd 
    }

    public void sendMail( String msg, String destination ) {
      try {
        sendMailUsingGoogle(msg,destination);
        sendMailUsingMailTrap(msg,destination);
      }catch(Exception e){... }
    }



Le operazioni *sendMailUsingGoogle*

.. code:: Java

    private void sendMailUsingGoogle(String msg, String destination){
      try {
        prop = new Properties();
       //prop.setProperty("mail.debug", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 25);  //587 porta TLS
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true"); //richiesto dal server gmail
        prop.put("mail.smtp.ssl.trust", "*"); //evita 'Could not convert socket to TLS'
        Session session = Session.getInstance(prop, new Authenticator() {
           @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            //Password set su Google Mail:  Password per le app
              return new PasswordAuthentication("...", "...");
          }
        });
        sendMail(session, msg, destination);
      } catch (Exception e) {...}
    }

    private void sendMail(Session session, String msg, String destination ) throws Exception{
        Message message = new MimeMessage(session);
        //message.setFrom(new InternetAddress("fittizio@gmail.com"));
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(destination)); //"agoognat@gmail.com"
        message.setSubject("System informazioni");
        message.setText(msg);
        Transport.send(message);  //javax.mail.Service
    }

Questo componente può essere invocato direttamente da :ref:`DataHandler`
oppure essere incapsulato in un observer.

Esso funge da *adapter* per la comunicazione dell'applicazione verso il mondo
esterno, in accordo con gli schemi della cleanArchitecture.

.. image:: ./_static/img/Architectures/cleanArch.jpg 
    :align: center
    :width: 60%
  
.. implementation 'com.github.tntim96:fakesmtp:2.0'   
.. https://mailtrap.io/register/signup
.. java -jar fakeSMTP.jar  -m   //save in memory

----------------------------------------------
SpringDataRest: refactoring a messaggi
----------------------------------------------

- La business logic può essere scorporata e resa disponibile in rete senza l'uso di Spring
- La business logic può essere vista come una risorsa, in ottica HATEOAS


----------------------------------
SpringBoot cli 
----------------------------------

.. https://www.digitalocean.com/community/tutorials/spring-boot-cli-setup-and-helloworld-example
.. https://github.com/PacktPublishing/Hands-On-Microservices-with-Spring-Boot-and-Spring-Cloud
.. code put in C:\Didattica\microservices


.. code::

  set PATH=C:\DidatticaTools\Spring\spring-2.7.5\bin;%PATH%

  /spring-boot-cli-2.7.5-bin.zip
  bin/spring --help
  set JAVA_HOME=C:\Program Files\Java\jdk-11.0.8
  bin/spring.bat

  spring init --force ^
  --boot-version=2.7.5 ^
  --build=gradle ^
  --java-version=1.8 ^
  --packaging=jar ^
  --name=product-service ^
  --package-name=it.unibo.core.core.product ^
  --groupId=it.unibo ^
  --dependencies=actuator,webflux ^
  --version=1.0.0 ^
  product-service

  //ESECUZIONE
  gradlew build 
  cd build/libs 
  java -jar product-service-1.0.0.jar 
  curl http://localhost:7001/product/3
  {"productId":3,"name":"name-3","weight":123,"serviceAddress":"natDell/192.168.1.132:7001"}

  curl http://localhost:8080/RestApi/person/1
  curl http://localhost:8080/RestApi/person/1?arg=abc   OK

  uuu