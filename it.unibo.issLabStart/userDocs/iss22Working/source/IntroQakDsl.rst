.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

.. _Eclipse Xtext: https://www.eclipse.org/Xtext/download.html
.. _Qak syntax: ./_static/Qactork.xtext

=============================================
QActor (meta)model
=============================================

`Qak syntax`_

--------------------------------------
StartUp
--------------------------------------

#. Scarica `Eclipse Xtext`_   e installa il **plug-in Eclipse per Kotlin**.
#. Imposta (tramite **Windows->Preferences** ) 
      il *compilatore Java* alla ``versione 1.8`` e il jre a ``jre1.8.0_ ...`` 

      .. (``C:\Program Files\Java\jre1.8.0_301``)

#. Copia nella directory **dropins** di Eclipse i file che costituiscono il supporto al metamodello-qak: 
    ``it.unibo.Qactork_1.2.4.jar``, ``it.unibo.Qactork.ui_1.2.4.jar``, ``it.unibo.Qactork.ide_1.2.4.jar``.




+++++++++++++++++++++++++++++++++++++++
Creazione di un nuovo progetto
+++++++++++++++++++++++++++++++++++++++

#. In **un'area di lavoro vuota**, crea un nuovo progetto Java
#. In ``src`` , crea un file con estensione ``qak``
   A questo punto Eclipse dovrebbe presentare una finestra come la seguente:
   
   .. image::  ./_static/img/Qak/qakStarting.png
      :align: center 
      :width: 50% 
#. Aggiungere la **natura Kotlin** al progetto
 


  Ricordamo che:

  :remark:`Un file qak include la definizione (testuale) di un modello`

  - che definisce :blue:`struttura, interazione e comportamento` di un sistema distribuito.


--------------------------------------
demonottodo.qak
--------------------------------------

Il linguaggio Qak mira a esprimere modelli eseguibili, ma   
**non è completo dal punto di vista computazionale**. Dunqu, parte del comportamento potrebbe talvolta 
dover essere espresso direttamente in Kotlin. Ma occorre non  esagerare l'uso di una tale possibilità.

.. code::  

  System demonottodo

  Context ctxdemonottodo ip [host="localhost" port=8055]

  QActor demonottodo context ctxdemonottodo{
    State s0 initial { 	 
    [#
      fun fibo(n: Int) : Int {
        if( n < 0 ) throw Exception("fibo argument must be >0")
        if( n == 0 || n==1 ) return n
        return fibo(n-1)+fibo(n-2)
      }
      println("---------------------------------------------------- ")
      println("This actor definies its activity completelyin Kotlin")	
      val n = 7
      val v = fibo(n)	
      println("fibo($n)=$v")
      println("----------------------------------------------------- ")
    #]
    }   
  }


++++++++++++++++++++++++++++++++++++++++
demobetter.qak
++++++++++++++++++++++++++++++++++++++++

Per limitare l'uso diretto di codice Kotlin, è opportuno introdurre classi di utilità e invocarne i metodi.

.. code::  

  System demobetter
  Context ctxdemobetter ip [host="localhost" port=8055]

  QActor demobetter context ctxdemobetter{
    [# var n = 7  #] //Global variable 
    State s0 initial { 	 
      [#  ut.outMsg( "fibo($n)=" + ut.fibo(n))    #]
    }   
  }

La utility **ut** potrebbe essere codice scritto in Java o in Kotlin. Se viene definita nel progetto in corso (ad esempio
in una directory :blue:`resource`) è bene sia scritta in Kotlin.

.. code::  kotlin

  import unibo.actor22comm.utils.ColorsOut

  object ut {
    
    fun fibo(n: Int) : Int {
      if( n < 0 ) throw Exception("fibo argument must be >0")
      if( n == 0 || n==1 ) return n
      var v = fibo(n-1)+fibo(n-2)
      return v
    }
    
    fun outMsg( m: String ){
      ColorsOut.outappl(m, ColorsOut.GREEN);
    }
  }  


:remark:`Per usare codice Java, fare ricorso a file jar`

 

--------------------------------------
demoStrange.qak
--------------------------------------




--------------------------------------
demo0.qak
--------------------------------------


--------------------------------------
sentinel.qak
--------------------------------------


--------------------------------------
demoReq.qak
--------------------------------------


--------------------------------------
demoAskfor.qak
--------------------------------------

--------------------------------------
demoStreams.qak
--------------------------------------

--------------------------------------
Coded Qak
--------------------------------------

