import alice.tuprolog.Term
import alice.tuprolog.Struct

object applsupport {
	 
	fun getSonarDistance( s : String ) : Int{
		//s = sonarrobot(D)
		val ts =  Term.createTerm(s) as Struct
		val d  =  ts.getArg(0).toString().toInt()
		//println(d)
		return d
	}
}