package kotlinCode
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.commons.io.IOUtils
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineScope
import it.unibo.supports.IssWsHttpKotlinSupport

import okhttp3.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

object CallRobotUsingWs {
	
	suspend fun test(scope: CoroutineScope){
		val support = IssWsHttpKotlinSupport.createForWs(scope, "localhost:8091" )
		support.wsconnect( testObserver )
		testObserver(scope, support )
	}
	
	val testObserver : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =
        fun(scope, support ) {
 			val obs = SonarObserver("sonarobs", scope)
			support.registerActor( obs )
	}
	
}

@ExperimentalCoroutinesApi
fun main() = runBlocking {
    println("==============================================")
    println("WebSocketUtilUsage | main start ${sysUtil.aboutThreads("main")}"  );
    println("==============================================")
    CallRobotUsingWs.test(this)
 
    println("==============================================")
    println("WebSocketUtilUsage | END ${sysUtil.aboutThreads("main")}");
    println("==============================================")

}
