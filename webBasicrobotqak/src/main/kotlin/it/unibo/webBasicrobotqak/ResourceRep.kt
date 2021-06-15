package it.unibo.webBasicrobotqak

import kotlinx.coroutines.channels.Channel

class ResourceRep {
    var robotanswerChannel = Channel<String>()

    companion object{
        var myself : ResourceRep? = null

        fun getResourceRep() : ResourceRep{
            if( myself == null ){
                myself = ResourceRep()
            }
            return myself!!
        }


    }

 }