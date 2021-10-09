package srttbacon.plugin.Map;

import java.util.ArrayList;

import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;

public class Maps
{
	public String Map_Name = "";
	public ArrayList<Map_Not_Break_Pos> IsNotBreakPos = new ArrayList<Map_Not_Break_Pos>();
	public ArrayList<Map_Not_Break_Pos> Ore_Restore_Pos = new ArrayList<Map_Not_Break_Pos>();
	public Location Blue_Team_Spawn_Pos = null;
	public Location Red_Team_Spawn_Pos = null;
	public Vector3 Blue_Team_Core_Position = null;
	public Vector3 Red_Team_Core_Position = null;
	public Map_Not_Break_Pos Map_Pos = null;
}