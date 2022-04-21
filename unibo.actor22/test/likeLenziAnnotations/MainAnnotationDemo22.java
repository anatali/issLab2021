package likeLenziAnnotations;
 
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.*;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@Context22(name = "demoCtx", host = "localhost",    port = "8080")
@Context22(name = "raspi",   host = "192.168.1.15", port = "8082")
@Actor22(name = "a1",  contextName = "demoCtx", implement=DemoActor22.class)
@Actor22(name = "a2",  contextName = "raspi" )
public class MainAnnotationDemo22 {
    
	public MainAnnotationDemo22() {
        CommSystemConfig.tracing = true;
        AnnotUtil.handleContextDeclaration(this);
        Qak22Context.showActorNames();
    }
    

    public static void main(String[] args) {
        new MainAnnotationDemo22();
        CommUtils.delay(1000);
        
    }
}