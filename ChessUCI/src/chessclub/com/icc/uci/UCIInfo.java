package chessclub.com.icc.uci;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public final class UCIInfo {
    public final String originalLine() {
        return origLine;
    }
    
    public final boolean haveDepth() {
        return values[UCIInfoEnum.DEPTH.ordinal()];
    }

    public final boolean haveSeldepth() {
        return values[UCIInfoEnum.SELDEPTH.ordinal()];
    }

    public final boolean haveTime() {
        return values[UCIInfoEnum.TIME.ordinal()];
    }

    public final boolean haveNodes() {
        return values[UCIInfoEnum.NODES.ordinal()];
    }

    public final boolean havePv() {
        return values[UCIInfoEnum.PV.ordinal()];
    }

    public final boolean haveMultipv() {
        return values[UCIInfoEnum.MULTIPV.ordinal()];
    }

    public final boolean haveScore() {
        return values[UCIInfoEnum.SCORE.ordinal()];
    }

    public final boolean haveCp() {
        return values[UCIInfoEnum.CP.ordinal()];
    }

    public final boolean haveMate() {
        return values[UCIInfoEnum.MATE.ordinal()];
    }

    public final boolean haveLowerbound() {
        return values[UCIInfoEnum.LOWERBOUND.ordinal()];
    }

    public final boolean haveUpperbound() {
        return values[UCIInfoEnum.UPPERBOUND.ordinal()];
    }

    public final boolean haveCurrmove() {
        return values[UCIInfoEnum.CURRMOVE.ordinal()];
    }

    public final boolean haveCurrmovenumber() {
        return values[UCIInfoEnum.CURRMOVENUMBER.ordinal()];
    }

    public final boolean haveHashfull() {
        return values[UCIInfoEnum.HASHFULL.ordinal()];
    }

    public final boolean haveNps() {
        return values[UCIInfoEnum.NPS.ordinal()];
    }

    public final boolean haveTbhits() {
        return values[UCIInfoEnum.TBHITS.ordinal()];
    }

    public final boolean haveCpuload() {
        return values[UCIInfoEnum.CPULOAD.ordinal()];
    }

    public final boolean haveString() {
        return values[UCIInfoEnum.STRING.ordinal()];
    }

    public final boolean haveRefutation() {
        return values[UCIInfoEnum.REFUTATION.ordinal()];
    }

    public final boolean haveLine() {
        return values[UCIInfoEnum.LINE.ordinal()];
    }

    public final int getDepth() {
        return depth;
    }

    public final int getSeldepth() {
        return seldepth;
    }

    public final int getTime() {
        return time;
    }

    public final long getNodes() {
        return nodes;
    }

    public final String getPv() {
        return pv;
    }

    public final int getMultipv() {
        return multipv;
    }

    public final Score getScore() {
        return score;
    }

    public final int getCp() {
        return score.cp;
    }

    public final int getMate() {
        return score.mate;
    }

    public final boolean getLowerbound() {
        return score.lowerbound;
    }

    public final boolean getUpperbound() {
        return score.upperbound;
    }

    public final String getCurrmove() {
        return currmove;
    }

    public final int getCurrmovenumber() {
        return currmovenumber;
    }

    public final int getHashfull() {
        return hashfull;
    }

    public final int getNps() {
        return nps;
    }

    public final int getTbhits() {
        return tbhits;
    }

    public final int getCpuload() {
        return cpuload;
    }

    public final String getString() {
        return string;
    }

    public final String getRefutation() {
        return refutation;
    }

    public final String getLine() {
        return line;
    }

    private boolean[] values = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
    private int depth;
    private int seldepth;
    private int time;
    private long nodes;
    private String pv;
    private int multipv;

    private class Score {
        private int cp;
        private int mate;
        private boolean lowerbound;
        private boolean upperbound;

        public final String loadScore(String infoline, boolean[] pvalues) {
            Pattern keyvalue = Pattern.compile("(\\S+)\\s+(\\S+)\\s*(.*)");
            Pattern keyword = Pattern.compile("(\\S+)\\s*(.*)");

            Matcher m;

            infoline = infoline.trim();

            while (true) {
                m = keyvalue.matcher(infoline);

                if (!m.matches()) {
                    return infoline;
                } else if ("cp".equals(m.group(1))) {
                    score.cp = Integer.parseInt(m.group(2));
                    pvalues[UCIInfoEnum.CP.ordinal()] = true;
                    infoline = m.group(3);
                } else if ("mate".equals(m.group(1))) {
                    score.mate = Integer.parseInt(m.group(2));
                    pvalues[UCIInfoEnum.MATE.ordinal()] = true;
                    infoline = m.group(3);
                } else {
                    m = keyword.matcher(infoline);
                    if (!m.matches()) {
                        return infoline;
                    } else if ("upperbound".equals(m.group(1))) {
                        pvalues[UCIInfoEnum.UPPERBOUND.ordinal()] = true;
                        upperbound = true;
                        infoline = m.group(2);
                    } else if ("lowerbound".equals(m.group(1))) {
                        pvalues[UCIInfoEnum.LOWERBOUND.ordinal()] = true;
                        lowerbound = true;
                        infoline = m.group(2);
                    } else {
                        return infoline;
                    }
                }
            }
        }
    };

    private Score score = new Score();
    private String currmove;
    private int currmovenumber;
    private int hashfull;
    private int nps;
    private int tbhits;
    private int cpuload;
    private String string;
    private String refutation;
    private String line;
    private String origLine;
    private static final Logger Log = LogManager.getLogger(UCIInfo.class);

    public UCIInfo(final String p_infoline) {
        origLine = p_infoline;
        
        Pattern start = Pattern.compile("info\\s+?(.*)");
        Pattern keyvalue = Pattern.compile("(\\S+)\\s+(\\S+)\\s*(.*)");
        Pattern keyword = Pattern.compile("(\\S+)\\s*(.*)");
        String infoline = p_infoline;

        Matcher m;
        boolean kvmatched = false;

        m = start.matcher(infoline);
        if (!m.matches()) {
            Log.debug("UCIInfo called without an info line: " + infoline);
            return;
        }

        infoline = m.group(1);

        do {
            kvmatched = false;
            m = keyvalue.matcher(infoline);
            if (Log.isDebugEnabled()) {
                Log.debug("UCIInfo:" + infoline + ",matches=" + m.matches() + ",groupCount=" + m.groupCount());
            }
            if (m.matches()) {
                if (Log.isDebugEnabled()) {
                    Log.debug("UCIInfo(KWV) keyword=" + m.group(1) + ",value=" + m.group(2));
                }
                if ("depth".equals(m.group(1))) {
                    values[UCIInfoEnum.DEPTH.ordinal()] = true;
                    depth = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("seldepth".equals(m.group(1))) {
                    values[UCIInfoEnum.SELDEPTH.ordinal()] = true;
                    seldepth = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("time".equals(m.group(1))) {
                    values[UCIInfoEnum.TIME.ordinal()] = true;
                    time = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("nodes".equals(m.group(1))) {
                    values[UCIInfoEnum.NODES.ordinal()] = true;
                    nodes = Long.parseLong(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("multipv".equals(m.group(1))) {
                    values[UCIInfoEnum.MULTIPV.ordinal()] = true;
                    multipv = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("currmove".equals(m.group(1))) {
                    values[UCIInfoEnum.CURRMOVE.ordinal()] = true;
                    currmove = m.group(2);
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("currmovenumber".equals(m.group(1))) {
                    values[UCIInfoEnum.CURRMOVENUMBER.ordinal()] = true;
                    currmovenumber = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("hashfull".equals(m.group(1))) {
                    values[UCIInfoEnum.HASHFULL.ordinal()] = true;
                    hashfull = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("nps".equals(m.group(1))) {
                    values[UCIInfoEnum.NPS.ordinal()] = true;
                    nps = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("tbhits".equals(m.group(1))) {
                    values[UCIInfoEnum.TBHITS.ordinal()] = true;
                    tbhits = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                } else if ("cpuload".equals(m.group(1))) {
                    values[UCIInfoEnum.CPULOAD.ordinal()] = true;
                    cpuload = Integer.parseInt(m.group(2));
                    infoline = m.group(3);
                    kvmatched = true;
                }
            }
            if (!kvmatched) {
                m = keyword.matcher(infoline);
                if (m.matches()) {
                    if (Log.isDebugEnabled()) {
                        Log.debug("UCIInfo(KWO) keyword=" + m.group(1));
                    }
                    if ("score".equals(m.group(1))) {
                        values[UCIInfoEnum.SCORE.ordinal()] = true;
                        infoline = score.loadScore(m.group(2), values);
                    } else if ("pv".equals(m.group(1))) {
                        values[UCIInfoEnum.PV.ordinal()] = true;
                        pv = m.group(2);
                        return;
                    } else if ("string".equals(m.group(1))) {
                        values[UCIInfoEnum.STRING.ordinal()] = true;
                        string = m.group(2);
                        return;
                    } else if ("refutation".equals(m.group(1))) {
                        values[UCIInfoEnum.REFUTATION.ordinal()] = true;
                        refutation = m.group(2);
                        return;
                    } else if ("currline".equals(m.group(1))) {
                        values[UCIInfoEnum.LINE.ordinal()] = true;
                        line = m.group(2);
                        return;
                    } else {
                        Log.debug("UCIInfo has no idea how to handle the end of the infoline (2): " + infoline);
                        return;
                    }
                } else {
                    Log.debug("UCIInfo has no idea how to handle the end of the infoline (1): " + infoline);
                    return;
                }
            }
        } while (infoline.length() != 0);
    }
};
