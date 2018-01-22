package chessclub.com.icc.l1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import chessclub.com.enums.Rating;
import chessclub.com.icc.l1l2.Titles;

public class FingerNotes extends Level1Packet {
    private static final Logger log = LogManager.getLogger(FingerNotes.class);
    @Override
    public void initialize(String packet) {
        super.initialize(packet);
        try {
            parse();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
    public class FingerComment {
        public String getAdmin() {
            return admin;
        }
        public DateTime getDate() {
            return date;
        }
        public String getComment() {
            return comment;
        }
        private String admin;
        private DateTime date;
        private String comment;
    }
    public FingerComment newFingerComment() {
        return new FingerComment();
    }
    public class FingerRating {
        public Rating getRatingType() {
            return ratingtype;
        }
        public int getRating() {
            return rating;
        }
        public int getNeed() {
            return need;
        }
        public int getWin() {
            return win;
        }
        public int getLoss() {
            return loss;
        }
        public int getDraw() {
            return draw;
        }
        public int getTotal() {
            return total;
        }
        public int getBest() {
            return best;
        }
        public DateTime getBestDate() {
            return bestDate;
        }
        private Rating ratingtype;
        private int rating;
        private int need;
        private int win;
        private int draw;
        private int loss;
        private int total;
        private int best;
        private DateTime bestDate;
    };
    public DateTime getDisconnected() {
        return disconnected;
    }
    public Duration getOnfor() {
        return onfor;
    }
    public Duration getIdle() {
        return idle;
    }
    public Titles getTitles() {
        return titles;
    }
    public HashMap<Rating, FingerRating> getRatings() {
        return ratings;
    }
    public void addRating(FingerRating rating) {
        ratings.put(rating.ratingtype, rating);
    }
    public String getUserName() {
        return username;
    }
    public String getRealName() {
        return realname;
    }
    public String getFingerNote(int which) {
        if(which < 0 || which > 9)
            return null;
        return fingernote[which];
    }
    public String[] getFingerNotes() {
        return fingernote;
    }
    public String getEmail() {
        return email;
    }
    public ArrayList<String> getGroups() {
        return groups;
    }
    public InetAddress getOnFrom() {
        return onFrom;
    }
    public void addComment(int which, FingerComment comment) {
        notes.add(which, comment);
    }
    public ArrayList<FingerComment> getComments() {
        return notes;
    }
    public DateTime getCreateDate() {
        return createDate;
    }
    public InetAddress getCreatedFrom() {
        return createdFrom;
    }
    private String username;
    private Duration onfor;
    private Duration idle;
    private Titles titles;
    private HashMap<Rating, FingerRating> ratings = new HashMap<Rating, FingerRating>();
    private ArrayList<String> groups = new ArrayList<String>();
    private String[] fingernote = new String[10];
    private String realname;
    private String email;
    private InetAddress onFrom;
    private ArrayList<FingerComment> notes = new ArrayList<FingerComment>();
    private DateTime createDate;
    private InetAddress createdFrom;
    private DateTime disconnected;
    private boolean ingame = false;
    private boolean examininggame = false;
    private boolean observinggames = false;

    private String opponent;
    private Titles opponenttitles;
    private Integer examinegamenumber;
    private String examinestring;
    private ArrayList<Integer> observeList = new ArrayList<Integer>();

    public boolean isPlaying() { return ingame; }
    public boolean isExamining() { return examininggame; }
    public boolean isObserving() { return observinggames; }
    public String getOpponent() { return opponent; }
    public Titles getOpponentTitles() { return opponenttitles; }
    public Integer getExamineGameNumber() { return examinegamenumber; }
    public String getExamineString() { return examinestring; }
    public ArrayList<Integer> getObservedGames() { return observeList; }

    private void parse() throws Exception {
        int line = 1;
        final String months = "jan feb mar apr may jun jul aug sep oct nov dec";
        while(getParm(line).indexOf("Statistics") == -1 && getParm(line).indexOf("Information") == -1) line++;
        // Statistics for djlogan         On for:11:43     Idle:    0
        Pattern p1 = Pattern.compile("\\s*Statistics\\s+for\\s+(\\S+?)(\\((.*)\\))?\\s+On\\s+for:\\s*((\\d+):)?(\\d+)\\s+Idle:\\s*((\\d+):)?(\\d+)");
        Matcher m = p1.matcher(getParm(line++));
        if(m.matches()) {
            username = m.group(1);
            if(m.group(2) != null)
                titles = new Titles(m.group(2));
            onfor = new Duration((((m.group(5) == null ? 0 : Long.parseLong(m.group(5))) * 60)+Long.parseLong(m.group(6))) * 60 * 1000);
            idle = new Duration((((m.group(8) == null ? 0 : Long.parseLong(m.group(8))) * 60)+Long.parseLong(m.group(9))) * 60 * 1000);
        } else {
            Pattern p1b = Pattern.compile("Information about (\\S+?)(\\((.*)\\))?\\s+\\(Last disconnected\\s+\\S+\\s+(\\S+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+):(\\d+)\\):");
            m = p1b.matcher(getParm(line-1));
            if(!m.matches())
                throw new Exception("Unable to find first finger note line");
            username = m.group(1);
            if(m.group(2) != null)
                titles = new Titles(m.group(2));
            disconnected = (new DateTime(Integer.parseInt(m.group(6)), months.indexOf(m.group(4).toLowerCase()) / 4 + 1,
                                Integer.parseInt(m.group(5)), Integer.parseInt(m.group(7)), Integer.parseInt(m.group(8)), DateTimeZone.forID("America/New_York"))).withZone(DateTime.now().getZone());
        }

        while(getParm(line).indexOf("[need]") == -1) {
            Pattern l2a = Pattern.compile("(\\S+)\\s+is currently involved in a match against\\s+(\\S+?)(\\(.*\\))*\\."); // 1=fingerdood 2=opponent
            m = l2a.matcher(getParm(line));
            if(m.matches()) {
                ingame = true;
                opponent = m.group(2);
                if(m.group(3) != null)
                    opponenttitles = new Titles(m.group(3));
            } else {
                Pattern l2b = Pattern.compile("(\\S+)\\s+is currently examining game\\s+(\\d+):\\s+(.*)\\."); // 1=fingerdood
                m = l2b.matcher(getParm(line));
                if(m.matches()) {
                    examininggame = true;
                    examinegamenumber = Integer.parseInt(m.group(2));
                    examinestring = m.group(3);
                } else {
                    Pattern l2c = Pattern.compile("(\\S+)\\s+is observing game.\\s*((\\d+,\\s+)*)((\\d+)\\s+and\\s+)?(\\d+)\\."); // 1=fingerdood
                    m = l2c.matcher(getParm(line));
                    if(m.matches()) {
                        observinggames = true;
                        observeList.add(Integer.parseInt(m.group(6)));
                        if(m.group(5) != null && m.group(5).length() > 0)
                            observeList.add(Integer.parseInt(m.group(5)));
                        if(m.group(2) != null) {
                            String[] rest = m.group(2).split(",\\s+");
                            for(String r : rest) {
                                if(r != null && r.length() > 0)
                                    observeList.add(Integer.parseInt(r));
                            }
                        }
                    } else {
                        throw new Exception("No idea what the finger note line is before [need]!");
                    }
                }
            }
            line++;
        }
        line++;
        // 1               2     4     5     6      7    8     10   11 12  13
        //15-minute       1685  [4]    63    47     8   118   1730 (14-Nov-2009) 
        Pattern p2 = Pattern.compile("(\\S+)\\s+(\\d+)\\s+(\\[(\\d+)\\]\\s+)?(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+((\\d+)\\s+\\((\\d+)-(\\S+)-(\\d+)\\))?\\s*");
        m = p2.matcher(getParm(line));
        while(m.matches() && line < numberParms()) {
            FingerRating newR = new FingerRating();
            newR.ratingtype = Rating.fromString(m.group(1));
            newR.rating = Integer.parseInt(m.group(2));
            if(m.group(4) != null) newR.need = Integer.parseInt(m.group(4));
            newR.win = Integer.parseInt(m.group(5));
            newR.loss = Integer.parseInt(m.group(6));
            newR.draw = Integer.parseInt(m.group(7));
            newR.total = Integer.parseInt(m.group(8));
            if(m.group(10) != null) {
                newR.best = Integer.parseInt(m.group(10));
                newR.bestDate = new DateTime(Integer.parseInt(m.group(13)), months.indexOf(m.group(12).toLowerCase()) / 4 + 1, Integer.parseInt(m.group(11)), 0, 0, DateTimeZone.forID("America/New_York"));
            }
            this.addRating(newR);
            if(++line < numberParms())
                m = p2.matcher(getParm(line));
        }

        int commentStart = -1;
        int commentEnd = -1;
        int emailLine = -1;
        int onfrom = -1;
        int nameLine = -1;
        int groupStart = -1;

        for(int x=this.numberParms()-1;x>= line;x--) {
            if(groupStart == -1 &&  getParm(x).startsWith(" Groups :")) {
                groupStart = x;
            } else if(emailLine == -1 && getParm(x).startsWith(" Email  :")) {
                emailLine = x;
            } else if(nameLine == -1 && getParm(x).startsWith(" Name   :")) {
                nameLine = x;
            } else if(onfrom == -1 && getParm(x).startsWith(" On From:")) {
                onfrom = x;
            } else if(commentStart == -1 && getParm(x).startsWith("Comments:")) {
                commentStart = x;
            }
        }
        int fingerStart = line;
        int fingerEnd = numberParms();

        if(commentStart != -1) fingerEnd = Math.min(fingerEnd, commentStart);
        if(emailLine != -1) fingerEnd = Math.min(fingerEnd, emailLine);
        if(onfrom != -1) fingerEnd = Math.min(fingerEnd, onfrom);
        if(nameLine != -1) fingerEnd = Math.min(fingerEnd, nameLine);
        if(groupStart != -1) fingerEnd = Math.min(fingerEnd, groupStart);
        
        if(commentStart != -1) {
            commentEnd = numberParms();
            if(emailLine != -1) commentEnd = Math.min(commentEnd, emailLine);
            if(onfrom != -1) commentEnd = Math.min(commentEnd, onfrom);
            if(nameLine != -1) commentEnd = Math.min(commentEnd, nameLine);
            if(groupStart != -1) commentEnd = Math.min(commentEnd, groupStart);
        }
        String note = null;
        int which = -1;
        for(line = fingerStart ; line < fingerEnd ; line++) {
            if(getParm(line).startsWith("  ")) {
                note += "\r\n" + getParm(line).substring(2);
            } else {
                if(note != null)
                    this.fingernote[which-1] = note;
                which = Integer.parseInt(getParm(line).substring(0, 2).trim());
                if(getParm(line).indexOf(':') != 2) {
                    throw new Exception("Error occurred parsing finger notes");
                }
                if(getParm(line).length() > 4)
                    note = getParm(line).substring(4);
                else
                    note = "";
            }
        }
        if(note != null)
            this.fingernote[which-1] = note;
        if(commentStart != -1) {
            line = commentStart + 1; // Skip the "Comments:" line
            //                  1  2  3  4  5  6   7         8
            //  1- Created Wed Feb 09 12:12:35 EST 2000 from 204.178.125.65.
            Pattern p3 = Pattern.compile(" 1- Created \\S+ (\\S+) (\\d+) (\\d+):(\\d+):(\\d+) (\\S+) (\\d+) from (.*)\\.");
            m = p3.matcher(getParm(line++));
            if(!m.matches())
                log.error("First comment line did not match 'Created' pattern");
            else {
                String tz = m.group(6);
                if("EDT".equals(tz))
                    tz = "EST5EDT";
                createDate = (new DateTime(Integer.parseInt(m.group(7)), months.indexOf(m.group(1).toLowerCase()) / 4 + 1, Integer.parseInt(m.group(2)),
                                    Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)),
                                    DateTimeZone.forID(tz))).withZone(DateTime.now().getZone());
                String cfrom = m.group(8);
                if(cfrom.indexOf("via") != -1)
                    cfrom = cfrom.substring(0, cfrom.indexOf(" via")-1);
                try {
                    createdFrom = InetAddress.getByName(cfrom);
                } catch(UnknownHostException e) {
                    createdFrom = null;
                }
            }
            for(; line < commentEnd ; line++) {
                FingerComment comment = new FingerComment();
                Pattern p4 = Pattern.compile("\\s*(\\d+)-\\s+(\\S+)\\s+\\((\\d+):(\\d+)\\s+(\\d+)-(\\S+)-(\\d+)\\s+(\\S+)\\):\\s+(.*)");
                m = p4.matcher(getParm(line));
                if(!m.matches()) {
                    throw new Exception("Comment matching didn't match for packet line #"+line+": "+getParm(line));
                }
                int year = Integer.parseInt(m.group(7));
                if(year < 80)
                    year += 2000;
                else
                    year += 1900;
                
                // Try to capture the actual timezone IDs for daylight savings zones like "EDT", "CDT", etc.
                // If I can't find something, then I just set tzid to tz and hope for the best, which should
                // fail of course because it's already looped through the list of valid IDs
                
                String tz = m.group(8);
                String tzid = null;
                for(String id : DateTimeZone.getAvailableIDs())
                if(tz.equals(id) || tz.length() == 3 && id.endsWith(tz)) {
                    tzid = id;
                    break;
                }
                if(tzid == null)
                    tzid = tz;

                comment.date = (new DateTime(year, 
                                            months.indexOf(m.group(6).toLowerCase()) / 4 + 1,
                                            Integer.parseInt(m.group(5)),
                                            Integer.parseInt(m.group(3)),
                                            Integer.parseInt(m.group(4)),
                                            0,
                                            DateTimeZone.forID(tzid)))
                                            .withZone(DateTime.now().getZone());
                if(!"*AUTO*".equals(m.group(2)))
                    comment.admin = m.group(2);
                comment.comment = m.group(9);
                this.addComment(Integer.parseInt(m.group(1))-2, comment);
            }
        }
        if(emailLine != -1) {
            email = getParm(emailLine).substring(getParm(emailLine).indexOf(": ")+2);
        }
        if(nameLine != -1) {
            realname = getParm(nameLine).substring(getParm(nameLine).indexOf(": ")+2);
        }
        if(onfrom != -1) {
            this.onFrom = InetAddress.getByName(getParm(onfrom).substring(getParm(onfrom).indexOf(": ")+2));
        }
        if(groupStart != -1) {
            line = groupStart;
            Collections.addAll(groups, getParm(line).substring(getParm(line).indexOf(": ")+2).split("\\s+"));
            for(line++;line < numberParms() ; line++) {
                Collections.addAll(groups, getParm(line).trim().split("\\s+"));
            }
        }
    }
    public boolean isLoggedOn() {
        return(this.onfor != null);
    }
}
