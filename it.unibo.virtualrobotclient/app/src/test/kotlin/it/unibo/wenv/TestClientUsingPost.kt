package it.unibo.wenv

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TestClientUsingPost {
    private lateinit var appl: ClientUsingPost
    @Before
    fun systemSetUp() {
        println("TestClientUsingPost | setUp: robot should be at HOME-DOWN ")
        //TODO: put the robot at home or design each test properly
        appl = ClientUsingPost()
    }

    @After
    fun terminate() {
        println("%%%  TestClientUsingPost |  terminates ")
    }

    //@Test     //done in Java
    fun testMoveLeftRight() {
        println("TestClientUsingPost | testWork ")
        var moveFailed = appl.moveLeft(300)
        Assert.assertTrue(!moveFailed)
        moveFailed = appl.moveRight(1000) //back to DOWN
        Assert.assertTrue(!moveFailed)
        moveFailed = appl.moveStop(100)
        Assert.assertTrue(!moveFailed)
    }

    //@Test     //done in Java
    fun testMoveForwardNoHit() {
        println("TestClientUsingPost | testMoveForward ")
        var moveFailed = appl.moveForward(600)
        Assert.assertTrue(!moveFailed)
        moveFailed = appl.moveBackward(600) //back to home
        Assert.assertTrue(!moveFailed)
    }

    //@Test     //done in Java
    fun testMoveForwardHit() {
        println("TestClientUsingPost | testMoveForward ")
        var moveFailed = appl.moveForward(1600)
        Assert.assertTrue(moveFailed)
        moveFailed = appl.moveBackward(1600) //back to home
        Assert.assertTrue(moveFailed)
    }

    //@Test
    fun testBoundary() {
        println("TestClientUsingPost | testBoundary ")
        var numOfSteps = appl.boundary()
        Assert.assertTrue(numOfSteps == 4)
    }

    @Test
    fun anothertestBoundary() {
        println("TestClientUsingPost | testBoundary ")
        var numOfSteps = ClientUsingPost.main(arrayOf<String>())
        Assert.assertTrue(ClientUsingPost.finalNumOfSteps == 4)
    }

}