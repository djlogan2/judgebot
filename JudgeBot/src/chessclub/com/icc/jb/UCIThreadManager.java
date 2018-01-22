package chessclub.com.icc.jb;

import chessclub.com.icc.jb.icc.AdjudicateBase;
import chessclub.com.icc.uci.UCIEngine;

import java.util.Stack;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class UCIThreadManager {

    public static void setBufferSize(int bufferSize) {
        UCIThreadManager.bufferSize = bufferSize;
    }
    public static void setUciProgram(String uciProgram) {
        UCIThreadManager.uciProgram = uciProgram;
    }
    private static final Logger log = LogManager.getLogger(UCIThreadManager.class);
    private static int bufferSize = 0;
    private static String uciProgram = null;
    private static int maximumThreads = 0;
    private static Stack<AdjudicateBase> uciRequestStack = new Stack<AdjudicateBase>();
    private static int currentThreads = 0;
    private static int maxQueueSize = 0;

    public static int activeEngines() { return currentThreads; }
    public static int maxEngines() { return maximumThreads; }

    synchronized static public void getEngine(AdjudicateBase handler) {
        if (currentThreads >= maximumThreads) {
            if (log.isDebugEnabled())
                log.debug("Adding handler "+handler+" to queue");
            uciRequestStack.add(handler);
            maxQueueSize = Math.max(maxQueueSize, uciRequestStack.size());
            return;
        }
        currentThreads++;
        if (log.isDebugEnabled())
            log.debug("Creating new engine, number active engine is now "+currentThreads);
        UCIEngine engine = new UCIEngine(bufferSize, uciProgram, handler, false);
        engine.start();
        return;
    }

    synchronized static public void setMaxEngines(int newmax) {
        maximumThreads = newmax;
        while(currentThreads < maximumThreads && !uciRequestStack.isEmpty()) {
            AdjudicateBase qhandler = uciRequestStack.pop();
            if (log.isDebugEnabled())
                log.debug("Creating new engine for now unqueued handler "+qhandler+", number active engine is now "+currentThreads);
            UCIEngine engine = new UCIEngine(bufferSize, uciProgram, qhandler, false);
            engine.start();
            return;
        }
    }
    
    synchronized static public void endEngine() {
        if (!uciRequestStack.isEmpty()) {
            AdjudicateBase qhandler = uciRequestStack.pop();
            if (log.isDebugEnabled())
                log.debug("Creating new engine for now unqueued handler "+qhandler+", number active engine is now "+currentThreads);
            UCIEngine engine = new UCIEngine(bufferSize, uciProgram, qhandler, false);
            engine.start();
            return;
        } else {
            currentThreads--;
            if (log.isDebugEnabled())
                log.debug("Number active engine is now "+currentThreads);
        }
    }
    
    public static int getCurrentQueueSize() { return uciRequestStack.size(); }
    public static int getMaxQueueSize() { return maxQueueSize; }

}
