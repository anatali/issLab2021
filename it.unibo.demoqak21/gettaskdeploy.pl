%====================================================================================
% gettaskdeploy description   
%====================================================================================
context(ctxgettaskdeploy, "127.0.0.1",  "TCP", "8025").
 qactor( taskdeploy, ctxgettaskdeploy, "it.unibo.taskdeploy.Taskdeploy").
  qactor( studmock, ctxgettaskdeploy, "it.unibo.studmock.Studmock").
