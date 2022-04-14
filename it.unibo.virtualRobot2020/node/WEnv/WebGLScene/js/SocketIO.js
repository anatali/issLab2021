import eventBus       from './eventBus/EventBus.js'
import eventBusEvents from './eventBus/events.js'

import { parseConfiguration, 
   updateSceneConstants, getinitialsceneConstants } from './utils/SceneConfigUtils.js' //DEC 2019
import sceneConfiguration from '../sceneConfig.js'                                     //DEC 2019
import SceneManager from './SceneManager.js'                                           //no more DEC 2019

export default (onKeyUp, onKeyDown, myonKeyDown) => {
    const socket = io()
        
    socket.on( 'moveForward',  (duration, moveindex)  => {moveForward(duration,moveindex)})
    socket.on( 'moveBackward', (duration, moveindex) => moveBackward(duration,moveindex) )
    socket.on( 'turnRight',    (duration, moveindex) => turnRight(duration,moveindex) )
    socket.on( 'turnLeft',     (duration, moveindex) => { turnLeft(duration,moveindex) })
    socket.on( 'alarm',        stopMoving    )
    socket.on( 'remove',       name => remove( name )  )   //DEC 2019  See WebpageServer.js
    
	socket.on( 'xxx',        obj => console.log("SocketIO xxxxxxxxxxxxxxxxxxxxxxxx")   )
		 
	
    socket.on( 'disconnect', () => console.log("server disconnected") )

    eventBus.subscribe( eventBusEvents.sonarActivated, sonarId =>
        socket.emit('sonarActivated', sonarId))
    eventBus.subscribe( eventBusEvents.collision, objectName => { 
		console.log(`SocketIO collision: ${objectName}`);
		socket.emit('collision', objectName); 	//va al callback del main.js
		stopMoving(); 
	})
   eventBus.subscribe( eventBusEvents.endmove, objectName => {
 		console.log(`SocketIO eventbus endmove: ${objectName}`);
 		socket.emit('endmove', objectName);
   })

    const keycodes = {
        W: 87,
        A: 65,
        S: 83,
        D: 68,
        R: 82,
        F: 70,
        Z: 90 //April2022: lo uso per stop rotation
    }
        
    let moveForwardTimeoutId
    let moveBackwardTimeoutId

    function moveForward(duration,moveindex) {
        clearTimeout(moveForwardTimeoutId)
        onKeyDown( { keyCode: keycodes.W },5000,true )
        if(duration >= 0) moveForwardTimeoutId = setTimeout( () => {
        							onKeyUp( { keyCode: keycodes.W } );
        							//NON emetto segnali al termine della mossa perchè
        							//ci potrebbe essere stato un ostacolo
          							//eventBus.post(eventBusEvents.endmove, "moveForward-base-"+moveindex)
          							eventBus.post(eventBusEvents.endmove, moveindex)
         							//console.log("SocketIO: moveForward done");
         						}, duration )
    }

    function moveBackward(duration,moveindex) {
        clearTimeout(moveBackwardTimeoutId)
        onKeyDown( { keyCode: keycodes.S },5000,true )
        if(duration >= 0) moveBackwardTimeoutId = setTimeout( () => {
        							onKeyUp( { keyCode: keycodes.S } )
        							//eventBus.post(eventBusEvents.endmove, "moveBackward-base-"+moveindex)
        							eventBus.post(eventBusEvents.endmove,  moveindex)
        						}, duration )
    }

    function turnRight(duration,moveindex) {
	console.log("turnRight from SocketIO "  )
//induce il movimento simulando onKeyDown 
        onKeyDown( { keyCode: keycodes.D }, duration, true )	//remote=true onKeyDown is in SceneManager
        eventBus.post(eventBusEvents.endmove,  moveindex)
    }

    function turnLeft(duration,moveindex) {
    	console.log("turnLeft from SocketIO moveindex=" + moveindex )
        onKeyDown( { keyCode: keycodes.A }, duration, true )
        eventBus.post(eventBusEvents.endmove,  moveindex)

    }

    function stopMoving() {
    console.log("stopMoving "  )
       onKeyUp( { keyCode: keycodes.W } )
       onKeyUp( { keyCode: keycodes.S } )
       onKeyDown( { keyCode: keycodes.Z }, 0, true )
        //stopRotating()  //from SceneManager
    }
    
//DEC 2019    
   function remove( objname ) {  //called by line 16
   		sceneConfiguration.staticObstacles.forEach( v  => {
   			//console.log(" ... "+ v.name)
   			if( v.name == objname ) {
		      console.log(" SocketIo remove " + v.name  )
		      v.centerPosition.x = 0
		      v.centerPosition.y = 0
		 	  v.size.x = 0
			  v.size.y = 0
		      //console.log(  " SocketIo remove " + v.size.x )       
		      const sceneConstants = getinitialsceneConstants()
		      updateSceneConstants(sceneConstants, parseConfiguration(sceneConfiguration) )			
   			}
   		})
   	  
  }//remove

}