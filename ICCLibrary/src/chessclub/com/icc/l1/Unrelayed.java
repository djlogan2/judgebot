package chessclub.com.icc.l1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.icc.l1l2.HelpQuestion;


/**
 * The instance that gets created when an ICC message is sent or received.
 * @author David Logan
 *
 */
public class Unrelayed extends Level1Packet {
//    public class question {
//        public Date asked;
//        public int questionid;
//        public String iface;
//        public boolean gone;
//        public String asker;
//        public String question;
//    }
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Unrelayed.class);
    private ArrayList<HelpQuestion> questionList = new ArrayList<HelpQuestion>();
    private boolean parsed = false;
    public ArrayList<HelpQuestion> getQuestionList() {
        if(!parsed)
            parse();
        return questionList;
    }
    private void parse() {
        for(int l = 1 ; l < this.numberParms() ; l++) {
            //1 23:31 D1.5.5English (gone) DeepGreen wrote: Is there still discount where students get membership extension
            Pattern p = Pattern.compile("(\\d+)\\s+(\\d+):(\\d+)\\s+(.*?)\\s+(\\(gone\\)\\s+)?(\\S+)(\\(.*\\))?\\s+wrote:\\s+(.*)");
            Matcher m = p.matcher(getParm(l));
            if(m.matches()) {
                HelpQuestion q = new HelpQuestion();
                q.questionId = Integer.parseInt(m.group(1));
                int hour = Integer.parseInt(m.group(2));
                int min = Integer.parseInt(m.group(3));
                int pm = 0;
                if(hour > 12) {
                    hour -= 12;
                    pm = 1;
                }
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
                c.set(Calendar.HOUR, hour);
                c.set(Calendar.MINUTE, min);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.AM_PM, pm);
                if(c.after(Calendar.getInstance(TimeZone.getTimeZone("America/New_York"))))
                    c.add(Calendar.DAY_OF_MONTH, -1);
                q.asked = c.getTime();
                q.interfac  = m.group(4);
                q.gone = (m.group(5) != null);
                q.user = m.group(6);
                //String titles = m.group(7);
                q.question = m.group(8);
                questionList.add(q);
            }
        }
        parsed = true;
    }
}
