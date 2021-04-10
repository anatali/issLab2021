package it.unibo.executor;

public class ApplMsgs {
    public final static String forwardMsg         = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    public final static String backwardMsg        = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    public final static String microStepMsg       = "{\"robotmove\":\"moveForward\", \"time\": 5}";
    public final static String littleBackwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 10}";
    public final static String turnLeftMsg        = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    public final static String turnRightMsg       = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    public final static String haltMsg            = "{\"robotmove\":\"alarm\", \"time\": 20}";
    public final static String goBackMsg          = "{\"goBack\":\"goBack\" }";
    public final static String resumeMsg          = "{\"resume\":\"resume\" }";
    public final static String forwardstepMsg     = "{\"robotmove\":\"moveForward\", \"time\": 350}";

    public final static String activateId         = "activate";
    public final static String activateMsg        = "{\"ID\":\"ARGS\" }".replace("ID", activateId);
    public final static String executorStartId    = "executorstart";
    public final static String executorstartMsg   = "{\"ID\":\"PATHTODO\" }".replace("ID", executorStartId);
    public final static String endMoveId          = "endmove";
    public final static String endMoveMsg         = "{\"ID\":\"ENDMOVE\" }".replace("ID", endMoveId);
    public final static String executorEndId      = "executorend";
    public final static String executorendokMsg   = "{\"ID\":\"ok\" }".replace("ID", executorEndId);
    public final static String executorendkoMsg   = "{\"ID\":\"PATHDONE\" }".replace("ID", executorEndId);
    public final static String runawyEndId       = "runawayend";
    public final static String runawyEndMsg      = "{\"ID\":\"RESULT\" }".replace("ID", runawyEndId);
    public final static String runawyStartId     = "runawaystart";
    public final static String runawyStartMsg    = "{\"ID\":\"PATHTODO\" }".replace("ID", runawyStartId);

    public final static String stepId            = "step";
    public final static String stepMsg           = "{\"ID\":\"TIME\" }".replace("ID", stepId);
    public final static String stepDoneId        = "stepDone";
    public final static String stepDoneMsg       = "{\"ID\":\"ok\" }".replace("ID", stepDoneId);
    public final static String stepFailId        = "stepFail";
    public final static String stepFailMsg       = "{\"ID\":\"TIME\" }".replace("ID", stepFailId);



}
