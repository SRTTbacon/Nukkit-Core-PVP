package srttbacon.plugin.Class.ScoreBoard.network.packet;

import cn.nukkit.network.protocol.DataPacket;

public class RemoveObjectivePacket extends DataPacket {

    public static final byte NETWORK_ID = 0x6a;

    public String objectiveName;

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
        this.putString( this.objectiveName );
    }
}
