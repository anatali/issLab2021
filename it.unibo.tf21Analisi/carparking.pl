%====================================================================================
% carparking description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( parkingservicegui, ctxcarparking, "it.unibo.parkingservicegui.Parkingservicegui").
  qactor( parkingmanagerservice, ctxcarparking, "it.unibo.parkingmanagerservice.Parkingmanagerservice").
  qactor( parkingservicestatusgui, ctxcarparking, "it.unibo.parkingservicestatusgui.Parkingservicestatusgui").
  qactor( trolley, ctxcarparking, "it.unibo.trolley.Trolley").
