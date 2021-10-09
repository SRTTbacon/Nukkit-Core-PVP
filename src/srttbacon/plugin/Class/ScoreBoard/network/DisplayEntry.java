package srttbacon.plugin.Class.ScoreBoard.network;

public class DisplayEntry {

    /**
     * The api is from the server software GoMint
     */

    private Scoreboard scoreboard = null;
    public long scoreId = 0;

    public DisplayEntry(Scoreboard score, long scoreId)
    {
    	scoreboard = score;
    	this.scoreId = scoreId;
    }
    public void setScore( int score ) {
        this.scoreboard.updateScore( this.scoreId, score );
    }

    public int getScore() {
        return this.scoreboard.getScore( this.scoreId );
    }

}