.. contents:: Contenuti
   :depth: 5
.. role:: red
.. role:: blue 
.. role:: remark 

.. _WebSocket: https://it.wikipedia.org/wiki/WebSocket
.. _Springio: https://start.spring.io/
 
.. _`WebSockets`:

======================================
Web sockets
======================================

.. code:: java 
 
    anaconda
    sphinx-quickstart

WebSocket_ è un protocollo che consente a due o più computer di comunicare tra loro 
contemporaneamente (full-duplex) su una singola connessione TCP.
È uno strato molto sottile su TCP che trasforma un flusso di byte in un flusso di messaggi 
(testo o binario).

Nella sua forma più semplice, 

:remark:`un WebSocket è solo un canale di comunicazione tra due applicazioni` 

e non deve essere necessariamente coinvolto un browser.

A differenza di HTTP, che è un protocollo a livello di applicazione, nel protocollo WebSocket 
semplicemente non ci sono abbastanza informazioni in un messaggio in arrivo affinché 
un framework o un container sappia come instradarlo o elaborarlo.

Per questo motivo il WebSocket RFC definisce l'uso di sottoprotocolli. 
Durante l'handshake, il client e il server possono utilizzare l'intestazione 
*Sec-WebSocket-Protocol* per :blue:`concordare un sottoprotocollo`, ovvero un protocollo 
a livello di applicazione superiore da utilizzare. 
L'uso di un sottoprotocollo non è richiesto, ma anche se non utilizzato, le applicazioni 
dovranno comunque scegliere un formato di messaggio che sia il client che il server 
possano comprendere. 


Tuttavia l'uso più comune di WebSocket è facilitare la comunicazione tra un un'applicazione
server e un'applicazione basata su browser.
Infatti, rispetto a HTTP RESTful ha il vantaggio di realizzare comunicazioni  a 
bidirezionali e in tempo reale. Ciò consente al server di inviare informazione al client 
in qualsiasi momento, anziché costringere il client al polling.

I WebSocket utilizzano le Socket nella loro implementazione basata su un protocollo standard
che definisce un *handshake* di connessione e un *frame* di messaggio.

------------------------------------------------------
WebSocket in SpringBoot: versione base
------------------------------------------------------

.. https://www.dariawan.com/tutorials/spring/spring-boot-websocket-basic-example/

Come primo semplice esempio di uso di WebSocket in Spring, creiamo una applicazione che consente
a un client di utilizzare un browser per inviare un messaggio o una immagine a un server 
che provvede a visualizzare il messaggio o l'immagine presso tutti i client collegati.

.. _SetupNoStomp:

+++++++++++++++++++++++++++++++++++++++++++++++
Setup
+++++++++++++++++++++++++++++++++++++++++++++++

#. Iniziamo creando una applicazione *SpringBoot* collegandoci a Springio_ e selezionando 
   come da figura:

   .. image:: ./_static/img/springioBase.PNG
     :align: center
     :width: 90+
#. Specifichiamo una nuova porta (il deafult è ``8080``) ponendo in *resources/application.properties*

    .. code:: Java

       server.port = 8070

#. Inseriamo un file ``index.html`` in **resources/static** per poter lanciare un'applicazione che 
   presenta un'area  di ouput per  la visualizzazione di messaggi e un'area di input per la loro 
   immissione

    .. code:: html

        <html>
        <head>
            <style>
                .messageAreaStyle {
                    text-align: left;
                    width: 50+;
                    padding: 1em;
                    border: 1px solid black;
                }
            </style>
            <title>wsdemoNoStomp client</title>
        </head>

        <body>
        <h1>Welcome</h1>
        <div id="messageArea"  class="messageAreaStyle"></div>

        <div class="input-fields">
            <p>Type a message and hit send:</p>
            <input id="inputmessage"/><button id="send">Send</button>
        </div>

        <script src="wsdemominimal.js"></script>
        </body>
        </html>

    La pagina iniziale si presenta come segue:

    .. image:: ./_static/img/pageMinimal.PNG
     :align: center
     :width: 50+ 
    

+++++++++++++++++++++++++++++++++++++++++++++++
wsminimal.js
+++++++++++++++++++++++++++++++++++++++++++++++

Lo script  ``wsminimal.js`` definisce funzioni che inviano al server il messaggio di input e che aggiungono
messaggi nella output area e funzioni per connettersi a una WebSocket.

+++++++++++++++++++++++++++++++++++++++++++++++++
Funzioni di input/output
+++++++++++++++++++++++++++++++++++++++++++++++++

.. code:: js

    const messageWindow   = document.getElementById("messageArea");
    const sendButton      = document.getElementById("send");
    const messageInput    = document.getElementById("inputmessage");

    sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
    }
    function sendMessage(message) {
        socket.send(message);
        addMessageToWindow("Sent Message: " + message);
    }
    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }

    var socket = connect();
 
+++++++++++++++++++++++++++++++++++++++++++++++++
Funzioni di connessione e ricezione messaggi
+++++++++++++++++++++++++++++++++++++++++++++++++

.. _connect:

.. code:: js

    function connect(){
        var socket;
        var host     = document.location.host;
        var pathname =  document.location.pathname;
        var addr     = "ws://" +host + pathname + "socket"  ;

        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             alert("WARNING: Connessione WebSocket già stabilita");
        }
        socket = new WebSocket(addr); //CONNESSIONE

        socket.onopen = function (event) {
            addMessageToWindow("Connected");
        };
        socket.onmessage = function (event) {
            addMessageToWindow(`Got Message: ${event.data}`);
        };
        return socket;
    }//connect



+++++++++++++++++++++++++++++++++++++++++++++++
Configurazione
+++++++++++++++++++++++++++++++++++++++++++++++

Affinché l'applicazione Spring inoltri le richieste di un client al server (l'endpoint), 
è necessario registrare un gestore utilizzando una classe di configurazione 
che implementa l'interfaccia ``WebSocketConfigurer``.

.. code:: java

    @Configuration
    @EnableWebSocket
    public class WebSocketConfiguration implements WebSocketConfigurer {
        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            registry.addHandler(new WebSocketHandler(), "/socket").setAllowedOrigins("*");
        }
    }

L'annotazione ``@EnableWebSocket`` (da aggiungere a una classe di configurazione ``@Configuration`` )  
abilita l'uso delle plain WebSocket. 

In base alla configurazione, il server risponderà a richieste inviate al seguente indirizzo:

.. code:: java

    ws://<serverIP>:8070/socket

+++++++++++++++++++++++++++++++++++++++++++++++
Handler
+++++++++++++++++++++++++++++++++++++++++++++++

La classe  ``WebSocketHandler`` definisce un gestore custom di messaggi come specializzazione della classe astratta
``AbstractWebSocketHandler`` (o delle sue sottoclassi ``TextWebSocketHandler`` o ``BinaryWebSocketHandler``).    

Nel nostro caso, la gestione reinvia sulla WebSocket il messaggio ricevuto .
Questa azione del server porrà in esecuzione sul client  l'operazione ``socket.onmessage`` (si veda) `connect`_) che visualizza 
il messaggio nell'area di output.

.. code:: java

    public class WebSocketHandler extends AbstractWebSocketHandler {
        ...
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
            System.out.println("New Text Message Received");
            session.sendMessage(message);
        }
        @Override
        protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
            System.out.println("New Binary Message Received");
            session.sendMessage(message);
        }
    }

+++++++++++++++++++++++++++++++++++++++++++++++
Propagazione a tutti i client
+++++++++++++++++++++++++++++++++++++++++++++++

Per propagare un messaggio a tutti i client connessi attraverso la WebSocket, basata tenere traccia
delle sessioni e 

.. code:: java

    public class WebSocketHandler extends AbstractWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Added the session:" + session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Removed the session:" + session);
        super.afterConnectionClosed(session, status);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("New Text Message Received");
        sendToAll(message);
    }
    protected void sendToAll(TextMessage message) throws IOException{
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }

    }

Notiamo che l'applicazione funziona anche in assenza di un controller, in quanto Spring utilizza di deafult il file
**resources/static/index.html**.


+++++++++++++++++++++++++++++++++++++++++++++++
Un client in Java
+++++++++++++++++++++++++++++++++++++++++++++++

E' un esempio di machine-to-machine interaction.

La classe ``WebsocketClientEndpoint`` riproduce in Java la stessa struttura del client già
vista in JavaScript; in più possiamo ora salvare su file l'informnazione ricevuta (in particolare immagini
di tipo ``jpg``).

L'annotazione ``@ClientEndpoint`` (che corrisponde alla interfaccia ``javax.websocket.ClientEndpoint``)
denota che un POJO è un web socket client. Come tale questo POJO può definire i metodi delle web socket lifecycle
usando le *web socket method level annotations*.

.. code:: java

    @ClientEndpoint
    public class WebsocketClientEndpoint {

    Session userSession = null;
    private IMessageHandler messageHandler;

    public WebsocketClientEndpoint(URI endpointURI) {
     try {
        WebSocketContainer container=
                ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, endpointURI);
     } catch (Exception e) { throw new RuntimeException(e); }
    }

    /**
     * Callback hook for Connection open events.
     * @param userSession the userSession which is opened.
    */
    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
    */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. 
     * This method will be invoked when a client send a message.
    */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    @OnMessage
    public void onMessage(ByteBuffer bytes) {
     try{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes.array());
        //Dai bytes alla immagine e salvataggio in un file
        BufferedImage bImage2    = ImageIO.read(bis);
        ImageIO.write(bImage2, "jpg", new File("outputimage.jpg") );
     }catch( Exception e){ throw new RuntimeException(e); }

    }
    /**
     * register message handler
      * @param msgHandler
    */
    public void addMessageHandler(IMessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }
    /**
     * Send a message.
     * @param message
    */
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
    }



+++++++++++++++++++++++++++++++++++++++++++++++
Introduzione di un Controller
+++++++++++++++++++++++++++++++++++++++++++++++


.. code:: java

    package it.unibo.wsdemoNoSTOMP;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    public class WebSocketController {
        @RequestMapping("/")
        public String textOnly() {
            return "indexNoImages";
        }

        @RequestMapping("/alsoimages")
        public String alsoImages() {
            return "indexAlsoImages";
        }
    }


+++++++++++++++++++++++++++++++++++++++++++++++
Gestione di immagini
+++++++++++++++++++++++++++++++++++++++++++++++

Lo script  ``wsalsoimages.js`` usato da ``indexAlsoImages.html`` definisce funzioni per la gestione delle immagini simili

.. code:: java

    sendImageButton.onclick = function (event) { //event is a PointerEvent
        let file = fileInput.files[0];  //file: object File
        sendMessage(file);
        fileInput.value = null;
    };

    socket.onmessage = function (event) {
        if (event.data instanceof ArrayBuffer) {
            addMessageToWindow('Got Image:');
            addImageToWindow(event.data);
        } else {
            addMessageToWindow(`Got Message: ${event.data}`);
        }
    };

    function addImageToWindow(image) {
        let url = URL.createObjectURL(new Blob([image]));
        imageWindow.innerHTML += `<img src="${url}"/>`
    }


------------------------------------------------------
WebSocket in SpringBoot: versione STOMP
------------------------------------------------------
:blue:`Simple Text Oriented Message Protocol`
(STOMP) è un protocollo di messaggistica text-based progettato per operare con MOM 
(Message Orinented Middleware) ed originariamente creato per l'uso 
in linguaggi di scripting con frame ispirati a HTTP. 

STOMP può essere utilizzato anche senza WebSocket, ad esempio tramite una connessione 
Telnet, HTTP o un  message broker. Tuttavia,
STOMP è ampiamente supportato e adatto per l'uso su WebSocket e sul web.

STOMP è progettato per interagire con un :blue:`broker di messaggi` realizzato in memoria (lato server);
dunque, rispetto all'uso delle WebSocket, rende più semplice inviare messaggi solo 
a un particolare utente o ad utenti che sono iscritti a un particolare argomento. 

++++++++++++++++++++++++++++++++++++++++++++++++
Setup e Dipendenze
++++++++++++++++++++++++++++++++++++++++++++++++

Partendo dal SetUp precedente `SetupNoStomp`_, aggiungiamo alcune dipendenze nel file ``build.gradle``:

.. code::

    dependencies {
    //Dipendenze generate dal Setup
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
    
    //Nuove dipendenze
    implementation 'org.webjars:webjars-locator-core'
	implementation 'org.webjars:sockjs-client:1.5.1'
	implementation 'org.webjars:stomp-websocket:2.3.4'
    implementation 'org.webjars:bootstrap:5.1.3'
    implementation 'org.webjars:jquery:3.6.0'

I :blue:`WebJar` sono dipendenze lato client impacchettate in file JAR e non sono legate a Spring.
Per approfondire, si veda: https://www.baeldung.com/maven-webjars e https://mvnrepository.com/artifact/org.webjars. 

++++++++++++++++++++++++++++++++++++++++++++++++ 
La funzione del servizio
++++++++++++++++++++++++++++++++++++++++++++++++
Il servizio accetta messaggi in formato JSON

++++++++++++++++++++++++++++++++++++++++++++++++ 
Componenti
++++++++++++++++++++++++++++++++++++++++++++++++

I componenti-base della applicazione in versione STOMP sono

++++++++++++++++++++++++++++++++++++++++++++++++
Configurazione Spring
++++++++++++++++++++++++++++++++++++++++++++++++

++++++++++++++++++++++++++++++++++++++++++++++++
Client (in javascript per browser)
++++++++++++++++++++++++++++++++++++++++++++++++

++++++++++++++++++++++++++++++++++++++++++++++++
Client (in Java per programmi)
++++++++++++++++++++++++++++++++++++++++++++++++



https://spring.io/guides/gs/messaging-stomp-websocket/

- Possibile premessa https://www.baeldung.com/intro-to-project-lombok
- 
- 
  .. code:: Java


- Create a Resource Representation Class. HelloMessage. Spring will use the Jackson JSON library to automatically marshal instances of type Greeting into JSON.
-  model the greeting representation, Greeting
- Create a Message-handling Controller. GreetingController
  
  .. code:: Java

  	@MessageMapping("/hello")    
      un msg inviato a /hello induce l'esecuzione del metodo con input un oggetto di tipo HelloMessage
      ricavato dal payload del emssaggio
    @SendTo("/topic/greetings")
      induce a inviare la risposta del metodo a tutti i sottoscrittori di /topic/greetings

- Configure Spring for STOMP messaging. WebSocketConfig
- 
  .. code:: Java

    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


- Create a Browser Client . index.html
- ./gradlew bootRun
- java -jar build/libs/gs-messaging-stomp-websocket-0.1.0.jar


https://www.baeldung.com/websockets-spring

https://www.dariawan.com/series/build-spring-websocket-application/

https://www.dariawan.com/tutorials/spring/spring-boot-websocket-basic-example/

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Esempio più articolato
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

https://www.dariawan.com/tutorials/spring/build-chat-application-using-spring-boot-and-websocket/

 