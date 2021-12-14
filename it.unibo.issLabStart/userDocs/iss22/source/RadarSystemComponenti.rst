+++++++++++++++++++++++++++++++++++++++++++++
Componenti per i dispositivi di I/O
+++++++++++++++++++++++++++++++++++++++++++++

Il primo :blue:`SPRINT` di questo nostro sviluppo bottom-up consiste nel realizzare componenti-base 
per i dispositivi di I/O, partendo dalle interfacce introdotte nella analisi. 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Dispositivi reali e Mock, DeviceFactory e file di configurazione
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Per agevolare la messa a punto di una applicazione, conviene spesso introdurre Mock-objects, cioè
dispositivi simulati che riproducono il comportamento dei dispositivi reali in modo controllato.

Inoltre, per facilitare la costruzione di dispositivi senza dover denotare in modo esplicito le classi
di implementazione, conviene introdurre una **Factory**:

.. code:: java

  public class DeviceFactory {
    public static ILed createLed() { ... }
    public static ISonar createSonar() { ... }
    public static IRadarGui createRadarGui() {
  }

Ciasun metodo di ``DeviceFactory`` restitusce una istanza di dispositivo reale o Mock in accordo alle specifiche
contenute in un file di Configurazione (``RadarSystemConfig.json``) scritto in JSon:

.. code:: java

  {
  "simulation"       : "true",
   ...
  "DLIMIT"           : "15"
  }

Si noti che questo file contiene anche la specifica di ``DLIMIT`` come richiesto in fase di analisi dei requisiti.

Questo file di configurazione viene letto dal metodo *setTheConfiguration* di un singleton Java ``RadarSystemConfig``
che inizializza variabili ``static`` accessibili all'applicazione:

.. code::  java

  public class RadarSystemConfig {
    public static boolean simulation = true;  //overridden by setTheConfiguration
    ...
    public static void setTheConfiguration( String resourceName ) { 
      ... 
      fis = new FileInputStream(new File(resourceName));
      JSONTokener tokener = new JSONTokener(fis);
      JSONObject object   = new JSONObject(tokener);
      simulation = object.getBoolean("simulation");
      ...
    }
  }

Per essere certi che un dispositivo Mock possa essere un sostituto efficace di un dispositivo reale,
introduciamo per ogni dispositivo una **classe astratta** comune alle due tipologie, 
che funga anche da Factory.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il Led
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

Un Led è un dispositivo di output che può essere modellato e gestito in modo semplice.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
La classe astratta LedModel
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

La classe astratta relativa al Led introduce un metodo :blue:`abstract` denominato ``ledActivate``
cui è demandata la responsabilità di accendere/spegnare il Led.

.. code:: java

  public abstract class LedModel implements ILed{
    private boolean state = false;	

    //Factory methods    
    public static ILed create() {
      ILed led;
      if( RadarSystemConfig.simulation ) led = createLedMock();
      else led = createLedConcrete();
      led.turnOff();      //Il led iniziale è spento
    }
    public static ILed createLedMock(){return new LedMock();  }
    public static ILed createLedConcrete(){return new LedConcrete();}	
    
    //Abstract methods
    protected abstract void ledActivate( boolean val);
    
    protected void setState( boolean val ) { 
      state = val; 
      ledActivate( state ); 
    }
    @Override
    public void turnOn(){ setState( true ); }
    @Override
    public void turnOff() { setState( false ); }
    @Override
    public boolean getState(){  return state;  }
  }

La variabile locale booleana ``state`` viene posta a ``true`` quando il Led è acceso.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il LedMock
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

In pratica il ``LedModel`` è già un ``LedMock``, in quanto tiene traccia dello stato corrente nella variabile
``state``. 

Poichè il metodo ``ledActivate`` ha la responsabilità di definire il codice specifico per
accedendere/spegenre il Led, a livello di Mock possiamo rendere visibile lo stato del Led
sullo standard output. 
 

.. code:: java

  public class LedMock extends LedModel implements ILed{
    @Override
    protected void ledActivate(boolean val) {	 showState(); }

    protected void showState(){ 
      System.out.println("LedMock state=" + getState() ); 
    }
  }


Una implementazione più user-friendly potrebbe 
introdurre una GUI che cambia di colore e/o dimensione a seconda che il Led sia acceso o spento.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il LedConcrete
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Il componente che realizza la gestione di un Led concreto, conesso a un RaspberryPi, si può avvalere
del software reso disponibile dal committente:

.. code:: java

  public class LedConcrete extends LedModel implements ILed{
  private Runtime rt  = Runtime.getRuntime();    
    @Override
    protected void ledActivate(boolean val) {
      try {
        if( val ) rt.exec( "sudo bash led25GpioTurnOn.sh" );
        else rt.exec( "sudo bash led25GpioTurnOff.sh" );
      } catch (IOException e) { ... }
    }
  }


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Testing del dispositivo Led
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Un test automatizzato di tipo unit-testing sul Led può essere espresso usando JUnit come segue:

.. code-block:: java

  public class TestLed {
    @Before
    public void up(){ System.out.println("up");	}
    @After
    public void down(){ System.out.println("down"); }	
    @Test 
    public void testLedMock() {
      RadarSystemConfig.simulation = true; 
      
      ILed led = DeviceFactory.createLed();
      assertTrue( ! led.getState() );
      
      led.turnOn();
      assertTrue(  led.getState() );
      Utils.delay(1000);		//to see the ledgui

      led.turnOff();
      assertTrue(  ! led.getState() );	
      Utils.delay(1000);		//to see the ledgui	
    }	
  }

Un test sul ``LedConcrete`` ha la stessa struttura del test sul ``LedMock``, ma bisogna avere l'avvertenza
di eseguirlo sul RaspberryPi. Eseguendo il test sul PC non vengono segnalati errori (in quanto
il Led 'funziona' da un punto di vista logico) ma compaiono messaggi del tipo:

.. code-block::

  LedConcrete | ERROR Cannot run program "sudo": ...  

 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il Sonar 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

Un Sonar è un dispositivo di input che deve fornire dati quando richiesto dalla applicazione.

Il software fornito dal committente per l'uso di un Sonar reale ``HC-SR04`` introduce
logicamente un componente attivo, che produce in modo autonomo sul dispositivo standard di output,
con una certa frequenza, una sequenza di valori interi di distanza.

La modellazione di un componente produttore di dati è più complicata di quella di un dispositivo di output
in quanto occorre affrontare un classico problema produttore-consumatore.
Al momento seguiremo un approccio tipico della programmazione concorrente, basato su memoria comune


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
La classe astratta SonarModel
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

La classe astratta relativa al Sonar introduce due metodi :blue:`abstract`,  uno per specificare il modo di inizializzare il sonar 
(metodo ``sonarSetUp``) e uno per specificare il modo di produzione dei dati (metodo ``sonarProduce``).
Inoltre, essa definisce due metodi ``create`` che costituiscono factory-methods per un sonar Mock e un sonar reale.

.. code:: java

  public abstract class SonarModel implements ISonar{
   protected boolean stopped = false;    //quando true, il sonar si ferma
   protected  IDistance curVal ;

    //Factory methods
    public static ISonar create() {
      if( RadarSystemConfig.simulation )  return createSonarMock(); 
      else  return createSonarConcrete();		
    }

    protected SonarModel() {  //hidden costructor, to force setup
      sonarSetUp();
    }

    public static ISonar createSonarMock() { return new SonarMock(); }
    public static ISonar createSonarConcrete() { return new SonarConcrete(); }


Il Sonar viene modellato come un processo produttore di dati che risulta attivo 
quando la variabile locale ``stopped`` è ``true``. 
Di qui le seguenti definizioni:

.. code:: java

    @Override
    public void deactivate() { stopped = true; }
    @Override
    public boolean isActive() { return ! stopped; }

Il codice realativo alla produzione dei dati viene incapsulato in un metodo abstract ``sonarProduce``
che dovrà essere definito in modo diverso da un ``SonarMock`` e un ``SonarConcrete``, così come il
metodo di inizializzazione ``sonarSetUp``:

.. code:: java

    //Abstract methods
    protected abstract void sonarSetUp() ;		 
    protected abstract void sonarProduce() ;


Con queste premesse, il metodo ``activate`` deve inizializzare il Sonar
e attivare un Thread interno di produzione di dati:

.. code:: java

    @Override
    public void activate() {
      stopped = false;
      new Thread() {
        public void run() {
          while( ! stopped  ) { sonarProduce(); }
        }
      }.start();
    }

La parte applicativa che funge da consumatore dei dati prodotti dal Sonar dovrà invocare il metodo
``geDistance`` che viene definito in modo da bloccare il chiamante se il Sonar è in 'fase di produzione',
riattivandolo non appena il dato è stato prodotto:  

.. code:: java

    protected boolean produced = false;   //synch var

    @Override
    public IDistance getDistance() {   //non synchronized perchè violerebbe l'interfaccia
      waitForUpdatedVal();
      return curVal;
    }       
    private synchronized void waitForUpdatedVal() {
      while( ! produced ) wait();
      produced = false;
    }
    synchronized void valueUpdated( ){
      produced = true;
      notify();   //riattiva il Thread in attesa su getDistance
    }
  }

.. _SonarMock:

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il SonarMock
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Un Mock-sonar che produce valori di distanza da ``90`` a ``0`` può quindi ora essere definito come segue:

.. code:: java

  public class SonarMock extends SonarModel implements ISonar{
    @Override
    protected void sonarSetUp(){  curVal = new Distance(90);  }
    @Override
    protected void sonarProduce() {
      if( RadarSystemConfig.testing ) {
        curVal.setVal( RadarSystemConfig.testingDistance );
        stopped = true;  //one shot
      }else {
        curVal.setVal( curVal.getVal() - 1 ) ;
        stopped = ( curVal.getVal() == 0 );
    }
    valueUpdated(   ); 
    Utils.delay(RadarSystemConfig.sonarDelay);  //avoid fast generation 
  }  

Si noti che: 

- viene definito un nuovo parametro di configurazioe ``testing`` che, quando ``true`` denota che
  il sonar sta lavorando in una fase di testing, per cui produce un solo valore dato dal
  parametro ``testingDistance``;
- viene definito un nuovo parametro di configurazine ``sonarDelay`` per un rallentamento
  della frequenza di generazione dei dati.
 
.. code:: java

  {
  "simulation"       : "true",
   ...
  "DLIMIT"           : "15",
  "testing"          : "false"
  "testingDistance"  : "10",
  "sonarDelay"       : "100"
  }

- viene definita una classe che implementa ``IDistance``.

.. code:: java

  public class Distance implements IDistance{
  private int v;
    public Distance(int d) { v=d;	}
    @Override
    public void setVal(int d) {	v = d;	}
    @Override
    public int getVal() { return v; }
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il SonarConcrete
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Il componente che realizza la gestione di un Sonar concreto, conesso a un RaspberryPi,
si può avvalere del programma ``SonarAlone.c`` fornito dal committente.
Per ridurre la frequenza di produzione, il metodo ereditato ``valueUpdated``, che sblocca un
consumatore di livello  applicativo, viene invocato ogni  ``numData`` 
valori emessi sul dispositivo standard di output.

.. code:: java

  public class SonarConcrete extends SonarModel implements ISonar{
  private int numData           = 5; 
  private int dataCounter       = 1;
  private  BufferedReader reader ;
	
  @Override
  protected void sonarSetUp() {
    curVal.setVal( 0 );		
    try {
      Process p  = Runtime.getRuntime().exec("sudo ./SonarAlone");
      reader = new BufferedReader( new InputStreamReader(p.getInputStream()));	
    }catch( Exception e) { ... 	}
  }
  protected void sonarProduce() {
    try {
      String data = reader.readLine();
      dataCounter++;
      if( dataCounter % numData == 0 ) { //every numData ...
        curVal.setVal( Integer.parseInt(data) );
        valueUpdated( );    
      }
    }catch( Exception e) { ... }
  }
  }


&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Testing del dispositivo Sonar
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

Il testing di un sonar riguarda due aspetti distinti:

#. il test sul corretto funzionamento del dispositivo in quanto tale. Supponendo di porre
   di fronte al Sonar un ostacolo a distanza :math:`D`, il Sonar deve emettere dati di valore
   :math:`D \pm \epsilon`.
#. il test sul corretto funzionamento del componente software responsabile della trasformazione del dispositivo
   in un produttore di dati consumabili da un altro componente.

Ovviamente qui ci dobbiamo occupare della seconda parte, supponendo che la prima sia soddisfatta. A tal fine
possiamo procedere come segue:

- per il *LedMock*, noi controlliamo la sequenza di valori emeessi e quindi possiamo
  verificare che  un consumatore riceva dal metodo ``getVal`` i valori nella giusta sequenza;
- per il *LedConcrete*, poniamo uno schermo a distanza prefissata :math:`D`  e verifichiamo che
  un consumatore riceva dal  metodo ``getVal`` valori :math:`D \pm \epsilon`.

Una TestUnit automatizzata per il ``SonarMock`` può essere quindi definita in JUnit come segue:

.. code:: java

  @Test 
  public void testSonarMock() {
    RadarSystemConfig.simulation = true;
    RadarSystemConfig.sonarDelay = 10; //quite fast generation...
    int delta = 1;

    ISonar sonar = DeviceFactory.createSonar();
    sonar.activate();
    int v0 = sonar.getDistance().getVal();   //first val consumed
    while( sonar.isActive() ) {
      int d = sonar.getDistance().getVal();   //blocking!
      int vexpectedMin = v0-delta;
      int vexpectedMax = v0+delta;
      assertTrue(  d <= vexpectedMax && d >= vexpectedMin );
      v0 = d; 
    }
  }

Una TestUnit per il ``SonarConcrete`` è simile, una volta fissato il valore :math:`delta=\epsilon` 
di varianza sulla distanza-base.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il Sonar come dispositivo osservabile
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Volendo impostare il Sonar come un dispositivo osservabile, 
introduciamo un nuovo contratto, che esetende il precedente:

.. code:: java

  interface ISonarObservable  extends ISonar{
    void register( IObserver obs );		 
    void unregister( IObserver obs );
  }

  interface IObserver extends java.util.Observer{
    public void update( int value );
  }

Nel quadro di un programma ad oggetti convenzionale, un ``ISonarObservable``  è un ``ISonar`` 
con la capacità di registrare osservatori e di invocare, ad ogni aggiornamento del valore
di distanza, il metodo ``update`` di tutti gli osservatori registrati.

Per aggiungere al Sonar le funzionalità di osservabilità,  possiamo avvalerci del pattern decorator_
trasformando oggetti di tipo ``SonarState`` in oggetti osservabili:

.. _decorator: https://it.wikipedia.org/wiki/Decorator

.. code:: java

  public class DistanceObservable extends Observable implements IDistance{
  private IDistance d;

  public DistanceObservable( IDistance d ) { this.d=d; }
    @Override
    public void setVal( int v ) {
      d.setVal( v );  //change the decorated object
      setChanged();  //IMPORTANT
      notifyObservers( v );		
    }
    @Override
    public int getVal() { return d.getVal(); }
  }


Il ``SonarObservableMock`` sarà una specializzazione del precedente :ref:`SonarMock<SonarMock>`, 
che ridefinisce 
il ``sonarSetUp`` creando un oggetto di tipo ``DistanceObservable``
e che implementa i metodi di registrazione ridiregendoli allo stato osservabile.

.. code:: java

  public class SonarObservableMock 
            extends SonarMock implements ISonarObservable  {
    @Override
    protected void sonarSetUp() { 
      super.sonarSetUp();
      curVal = new DistanceObservable(myCurVal);//wraps the original state
    }
    @Override
    public void register(IObserver obs) {
        ((DistanceObservable)curVal).addObserver(obs);		 
    }
    @Override
    public void unregister(IObserver obs) {
      ((DistanceObservable)curVal).deleteObserver(obs);		
    }
  }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Testing del sonar osservabile
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il testing sul ``SonarObservableMock`` viene qui impostato nel modo che segue:

- si regola il Sonar in modo che produca un valore costante definito in ``RadarSystemConfig.testingDistance``
- si introduce (almeno) un observer che controlla che il dato osservato sia quello emesso

.. code:: java

  @Test 
  public void testSingleshotSonarObservableMock() {
    RadarSystemConfig.testing = true;
    ISonarObservable sonar = DeviceFactory.createSonarObservable();
    IObserver obs1         = new SonarObserverFortesting("obs1",true) ;
    sonar.register( obs1 );	//add then observer
    sonar.activate();
    int v0 = sonar.getDistance().getVal();
    assertTrue(  v0 == RadarSystemConfig.testingDistance );
  }

L'*obserer* viene impostato in modo da controllare anche dati emessi da un sonar reale
che opera con ostacolo fisso posto davanti ad esso, alla distanza prefissata.

.. code:: java

  class SonarObserverFortesting implements IObserver{
  private String name;
  private boolean oneShot = false;
  private int v0          = -1;
  private int delta       =  1;
	
  public SonarObserverFortesting(String name, boolean oneShot) {
    this.name    = name;
    this.oneShot = oneShot;
  }
  @Override  //from java.util.Observer
  public void update(Observable source, Object data) {
    int v = Integer.parseInt(data.toString());
    update( v );
  }
  @Override //from IObserver
  public void update(int value) {
    if(oneShot) {
      assertTrue(  value == RadarSystemConfig.testingDistance );	
    }else {
      if( v0 == -1 ) {//set the first value observed
        v0 = value;
      }else {
        int vexpectedMin = v0-delta;
        int vexpectedMax = v0+delta;
        assertTrue(  value <= vexpectedMax && value >= vexpectedMin );
        v0 = value;			 
      }
    }
  }
  }//SonarObserverFortesting



.. _controller: 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il Controller
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 
Il componente che realizza la logica applicativa può essere definito partendo dal modello introdotto
nella fase di analisi, attivando un Thread che realizza lo schema *read-eval-print*.
Nel codice che segue realizzeremo ciascun requisito con un componente specifico:

.. code:: java

  public class Controller {
    public static void activate( ILed led, ISonar sonar,IRadarDisplay radar) {
      System.out.println("Controller | activate"  );
      new Thread() {
        public void run() { 
          try {
            while( sonar.isActive() ) {
              IDistance d = sonar.getDistance();  
              RadarGuiUsecase.doUseCase( radar,d  );	 
              LedAlarmUsecase.doUseCase( led,  d  );   
            }
          } catch (Exception e) { ...  }					
        }
      }.start();
    }
  } 

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
LedAlarmUsecase
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
.. code:: java

  public class LedAlarmUsecase {
    public static void doUseCase(ILed led, IDistance d) {
      try {
        if( d.getVal() <  RadarSystemConfig.DLIMIT ) led.turnOn(); 
        else  led.turnOff();
      } catch (Exception e) { ... }					
    }
  } 

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
RadarGuiUsecase
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
.. code:: java

  public class RadarGuiUsecase {
    public static void doUseCase( IRadarDisplay radar, IDistance d ) {
      radar.update(""+d.getVal(), "90");
    }	 
  }


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Il sistema simulato su PC
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il sistema viene dapprima costruito secondo le specifiche contenute nel file di configurazione e 
successivamente attivato facendo partire il Sonar.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Fase di setup
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
.. code:: java

  public class RadarSystemMainOnPc {
  private ISonar sonar        = null;
  private ILed led            = null;
  private IRadarDisplay radar = null;

    ...
    public static void main( String[] args) throws Exception {
      RadarSystemMainOnPc sys = new RadarSystemMainOnPc();
      sys.setup( "RadarSystemConfigPcControllerAndGui.json" );
      sys.build();
      sys.activateSonar();
    }  
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Il file di configurazione
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 
.. code:: java

  {
  "simulation"       : "true",
  "ControllerRemote" : "false",
  "LedRemote"        : "false",
  "SonareRemote"     : "false",
  "RadarGuieRemote"  : "false",
  "pcHostAddr"       : "localhost",
  "raspHostAddr"     : "192.168.1.12",
  "radarGuiPort"     : "8014",
  "ledPort"          : "8010",
  "sonarPort"        : "8012",
  "controllerPort"   : "8016",
  "serverTimeOut"    : "600000",
  "applStartdelay"   : "3000",
  "sonarDelay"       : "100",
  "DLIMIT"           : "15",
  "testing"          : "false"
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Fase di costruzione del sistema
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  
.. code:: java

  public class RadarSystemMainOnPc {
    ...
    public void build() throws Exception {			
      //Dispositivi di Input
      sonar  = DeviceFactory.createSonar();
      //Dispositivi di Output
      led    = DeviceFactory.createLed();
      radar  = DeviceFactory.createRadarGui();	
      //Controller 
      Controller.activate(led, sonar, radar);
    }    
    public void activateSonar() {
      if( sonar != null ) sonar.activate();
    }
    public static void main( String[] args) throws Exception { ... }
  }

Una unità di testing può automatizzare l'esecuzione di questo sistema ed 
effettuare controlli sul suo funzionamento.

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Utilità per il testing
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& 

Inseriamo nel main program  metodi che restitusicono un riferimento ai componenti del sistema:

.. code:: java

  public class RadarSystemMainOnPc {
    ... 
    public ILed getLed() { return led; }
    public ISonar getSonar() { return sonar; }
  }

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Testing del sistema simulato su PC
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

La testUnit introduce un metodo di setup per definire i parametri di configurazione 
(in modo da non dipendere da files esterni) e per costruire il sistema.

.. code:: java

  public class TestBehaviorAllOnPc {
  private RadarSystemAllOnPc sys;
    @Before
    public void setUp() {
      System.out.println("setUp");
      try {
        sys = new RadarSystemAllOnPc();
        //Set system configuration (we don't use RadarSystemConfig.json)
        RadarSystemConfig.simulation        = true;    
        RadarSystemConfig.testing           = true;    		
        RadarSystemConfig.ControllerRemote  = false;    		
        RadarSystemConfig.LedRemote         = false;    		
        RadarSystemConfig.SonareRemote      = false;    		
        RadarSystemConfig.RadarGuieRemote   = false;    	
        RadarSystemConfig.pcHostAddr        = "localhost";
        sys.build();
      } catch (Exception e) {
        fail("setup ERROR " + e.getMessage() );
      }
    }

Come anticipato in fase di analisi dei requisiti, impostiamo un test nel caso in cui  
il Sonar produca un valore ``d>DLIMIT`` e un altro test per il Sonar che produce un valore ``d<DLIMIT``.

.. code:: java

  @Test 
  public void testFarDistance() {
    RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT + 20;
    testTheDistance( false );
  }	
  @Test 
  public void testNearDistance() {
    RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT - 1;
    testTheDistance( true );
  }
   
  protected void testTheDistance( boolean ledStateExpected ) {
    RadarDisplay radar = RadarDisplay.getRadarDisplay();  //singleton
    sys.activateSonar();   //il sonar produce un solo valore
    while( sys.getSonar().isActive() ) Utils.delay(10); //give time to work 
      assertTrue(  sys.getLed().getState() == ledStateExpected
        && radar.getCurDistance() == RadarSystemConfig.testingDistance);
    Utils.delay(1000) ; //give time to look at the display		
  }

  

