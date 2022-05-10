.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

    
==================================================
BasicRobot22
==================================================

.. code::
    
    RobotCmdSender ---request---> BasicRobotActor (usa) BasicRobotActor (usa) RobotReal
                endMoveOk    <---                                             --> WEnv


Un attore che esegue comandi a un robot 'astratto' di alto livello, che invia risposte 
endMoveok o endMoveKo.

- BasicRobotAdapter
- BasicRobotActor usa BasicRobotAdapter ed esegue richieste per comandi aril  
- RobotCmdSender esempio di utilizzo di BasicRobotActor