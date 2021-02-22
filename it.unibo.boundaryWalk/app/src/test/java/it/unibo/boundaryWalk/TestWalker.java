package it.unibo.boundaryWalk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestWalker {
    private ClientWebsockJavax appl;

    @Before
    public void systemSetUp() {
        System.out.println("TestClientUsingPost | setUp: robot should be at HOME-DOWN ");
        appl = new ClientWebsockJavax("localhost:8091");
    }
    @After
    public void  terminate() {
        System.out.println("%%%  TestWalker |  terminates ");
    }

    @Test
    public void testWalk() {
        System.out.println( "Journay at start:" + appl.getJourney() );
        try{
            String result = appl.doBoundaryWalk();
            System.out.println( "Journay result:" + result );
            assertTrue( result.equals("wwwlwwwlwwwlwwwl")  );
        }catch( Exception e){
            fail();
        }

        //try{ Thread.sleep(15000); }catch( Exception e){}

        System.out.println("Journay at end:" + appl.getJourney());

    }


}
