package it.unibo.interaction;
import it.unibo.robotUtils.MsgRobotUtil;

@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url="http://localHost:8090/api/move"
)
public class UsageIssHttp {
    private IssOperations support;

    public UsageIssHttp(){
        support = IssCommsFactory.create( this  );
    }

     public void testuseSupport(){
        String answer = support.requestSynch(MsgRobotUtil.turnLeftMsg);
        System.out.println( "UsageIssHttp | answer=" + answer  );
    }

    public static void main(String args[])   {
        new UsageIssHttp().testuseSupport();;
    }
}
