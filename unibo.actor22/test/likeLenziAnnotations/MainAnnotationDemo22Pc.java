package likeLenziAnnotations;
 
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.*;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

/*
 * TestPlan: l'attore a1 deve ricevere il messaggio msg(demo,dispatch,a2,a3Pc,do,1)
 */
@Context22(name = "pcCtx", host = "localhost",    port = "8080")
@Context22(name = "raspCtx",   host = "127.0.0.1", port = "8082")
@Actor22(name = "a1",     contextName = "pcCtx", implement=A1Actor22OnPc.class)
@Actor22(name = "a3Pc",   contextName = "pcCtx", implement=A3Actor22.class )
@Actor22(name = "a2",     contextName = "raspCtx" )
@Actor22(name = "a3Rasp", contextName = "raspCtx" )

public class MainAnnotationDemo22Pc {
    
	public MainAnnotationDemo22Pc() {
        CommSystemConfig.tracing = false;
        Qak22Context.configureTheSystem(this);
        Qak22Context.showActorNames();
    }
    

    public static void main(String[] args) {
        new MainAnnotationDemo22Pc();
        CommUtils.delay(1000);
        
    }
}