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
impostiamo un processo di produzione del software relativo a un nuovo insieme di requisiti


.. _requirements:

--------------------------------------
SonarObservable: Requisiti
--------------------------------------

Si desidera realizzare una versione osservabile (*SonarObservable*) del dispositivo Sonar introdotto in :ref:`Il Sonar` che soddisfi i seguenti 
requisiti:

- il *SonarObservable* deve inviare informazioni a tutti componenti software interessati alla rilevazione dei valori di distanza;
- il *SonarObservable* deve fornire valori di distanza solo quando questi si modificano in modo significativo;
- i componenti interessati ai valori di distanza prodotti dal *SonarObservable* sono denominati *Observer* e pssono risiedere
  sullo stesso nodo del *SonarObservable* (cioè sul RaspberryPi) o su un nodo remoto (ad esempio sul PC);
- il funzionamento del *SonarObservable* deve essere testato in modo automatizzato ponendo un ostacolo a distanza fissa ``DTESTING1`` davanti
  ad esso, controllando che tutti gli *Observers* ricevano il valore ``DTESTING1``. Dopo un qualche tempo, si modifica
  la posizione dell'ostacolo a una nuova distanza ``DTESTING2`` e si controlla che gli tutti gli *Observers* ricevano il valore ``DTESTING2``.

+++++++++++++++++++++++++++++++++++++++++++
SonarObservable: analisi dei requisiti
+++++++++++++++++++++++++++++++++++++++++++

:worktodo:`Chiarire, interagendo con il committente, i punti ritenuti oscuri nel testo dei requisiti.`

+++++++++++++++++++++++++++++++++++++++++++
SonarObservable: analisi del problema
+++++++++++++++++++++++++++++++++++++++++++

La transizione ad un Sonar osservabile prospettata in :ref:`patternObserver` può essere affrontata pensando il 
*SonarObservable* in due modi:

- come una risorsa che modifica il proprio stato interno ad ogni passo di produzione 
  e che invia agli *Observer* una notifica sul nuovo stato;
- come ad un dispositivo che aggiorna un oggetto :blue:`DistanceMeasured` che diventa 
  :blue:`la reale risorsa osservabile`.

:worktodo:`Dioscutere, come analisti, quale delle due prospettive è preferibile e fare una scelta, motivandola.`