%====================================================================================
% ledalone description   
%====================================================================================
mqttBroker("192.168.1.51", "1883", "sonar/data").
context(ctxledalone, "localhost",  "TCP", "8095").
 qactor( ledalone, ctxledalone, "it.unibo.ledalone.Ledalone").
