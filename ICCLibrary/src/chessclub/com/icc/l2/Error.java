package chessclub.com.icc.l2;

public class Error extends Level2Packet {
    // 154 int-unique-error-number 2-letter-iso-language {string-error-text} {URL}
    public final int errornumber() {
        return Integer.parseInt(getParm(1));
    }

    public final String iso() {
        return getParm(2);
    }

    public final String text() {
        return getParm(3);
    }

    public final String url() {
        return getParm(4);
    }
}
