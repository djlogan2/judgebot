package chessclub.com.icc.jb.icc;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.jb.timeDelay;
import chessclub.com.icc.jb.UCIThreadManager;
import chessclub.com.icc.jb.ifac.IDatabaseService;

public class ICCBean implements Runnable {
    private static final Logger log = LogManager.getLogger(ICCBean.class);
    private boolean ending = false;
    public void end() { ending = true; }
    
    public boolean getMessageWinner() { return messageWinner; }
    public boolean getMessageLoser() { return messageLoser; }
    public boolean getTellWinner() { return tellWinner; }
    public boolean getTellLoser() { return tellLoser; }
    public boolean getMessageAbort() { return messageAbort; }
    public boolean getTellAbort() { return tellAbort; }
    public boolean getMessageDraw() { return messageDraw; }
    public boolean getTellDraw() { return tellDraw; }
    public void setMessageAbort(boolean messageAbort) {
        this.messageAbort = messageAbort;
    }
    public void setMessageDraw(boolean messageDraw) {
        this.messageDraw = messageDraw;
    }
    public void setMessageWinner(boolean messageWinner) {
        this.messageWinner = messageWinner;
    }
    public void setMessageLoser(boolean messageLoser) {
        this.messageLoser = messageLoser;
    }
    public void setTellAbort(boolean tellAbort) {
        this.tellAbort = tellAbort;
    }
    public void setTellDraw(boolean tellDraw) {
        this.tellDraw = tellDraw;
    }
    public void setTellWinner(boolean tellWinner) {
        this.tellWinner = tellWinner;
    }
    public void setTellLoser(boolean tellLoser) {
        this.tellLoser = tellLoser;
    }
    public void setTimeDelay(int _timeDelay) {
        this._timeDelay = _timeDelay;
    }
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    public void setMaxEngines(int maxEngines) {
        this.maxEngines = maxEngines;
    }
    public void setUciProgram(String uciProgram) {
        this.uciProgram = uciProgram;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setSocketBufferSize(int socketBufferSize) {
        this.socketBufferSize = socketBufferSize;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
    
    public void setICCHandler(ICCHandler handler) {
        this.handler = handler;
    }
    
    public void setTestOnly(boolean testOnly) {
        String allowProduction = System.getenv("ALLOW_PRODUCTION");
        if(allowProduction != null && "allow".equals(allowProduction.toLowerCase()))
            this.testOnly = testOnly;
        else {
            log.info("Forcing testOnly to TRUE because environment value ALLOW_PRODUCTION is not set to 'allow'");
            this.testOnly = true;
        }
    }
    
    private void checkEnvironmentVariables() {
        if(System.getenv("ICC_HOST") != null)
            host = System.getenv("ICC_HOST");
        if(System.getenv("ICC_PORT") != null)
            port = Integer.parseInt(System.getenv("ICC_PORT"));
        if(System.getenv("ICC_USERID") != null)
            userid = System.getenv("ICC_USERID");
        if(System.getenv("ICC_PASSWORD") != null)
            password = System.getenv("ICC_PASSWORD");
        if(System.getenv("ICC_SOCKETBUFFERSIZE") != null)
            socketBufferSize = Integer.parseInt(System.getenv("ICC_SOCKETBUFFERSIZE"));
        if(System.getenv("ICC_TIMEDELAY") != null)
            _timeDelay = Integer.parseInt(System.getenv("ICC_TIMEDELAY"));
        if(System.getenv("ICC_BUFFERSIZE") != null)
            bufferSize = Integer.parseInt(System.getenv("ICC_BUFFERSIZE"));
        if(System.getenv("ICC_MAXENGINES") != null)
            maxEngines = Integer.parseInt(System.getenv("ICC_MAXENGINES"));
        if(System.getenv("ICC_UCIPROGRAM") != null)
            uciProgram = System.getenv("ICC_UCIPROGRAM");
        if(System.getenv("ICC_ADMINPASSWORD") != null)
            adminPassword = System.getenv("ICC_ADMINPASSWORD");
        if(System.getenv("ICC_MESSAGEWINNER") != null)
            messageWinner = Boolean.parseBoolean(System.getenv("ICC_MESSAGEWINNER"));
        if(System.getenv("ICC_MESSAGELOSER") != null)
            messageLoser = Boolean.parseBoolean(System.getenv("ICC_MESSAGELOSER"));
        if(System.getenv("ICC_TELLLOSER") != null)
            tellLoser = Boolean.parseBoolean(System.getenv("ICC_TELLLOSER"));
        if(System.getenv("ICC_TELLWINNER") != null)
            tellWinner = Boolean.parseBoolean(System.getenv("ICC_TELLWINNER"));
        if(System.getenv("ICC_MESSAGEABORT") != null)
            messageAbort = Boolean.parseBoolean(System.getenv("ICC_MESSAGEABORT"));
        if(System.getenv("ICC_TELLABORT") != null)
            tellAbort = Boolean.parseBoolean(System.getenv("ICC_TELLABORT"));
        if(System.getenv("ICC_MESSAGEDRAW") != null)
            messageDraw = Boolean.parseBoolean(System.getenv("ICC_MESSAGEDRAW"));
        if(System.getenv("ICC_TELLDRAW") != null)
            tellDraw = Boolean.parseBoolean(System.getenv("ICC_TELLDRAW"));
        if(System.getenv("ICC_TESTONLY") != null)
            this.setTestOnly(Boolean.parseBoolean(System.getenv("ICC_TESTONLY")));
    }
    private boolean testOnly = true;
    private static ICCInstance icc;
    private ICCHandler handler;
    private String host;
    private int port;
    private String userid;
    private String password;
    private int socketBufferSize;
    private static boolean haveOne = false;
    private int _timeDelay;
    private int bufferSize;
    private int maxEngines;
    private String uciProgram;
    private String adminPassword;
    private boolean messageWinner;
    private boolean messageLoser;
    private boolean tellWinner;
    private boolean tellLoser;
    private boolean messageAbort;
    private boolean tellAbort;
    private boolean messageDraw;
    private boolean tellDraw;
    
    @Autowired
    MessageSource messageSource;
    
    @Autowired
    IDatabaseService databaseService;
    
    //public void setDb(IDatabaseService db) { databaseService = db; }
    //public void setMs(MessageSource ms) { messageSource = ms; }
    
    @PostConstruct
    private void startICC() throws Exception {
        if(ICCBean.haveOne)
            throw new Exception("An instance of ICCBean has already been constructed");

        ICCBean.haveOne = true;
        
        checkEnvironmentVariables();
        UCIThreadManager.setBufferSize(bufferSize);
        UCIThreadManager.setMaxEngines(maxEngines);
        UCIThreadManager.setUciProgram(uciProgram);

        handler = new ICCHandler(messageSource, databaseService, this, testOnly, adminPassword);

        timeDelay.getInstance().setTimeDelay(_timeDelay);
        timeDelay.getInstance().setInterface(handler);
        
        icc = new ICCInstance();
        icc.setInstance(host, port, userid, password, handler, socketBufferSize); 
    }

    @Override
    public void run() {
        while(!ending) {
            icc.start();
            try {
                icc.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    };
}
