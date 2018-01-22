package chessclub.com.icc.l1l2;

public class HelpQuestionModified {
    public static final int HANDLING = 0;
    public static final int REMOVED = 1;
    private int action;
    private String admin;
    private int qid;
    
    public int getAction() {
        return action;
    }
    public int getQuestionID() {
        return qid;
    }
    public String getAdmin() {
        return admin;
    }
    public void setAction(int action) {
        this.action = action;
    }
    public void setQuestionID(int qid) {
        this.qid = qid;
    }
    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
