package chessclub.com.icc.jb;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JudgeBot {

	final String CONTEXTPATH = "/JudgeBot";
	
//    public static void main(String args[]) throws Exception {
//        JudgeBot b = new JudgeBot();
//        b.run();
//    }
//
//    private void run() throws Exception {
//        Server server = new Server(8090);
//    	server.setHandler(new WebAppContext("WebContent", CONTEXTPATH));
//        server.start();
//    }
    public static void main(String args[]) throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        Thread runningBean = (Thread) applicationContext.getBean("runningBean");
        runningBean.join();
    }

    @SuppressWarnings("unused")
    private void run() throws Exception {
        Server server = new Server(8090);
        server.setHandler(new WebAppContext("WebContent", CONTEXTPATH));
        server.start();
    }

}
