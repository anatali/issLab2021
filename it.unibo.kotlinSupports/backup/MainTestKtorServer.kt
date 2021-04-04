package it.unibo.executors

import it.unibo.actor0.sysUtil
import it.unibo.supports.IssWsHttpKotlinSupport
import it.unibo.supports.WebSocketKotlinSupportUsage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainTestKtorServer {


}

@ExperimentalCoroutinesApi
fun main() = runBlocking {
    println("==============================================")
    println("MainTestKtorServer | start ${sysUtil.aboutThreads("main")}"  );
    println("==============================================")
    val support = IssWsHttpKotlinSupport.getConnectionWs(this, "echo.websocket.org" ) //localhost:8030"
    support.registerActor( NaiveObserver("obs", this))

    //support.wsconnect( fun(scope, support ) {println("test | connected ")} )

    support.sendWs("chat/hello")
    println("==============================================")
    println("MainTestKtorServer | BEFORE END ${sysUtil.aboutThreads("main")}" );
    println("==============================================")

    //give time to see messages ...
    delay(3000)  //CREATE new threads  !!!
    support.close()

    println("==============================================")
    println("WebSocketUtilUsage | END ${sysUtil.aboutThreads("main")}");
    println("==============================================")

}