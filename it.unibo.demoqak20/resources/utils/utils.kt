package utils

import java.io.File

fun append( line : String ){
	File("taskstorage.txt").appendText("\n$line")
	 
}