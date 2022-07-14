.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

.. _templateToFill.html: ../../../../../it.unibo.issLabStart/userDocs/templateToFill.html
.. _domande.html: ../../../../../it.unibo.issLabStart/userDocs/domande.html

=========================================
TemaFinaleNote
=========================================

 

-----------------------------
Non functional requirements
-----------------------------

#. The ideal work team is composed of 3 persons. Teams of 1 or 2 persons (:blue:`NOT 4 or more`) are also allowed.
#. The team must present a workplan as the result of the requirement/problem analysis,
   including some significan TestPlan.
#. The team must present the sequence of SPRINT performed, with appropriate motivations.
#. Each SPRINT must be associated with its own 'chronicle' (see `templateToFill.html`_) that presents, in **concise way**,
   the key-points related to each phases of development.
   Hopefully, the team could also deploy the system using Docker.
#. Each team must publish and maintain a :blue:`GIT-repository` (referred in the `templateToFill.html`_)
   with the code and the related documents.
#. The team must present (in synthetic, schematic way) the :blue:`specific activity` of each team-component.

-----------------------------
Linee-guida
-----------------------------

 
- Il numero e le finalità degli SPRINT sono definiti dal Team di sviluppo dopo opportune interazioni con il
  committente.
- Il committente (e/o il product-owner) è disponibile ONLINE in linea di massima ogni :blue:`Giovedi dalle 16 alle 18`
  fino al :blue:`21 Luglio 2022`, ma è sempre contattabile on-demand via email.

Lo svolgimento del lavoro dovrebbe avvenire in diverse fasi:

#. :blue:`Fase di analisi`, che termina con la definizione di una architettura logica del sistema, di modelli eseguibili e
   alcuni, significativi piani di testing.
   E' raccomandato che i risultati di questa fase vengano presentati al committente (con opportuno
   appuntamento) prima della consegna finale del prodotto.
#. :blue:`Fase di progetto e realizzazione`, che termina con il deployment del prodotto.
#. :blue:`Fase di discussione` del lavoro svolto, che dovrà svolgersi IN PRESENZA in LAb2 (preferibilmente).

E' opportuno che ogni partecipante sia pronto a discutere anche sugli elaborati che ha prodotto durante il corso.


-----------------------------
FASI del colloquio
-----------------------------

#. Presentazione (collettiva di gruppo) di una :blue:`demo 'live'` del sistema
   di durata 10-15(max) minuti.
   L'ordine di presentazione dei gruppi verrà opportunamente stabilito dal docente.
   La demo deve mostrare la esecuzione di :blue:`almeno un Test(Plan)` automatizzato ritenuto significativo.
   Per applicazioni che NON usano robot reali NON sono ammessi video
#. Presentazione (collettiva di gruppo) del progetto del sistema e della sua relazione
   con la fase di analisi.
   In questa fase è :blue:`RICHIESTA la preparazione` di ``2-3 SLIDES`` di illustrazione delle architetture con figure e (se
   ritenuto utile) riferimenti al codice.
   Al termine di queste fasi, ogni componente il gruppo può raggiungere un punteggio
   massimo di ``27/30``.
#.  :blue:`Domande` (per esempi, si veda il file `domande.html`_) rivolte dal docente a singole persone,
    riguardo al prodotto, al progetto e alla analisi del problema /requisiti.
    Al termine di questa fase una singola persona può raggiungere un punteggio massimo di ``29/30``.
#.  :blue:`Altre domande` rivolte dal docente a singole persone.
    Al termine di questa fase, una singola persona può raggiungere un punteggio di ``30elode``.

---------------------------------------------
Aggiornamenti del software
---------------------------------------------

Ho pubblicato sul GIT del corso:

- il progetto *unibo.comm22* con varie implementazioni di *Interaction2021* con diversi protocolli.
- il progetto *webrobot22* (e il file *webRobot22.html* nelle dispense) che fornisce una webGUI per il basicrobot.
- il progetto *unibo.testqakexample* come un esempio di JUnit per il testing della richiesta *pickup* del tema finale, 
  basato su un modello qak e sul fatto che gli attori qak sono risorse CoAP.

---------------------------------------------
Precisazioni del commitente
---------------------------------------------

Riportiamo le risposte date dal committente ad alcune  domande:

- Sarebbe bene mandare via il truck appena possibile.
- Il *WasteSerice* potrebbe ricevere un nuova richiesta mentre sta ancora eseguendo la deposit action di quella precedente.
- Una volta pieni, i contenitori non verranno svuotati, se non riavviando l'applicazione.
- Come  *posizione del trolley*, sono valide anche  indicazioni solo qualitative, quali 'at indooor', 'at glassbox', etc. 
- Non è richiesto che il robot si muova in modo ottimizzato, ma non dovrebbe nemmeno girovagare troppo nella stanza.
- Il tempo di raccolta del materiale dal truck è sempre limitato e prevedibile, mentre il tempo necessario 
  per il deposito potrebbe  essere anche alquanto lungo (anche in relazione al punto preceente).
- Il sonar/led NON sono sul trolley, ma su un RaspberryPi a parte.
- Il Sonar rileva oggetti che saremo noi a porgli davanti 
- Il DDR fornito dal committente ha un sonar on-board che viene usato per rilevare ostacoli quando si muove.

---------------------------------------------
Note dopo le interazioni in rete
---------------------------------------------

#. Lo scopo del tema finale NON è (solo) risolvere il problema ma avere un caso di studio su cui ragionare sul 
   processo di costruzione del software.

#. Lo scopo della analisi dei requisiti è ridefinire il testo dato dal committente in modo 'preciso' (e formale, cioè
   per noi, comprensibile alla macchina). Per le varie entità menzionate nel testo, occorre dare risposta ad alcune
   precise domande, tra cui (altri tipi di domande sarebbe bene fossero individuate da voi):

     - cosa intende il committente per xxx (ad esempio DDR robot)?
     - il committenete fornisce indicazioni sul software da usare per xxx?
     - se si, è possibile fornire un modello di tale software (per capire bene cosa bisogna sapere per usarlo)?

   Occorre anche porre molta attenzione alle frasi scritte in linguaggio naturale e dare loro una interpretazione
   non ambigua. Ad esempio: 

       - per la frase *a DDR robot working as a transport trolley*, che relazione si pensa debba esistere tra 
         l'entità *trolley* e l'entità *DDR robot*?

#. Ogni fase (a partire dai requisiti) dovrebbe terminare con la specifica di un modello (anche non eseguibile) 
   che costituisce l'inizio della fase successiva.  
#. I file HTML  in *userdocs* non devono essere visti come  'documentazione', ma come una sorta di 'diario del capitano'
   che appunta (in modo sintetico,  in linguaggio naturale) i punti essenziali che hanno portato a quei modelli.
#. L'uso del linguaggio **qak** va motivato in relazione alle caratteristiche del (sotto)problema esaminato  e 
   introdotto con un link a un documento che spiega cosa esso sia. 
   Sarebbe bene che questo documento fosse scritto - una volta sola - 
   da chi ne propone l'uso (con links ai documenti 'ufficiali'), in modo da porre in evidenza il perchè 
   lo si propone, nel contesto del problema (è anche un modo per 'ripassare' quanto fatto nel corso).
#. Ogni modello dovrebbe essere accompagnato da almeno un TestPlan funzionale significativo.
#. Lo scopo della fase di analisi è definire una modello (eseguibile) della architettura logica e 
   dare elementi utili per la costruzione di un *piano di lavoro*.
#. Il primo SPRINT dovrebbe scaturire dal piano di lavoro e iniziare a partire dal modello dell'analisi 
   
#. Ogni SPRINT dovrebbe:

   - essere associato a un preciso obiettivo (SCRUM goal) 
   - approfondire l'analisi relativa al sottoproblema relativo al goal dello SPRINT 
   - estendere/precisare l'architettura logica e i TestPlan
   - definire una architettura di progetto e Test relativi 
   - terminare con un prototipo eseguibile (da discutere con il committente)  e una proposta di nuovo SPRINT 
     (che potrebbe anche consistere in una revisione dell'analisi, se si vede che è stata fatta male)
#. Ogni SPRINT dovrebbe terminare con una pagina di sintesi che riporta l'architettura finale 
   del sistema (con i link al modello e ai Test). 
   Questa pagina sarà l'inizio del documento relativo allo SPRINT successivo.


