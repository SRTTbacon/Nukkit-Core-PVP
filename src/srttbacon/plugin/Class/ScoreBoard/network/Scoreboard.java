package srttbacon.plugin.Class.ScoreBoard.network;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.network.protocol.DataPacket;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import srttbacon.plugin.Class.ScoreBoard.map.Long2ObjectArrayMap;
import srttbacon.plugin.Class.ScoreBoard.network.packet.RemoveObjectivePacket;
import srttbacon.plugin.Class.ScoreBoard.network.packet.SetObjectivePacket;
import srttbacon.plugin.Class.ScoreBoard.network.packet.SetScorePacket;

public class Scoreboard {

    /**
     * The api is from the server software GoMint
     */

    // Scores
    private long scoreIdCounter = 0;
    private Long2ObjectMap<ScoreboardLine> scoreboardLines = new Long2ObjectArrayMap<>();

    // Viewers
    private Set<Player> viewers = new HashSet<>();

    // Displays
    private Map<DisplaySlot, ScoreboardDisplay> displays = new EnumMap<>( DisplaySlot.class );

    public ScoreboardDisplay addDisplay( DisplaySlot slot, String objectiveName, String displayName ) {
        return this.addDisplay( slot, objectiveName, displayName, SortOrder.ASCENDING );
    }

    public ScoreboardDisplay addDisplay( DisplaySlot slot, String objectiveName, String displayName, SortOrder sortOrder ) {
        ScoreboardDisplay scoreboardDisplay = this.displays.get( slot );
        if( scoreboardDisplay == null ) {
            scoreboardDisplay = new ScoreboardDisplay(this, objectiveName, displayName, sortOrder, new LinkedHashMap<>() );
            this.displays.put( slot, scoreboardDisplay );

            this.broadcast( this.constructDisplayPacket( slot, scoreboardDisplay ) );
        }

        return scoreboardDisplay;
    }

    public void removeDisplay( DisplaySlot slot ) {
        ScoreboardDisplay display = this.displays.remove( slot );
        if( display != null ) {
            // Remove all scores on this display
            LongList validScoreIDs = new LongArrayList();

            // Map fake entries first
            Long2ObjectMap.FastEntrySet<ScoreboardLine> fastSet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
            ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastSet.fastIterator();
            while ( fastIterator.hasNext() ) {
                Long2ObjectMap.Entry<ScoreboardLine> entry = fastIterator.next();
                if( entry.getValue().objective.equals( display.objectiveName ) ) {
                    validScoreIDs.add( entry.getLongKey() );
                }
            }

            // Remove all scores
            for ( long scoreID : validScoreIDs ) {
                this.scoreboardLines.remove( scoreID );
            }

            this.broadcast( this.constructRemoveScores( validScoreIDs ) );

            // Now get rid of the objective
            this.broadcast( this.constructRemoveDisplayPacket( display ) );
        }
    }

    private DataPacket constructDisplayPacket( DisplaySlot slot, ScoreboardDisplay display ) {
        SetObjectivePacket packetSetObjective = new SetObjectivePacket();
        packetSetObjective.criteriaName = "dummy";
        packetSetObjective.displayName = display.displayName;
        packetSetObjective.objectiveName = display.objectiveName;
        packetSetObjective.displaySlot = slot.name().toLowerCase();
        packetSetObjective.sortOrder = display.sortOrder.ordinal();
        return packetSetObjective;
    }

    private void broadcast( DataPacket packet ) {
        for ( Player viewer : this.viewers ) {
            viewer.dataPacket( packet );
        }
    }

    long addOrUpdateLine( String line, String objective, int score ) {
        // Check if we already have this registered
        Long2ObjectMap.FastEntrySet<ScoreboardLine> fastEntrySet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
        ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastEntrySet.fastIterator();
        while ( fastIterator.hasNext() ) {
            Long2ObjectMap.Entry<ScoreboardLine> entry = fastIterator.next();
            if( entry.getValue().type == 3 && entry.getValue().line.equals( line ) && entry.getValue().objective.equals( objective ) ) {
                return entry.getLongKey();
            }
        }

        // Add this score
        long newId = this.scoreIdCounter++;
        ScoreboardLine scoreboardLine = new ScoreboardLine( (byte) 3, 0, line, objective, score );
        this.scoreboardLines.put( newId, scoreboardLine );

        // Broadcast entry
        this.broadcast( this.constructSetScore( newId, scoreboardLine ) );
        return newId;
    }

    long addOrUpdateEntity( Entity entity, String objective, int score ) {
        // Check if we already have this registered
        Long2ObjectMap.FastEntrySet<ScoreboardLine> fastEntrySet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
        ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastEntrySet.fastIterator();
        while ( fastIterator.hasNext() ) {
            Long2ObjectMap.Entry<ScoreboardLine> entry = fastIterator.next();
            if( entry.getValue().entityId == entity.getId() && entry.getValue().objective.equals( objective ) ) {
                return entry.getLongKey();
            }
        }

        // Add this score
        long newId = this.scoreIdCounter++;
        ScoreboardLine scoreboardLine = new ScoreboardLine( (byte) ( ( entity instanceof Player ) ? 1 : 2 ), entity.getId(), "", objective, score );
        this.scoreboardLines.put( newId, scoreboardLine );

        // Broadcast entry
        this.broadcast( this.constructSetScore( newId, scoreboardLine ) );

        return newId;
    }

    private DataPacket constructSetScore( long newId, ScoreboardLine line ) {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.type = (byte)0;

        setScorePacket.entries = (new ArrayList<SetScorePacket.ScoreEntry>() {{
            add( new SetScorePacket.ScoreEntry( newId, line.objective, line.score, line.type, line.line, line.entityId ) );
        }});

        return setScorePacket;
    }

    private DataPacket constructSetScore() {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.type = (byte)0;

        List<SetScorePacket.ScoreEntry> entries = new ArrayList<>();
        Long2ObjectMap.FastEntrySet<ScoreboardLine> fastEntrySet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
        ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastEntrySet.fastIterator();
        while ( fastIterator.hasNext() ) {
            Long2ObjectMap.Entry<ScoreboardLine> entry = fastIterator.next();
            entries.add( new SetScorePacket.ScoreEntry( entry.getLongKey(), entry.getValue().objective, entry.getValue().score, entry.getValue().type, entry.getValue().line, entry.getValue().entityId ) );
        }

        setScorePacket.entries = entries;
        return setScorePacket;
    }

    public void showFor( Player player ) {
        if( this.viewers.add( player ) ) {
            // We send display information first
            for ( Map.Entry<DisplaySlot, ScoreboardDisplay> entry : this.displays.entrySet() ) {
                player.dataPacket( this.constructDisplayPacket( entry.getKey(), entry.getValue() ) );
            }

            // Send scores
            player.dataPacket( this.constructSetScore() );
        }
    }

    public void hideFor( Player player ) {
        if( this.viewers.remove( player ) ) {
            // Remove all known scores
            LongList validScoreIDs = new LongArrayList();

            // Map fake entries first
            Long2ObjectMap.FastEntrySet<ScoreboardLine> fastSet = (Long2ObjectMap.FastEntrySet<ScoreboardLine>) this.scoreboardLines.long2ObjectEntrySet();
            ObjectIterator<Long2ObjectMap.Entry<ScoreboardLine>> fastIterator = fastSet.fastIterator();
            while ( fastIterator.hasNext() ) {
                validScoreIDs.add( fastIterator.next().getLongKey() );
            }

            // Remove all scores
            player.dataPacket( this.constructRemoveScores( validScoreIDs ) );

            // Remove all known displays
            for ( Map.Entry<DisplaySlot, ScoreboardDisplay> entry : this.displays.entrySet() ) {
                player.dataPacket( this.constructRemoveDisplayPacket( entry.getValue() ) );
            }
        }
    }

    private DataPacket constructRemoveScores( LongList scoreIDs ) {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.type = (byte)1;

        List<SetScorePacket.ScoreEntry> entries = new ArrayList<>();
        for ( long scoreID : scoreIDs ) {
            entries.add( new SetScorePacket.ScoreEntry( scoreID, "", 0 ) );
        }

        setScorePacket.entries = entries;
        return setScorePacket;
    }

    private DataPacket constructRemoveDisplayPacket( ScoreboardDisplay display ) {
        RemoveObjectivePacket removeObjectivePacket = new RemoveObjectivePacket();
        removeObjectivePacket.objectiveName = display.objectiveName;
        return removeObjectivePacket;
    }

    public void updateScore( long scoreId, int score ) {
        ScoreboardLine line = this.scoreboardLines.get( scoreId );
        if( line != null ) {
            line.score = score;

            this.broadcast( this.constructSetScore( scoreId, line ) );
        }
    }

    public void removeScoreEntry( long scoreId ) {
        ScoreboardLine line = this.scoreboardLines.remove( scoreId );
        if( line != null ) {
            this.broadcast( this.constructRemoveScores( scoreId ) );
        }
    }

    private DataPacket constructRemoveScores( long scoreId ) {
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.type = (byte)1;
        setScorePacket.entries = new ArrayList<SetScorePacket.ScoreEntry>() {{
            add( new SetScorePacket.ScoreEntry( scoreId, "", 0 ) );
        }};
        return setScorePacket;
    }

    public int getScore( long scoreId ) {
        ScoreboardLine line = this.scoreboardLines.remove( scoreId );
        if( line != null ) {
            return line.score;
        }

        return 0;
    }

    private class ScoreboardLine
    {
        private byte type;
        private long entityId;
        private String line;
        private String objective;
        private int score;
        public ScoreboardLine(byte type, long entityId, String line, String objective, int score)
        {
        	this.type = type;
        	this.entityId = entityId;
        	this.line = line;
        	this.objective = objective;
        	this.score = score;
        }
    }

}