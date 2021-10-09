package srttbacon.plugin.Class.ScoreBoard.network;

import java.util.LinkedHashMap;

import cn.nukkit.entity.Entity;

public class ScoreboardDisplay {

    /**
     * The api is from the server software GoMint
     */

    public Scoreboard scoreboard = null;
    public String objectiveName = "";
    public String displayName = "";
    public SortOrder sortOrder = null;

    private LinkedHashMap<Integer, String> lineEntry;

    public ScoreboardDisplay(Scoreboard scoreboard, String objectiveName, String displayName, SortOrder sortOrder, LinkedHashMap<Integer, String> lineEntry)
    {
    	this.scoreboard = scoreboard;
    	this.objectiveName = objectiveName;
    	this.displayName = displayName;
    	this.sortOrder = sortOrder;
    	this.lineEntry = lineEntry;
    }
    public DisplayEntry addEntity( Entity entity, int score ) {
        long scoreId = this.scoreboard.addOrUpdateEntity( entity, this.objectiveName, score );
        return new DisplayEntry( this.scoreboard, scoreId );
    }

    public DisplayEntry addLine( String line, int score ) {
        long scoreId = this.scoreboard.addOrUpdateLine( line, this.objectiveName, score );
        this.lineEntry.put( score, line );
        return new DisplayEntry( this.scoreboard, scoreId );
    }

    public void removeEntry( DisplayEntry entry ) {
    	if (entry == null)
    		return;
        this.scoreboard.removeScoreEntry(entry.scoreId);
    }

    public String getLine( int score ) {
        return this.lineEntry.getOrDefault( score, null );
    }

}