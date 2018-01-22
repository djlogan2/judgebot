package david.logan.chess.support;


public interface BoardChangeListener {
    public void squareChanged(Square square);
    public void boardChanged();
}
