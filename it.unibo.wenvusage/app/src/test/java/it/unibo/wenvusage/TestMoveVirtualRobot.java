package it.unibo.wenvusage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestMoveVirtualRobot {
    private MoveVirtualRobot appl;

    @Before
    public void systemSetUp() {
        System.out.println("TestMoveVirtualRobot | setUp: robot should be at HOME-DOWN ");
        appl = new MoveVirtualRobot();
    }

    @After
    public void  terminate() {
        System.out.println("%%%  TestMoveVirtualRobot |  terminates ");
    }

    @Test
    public void testMovesNoFailing() {
        System.out.println("TestMoveVirtualRobot | testWork ");
        boolean moveFailed = appl.moveLeft(300);
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveRight(1000);    //back to DOWN
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveStop(100);
        assertTrue( ! moveFailed  );
    }

    @Test
    public void testMoveForwardNoHit() {
        System.out.println("TestMoveVirtualRobot | testMoveForward ");
        boolean moveFailed = appl.moveForward(600);
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveBackward(600);  //back to home
        assertTrue( ! moveFailed  );
    }

    //@Test
    public void testMoveForwardHit() {
        System.out.println("TestMoveVirtualRobot | testMoveForward ");
        boolean moveFailed = appl.moveForward(1600);
        assertTrue( moveFailed  );
        moveFailed = appl.moveBackward(1600);       //back to home
        assertTrue( moveFailed  );
    }

}
