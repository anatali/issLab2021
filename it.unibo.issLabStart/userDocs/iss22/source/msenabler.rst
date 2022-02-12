.. role:: red 
.. role:: blue 
.. role:: remark

==================================
WebGui
==================================  

---------------------------------------
IApplicationFacade
---------------------------------------


.. code:: java 

   public interface IApplicationFacade { //extends IApplication
      //public void doJob(String configFileName);
      public String getName();
      public void setUp( String configFile );
      public void ledActivate( boolean v );	
      public String ledState(   );
      public void sonarActivate(   );
      public boolean sonarIsactive(   );
      public void sonarDectivate(   );
      public String sonarDistance(   );	
      public void takePhoto( String fName  );	
      public void sendCurrentPhoto();
      public void startWebCamStream(   );	
      public void stopWebCamStream(   );	
      public String getImage(String fName);
      public void storeImage(String encodedString, String fName);

   //	public ISonarObservable getSonar();
      public void doLedBlink();
      public void stopLedBlink();
   }
 

---------------------------------------------------
it.unibo.msenabler
---------------------------------------------------

Il progetto *it.unibo.msenabler*  è sviluppato in ``Java11`` e utilizza SpringBoot per fornire 
una WebGui alla porta ``8081`` che permette di comandare il Led e il Sonar. 

La GUI si presenta come segue:

.. image:: ./_static/img/Radar/msenablerGui.PNG
   :align: center
   :width: 60%

L'applicazione Spring alla base di *it.unibo.msenabler* potrebbe operare in due modi diversi:

#. *caso locale*: essere attivata su un Raspberry basato su **Buster**, che utilizza ``Java11`` ed 
   utlizzare l'applicazione **a** che fa riferimento ai dispositivi reali connessi al Raspberry. 
   Aprendo un browser su  ``http://<RaspberryIP>:8081``, un uente può inviare comandi al Led e ricevere i dati
   del Sonar in due modi diversi:

  - inviando al sonar il comando getDistance
  - utilizzando una websocket (con URI=/radarsocket). Per questa parte, si consiglia la lettura preliminare 
    di :ref:`WebSockets<WebSockets>`.   

#. caso remoto: essere attivata su un PC ed utlizzare l'applicazione **A** (o 9) per inviare e ricevere informazione 
   via MQTT dalla parte applicativa ( **a** o 7)  operante sul Raspberry.

++++++++++++++++++++++++++++++++++++++++++++++++
Caso locale 
++++++++++++++++++++++++++++++++++++++++++++++++

Come ogni applicazione SpringBoot, gli elementi salienti sono:

- Un controller (denominato ``HumanEnablerController``) che presenta all'end user una pagina 
- La pagina che utilillza Bootstrap è ``RadarSystemUserConsole.html``
- WebSocketConfiguration

Sembra molto lento, in particolare quando si attiva la webcam.

++++++++++++++++++++++++++++++++++++++++++++++++
Caso remoto 
++++++++++++++++++++++++++++++++++++++++++++++++

Su Raspberry, attiviamo 7 (RadarSystemDevicesOnRasp) e su PC 9 (RadarSystemMainOnPcCoap)
all'interno di una applicazione SpringBoot.