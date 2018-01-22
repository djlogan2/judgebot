package david.logan.chess.support;

public class Square {
    public int rank;
    public int file;
    
    public Square() {
        rank = -1;
        file = -1;
    }
    
    public Color getColor() {
        if(file == -1 || rank == -1)
            return Color.NONE;
        return Color.getColor((rank%2+file%2)%2 + 1);
    }
    
    public Square(final String square) {
        rank = (byte) ((int) square.charAt(1) - (int) '1');
        file = (byte) ((int) square.charAt(0) - (int) 'a');
    }
    
    public Square(Square sq) {
        this.rank = sq.rank;
        this.file = sq.file;
    }

    public Square(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }
    
    @Override
    public String toString() {
        return "" + (char) ((byte) ((file & 0xFF) + (int) 'a')) + (char) ((byte) ((rank & 0xFF) + (int) '1'));
    }
    
    @Override
    public int hashCode()
    {
    	return 0xDEAD0000 | (rank<<16) | file;
    }
    
    @Override
    public boolean equals(Object o2) {
        if(o2 instanceof Square) {
            return (rank == ((Square)o2).rank && file == ((Square)o2).file);
        } else if(o2 instanceof String) {
            return this.toString().equals(o2);
        } else {
            return false;
        }
    }
}
