package it.unibo.boundaryWalk;

import it.unibo.robotAppls.RobotApplicationStarter;
import it.unibo.robotAppls.UseRobotAril;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestRobotAril {
    private UseRobotAril appl;

    @Before
    public void systemSetUp() {
        System.out.println("TestRobotAril | setUp: robot should be at HOME-DOWN ");
        System.out.println("WARNING The configuration files should be under DIRECTORY" + System.getProperty("user.dir"));
        Object obj = RobotApplicationStarter.createInstance(UseRobotAril.class);
        appl = (UseRobotAril) obj;

    }
    @After
    public void  terminate() {
        System.out.println("%%%  TestRobotAril |  terminates ");
    }

    @Test
    public void TestRobotAril() {
        try{
            String result = appl.doBoundaryLogic( );
            System.out.println( "TestRobotAril result:" + result );
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
