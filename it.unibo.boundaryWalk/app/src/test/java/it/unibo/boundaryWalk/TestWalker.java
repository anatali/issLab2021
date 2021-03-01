package it.unibo.boundaryWalk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestWalker {
    private ClientWebsockJavax appl;

    @Before
    public void systemSetUp() {
        System.out.println("TestWalker | setUp: robot should be at HOME-DOWN ");
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
            assertTrue(  checkJourney(result) );
        }catch( Exception e){
            fail();
        }
    }
    protected boolean checkJourney(String result){
        Pattern pattern = Pattern.compile("w*l");
        Matcher matcher = pattern.matcher(result);
        int n = 0;
        while(matcher.find()) {
            n++;
        }
        return n==4;
    }


}
