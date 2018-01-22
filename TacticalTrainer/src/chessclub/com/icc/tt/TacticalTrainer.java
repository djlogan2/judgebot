package chessclub.com.icc.tt;

// This is not a tactic:
// This seems to be a possible tactic: 445,-445  : 2R4k/1p4p1/4p3/4Pn1p/1r2pr2/1P1p3P/P5P1/3R1NK1 b - - : h8h7,bom
// This seems to be a possible tactic: 691,-691  : 2R5/1p4pk/4p3/4P2p/3r1r2/1P1pn2P/P2R2P1/6K1 b - - : f4f1,e3f1
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.webapp.WebAppContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TacticalTrainer {

    @SuppressWarnings("unused")
    private static final Logger log = LogManager.getLogger(TacticalTrainer.class);

    final String CONTEXTPATH = "/TacticalTrainer";
    
    public static void main(String args[]) throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        Thread runningBean = (Thread) applicationContext.getBean("runningBean");
        runningBean.join();
    }
/*
 *  We are not using jetty at this time
 *  
    @SuppressWarnings("unused")
    private void run() throws Exception {
        Server server = new Server(8090);
        server.setHandler(new WebAppContext("WebContent", CONTEXTPATH));
        server.start();
    }
*/
}
