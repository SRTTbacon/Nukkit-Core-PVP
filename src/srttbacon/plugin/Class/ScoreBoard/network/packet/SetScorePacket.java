package srttbacon.plugin.Class.ScoreBoard.network.packet;

import java.util.List;

import cn.nukkit.network.protocol.DataPacket;

public class SetScorePacket extends DataPacket {

    public static final byte NETWORK_ID = 0x6c;

    public byte type;
    public List<ScoreEntry> entries;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        //Ignore
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte( this.type );
        this.putUnsignedVarInt( this.entries.size() );

        for ( ScoreEntry entry : this.entries ) {
            this.putVarLong( entry.scoreId );
            this.putString( entry.objective );
            this.putLInt( entry.score );

            if(this.type == 0){
                this.putByte( entry.entityType );

                switch ( entry.entityType ) {
                    case 3: // Fake entity
                        this.putString( entry.fakeEntity );
                        break;
                    case 1:
                    case 2:
                        this.putUnsignedVarLong( entry.entityId );
                        break;
                }

            }
        }
    }

    public static class ScoreEntry {
        private long scoreId = 0;
        private String objective = "";
        private int score = 0;

        // Add entity type
        private byte entityType;
        private String fakeEntity;
        private long entityId;
        public ScoreEntry(long scoreId, String objective, int score, byte entityType, String fakeEntity, long entityId)
        {
        	this.scoreId = scoreId;
        	this.objective = objective;
        	this.score = score;
        	this.entityType = entityType;
        	this.fakeEntity = fakeEntity;
        	this.entityId = entityId;
        }
        public ScoreEntry(long scoreId, String objective, int score)
        {
        	this.scoreId = scoreId;
        	this.objective = objective;
        	this.score = score;
        }
    }
}
