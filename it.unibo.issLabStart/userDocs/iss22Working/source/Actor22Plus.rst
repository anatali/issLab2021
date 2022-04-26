.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

.. _visione olistica: https://it.wikipedia.org/wiki/Olismo
.. _state diagram: https://en.wikipedia.org/wiki/State_diagram#:~:text=A%20state%20diagram%20is%20a,this%20is%20a%20reasonable%20abstraction.
.. _Automa a stati finiti: https://it.wikipedia.org/wiki/Automa_a_stati_finiti
.. _Macchina d Moore: https://it.wikipedia.org/wiki/Macchina_di_Moore
.. _opinionated: https://govdevsecopshub.com/2021/02/26/opinionated-software-what-it-is-and-how-it-enables-devops/


==================================
Actor22Plus
==================================

Progetto: **unibo.wenvUsage22** package: *unibo.wenvUsage22.unibo.wenvUsage22.annot.walker*.

- BoundaryWalkerAnnot  extends QakActor22FsmAnnot 
- WsConnSysObserver ha un timer che viene usato ad ogni sendLine
- WsConnWEnvObserver  Trasforma dati ricevuti su WS in SystemData.endMoveOk o in SystemData.endMoveKo
- VRobotMoves