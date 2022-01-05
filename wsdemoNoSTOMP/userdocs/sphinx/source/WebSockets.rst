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

Come primo semplice esempio di uso di WebSocket in Spring, creiamo una applicazione che consente
a un client di utilizzare un browser per inviare un messaggio o una immagine a un server 
che provvede a visualizzare il messaggio o l'immagine presso tutti i client collegati.

+++++++++++++++++++++++++++++++++++++++++++++++
Setup
+++++++++++++++++++++++++++++++++++++++++++++++

#. Iniziamo creando una applicazione *SpringBoot* collegandoci a Springio_ e selezionando 
   come da figura:

   .. image:: ./_static/img/springioBase.PNG
     :align: center
     :width: 90%
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
                    width: 50%;
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
     :width: 50% 
    

+++++++++++++++++++++++++++++++++++++++++++++++
wsdemominimal.js
+++++++++++++++++++++++++++++++++++++++++++++++

Lo script  ``wsdemominimal.js`` definisce funzioni che inviano al server il messaggio di input e che aggiungono
messaggi nella output area e funzioni per connettersi a una WebSocket.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Funzioni di input/output
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

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
 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Funzioni di connessione e ricezione messaggi
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

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

+++++++++++++++++++++++++++++++++++++++++++++++
Introduzione di un Controller
+++++++++++++++++++++++++++++++++++++++++++++++


package it.unibo.wsdemoNoSTOMP;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSocketController {

    @RequestMapping("/")
    public String starting() {
        return "indexAlsoImages";//"wsbroadcast";
    }
/*
    @RequestMapping("/socket")
    public String getWebSocket() {
        return "indexAlsoImages";//"wsbroadcast";
    }

 */
}


+++++++++++++++++++++++++++++++++++++++++++++++
Gestione di immagini
+++++++++++++++++++++++++++++++++++++++++++++++

Lo script  ``wsdemominimal.js`` definisce funzioni

.. code:: java



------------------------------------------------------
WebSocket in SpringBoot: versione STOMP
------------------------------------------------------

STOMP è un semplice protocollo di messaggistica originariamente creato per l'uso 
in linguaggi di scripting con frame ispirati a HTTP. 
STOMP è ampiamente supportato e adatto per l'uso su WebSocket e sul web.

STOMP può essere utilizzato anche senza WebSocket, ad esempio tramite una connessione 
Telnet, HTTP o un servizio di message broker.

STOMP è progettato per interagire con un :blue:`broker di messaggi` realizzato in memoria (lato server);
dunque, rispetto all'uso delle WebSocket, rende più semplice inviare messaggi solo 
a un particolare utente o ad utenti che sono iscritti a un particolare argomento. 




https://www.baeldung.com/websockets-spring

https://www.dariawan.com/series/build-spring-websocket-application/

https://www.dariawan.com/tutorials/spring/build-chat-application-using-spring-boot-and-websocket/