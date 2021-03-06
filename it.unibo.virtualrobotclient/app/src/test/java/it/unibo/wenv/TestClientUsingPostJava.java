package it.unibo.wenv;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TestClientUsingPostJava {
    private ClientUsingPost appl;

    @Before
    public void systemSetUp() {
        System.out.println("TestClientUsingPost | setUp: robot should be at HOME-DOWN ");
        //TODO: put the robot at home or design each test properly
        appl = new ClientUsingPost();
    }

    @After
    public void  terminate() {
        System.out.println("%%%  TestClientUsingPost |  terminates ");
    }

    @Test
    public void testMoveLeftRight() {
        System.out.println("TestClientUsingPost | testWork ");
        boolean moveFailed = appl.moveLeft(300);
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveRight(1000);    //back to DOWN
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveStop(100);
        assertTrue( ! moveFailed  );
    }

    @Test
    public void testMoveForwardNoHit() {
        System.out.println("TestClientUsingPost | testMoveForward ");
        boolean moveFailed = appl.moveForward(600);
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveBackward(600);  //back to home
        assertTrue( ! moveFailed  );
    }

    @Test
    public void testMoveForwardHit() {
        System.out.println("TestClientUsingPost | testMoveForward ");
        boolean moveFailed = appl.moveForward(1600);
        assertTrue( moveFailed  );
        moveFailed = appl.moveBackward(1600);       //back to home
        assertTrue( moveFailed  );
    }

}
