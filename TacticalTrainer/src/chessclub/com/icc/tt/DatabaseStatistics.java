package chessclub.com.icc.tt;

public class DatabaseStatistics {
    private int[] numberTactics; // #  tactics, sorted 0-20+ games
    private int tacticAvgRating;
    
    private int[] numberPlayers; // # provisional players, sorted 0-20+ games
    private int playerAvgRating;

    private String highestRated; // Highest rated person
    private int highRating;      // The rating
    private String mostTacticsPlayed; // The person with the most tactics
    private int gamesPlayed;          // How many tactics
    
    public int getEstTactics() {
        return numberTactics[20];
    }
    public int[] getTacticalCounts() {
        return numberTactics;
    }
    public void setNumberTactics(int[] numberTactics) {
        this.numberTactics = numberTactics;
    }
    public int getAvgTacticRating() {
        return tacticAvgRating;
    }
    public void setAvgTacticRating(int tacticAvgRating) {
        this.tacticAvgRating = tacticAvgRating;
    }
    public int getEstPlayers() {
        return numberPlayers[20];
    }
    public int[] getNumberPlayers() {
        return numberPlayers;
    }
    public void setNumberPlayers(int[] numberPlayers) {
        this.numberPlayers = numberPlayers;
    }
    public int getAvgPlayerRating() {
        return playerAvgRating;
    }
    public void setAvgPlayerRating(int tacticAvgRating) {
        this.playerAvgRating = tacticAvgRating;
    }
    public String getHighestRated() {
        return highestRated;
    }
    public void setHighestRated(String hignestRated) {
        this.highestRated = hignestRated;
    }
    public int getHighRating() {
        return highRating;
    }
    public void setHighRating(int highRating) {
        this.highRating = highRating;
    }
    public String getMostTacticsPlayed() {
        return mostTacticsPlayed;
    }
    public void setMostTacticsPlayed(String mostTacticsPlayed) {
        this.mostTacticsPlayed = mostTacticsPlayed;
    }
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
}
