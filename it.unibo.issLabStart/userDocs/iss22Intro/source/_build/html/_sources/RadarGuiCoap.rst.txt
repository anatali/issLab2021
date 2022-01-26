.. contents:: Overview
   :depth: 5
.. role:: red 
.. role:: blue 
.. role:: remark

.. `` 

======================================
RadarGuiCoap
======================================

Vogliamo costruire un sistema software capace di:

#. (requisito :blue:`radarGui`): presentare all'utente, mediante un PC convenzionale, un display a forma di radar (``RadarDisplay``).
   Per soddisfare questo requisito possiamo disporre del supporto ``radarPojo.jar`` scritto in JAVA che fornisce un oggetto
   di classe ``radarSupport`` capace di creare il *RadarDisplay* e di visualizzare dati su di esso:

       .. code::

         public class radarSupport {
         private  static RadarControl radarControl;
            public static void setUpRadarGui( ) {
               radarControl = new RadarControl( null ); }
 	         public static void update(String d,String theta){
		         radarControl.update( d, theta );
	        }
         }    

#.  (requisito :blue:`radarUpdate`) permettere ad applicazioni di inviare via rate dati alla *radarGui*  utilizzando API
    in stile RESTful.


#. Costruiamo una risorsa accessibile via rete mediante CoaP che aggiorna uno stato
#. Costruiamo una applicazione RadarGuiCoap che osserva le variazioni di stato della risorsa e modifica il RadarDisplay
#. Costruiamo radarGui rendendo accessibile RadarGuiCoap via rete con SpringBoot / HTTP in modo RESTFUL 
#. Facciamo il deployment su docker rendendo accessibile sia la risorsa sia la radarGui

--------------------------------------------
La distanza come risorsa CoAP
--------------------------------------------

Viene definita come observable e quindi può avere molti observers che sono invocati in modo automatico quando
la risorsa cambia il suo valore (verbo ``PUT``).




