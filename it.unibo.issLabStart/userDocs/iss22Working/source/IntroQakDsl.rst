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
demo0.qak
--------------------------------------

.. list-table:: 
  :widths: 40,60
  :width: 100%

  * -  
      .. image::  ./_static/img/Qak/demoDSL.png
         :align: center 
         :width: 120% 
    -  
      .. code::

        System demo0    
        Dispatch msg1 : msg1(ARG)
        Dispatch msg2 : msg2(ARG)  
        Event alarm   : alarm( KIND )    
        
        Context ctxdemo0 ip [host="localhost" port=8095]

          
        QActor demo context ctxdemo0{
          State s0 initial { 	    
            discardMsg Off
            //[# sysUtil.logMsgs=true #]
          }     
          Goto s1  	
          State s1{
              println("demo in s1") 
            //printCurrentMessage 
          }
          Transition t0 whenMsg msg1 -> s2
                        whenMsg msg2 -> s3 

          State s2{ 
            printCurrentMessage
            onMsg( msg1:msg1(ARG) ){
              println("s2:since msg1:msg1(${payloadArg(0)})")
              delay 1000  
            }
            } 
          Transition t0 whenMsg msg2 -> s3

          State s3{ 
            printCurrentMessage 
            //msg is received but not elaborated
            onMsg( msg2:msg2(1) ){ 
              println("s3: since msg2:msg2(${payloadArg(0)})")
            } 
            }
            Goto s1      
        }    
          
        QActor perceiver context ctxdemo0{
          State s0 initial { 	
            println("perceiver waits ..")
          }
          Transition t0 whenEvent alarm -> handleAlarm
          
          State  handleAlarm{
            printCurrentMessage
          }
          Goto s0
        } 


Questo esempio evidenzia che, seleziondando ``discardMsg Off`` i messaggi che non sono di interesse 
in un certo stato vengono conservati, mentre con ``discardMsg On``, essi vengono eliminati

:blue:`Output con discardMsg On` 

.. code::

  demo in s1
  demo in s2 since msg1:msg1(1)
  demo in s3 since msg2:msg2(1)
  demo in s1
 
:blue:`Output con discardMsg Off` 

.. code::

  demo in s1
  demo in s2 since msg1:msg1(1)
  demo in s3 since msg2:msg2(1)
  demo in s1
  demo in s2 since msg1:msg1(2)

--------------------------------------
demoStrange.qak
--------------------------------------

  
  
.. list-table:: 
  :widths: 30,70
  :width: 100%

  * -  
      .. image::  ./_static/img/Qak/demostrange.png
         :align: center 
         :width: 100% 
    -  
      .. code::
          
        System  demostrange
        Dispatch cmd : cmd(X) 

        Context ctxdemostrange ip [host="localhost" port=8055]

        QActor demostrange context ctxdemostrange{
          State s0 initial { 	 
            printCurrentMessage
            forward demostrange -m cmd : cmd(a)
          }   
          Goto s1             
          State s1{
            printCurrentMessage  
            forward demostrange -m cmd : cmd(b) 
          }
          Goto s2            
          State s2{
            printCurrentMessage
          }
          Transition t0 whenTime 10 -> s3
                        whenMsg cmd -> s2  
          State s3{
            printCurrentMessage
            println("demostrange | s3, BYE")            
          }
        }

Questo esempio evidenzia che:

 - una empty-move è realizzata con emissione di un evento ``local_noMsg`` 
 - una empty-move non crea indicazioni sui messaggi da elaborare: i messaggi in arrivo 
   (inviati dall'attore stesso come auto-messaggi) sono memorizzati
   nella coda interna locale e vengono gestiti nello stato ``s2``
 - un attore non deve rimanare in attesa perenne di messaggi, in quanto può fare una empty-move 
   dopo un certo tempo (**timeOut**) 
 - lo scadere del *timeOut* provoca l'emissione di un evento di indentificatore univoco 
   ``local_tout_aaa_sss`` ove ``aaa`` è il nome dell'attore e ``sss`` è  il nome dello stato corrente

 
:blue:`Output`
 

.. code::

  demostrange in s0 | msg(autoStartSysMsg,dispatch,demostrange,demostrange,start,5)
  demostrange in s1 | msg(local_noMsg,event,demostrange,none,noMsg,4)
  demostrange in s2 | msg(local_noMsg,event,demostrange,none,noMsg,4)
  demostrange in s2 | msg(cmd,dispatch,demostrange,demostrange,cmd(a),6)
  demostrange in s2 | msg(cmd,dispatch,demostrange,demostrange,cmd(b),7)
  demostrange in s3 | msg(local_tout_demostrange_s2,event,timer,none,local_tout_demostrange_s2,8)
  demostrange | s3, BYE


 





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

