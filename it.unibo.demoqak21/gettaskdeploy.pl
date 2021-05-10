%====================================================================================
% gettaskdeploy description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/tasks/events").
context(ctxgettaskdeploy, "127.0.0.1",  "TCP", "8025").
 qactor( taskdeploy, ctxgettaskdeploy, "it.unibo.taskdeploy.Taskdeploy").
  qactor( studmock, ctxgettaskdeploy, "it.unibo.studmock.Studmock").
