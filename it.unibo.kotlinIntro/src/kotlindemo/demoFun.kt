package kotlindemo

fun fsum(a:Int, b:Int) : Int {
  return a+b
}

var fcounter = 0
fun incCounter() : Unit { fcounter++ }
fun decCounter() { fcounter-- }

fun fsquare(v: Int) = v * v		//oneline function


val ftaction : () -> Unit
					= { println("hello") } 		//lambda expression
val ftsum : ( Int,  Int) -> Int
					= {  x:Int, y:Int -> x+y }  //lambda expression

val ftgreet: (String ) -> ()->Unit
					= {  m: String -> { println(m)}   }

val fva = ftsum(1,2)

val fel = {  print( "Last exp val:" ); 100  }


//val faction: ()-> Unit   = fun() { println("Hello from faction") }
val faction  = fun() { println("Hello from faction") }
//val fsquare: (Int)->Int  = fun(x) = x * x
val fsquare  = fun(x:Int) = x * x
//val greet: (String) -> () -> Unit = fun(m:String) = fun(){ println("Printing $m") }
val greet = fun(m:String) = fun(){ println("Printing $m") }

fun fexec23( op:(Int, Int)->Int ) : Int { return op(2,3) }  

//--------------------------------------------------------------
fun funDemoWork(){
	println("Hello from funDemoWork")
	println(  fsum(3,6) )	   	//9
	
	println( "pre=$fcounter  " ) //pre=0
	incCounter()
	println( "post=$fcounter " ) //post=1

	println(  fsquare(3)  ) 	//9
	
	ftaction()		//hello
	
	println("fva=$fva")	      //fva=3
	
	
	ftgreet( "Hello World" )() 	//Hello World
	
	println( "${fel()}" )  		//Last exp val=100
	
 	println( { println( "Welcome" ) } )			//() -> kotlin.Unit
 	println( { println( "Welcome" ) }() )		//Welcome  kotlin.Unit
	
println("Anonymous functions ----------")	
	faction() 								 //Hello from faction
	println("fsquare=${fsquare(3)}")		 //fsquare=9
	println( greet )						//(kotlin.String) -> () -> kotlin.Unit
	println( greet( "Hello World1" ) )		//() -> kotlin.Unit
	greet( "Hello World1" ) 				// ???
	greet( "Hello World2" )( ) 				//Printing Hello World
	
	println( fun(x:Int,y:Int):Int {return x+y} )	//(kotlin.Int, kotlin.Int) -> kotlin.Int
  	val v23 = fexec23( fun(x:Int,y:Int):Int {return x+y} )		 
  	println("v23=$v23")          //v23=5
 	println( fexec23( { x:Int, y:Int -> x*y } )	 )	 	//6
 }

fun main() {
    println("BEGINS CPU=$cpus ${curThread()}")
	
    println( "work done in time= ${measureTimeMillis(  { funDemoWork() } )}"  )
	
    println("ENDS ${curThread()}")
}