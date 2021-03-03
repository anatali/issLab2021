package it.unibo.boundaryWalk;

import it.unibo.interactionExamples.UseRobotArilHeavy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestUseRobotArilHeavy {
    private UseRobotArilHeavy appl;

    @Before
    public void systemSetUp() {
        System.out.println("TestBoundary | setUp: robot should be at HOME-DOWN ");
        appl = UseRobotArilHeavy.create();
    }
    @After
    public void  terminate() {
        System.out.println("%%%  TestWalker |  terminates ");
    }

    @Test
    public void testBoundary() {
        try{
            String result = appl.doBoundary(1, "");
            System.out.println( "testBoundary result:" + result );
            assertTrue( checkJourney(result) );
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
