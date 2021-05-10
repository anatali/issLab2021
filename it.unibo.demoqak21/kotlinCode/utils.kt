package kotlinCode

import java.io.File

object utils{
	fun append( line : String ){
		File("taskstorage.txt").appendText("\n$line")
		 
	}
}