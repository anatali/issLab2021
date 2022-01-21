.. role:: red 
.. role:: blue 
.. role:: remark

.. _filters: https://spring.io/guides/topicals/spring-security-architecture#:~:text=basa%20su%20Servlet-,Filters,-%2C%20quindi%20%C3%A8%20utile

.. https://spring.io/guides/topicals/spring-security-architecture
.. https://www.marcobehler.com/guides/spring-security


=============================================

=============================================
Un'applicazione Web Spring è una servlet ( ``DispatcherServlet`` ), 
che reindirizza le richieste HTTP ai  ``@Controllers`` o ``@RestControllers``.
Per fare in modo che l'autenticazione e l'autorizzazione siano eseguite prima che una richiesta 
raggiunga i Controller, si pongono filtri davanti alla Servlet, 



Spring Security è un insieme di filtri servlet che aggiungono funzioni di autenticazione 
e autorizzazione alle applicazioni web.

Si integra bene anche con framework come Spring Web MVC (o Spring Boot ), 
nonché con standard come OAuth2 o SAML. 
Genera automaticamente pagine di login/logout e protegge da exploit comuni come CSRF.

---------------------------
Filtri
---------------------------
La specifica Java Servlet versione 2.3 introduce il nuovo tipo di componente denominato filtro.
Un filtro intercetta dinamicamente richieste e risposte per utilizzare e/o trasformare le informazioni 
in esse contenute e quindi è molto utili per:

- autenticare e autorizzare 
- incapsulare attività ricorrenti in unità riutilizzabili
- adattare i dati di risposta alle esigenze del cliente

Il filtro inoltre prevede il disaccoppiamento tra programmazione e  configurazione.
Di conseguenza, in molti casi, per modificare l'input o l'output di un'applicazione Web,
non è necessario ricompilare nulla: basta modificare un file di testo o utilizzare uno strumento 
per modificare la configurazione.

---------------------------
L'approccio di Spring
---------------------------

Spring Security nel livello Web si basa su Servlet filters_ e 
ha un'architettura progettata per separare 
l':blue:`autenticazione`  dall':blue:`autorizzazione`, fornendo strategie 
e punti di estensione per entrambi.





+++++++++++++++++++++++++++++++++++
Autenticazione
+++++++++++++++++++++++++++++++++++
.. image:: ./_static/img/Access/springAuthentication.png 
   :align: center
   :width: 70%


Una volta che l'autenticazione ha esito positivo, possiamo passare all'autorizzazione

+++++++++++++++++++++++++++++++++++
Autorizzazione
+++++++++++++++++++++++++++++++++++

Il check realtvo a **Cross site request forgery** (CSRF) :blue:`CSRF` è abilitato di defualt da Spring Security. 

.. image:: ./_static/img/Access/csrf.png 
   :align: center
   :width: 70%

.. mperva.com/learn/application-security/csrf-cross-site-request-forgery/#:~:text=Cross%20site%20request%20forgery%20(CSRF)%2C%20also%20known%20as%20XSRF,a%20user%20is%20logged%20in.


.. image:: ./_static/img/Access/filters.png 
   :align: center
   :width: 70%

Il client invia una richiesta all'applicazione e il contenitore decide quali filtri 
e quale servlet si applicano ad essa in base al percorso dell'URI della richiesta. 
Al massimo, un servlet può gestire una singola richiesta, ma i filtri formano 
una catena, quindi vengono ordinati. 

.. In effetti, un filtro può porre il veto al resto della catena se vuole gestire  la richiesta stessa. Un filtro può anche modificare la richiesta o  la risposta utilizzata nei filtri e nel servlet a valle.  


Spring Security è installato come un singolo Filter nella catena e 
il suo tipo concreto è ``FilterChainProxy``.

In un'applicazione Spring Boot, il filtro di sicurezza è ``@Bean in ApplicationContext``, 
ed è installato per impostazione predefinita in modo che venga applicato 
a ogni richiesta. 
Viene installato in una posizione definita da 
``SecurityProperties.DEFAULT_FILTER_ORDER``, 
che a sua volta è ancorata da FilterRegistrationBean.
``REQUEST_WRAPPER_FILTER_MAX_ORDER``(l'ordine massimo che un'applicazione 
Spring Boot si aspetta che i filtri abbiano se avvolgono la richiesta, 
modificandone il comportamento). 
C'è di più, però: dal punto di vista del container, 
Spring Security è un filtro unico, ma, al suo interno, 
ci sono filtri aggiuntivi, ognuno dei quali gioca un ruolo speciale. 


.. image:: ./_static/img/Access/security-filters.png 
   :align: center
   :width: 70%







Possono esserci più catene di filtri, tutte gestite da Spring Security nello 
stesso livello superiore FilterChainProxye tutte sconosciute al container. 
Il filtro Spring Security contiene un elenco di catene di filtri e 
invia una richiesta alla prima catena che corrisponde. 
L'immagine seguente mostra l'invio in base alla corrispondenza con 
il percorso della richiesta ( /foo/**corrisponde a prima /**). 
Questo è molto comune ma non è l'unico modo per abbinare una richiesta. 
La caratteristica più importante di questo processo di invio è che solo 
una catena gestisce una richiesta.

.. image:: ./_static/img/Access/security-filters-dispatch.png
   :align: center
   :width: 70%


-----------------------------------------
Hands on 
-----------------------------------------

.. code::

  implementation 'org.springframework.boot:spring-boot-starter-security'      //SECURITY


