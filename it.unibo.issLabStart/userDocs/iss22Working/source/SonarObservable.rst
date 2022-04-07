.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo 

.. _mvc: https://it.wikipedia.org/wiki/Model-view-controller

.. _MVP: https://www.nexsoft.it/model-view-presenter/

=============================================
Il SonarObservable
=============================================

Tenendo conto di quanto detto in *Indicazioni-sul-processo-di-produzione* (:doc:`CostruireSoftware`), 
impostiamo un processo di produzione del software relativo a un nuovo insieme di requisiti.


.. _requirements:

--------------------------------------
SonarObservable: Requisiti
--------------------------------------

Si desidera realizzare una versione osservabile (*SonarObservable*) del dispositivo Sonar introdotto in :ref:`Il Sonar` che soddisfi i seguenti 
requisiti:

- il *SonarObservable* deve inviare informazioni a tutti componenti software interessati alla rilevazione 
  dei valori di distanza;
- il *SonarObservable* deve fornire valori di distanza solo quando questi si modificano in modo significativo;
- i componenti interessati ai valori di distanza prodotti dal *SonarObservable* sono denominati *Observer* 
  e pssono risiedere sullo stesso nodo del *SonarObservable* (cioè sul RaspberryPi) o su un nodo remoto 
  (ad esempio sul PC);
- il funzionamento del *SonarObservable* deve essere testato in modo automatizzato ponendo un ostacolo 
  a distanza fissa ``DTESTING1`` davanti
  ad esso, controllando che tutti gli *Observers* ricevano il valore ``DTESTING1``. Dopo un qualche tempo, si modifica
  la posizione dell'ostacolo a una nuova distanza ``DTESTING2`` e si controlla che gli tutti gli *Observers* 
  ricevano il valore ``DTESTING2``.

+++++++++++++++++++++++++++++++++++++++++++
SonarObservable: analisi dei requisiti
+++++++++++++++++++++++++++++++++++++++++++

:worktodo:`Chiarire, interagendo con il committente, i punti ritenuti oscuri nel testo dei requisiti.`

+++++++++++++++++++++++++++++++++++++++++++
SonarObservable: analisi del problema
+++++++++++++++++++++++++++++++++++++++++++

La transizione ad un Sonar osservabile prospettata in :ref:`patternObserver` può essere affrontata pensando il 
*SonarObservable* in due modi:

- modo1: ...
- modo2: ...

.. come una risorsa che modifica il proprio stato interno ad ogni passo di produzione  e che invia agli *Observer* una notifica sul nuovo stato;
.. come ad un dispositivo che aggiorna un oggetto (diciamo ):blue:`DistanceMeasured`) che diventa :blue:`la reale risorsa osservabile`.

:worktodo:`Discutere, come analisti, quale sono i due modi e quale è preferibile, dando le motivazioni.`

Prima di procedere osserviamo quanto segue:

- una adeguata analisi del problema è premessa indispensabile ad ogni progetto e ad ogni produzione di software;
- le considerazioni introdotte in fase di analisi (e la loro formalizzazione) sono spesso oggetto delle domande 
  citate in :ref:`Dettagli sul colloquio orale`;
- il Sonar è un *dispositivo di misura* che *produce valori* di distanza;
- cosa significa **osservare un dispositivo**? Significa osservare i valori prodotti o il suo stato interno
  (carica delle batterie, temperatura interna, rate di generazione dei dati, etc.)?
- si può dire che il valore della distanza sia una proprietà del dispositivo di misura (cioè del Sonar)?
- se siamo convinti che si debbano osservare i valori prodotti pittosto che il dispositivo in sè, 
  come **formalizzare** questo approccio nel caso del SonarObservable?


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
La distanza misurata come risorsa osservabile
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Optiaando per l'idea che il Sonar  sia un processo che aggiorna 
il valore  di una distanza, introduciamo il concetto di **distanza misurata** formalizzandolo con una 
interfaccia.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
IDistanceMeasured
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
 

.. code:: java

  public interface IDistanceMeasured extends IDistance, IObservable{
    public void setVal( IDistance d );
  }

L'interfaccia ``IDistanceMeasured`` dice che una *distanza misurata* è una distanza :ref:`IDistance<IDistance>`
il cui valore può essere modificato. Inoltre è anche un observable in quanto 
estende  :ref:`IObservable` permettendo la 
registrazione/rimozione di *Observer* che implementano l'interfaccia :ref:`IObserver`.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
IObservable
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
 

.. code:: java

  public interface IObservable{
   public void addObserver(IObserver obs );//implemented by Observable
   public void deleteObserver(IObserver obs);//implemented by Observable
  }


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
IObserver
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
 
.. code:: java

  public interface IObserver extends Observer{
   public void update( String value );
   //From Observer: public void update(Observable o, Object news)
  }



+++++++++++++++++++++++++++++++++++++++++++
SonarObservable: progetto
+++++++++++++++++++++++++++++++++++++++++++

Il progetto consiste nel realizzare i seguenti componenti:

- :ref:`DistanceMeasured` : implementa :ref:`IDistanceMeasured` utlizzando la classe ``java.util.Observable`` per
  le gestione degli Observer;
- :ref:`SonarMockForObs` : specializza :ref:`SonarModel` realizzando un Sonar mock orientato alla 
  osservabilità dei dati, in quanto produttore di valori di  *distanza misurata osservabile*.  
- :ref:`SonarConcreteForObs` : specializza  :ref:`SonarModel` realizzando un Sonar concreto orientato alla 
  osservabilità dei dati, in quanto produttore di valori di  *distanza misurata osservabile*.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
DistanceMeasured
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La casse :blue:`DistanceMeasured` che realizza il concetto di :blue:`distanza misurata osservabile` può essere definita
come segue:

.. code:: java

  public class DistanceMeasured 
        extends java.util.Observable implements IDistanceMeasured{
  private IDistance d;
  public DistanceMeasured() {}
    @Override
    public void setVal( IDistance v ) {
      d = v;
      setChanged(); //IMPORTANT!!
      notifyObservers( d );		
    }
    @Override
    public IDistance getDistance(   ) { return d; }	
    @Override
    public int getVal() { return d.getVal(); }	
    @Override
    public String toString() { return ""+ getVal(); }
  	@Override
    public void addObserver(IObserver obs) {
      super.addObserver(obs);
    }
    @Override
    public void deleteObserver(IObserver obs) {
      super.deleteObserver(obs);
    }
  }
 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
SonarMockForObs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Simile a :ref:`SonarMock<Il SonarMock>`, con la differenza che i valori correnti di distanza sono di tipo :ref:`IDistanceMeasured`
e che implementa il metodo ``public IDistanceMeasured getDistance()``.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
SonarConcreteForObs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Simile a :ref:`SonarConcrete<Il SonarConcrete>`, con la differenza che i valori correnti di distanza sono di tipo :ref:`IDistanceMeasured`
e che implementa il metodo ``public IDistanceMeasured getDistance()``.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Aggiornamento di DeviceFactory
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

La nascita del nuovo tipo di Sonar induce a introdurre nuovi metodi in :ref:`DeviceFactory<DeviceFactory>`:

.. code:: java

  public static ISonarObservable createSonarForObs() {
    if( DomainSystemConfig.simulation)  { return new SonarMockForObs();
    }else { return SonarConcreteForObs(); }	
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Testing del Sonar 'osservabile'
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il testing sul ``SonarMockObservable`` viene qui impostato nel modo che segue:

- si regola il Sonar in modo che produca un valore costante definito in ``DomainSystemConfig.testingDistance``
- si introduce (almeno) un observer che controlla che il dato osservato sia quello emesso

.. code:: java

  @Test 
  public void testSingleshotSonarForObsMock() {
    DomainSystemConfig.testing = true;
    DomainSystemConfig.simulation      = true;
    DomainSystemConfig.testingDistance = 22;
    boolean oneShot           = true;			
    ISonarForObs  sonar       = DeviceFactory.createSonarForObs();
    IObserver obs1            = new SonarObserverFortesting(
                                    "obs1",sonar,oneShot) ;
    sonar.getDistance().addObserver( obs1 );	 
    sonar.activate();
    BasicUtils.delay(100);   
    int v0 = sonar.getDistance().getVal();
    assertTrue(  v0 == DomainSystemConfig.testingDistance );
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Un observer per il testing
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
L'*observer* viene impostato in modo da controllare anche dati emessi da un sonar reale
che opera con ostacolo fisso posto davanti ad esso, alla distanza prefissata.

.. _SonarObserverFortesting:

.. code:: java

  class SonarObserverFortesting implements IObserver{
  private String name;
  private boolean oneShot = false;
  private int v0          = -1;
  private int delta       =  1;
  private ISonarObservable sonar;
	
  public SonarObserverFortesting(
          String name,ISonarObservable sonar,boolean oneShot) {
    this.name    = name;
    this.oneShot = oneShot;
  }
  @Override  //from java.util.Observer
  public void update(Observable source, Object data) {
    update(data.toString() );
  }
  @Override //from IObserver
  public void update(int value) {
    if(oneShot) {
      assertTrue( value == DomainSystemConfig.testingDistance );	
    }else {
      int value = Integer.parseInt(vs);
      if( v0 == -1 ) {//set the first value observed
        v0 = value;
      }else {
        int vexpectedMin = v0-delta;
        int vexpectedMax = v0+delta;
        assertTrue(value<=vexpectedMax && value>=vexpectedMin );
        v0 = value;			 
      /*
      //Elimino l'observer 
        if( v0 == 70 && name.equals("obs1")) 
          sonar.getDistance().deleteObserver(this);
        if( v0 == 50 && name.equals("obs2")) 
          sonar.getDistance().deleteObserver(this);
      */
        }
      }
    }
  }
  }//SonarObserverFortesting

Si noti che observer di questo tipo vengono di norma eseguiti all'interno del Thread dell'observable che sta operando 
per conto di un qualche client.

.. _ledOsservabile: 






------------------------------------------
Verso gli eventi
------------------------------------------


Il classico :ref:`patternObserver` prevede che la sorgente di informazione (nel nostro caso :ref:`DistanceMeasured`)
offra metodi per la registrazione dei riferimenti agli oggetti Observer interessati agli aggiornamenti.
Inoltre ogni Observer deve implementare un metodo (``update``) che viene invocato dal Sonar all'interno del suo 
Thread di lavoro o mediante altro Thread appositamente creato. L'aggiornamento degli Observer pone infatti un
problema:

- se la sorgente invoca l'update degli observer nel suo Thread di lavoro, essa può subire un ritardo che 
  dipende dal tempo di esecuzione dell'Observer; se un Observer entra in loop, la sorgente di blocca;
- se la sorgente invoca l'update degli observer in un Thread creato ad hoc, vi può essere un prolificazione 
  non tollerabile di Thread nel caso di molti observer.

Ci sentiamo quindi indotti a introdurre un principio:

:remark:`una sorgente osservabile non dovrebbe essere influenzata dalla presenza di Observer`

.. In meccanica quantistica, un Observer sembra determinare la natura stessa della sorgente  


Secondo questo principio, dovremmo escludere anche la possibilità di rendere il Sonar osservabile 
a livello applicativo attraverso 
l'uso di un protocollo *publish-subscribe*, in quanto dipenderebbe da uno specifico protocollo,
con relativa libreria e con riferimenti espliciti a un broker.

Da un punto di vista concettuale, una sorgente di informazione dovrebbe risultare osservabile in quanto capace di 
emettere informazione percebibile da un numero qualsiasi di osservatori interessati,
grazie a meccanismi che non alterano il funzionamento della sorgente.

Questa idea fa sorgere un **abstraction gap**, e suggerisce un nuovo modello:

:remark:`una sorgente è osservabile quando emette informazione in forma di evento`

Il concetto di **evento** è stato introdotto in :ref:`Terminologia di riferimento` 
come un messaggio che non ha un preciso destinatario; una sua implementazione è descritta in :ref:`Eventi`


------------------------------------------
Il Sonar come attore che emette eventi
------------------------------------------