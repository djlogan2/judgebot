package david.logan.chess.support;


public class IllegalMoveException extends Exception {
    private static final long serialVersionUID = 1L;
    private Square from;
    private Square to;
    private String fen;
    public IllegalMoveException(String fen, Square from, Square to) {
        this.fen = fen;
        this.from = from;
        this.to = to;
    }

    @Override
    public String getMessage() {
        String ret = "Illegal move "+ fen + System.getProperty("line.separator");
        if(from != null)
            ret += ". from " + from.toString();
        if(to != null)
            ret += " to " + to.toString();
        return ret;
    }
}
