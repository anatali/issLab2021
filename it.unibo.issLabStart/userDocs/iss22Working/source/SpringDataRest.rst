.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

=======================================
SpringDataRest
=======================================
#. Creazione di un progetto SpringBoot :ref:`Progetto SpringDataRest - iniziale` con 
   interazioni *HumanMachine* (:blue:`hm`) e *MachineToMachine* (:blue:`m2m`)
#. Testing con RestTemplate ()
#. Swagger
#. Creazione di un database usando H2 : :ref:`Progetto SpringDataRest - database`
#. Testing con :ref:`MockMvc`  
#. Progetto SpringDataRest - servizi (e controller)
#. SpringDataRest - HAL browser
 


- :blue:`HATEOAS` sta per *Hypermedia as the Engine of Application State*.
- :blue:`HAL` (*Hypertext Application Language*)  fornisce un formato coerente  per il collegamento 
  ipertestuale tra le risorse.

.. Buone spiegazioni in https://spring.io/guides/gs/accessing-data-rest/ Accessing JPA Data with REST

-------------------------------------
Progetto SpringDataRest - iniziale
-------------------------------------
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


+++++++++++++++++++++++++++
Person
+++++++++++++++++++++++++++

.. code:: Java

    @Data  //Lombok
    public class Person {
      private long id;
      private String firstName;
      public String toString(){
        return "person id="+id+" firstName="+firstName + " lastName="+lastName;
    }        
    }

+++++++++++++++++++++++++++
DataHandler
+++++++++++++++++++++++++++

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
  }

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
SpringDataRest - Swagger
++++++++++++++++++++++++++++++


Aggiungiamo in :ref:`SpringDataRest - build.gradle iniziale` la dipendenza alla libreria springdoc-openapi 
che sostituisce la libreria SpringFox, non più mantenuta. Questa libreria
esamina a runtime  l'applicazione, per inferirne la API semantics basata sulla configurazione Spring,
sulla struttura delle classi e sulle annotwzioni.

.. code:: 

	implementation 'org.springdoc:springdoc-openapi-ui:1.6.11'

.. code:: 

	spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER  

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



 