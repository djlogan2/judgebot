package chessclub.com.icc.tt;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import chessclub.com.icc.tt.ifac.IDatabaseService;

@SuppressWarnings("unused")
public class TestStatistics {

    private ICCTacticalTrainer tt = new ICCTacticalTrainer(null, null, null);

    private ApplicationContext applicationContext;
    private IDatabaseService serv;
    
    public TestStatistics() {
        applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        serv = (IDatabaseService) applicationContext.getBean("databaseService");
    }

    @Test
    public void testRating() {
        DatabaseStatistics ds = serv.getStatistics();
        System.out.println("Here");
    }
}
