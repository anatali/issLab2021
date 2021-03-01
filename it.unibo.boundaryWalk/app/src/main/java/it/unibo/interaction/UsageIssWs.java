package it.unibo.interaction;
import it.unibo.robotUtils.MsgRobotUtil;

@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.WS,
        url      ="localHost:8091"
)
public class UsageIssWs {
    private IssOperations support;

    //Factory method
    public static UsageIssWs create(){
        UsageIssWs obj        = new UsageIssWs();
        IssOperations support = new IssCommsFactory().create( obj  );
        obj.setCommSupport(support);
        return obj;
    }

    private UsageIssWs(){
    }

    protected void setCommSupport(IssOperations support){
        this.support = support;
    }

    public void testuseSupport()  {
        try {
            String answer = "message sent";
            support.forward(MsgRobotUtil.turnLeftMsg);
            Thread.sleep(400);  //give time ...
            System.out.println("UsageIssWs | answer=" + answer);
            support.forward(MsgRobotUtil.turnRightMsg);
            Thread.sleep(400);  //give time ...
            System.out.println("UsageIssWs | answer=" + answer);

            //Thread.sleep(1000);

            answer = support.requestSynch(MsgRobotUtil.turnRightMsg);
            System.out.println("UsageIssWs | answer=" + answer);

            answer = support.requestSynch(MsgRobotUtil.turnLeftMsg);
            System.out.println("UsageIssWs | answer=" + answer);

            answer = support.requestSynch(MsgRobotUtil.forwardMsg);
            System.out.println("UsageIssWs | answer=" + answer);

        }catch( Exception e ){
            System.out.println("UsageIssWs | ERROR " + e);
        }
    }

      public static void main(String args[]){
          UsageIssWs appl = UsageIssWs.create();
          appl.testuseSupport();
      }
}
