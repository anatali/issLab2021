package it.unibo.interactionExamples;

import it.unibo.annotations.IssProtocolSpec;
import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;
import it.unibo.robotSupports.IssCommsSupportFactory;

@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url      = "http://localHost:8090/api/move"
)
public class UsageIssHttp {
    private IssOperations support;

    public UsageIssHttp(){
        support = IssCommsSupportFactory.create( this  );
    }

     public void testuseSupport(){
        String answer = support.requestSynch(MsgRobotUtil.turnLeftMsg);
        System.out.println( "UsageIssHttp | answer=" + answer  );
    }

    public static void main(String args[])   {
        new UsageIssHttp().testuseSupport();;
    }
}
