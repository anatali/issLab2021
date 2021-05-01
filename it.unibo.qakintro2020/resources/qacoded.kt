package resources
import it.unibo.kactor.*

class qacoded( name : String ) : ActorBasic( name ){
 
    init{ 
		println("	$name starts ")
    }
	

    override suspend fun actorBody(msg : ApplMessage){
        println("	$name handles $msg ")
		emit("alarm", "alarm(fire)")
    }
}