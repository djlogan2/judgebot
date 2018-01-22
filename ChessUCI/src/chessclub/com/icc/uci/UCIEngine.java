package chessclub.com.icc.uci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.MoveList;

public final class UCIEngine extends Thread {

    private static final Logger log = LogManager.getLogger(UCIEngine.class);
    private String location = null;
    private UCIRequest request = null;
    private Process process = null;
    private OutputStream toengine = null;
    private InputStream stderr = null;
    private InputStream fromengine = null;
    private int bufferSize = 4096;
    private HashMap<String, Option> options = new HashMap<String, Option>();
    private boolean ending = false;
    private boolean lowpriority = false;
    //private String waitingCommand;
    private LinkedList<String> waitingCommand = new LinkedList<String>();
    
    private long analysisStarted;
    private SearchParameters analysisParameters;
    private Thread timeChecker = null;
    private Integer calculateCounter = 0;
    private boolean forcestop = false;
    private String calculateRunning = null;
    
    public UCIEngine(int pbufferSize, String uciProgram, UCIRequest prequest, boolean lowpriority) {
        location = uciProgram;
        this.request = prequest;
        this.bufferSize = pbufferSize;
        this.lowpriority = lowpriority;
    }

    @Override
    public final String toString() {
        return "UCIEngine " + location;
    }

    public final void writeCommand(String command) {
    	//waitingCommand = command;
    	synchronized(waitingCommand) {
    		waitingCommand.add(command);
	    	log.debug("writeCommand saving command '" + command + "' and sending isready");
	    	if(waitingCommand.size() == 1)
	    		_writeCommand("isready");
    	};
    }
    
    private synchronized void _writeCommand(String command) {
        try {
        	if("stop".equals(command) && !forcestop) {
        		log.debug("Skipping the writing of 'stop' because we are not currently trying to force a stop");
        		return;
        	};
            log.debug("writeCommand(" + command + ")");
            toengine.write((command + "\n").getBytes());
            toengine.flush();
        } catch (IOException e) {
            log.error("UCIEngine::writeCommand IOException " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cleanup() {
        String line;
        try {
            toengine.close();
            // clean up if any output in stdout
            BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(fromengine));
            while ((brCleanUp.readLine()) != null) {
            }
            brCleanUp.close();
            fromengine.close();

            // clean up if any output in stderr
            brCleanUp = new BufferedReader(new InputStreamReader(stderr));
            while ((line = brCleanUp.readLine()) != null) {
                log.debug("UCIEngine::quit, stderr from engine: " + line);
            }
            brCleanUp.close();
            stderr.close();
        } catch (IOException e) {
            log.error("UCIEngine::quit IOException " + e.getMessage());
            e.printStackTrace();
        }
    }

    public final void quit() {
        log.debug("quit()");
        ending = true;
        writeCommand("quit");
    }

    @Override
    public final void run() {
    	Thread.currentThread().setName("UCI Engine");
    	log.debug("UCIEngine running");
        try {
        	if(lowpriority)
        		location = "nice -15 " + location;
            process = Runtime.getRuntime().exec(location);
            toengine = process.getOutputStream();
            stderr = process.getErrorStream();
            fromengine = process.getInputStream();
            String currentString = "";

            toengine.write("uci\n".getBytes());
            toengine.flush();

            byte[] bytebuffer = new byte[bufferSize];
            int nRead;
            nRead = fromengine.read(bytebuffer);

            while (nRead != -1) {
                for (int i = 0; i < nRead; i++) {
                    if (bytebuffer[i] == 10 || bytebuffer[i] == 13) {
                        if (currentString.length() > 0) {
                            parse(currentString);
                            currentString = "";
                        }
                    } else {
                        currentString += (char) bytebuffer[i];
                    }
                }
                nRead = fromengine.read(bytebuffer);
            }
        } catch (IOException e) {
            if (!ending) {
                log.error("UCIEngine::run IOException " + e.getMessage());
                e.printStackTrace();
                quit();
            } else if (!"Stream closed".equals(e.getMessage())) {
                log.error("UCIEngine::run IOException " + e.getMessage());
                e.printStackTrace();
            }
        }
        cleanup();
        request.engineStopping();
        log.debug("UCIEngine ending");
    }

    // 16:31:09,117 Thread-7 AdjudicateBase - Unknown UCI line:
    Pattern id = Pattern.compile("id\\s+(name|author)??\\s+(.*)");
    // option name New Game type button
    Pattern option_button = Pattern.compile("option\\s+name\\s+(.+?)\\s+button");
    Pattern option_string = Pattern.compile("option\\s+name\\s+(.+)\\s+type\\s+string\\s+default\\s+(.+)");
    Pattern option_check = Pattern.compile("option\\s+name\\s+(.+)\\s+type\\s+check\\s+default\\s+(true|false)");
    Pattern option_spin = Pattern.compile("option\\s+name\\s+(.+)\\s+type\\s+spin\\s+default\\s+(-?\\d+)\\s+min\\s+(-?\\d+)\\s+max\\s+(-?\\d+)");
    Pattern option_combo = Pattern.compile("option\\s+name\\s+(.+)\\s+type\\s+combo\\s+default\\s+(\\.+?)(\\s+var\\s+(.+?))*");
    //Pattern option = Pattern.compile("option\\s+name\\s+(.+?)\\s+(?:type\\s+(.+?)\\s+)??(?:default\\s+(.+?))??(?:min\\s+(.+?))??(?:max\\s+(.+?))??(?:var\\s+(.+?))??");
    Pattern badoption = Pattern.compile("No such option:\\s+(.*)");
    Pattern bestmove = Pattern.compile("bestmove\\s+(\\S+)(?:\\s+ponder\\s+(\\S+))??");

    private void parse(String line) {
        log.debug(line);
        if ("uciok".equals(line)) {
        	log.debug("uciok");
            request.engineStarted(this);
            return;
        }
        if ("readyok".equals(line)) {
        	log.debug("readyok");
        	if(waitingCommand.size() != 0) {
        		synchronized(waitingCommand) {
        			String command = waitingCommand.pop();
            		_writeCommand(command);
            		if(waitingCommand.size() != 0) {
            			log.debug("Sending isready so that we can send our next saved command of " + waitingCommand.peek());
            			_writeCommand("isready");
            		}
        		}
        	};
            return;
        }
        
        Matcher m;

        m = id.matcher(line);
        if (m.matches()) {
            log.debug("Loaded chess engine, " + m.group(1) + " is " + m.group(2));
            return;
        }
        m = option_string.matcher(line);
        if(m.matches()) {
        	Option myoption = new Option();
        	myoption.setString(m.group(1), m.group(2));
        	options.put(myoption.getName(), myoption);
        	return;
        }
        m = option_check.matcher(line);
        if(m.matches()) {
        	Option myoption = new Option();
        	myoption.setCheck(m.group(1), Boolean.parseBoolean(m.group(2)));
        	options.put(myoption.getName(), myoption);
        	return;
        }
        m = option_spin.matcher(line);
        if(m.matches()) {
        	Option myoption = new Option();
        	myoption.setSpin(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
        	options.put(myoption.getName(), myoption);
        	return;
        }
        m = option_combo.matcher(line);
        if(m.matches()) {
        	Option myoption = new Option();
        	String[] varsplit = m.group(3).split("\\s*var\\s+");
        	myoption.setCombo(m.group(1), m.group(2), varsplit);
        	options.put(myoption.getName(), myoption);
        	return;
        }

//        m = option.matcher(line);
//        if (m.matches()) {
//            Option myoption = new Option(m.group(1));
//            myoption.setType(m.group(2));
//            myoption.setDefaultValue(m.group(3));
//            myoption.setMin(m.group(4));
//            myoption.setMax(m.group(5));
//            myoption.setVar(m.group(6));
//            log.debug("Chess engine option, " + m.group(1) + " is " + m.group(2));
//            options.put(m.group(1), m.group(2));
//            request.setUciOption(myoption);
//            return;
//        }
//
        m = option_button.matcher(line);
        if (m.matches()) {
            request.setUciButton(m.group(1));
            return;
        }

        m = badoption.matcher(line);
        if (m.matches()) {
            request.badUciOption(new BadOption(m.group(1)));
            return;
        }

        if (line.startsWith("info ")) {
            UCIInfo i = new UCIInfo(line);
            if(analysisParameters.getMovetime() != -1) {
                long seconds = analysisStarted + analysisParameters.getMovetime();
                if((new Date()).getTime() > seconds) {
                    stopengine();
                    return;
                }
            }
            if(analysisParameters.getDepth() != -1) {
                if(i.haveDepth() && i.getDepth() > analysisParameters.getDepth()) {
                    stopengine();
                    return;
                }
            }
            if(analysisParameters.getDepth() != -1) {
                if(i.haveMate() && i.getMate() <= analysisParameters.getMate()) {
                    stopengine();
                    return;
                }
            }
            if(analysisParameters.getNodes() != -1) {
                if(i.haveNodes() && i.getNodes() > analysisParameters.getNodes()) {
                    stopengine();
                    return;
                }
            }
            // TODO: Do the rest of the search parms (for when we don't get a bestmove)
//          analysisParameters.getMovesToGo();
//          analysisParameters.getSearchMoves();
            request.haveInfo(i);
            return;
        }

        m = bestmove.matcher(line);
        if (m.matches()) {
        	forcestop = false;
            synchronized(calculateCounter) {
                calculateCounter++;
                if(timeChecker != null) {
                    log.trace("Main thread interrupting timeChecker for what should be " + (calculateCounter - 1));
                    timeChecker.interrupt();
                    timeChecker = null;
                }
            }
        	calculateRunning = null;
            request.bestmove(m.group(1), m.group(2));
            return;
        }

        request.unknownuci(line);
    }

    public final void debug(boolean on) {
        writeCommand("debug " + (on ? "on" : "off"));
    }

    //
    // TODO: Need to finish up figuring out how to manage the options
    // TODO: Need to figure out how to support the combo box options
    //
    public final void setOption(String poption, String value) throws Exception {
        writeCommand("setoption name " + poption + " value " + value);
    }

    public final void NewGame() {
        writeCommand("ucinewgame");
        //writeCommand("isready");
    }

    public final void newPosition() {
        //NewGame();
        writeCommand("position startpos");
    }

    public final void newPosition(Fen fen) {
        writeCommand("position fen " + fen.getFEN());
    }

    public final void newPosition(MoveList moves) throws Exception {
        //NewGame();
    	if(moves == null || moves.halfMoveCount() == 0) {
    		newPosition();
    		return;
    	}
        String str = "position startpos moves";
        for(Iterator<Move> i = moves.iterator() ; i.hasNext() ; )
            str += " " + i.next().getShortSmithMove();
        writeCommand(str);
    }

    public final void newPosition(Fen fen, MoveList moves) throws Exception {
        //NewGame();
    	if(moves == null || moves.halfMoveCount() == 0) {
    		newPosition(fen);
    		return;
    	}
        String str = "position fen " + fen.getFEN() + " moves";
        for(Iterator<Move> i = moves.iterator() ; i.hasNext() ; )
            str += " " + i.next().getShortSmithMove();
        writeCommand(str);
    }

    public final void calculate(String debugText, SearchParameters parms) throws Exception {
        String str = "go";
        analysisParameters = parms;
        
        if (parms.getSearchMoves() != null) {
            str += " searchmoves";
            for(Iterator<Move> i = parms.getSearchMoves().iterator() ; i.hasNext() ; )
                str += " " + i.next().getShortSmithMove();
        }
        if (parms.isPonderMode()) {
            str += " ponder";
        }

        if (parms.getWmsec() != -1) {
            str += " wtime " + parms.getWmsec();
        }

        if (parms.getBmsec() != -1) {
            str += " btime " + parms.getBmsec();
        }

        if (parms.getWinc() != -1) {
            str += " winc " + parms.getWinc();
        }

        if (parms.getBinc() != -1) {
            str += " binc " + parms.getBinc();
        }

        if (parms.getMovesToGo() > 0) {
            str += " movestogo " + parms.getMovesToGo();
        }

        if (parms.getDepth() != -1) {
            str += " depth " + parms.getDepth();
        }

        if (parms.getNodes() != -1) {
            str += " nodes " + parms.getNodes();
        }

        if (parms.getMate() != -1) {
            str += " mate " + parms.getMate();
        }

        if (parms.getMovetime() != -1) {
            str += " movetime " + parms.getMovetime();
            final int mv = parms.getMovetime();
            synchronized(calculateCounter) {
                calculateCounter++;
                timeChecker = new Thread(){
                    private int counter = calculateCounter;
                    @Override
                    public void run() {
                    	Thread.currentThread().setName("UCI Expiry");
                        log.debug("counter = " + counter + ", calculateCounter=" + calculateCounter + ", mv=" + mv);
                        try {
                            Thread.sleep(mv + 5000);
                            synchronized(calculateCounter) {
                                if(calculateCounter == counter) {
                                    log.warn("WARNING: Having to stop the engine from the subthread ("+calculateCounter+")");
                                    stopengine();
                                }
                            }
                        } catch (InterruptedException e) {
                            log.trace(counter + " has been interrupted (calculateCounter is now  "+calculateCounter+")");
                        }
                        synchronized(calculateCounter) {
                            if(timeChecker == this) {
                                log.trace("Setting timeChecker for " + calculateCounter + " to null");
                                timeChecker = null;
                            }
                        }
                    }
                };
                timeChecker.start();
            }
        }

        if (parms.isInfinite()) {
            str += " infinite";
        }

        analysisStarted = (new Date()).getTime();
        if(calculateRunning != null)
        	throw new Exception("Calculation " + calculateRunning + " already running");
        calculateRunning = debugText;
        writeCommand(str);
    }

    public final void stopengine() {
    	if(forcestop) return;
    	forcestop = true;
        writeCommand("stop");
    }
}
