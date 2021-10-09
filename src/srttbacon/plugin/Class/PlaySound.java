package srttbacon.plugin.Class;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.network.protocol.StopSoundPacket;
import srttbacon.plugin.Sub_Code;

public class PlaySound
{
	public enum Sounds
	{
		Normal_Attack_01,
		Skill_Attack_01,
		Normat_Receive_Damage_01,
		Skill_Receive_Damage_01,
		Ally_Kill_01,
		Enemy_Kill
	}
	public static void PlaySoundOne(Player_Data p, String SoundName, Vector3 Position)
	{
		PlaySoundPacket packet = new PlaySoundPacket();
		packet.name = SoundName;
		if (SoundName.substring(0, SoundName.indexOf(".")).equalsIgnoreCase("voice"))
		{
		    packet.volume = p.Voice_Volume;
			packet.pitch = p.Voice_Pitch;
		}
	    else
		{
		    packet.volume = p.SE_Volume;
			packet.pitch = 1.0f;
		}	
		packet.x = Position.getFloorX();
	    packet.y = Position.getFloorY();
		packet.z = Position.getFloorZ();
		p.player.dataPacket(packet);
	}
	public static void PlaySoundAll(Player_Data Send_Player, String SoundName, Vector3 Position)
	{
		for (Player_Data p: Sub_Code.Players_Info)
		{
			PlaySoundPacket packet = new PlaySoundPacket();
			packet.name = SoundName;
			if (SoundName.substring(0, SoundName.indexOf(".")).equalsIgnoreCase("voice"))
			{
		        packet.volume = p.Voice_Volume;
				packet.pitch = Send_Player.Voice_Pitch;
			}
	        else
			{
		        packet.volume = p.SE_Volume;
				packet.pitch = 1.0f;
			}
			packet.x = Position.getFloorX();
		    packet.y = Position.getFloorY();
			packet.z = Position.getFloorZ();
			p.player.dataPacket(packet);
		}
	}
	public static void PlaySoundAll(Player_Data Send_Player, String SoundName, Vector3 Position, float Volume)
	{
		for (Player_Data p: Sub_Code.Players_Info)
		{
			PlaySoundPacket packet = new PlaySoundPacket();
			packet.name = SoundName;
			if (p.Voice_Volume - (1.0f - Volume) < 0)
				packet.volume = 0.0f;
			else
			{
				if (SoundName.substring(0, SoundName.indexOf(".")).equalsIgnoreCase("voice"))
				{
				    packet.volume = p.Voice_Volume - (1.0f - Volume);
					packet.pitch = Send_Player.Voice_Pitch;
				}
	            else
				{
				    packet.volume = p.SE_Volume - (1.0f - Volume);
					packet.pitch = 1.0f;
				}
			}
			packet.x = Position.getFloorX();
		    packet.y = Position.getFloorY();
			packet.z = Position.getFloorZ();
			p.player.dataPacket(packet);
		}
	}
	public static void StopSoundOne(Player p, String SoundName)
	{
		StopSoundPacket packet = new StopSoundPacket();
        packet.name = SoundName;
        packet.stopAll = true;
        p.dataPacket(packet);
	}
	public static void StopSoundOne(Player p)
	{
		StopSoundPacket packet = new StopSoundPacket();
        packet.name = "";
        packet.stopAll = true;
        p.dataPacket(packet);
	}
	public static void StopSoundAll(String SoundName)
	{
		StopSoundPacket packet = new StopSoundPacket();
        packet.name = SoundName;
        packet.stopAll = true;
        for (Player p: Sub_Code.Server_Main.getOnlinePlayers().values())
			p.dataPacket(packet);
	}
}