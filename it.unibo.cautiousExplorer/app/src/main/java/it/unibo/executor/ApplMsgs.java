package it.unibo.executor;

import org.json.JSONObject;

public class ApplMsgs {
    final static String forwardMsg         = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final static String backwardMsg        = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final static String microStepMsg       = "{\"robotmove\":\"moveForward\", \"time\": 10}";
    final static String littleBackwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 10}";
    final static String turnLeftMsg        = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final static String turnRightMsg       = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final static String haltMsg            = "{\"robotmove\":\"alarm\", \"time\": 20}";
    final static String goBackMsg          = "{\"goBack\":\"goBack\" }";
    final static String resumeMsg          = "{\"resume\":\"resume\" }";
    final static String forwardstepMsg     = "{\"robotmove\":\"moveForward\", \"time\": 350}";

    final static String activateId         = "activate";
    final static String activateMsg        = "{\"ID\":\"ARGS\" }".replace("ID", activateId);
    final static String executorStartId    = "executorstart";
    final static String executorstartMsg   = "{\"ID\":\"PATHTODO\" }".replace("ID", executorStartId);
    final static String endMoveId          = "endmove";
    final static String endMoveMsg         = "{\"ID\":\"ENDMOVE\" }".replace("ID", endMoveId);
    final static String executorEndId      = "executorend";
    final static String executorendokMsg   = "{\"ID\":\"ok\" }".replace("ID", executorEndId);
    final static String executorendkoMsg   = "{\"ID\":\"PATHDONE\" }".replace("ID", executorEndId);
    final static String runawyEndId       = "runawayend";
    final static String runawyEndMsg      = "{\"ID\":\"RESULT\" }".replace("ID", runawyEndId);
    final static String runawyStartId     = "runawaystart";
    final static String runawyStartMsg    = "{\"ID\":\"PATHTODO\" }".replace("ID", runawyStartId);

    final static String stepId            = "step";
    final static String stepMsg           = "{\"ID\":\"TIME\" }".replace("ID", stepId);
    final static String stepDoneId        = "stepDone";
    final static String stepDoneMsg       = "{\"ID\":\"ok\" }".replace("ID", stepDoneId);
    final static String stepFailId        = "stepFail";
    final static String stepFailMsg       = "{\"ID\":\"TIME\" }".replace("ID", stepFailId);



}
