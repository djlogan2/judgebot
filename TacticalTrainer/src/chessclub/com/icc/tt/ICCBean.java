package chessclub.com.icc.tt;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.tt.ifac.IDatabaseService;

public class ICCBean implements Runnable {
    @SuppressWarnings("unused")
    private static final Logger log = LogManager.getLogger(ICCBean.class);
    private boolean ending = false;
    public void end() { ending = true; }
    
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    public int getUciThreads() {
    	return this.uciThreads;
    }
    public int getuciInTacticTime() {
    	return this.uciInTacticTime;
    }
    public int getuciNotInTacticTime() {
    	return this.uciNotInTacticTime;
    }
    public String getUciProgram() {
        return this.uciProgram;
    }
    public void setUciProgram(String uciProgram) {
        this.uciProgram = uciProgram;
    }
    public String getUciBookFile() {
        return this.uciBookFile;
    }
    public void setUciBookFile(String uciBookFile) {
        this.uciBookFile = uciBookFile;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setUciThreads(int threads) {
    	this.uciThreads = threads;
    }
    public void setUciInTacticTime(int time) {
    	this.uciInTacticTime = time;
    }
    public void setUciNotInTacticTime(int time) {
    	this.uciNotInTacticTime = time;
    }
    public void setTrainerUserid(String userid) {
        this.trainerUserid = userid;
    }
    public void setTrainerPassword(String password) {
        this.trainerPassword = password;
    }
    public void setCollectorUserid(String userid) {
        this.collectorUserid = userid;
    }
    public void setCollectorPassword(String password) {
        this.collectorPassword = password;
    }
    public void setSocketBufferSize(int socketBufferSize) {
        this.socketBufferSize = socketBufferSize;
    }

//    public void setICCHandler(ICCTacticalTrainer handler) {
//        this.handler = handler;
//    }
    
    private void checkEnvironmentVariables() {
        if(System.getenv("ICC_HOST") != null)
            host = System.getenv("ICC_HOST");
        if(System.getenv("ICC_PORT") != null)
            port = Integer.parseInt(System.getenv("ICC_PORT"));
        if(System.getenv("ICC_UCITHREADS") != null)
            uciThreads = Integer.parseInt(System.getenv("ICC_UCITHREADS"));
        if(System.getenv("ICC_UCIINTACTICTIME") != null)
            uciInTacticTime = Integer.parseInt(System.getenv("ICC_UCIINTACTICTIME"));
        if(System.getenv("ICC_UCINOTINTACTICTIME") != null)
            uciNotInTacticTime = Integer.parseInt(System.getenv("ICC_UCINOTINTACTICTIME"));
        if(System.getenv("ICC_TRAINERUSERID") != null)
            trainerUserid = System.getenv("ICC_TRAINERUSERID");
        if(System.getenv("ICC_TRAINERPASSWORD") != null)
            trainerPassword = System.getenv("ICC_TRAINERPASSWORD");
        if(System.getenv("ICC_COLLECTORUSERID") != null)
            collectorUserid = System.getenv("ICC_COLLECTORUSERID");
        if(System.getenv("ICC_COLLECTORPASSWORD") != null)
            collectorPassword = System.getenv("ICC_COLLECTORPASSWORD");
        if(System.getenv("ICC_SOCKETBUFFERSIZE") != null)
            socketBufferSize = Integer.parseInt(System.getenv("ICC_SOCKETBUFFERSIZE"));
        if(System.getenv("ICC_BUFFERSIZE") != null)
            bufferSize = Integer.parseInt(System.getenv("ICC_BUFFERSIZE"));
        if(System.getenv("ICC_UCIPROGRAM") != null)
            uciProgram = System.getenv("ICC_UCIPROGRAM");
        if(System.getenv("ICC_UCIBOOKFILE") != null)
            uciBookFile = System.getenv("ICC_UCIBOOKFILE");
    }
    private static ICCInstance trainerIcc;
    private static ICCInstance collectorIcc;
    private ICCTacticalTrainer trainerHandler;
    private ICCTacticsCollector collectorHandler;
    private String host;
    private int port;
    private String trainerUserid;
    private String collectorUserid;
    private String trainerPassword;
    private String collectorPassword;
    private int socketBufferSize;
    private static boolean haveOne = false;
    private int bufferSize;
    private String uciProgram;
    private String uciBookFile;
    private int uciThreads = 1;
    private int uciInTacticTime = 1;
    private int uciNotInTacticTime = 1;
    
    @Autowired
    MessageSource messageSource;
    
    @Autowired
    IDatabaseService databaseService;
    
    @PostConstruct
    private void startICC() throws Exception {
        if(ICCBean.haveOne)
            throw new Exception("An instance of ICCBean has already been constructed");

        ICCBean.haveOne = true;
        
        checkEnvironmentVariables();

        trainerHandler = new ICCTacticalTrainer(messageSource, databaseService, this);
        trainerIcc = new ICCInstance();
        trainerIcc.setInstance(host, port, trainerUserid, trainerPassword, trainerHandler, socketBufferSize); 
        
        collectorHandler = new ICCTacticsCollector(messageSource, databaseService, this, bufferSize, uciProgram, uciBookFile);
        collectorIcc = new ICCInstance();
        collectorIcc.setInstance(host, port, collectorUserid, collectorPassword, collectorHandler, socketBufferSize); 

    }

    @Override
    public void run() {
        while(!ending) {
            trainerIcc.start();
            collectorIcc.start();
            try {
                trainerIcc.join();
                collectorIcc.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    };
}
