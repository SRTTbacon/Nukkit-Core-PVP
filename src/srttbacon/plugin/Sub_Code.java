package srttbacon.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockStairs;
import cn.nukkit.block.BlockWool;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Achievement.Battle_Achievement;
import srttbacon.plugin.Class.Achievement_Class;
import srttbacon.plugin.Class.Block_Copy;
import srttbacon.plugin.Class.Block_Replace;
import srttbacon.plugin.Class.Map_Select;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.Teleport_Class;
import srttbacon.plugin.Map.Map_Setting;
import srttbacon.plugin.Map.Maps;

public class Sub_Code 
{
	public enum Team
	{
		None,
		Blue,
		Red
	}
	public static List<Block_Replace> Block_Replace_List = new ArrayList<Block_Replace>();
	public static List<Block_Copy> Block_Copy_List = new ArrayList<Block_Copy>();
	public static List<Map_Select> Map_Select_List = new ArrayList<Map_Select>();
	public static List<Map_Select> Map_Not_Break_List = new ArrayList<Map_Select>();
	public static List<Map_Select> Map_Ore_Restore_List = new ArrayList<Map_Select>();
	public static List<Player_Data> Players_Info = new ArrayList<Player_Data>();
	public static List<Achievement_Class> Achievement_List = new ArrayList<Achievement_Class>();
	public static List<Teleport_Class> Teleport_List = new ArrayList<Teleport_Class>();
	public static List<String> Voice_Type_List = new ArrayList<String>();
	static final Map<Vector3, FloatingTextParticle> texts = new HashMap<>();
	static final Map<String, Integer> Player_Info_Index = new HashMap<>();
	public static Server Server_Main;
	public static String Plugin_Dir = "";
	//指定範囲をブロックで敷き詰める
	public static int Set_Replace(int Index, boolean IsAir)
	{
		int Set_Block_Count = 0;
		Block_Replace Temp = Block_Replace_List.get(Index);
		Vector3 Start = Temp.Start_Position;
		Vector3 End = Temp.End_Position;
		int Start_X = Math.min((int)Start.x, (int)End.x);
		int Start_Y = Math.min((int)Start.y, (int)End.y);
		int Start_Z = Math.min((int)Start.z, (int)End.z);
		int End_X = Math.max((int)Start.x, (int)End.x);
		int End_Y = Math.max((int)Start.y, (int)End.y);
		int End_Z = Math.max((int)Start.z, (int)End.z);
		for (int Number_01 = Start_X; Number_01 <= End_X; Number_01++)
		{
			for (int Number_02 = End_Y; Number_02 >= Start_Y; Number_02--)
			{
				for (int Number_03 = Start_Z; Number_03 <= End_Z; Number_03++)
				{
					Block block = Block.get(Temp.Block_Index);  ///Blockオブジェクトの生成
					if (IsAir)
						block = Block.get(Block.AIR);
					Vector3 vector = new Vector3(Number_01, Number_02, Number_03);
					Temp.Player_Name.level.setBlock(vector, block);
					Set_Block_Count++;
				}
			}
		}
		Block_Replace_List.remove(Index);
		return Set_Block_Count;
	}
	//ブロックが置かれていないところにのみ設置
	public static int Set_Replace_IsAir(int Index)
	{
		int Set_Block_Count = 0;
		Block_Replace Temp = Block_Replace_List.get(Index);
		Vector3 Start = Temp.Start_Position;
		Vector3 End = Temp.End_Position;
		int Start_X = Math.min((int)Start.x, (int)End.x);
		int Start_Y = Math.min((int)Start.y, (int)End.y);
		int Start_Z = Math.min((int)Start.z, (int)End.z);
		int End_X = Math.max((int)Start.x, (int)End.x);
		int End_Y = Math.max((int)Start.y, (int)End.y);
		int End_Z = Math.max((int)Start.z, (int)End.z);
		for (int Number_01 = Start_X; Number_01 <= End_X; Number_01++)
		{
			for (int Number_02 = Start_Y; Number_02 <= End_Y; Number_02++)
			{
				for (int Number_03 = Start_Z; Number_03 <= End_Z; Number_03++)
				{
					Block Before_Block = Temp.Player_Name.level.getBlock(new Vector3(Number_01, Number_02, Number_03));
					if (Before_Block.getId() == Block.AIR)
					{
						Block block = Block.get(Temp.Block_Index);  ///Blockオブジェクトの生成
						Vector3 vector = new Vector3(Number_01, Number_02, Number_03);
						Temp.Player_Name.level.setBlock(vector, block);
						Set_Block_Count++;
					}
				}
			}
		}
		Block_Replace_List.remove(Index);
		return Set_Block_Count;
	}
	public static int Set_Replace_Only_Air(int Index)
	{
		int Set_Block_Count = 0;
		Block_Replace Temp = Block_Replace_List.get(Index);
		Vector3 Start = Temp.Start_Position;
		Vector3 End = Temp.End_Position;
		int Start_X = Math.min((int)Start.x, (int)End.x);
		int Start_Y = Math.min((int)Start.y, (int)End.y);
		int Start_Z = Math.min((int)Start.z, (int)End.z);
		int End_X = Math.max((int)Start.x, (int)End.x);
		int End_Y = Math.max((int)Start.y, (int)End.y);
		int End_Z = Math.max((int)Start.z, (int)End.z);
		for (int Number_01 = Start_X; Number_01 <= End_X; Number_01++)
		{
			for (int Number_02 = End_Y; Number_02 >= Start_Y; Number_02--)
			{
				for (int Number_03 = Start_Z; Number_03 <= End_Z; Number_03++)
				{
					Block Before_Block = Temp.Player_Name.level.getBlock(new Vector3(Number_01, Number_02, Number_03));
					if (Before_Block.getId() == Block.AIR)
					{
						Vector3 vector = new Vector3(Number_01, Number_02, Number_03);
						Temp.Player_Name.level.setBlock(vector, Block.get(Temp.Block_Index));
						Set_Block_Count++;
					}
				}
			}
		}
		Block_Replace_List.remove(Index);
		return Set_Block_Count;
	}
	//指定範囲のブロックをコピー
	public static int Set_Copy(int Index, boolean With_Air)
	{
		int Set_Block_Count = 0;
		Block_Copy Temp = Block_Copy_List.get(Index);
		Vector3 Start = Temp.Start_Position;
		Vector3 End = Temp.End_Position;
		int Start_X = Math.min((int)Start.x, (int)End.x);
		int Start_Y = Math.min((int)Start.y, (int)End.y);
		int Start_Z = Math.min((int)Start.z, (int)End.z);
		int End_X = Math.max((int)Start.x, (int)End.x);
		int End_Y = Math.max((int)Start.y, (int)End.y);
		int End_Z = Math.max((int)Start.z, (int)End.z);
		int Length_X = End_X - Start_X;
		int Length_Y = End_Y - Start_Y;
		int Length_Z = End_Z - Start_Z;
		for (int Number_01 = 0; Number_01 <= Length_X; Number_01++)
		{
			for (int Number_02 = 0; Number_02 <= Length_Y; Number_02++)
			{
				for (int Number_03 = 0; Number_03 <= Length_Z; Number_03++)
				{
					Vector3 Before_Position = new Vector3(Start_X + Number_01, Start_Y + Number_02, Start_Z + Number_03);
					Block Before_Block = Temp.Player_Name.level.getBlock(Before_Position);
					if (!With_Air && Before_Block.getId() == Block.AIR)
						continue;
					Vector3 After_Position = new Vector3(Temp.Player_Name.x + Number_01, Temp.Player_Name.y + Number_02, Temp.Player_Name.z + Number_03);
					if (Before_Block.getId() == Block.WOOL)
					{
						BlockWool wool = (BlockWool)Before_Block;
						if (wool.getDyeColor() == DyeColor.RED)
							Temp.Player_Name.level.setBlock(After_Position, new BlockWool(DyeColor.LIGHT_BLUE));
						else if (wool.getDyeColor() == DyeColor.BLUE || wool.getDyeColor() == DyeColor.LIGHT_BLUE)
							Temp.Player_Name.level.setBlock(After_Position, new BlockWool(DyeColor.RED));
					}
					else
						Temp.Player_Name.level.setBlock(After_Position, Before_Block);
					Set_Block_Count++;
				}
			}
		}
		Block_Copy_List.remove(Index);
		return Set_Block_Count;
	}
	//指定範囲のブロックを反対にしてコピー
	public static int Set_Copy_Rotation(int Index, boolean With_Air)
	{
		int Set_Block_Count = 0;
		Block_Copy Temp = Block_Copy_List.get(Index);
		Vector3 Start = Temp.Start_Position;
		Vector3 End = Temp.End_Position;
		int Start_X = Math.min((int)Start.x, (int)End.x);
		int Start_Y = Math.min((int)Start.y, (int)End.y);
		int Start_Z = Math.min((int)Start.z, (int)End.z);
		int End_X = Math.max((int)Start.x, (int)End.x);
		int End_Y = Math.max((int)Start.y, (int)End.y);
		int End_Z = Math.max((int)Start.z, (int)End.z);
		int Length_X = End_X - Start_X;
		int Length_Y = End_Y - Start_Y;
		int Length_Z = End_Z - Start_Z;
		for (int Number_01 = 0; Number_01 <= Length_X; Number_01++)
		{
			for (int Number_02 = 0; Number_02 <= Length_Y; Number_02++)
			{
				for (int Number_03 = 0; Number_03 <= Length_Z; Number_03++)
				{
					Vector3 Before_Position = new Vector3(Start_X + Number_01, Start_Y + Number_02, Start_Z + Number_03);
					Block Before_Block = Temp.Player_Name.level.getBlock(Before_Position);
					if (!With_Air && Before_Block.getId() == Block.AIR)
						continue;
					Vector3 After_Position = new Vector3(Temp.Player_Name.x + Length_X - Number_01, Temp.Player_Name.y + Number_02, Temp.Player_Name.z + Length_Z - Number_03);
					if (Before_Block instanceof BlockStairs)
					{
						Block b1;
						int Meta = Before_Block.getFullId() - (Before_Block.getId() << 4);
						if (Meta == 0)
							b1 = Block.get(Before_Block.getId(), 1);
						else if (Meta == 1)
							b1 = Block.get(Before_Block.getId(), 0);
						else if (Meta == 2)
							b1 = Block.get(Before_Block.getId(), 3);
						else if (Meta == 3)
							b1 = Block.get(Before_Block.getId(), 2);
						else if (Meta == 4)
							b1 = Block.get(Before_Block.getId(), 5);
						else if (Meta == 5)
							b1 = Block.get(Before_Block.getId(), 4);
						else if (Meta == 6)
							b1 = Block.get(Before_Block.getId(), 7);
						else if (Meta == 7)
							b1 = Block.get(Before_Block.getId(), 6);
						else
							b1 = Block.get(Before_Block.getId());
						Temp.Player_Name.level.setBlock(After_Position, b1);
					}
					else if (Before_Block instanceof BlockWool)
					{
						BlockWool bw = (BlockWool)Before_Block;
						if (bw.getDyeColor() == DyeColor.RED)
							bw = new BlockWool(DyeColor.LIGHT_BLUE);
						else if (bw.getDyeColor() == DyeColor.BLUE || bw.getDyeColor() == DyeColor.LIGHT_BLUE)
							bw = new BlockWool(DyeColor.RED);
						Temp.Player_Name.level.setBlock(After_Position, bw);
					}
					else
						Temp.Player_Name.level.setBlock(After_Position, Before_Block);
					Set_Block_Count++;
				}
			}
		}
		Block_Copy_List.remove(Index);
		return Set_Block_Count;
	}
	
	//指定範囲のブロックを反対にしてコピー
	public static int Set_Copy_Rotation_90(int Index, boolean With_Air)
	{
		int Set_Block_Count = 0;
		Block_Copy Temp = Block_Copy_List.get(Index);
		Vector3 Start = Temp.Start_Position;
		Vector3 End = Temp.End_Position;
		int Start_X = Math.min((int)Start.x, (int)End.x);
		int Start_Y = Math.min((int)Start.y, (int)End.y);
		int Start_Z = Math.min((int)Start.z, (int)End.z);
		int End_X = Math.max((int)Start.x, (int)End.x);
		int End_Y = Math.max((int)Start.y, (int)End.y);
		int End_Z = Math.max((int)Start.z, (int)End.z);
		int Length_X = End_X - Start_X;
		int Length_Y = End_Y - Start_Y;
		int Length_Z = End_Z - Start_Z;
		for (int Number_01 = 0; Number_01 <= Length_X; Number_01++)
		{
			for (int Number_02 = 0; Number_02 <= Length_Y; Number_02++)
			{
				for (int Number_03 = 0; Number_03 <= Length_Z; Number_03++)
				{
					Vector3 Before_Position = new Vector3(Start_X + Number_01, Start_Y + Number_02, Start_Z + Number_03);
					Block Before_Block = Temp.Player_Name.level.getBlock(Before_Position);
					if (!With_Air && Before_Block.getId() == Block.AIR)
						continue;
					Vector3 After_Position = new Vector3(Temp.Player_Name.x + Length_Z - Number_03, Temp.Player_Name.y + Number_02, Temp.Player_Name.z + Length_Z - Number_01);
					if (Before_Block instanceof BlockStairs)
					{
						Block b1;
						int Meta = Before_Block.getFullId() - (Before_Block.getId() << 4);
						if (Meta == 0)
							b1 = Block.get(Before_Block.getId(), 1);
						else if (Meta == 1)
							b1 = Block.get(Before_Block.getId(), 0);
						else if (Meta == 2)
							b1 = Block.get(Before_Block.getId(), 3);
						else if (Meta == 3)
							b1 = Block.get(Before_Block.getId(), 2);
						else if (Meta == 4)
							b1 = Block.get(Before_Block.getId(), 5);
						else if (Meta == 5)
							b1 = Block.get(Before_Block.getId(), 4);
						else if (Meta == 6)
							b1 = Block.get(Before_Block.getId(), 7);
						else if (Meta == 7)
							b1 = Block.get(Before_Block.getId(), 6);
						else
							b1 = Block.get(Before_Block.getId());
						Temp.Player_Name.level.setBlock(After_Position, b1);
					}
					else if (Before_Block instanceof BlockWool)
					{
						BlockWool bw = (BlockWool)Before_Block;
						if (bw.getDyeColor() == DyeColor.RED)
							bw = new BlockWool(DyeColor.LIGHT_BLUE);
						else if (bw.getDyeColor() == DyeColor.BLUE || bw.getDyeColor() == DyeColor.LIGHT_BLUE)
							bw = new BlockWool(DyeColor.RED);
						Temp.Player_Name.level.setBlock(After_Position, bw);
					}
					else
						Temp.Player_Name.level.setBlock(After_Position, Before_Block);
					Set_Block_Count++;
				}
			}
		}
		Block_Copy_List.remove(Index);
		return Set_Block_Count;
	}
	
	public static Player_Data Get_Player_Data(Player p)
	{
		try
		{
			int Index = Player_Info_Index.get(p.getName());
			if (Index > -1)
			    return Players_Info.get(Index);
			/*for (int Number = 0; Number < Sub_Code.Players_Info.size(); Number++)
			{
				Player_Data Temp = Sub_Code.Players_Info.get(Number);
				if (Temp.player != null && Temp.player.getName() == p.getName())
				{
					return Temp;
				}
			}*/
			return null;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	public static BlockColor Get_Team_Color(Team Team_Color)
	{
		if (Team_Color == Team.Blue)
			return new BlockColor(115, 198, 182);
		else if (Team_Color == Team.Red)
			return new BlockColor(231, 76, 60);
		return new BlockColor(10,10,10);
	}
	public static Maps Get_Map_By_Name(String Name)
	{
   	    for (int Number1 = 0; Number1 < Map_Setting.Map_Contents.size(); Number1++)
   		    if (Map_Setting.Map_Contents.get(Number1).Map_Name.contains(Name))
   			    return Map_Setting.Map_Contents.get(Number1);
   	    return null;
	}
	public static void Set_Player_Name(Player_Data p)
	{
		if (p.Team_Color == Sub_Code.Team.None)
		{
			p.player.setDisplayName(p.Achievement_Now + TextFormat.WHITE + p.player.getName() + "(" + p.Skill_Now + ")");
			p.player.setNameTag(p.Achievement_Now + TextFormat.WHITE + p.player.getName() + "(" + p.Skill_Now + ")");
		}
		else if (p.Team_Color == Sub_Code.Team.Blue)
		{
			p.player.setDisplayName(p.Achievement_Now + TextFormat.BLUE + p.player.getName() + TextFormat.WHITE + "(" + p.Skill_Now + ")");
			p.player.setNameTag(p.Achievement_Now + TextFormat.BLUE + p.player.getName() + TextFormat.WHITE + "(" + p.Skill_Now + ") " + TextFormat.AQUA + (int)p.player.getHealth() + "/" + p.player.getMaxHealth());
		}
		else if (p.Team_Color == Sub_Code.Team.Red)
		{
			p.player.setDisplayName(p.Achievement_Now + TextFormat.RED + p.player.getName() + TextFormat.WHITE + "(" + p.Skill_Now + ")");
			p.player.setNameTag(p.Achievement_Now + TextFormat.RED + p.player.getName() + TextFormat.WHITE + "(" + p.Skill_Now + ") " + TextFormat.AQUA + (int)p.player.getHealth() + "/" + p.player.getMaxHealth());
		}
	}
	public static void Set_Player_PT(Player_Data p, int PT)
	{
		p.PT = PT;
		PT_Achievement(p);
	}
	public static void Add_Player_PT(Player_Data p, int Add_PT)
	{
		p.PT += Add_PT;
		PT_Achievement(p);
	}
	public static void PT_Achievement(Player_Data p)
	{
		int GetAchievement = -1;
		if (p.PT >= 5000 && !p.Has_Achievements.contains(Battle_Achievement.PT_All_Achievement.get(0).Name))
			GetAchievement = 0;
		if (p.PT >= 10000 && !p.Has_Achievements.contains(Battle_Achievement.PT_All_Achievement.get(1).Name))
		    GetAchievement = 1;
		if (p.PT >= 10000 && !p.Has_Achievements.contains(Battle_Achievement.PT_All_Achievement.get(2).Name))
		    GetAchievement = 2;
		if (GetAchievement > -1)
	    {
			p.Has_Achievements.add(Battle_Achievement.PT_All_Achievement.get(GetAchievement).Name);
			p.player.level.addSound(p.player.getLocation(), Sound.RANDOM_LEVELUP, 0.75f, 1.0f, p.player);
			p.player.sendMessage(TextFormat.GREEN + "所持TPが一定以上を上回ったため、称号:" + TextFormat.WHITE + "【" + 
			    Battle_Achievement.PT_All_Achievement.get(GetAchievement).Name + TextFormat.WHITE + "】" + TextFormat.GREEN + "を獲得しました。");
		}
	}
	public static void Skill_Achievement(Player_Data p, String Skill_Name)
	{
		boolean GetAchievement = false;
		if (!p.Has_Achievements.contains(Battle_Achievement.Get_Skill_Achievement_Name(Skill_Name)))
		    GetAchievement = true;
		if (GetAchievement)
		{
			p.Has_Achievements.add(Battle_Achievement.Get_Skill_Achievement_Name(Skill_Name));
			p.player.level.addSound(p.player.getLocation(), Sound.RANDOM_LEVELUP, 0.75f, 1.0f, p.player);
			p.player.sendMessage(TextFormat.GREEN + "職業:" + p.Skill_Now + "で条件を達成したため、称号:" + Battle_Achievement.Get_Skill_Achievement_Name(Skill_Name) + TextFormat.GREEN + "を獲得しました。");
		}
	}
	public static void Add_Text(Vector3 pos, String title, String text)
	{
        FloatingTextParticle newFtext = new FloatingTextParticle(pos, title, TextFormat.colorize(text));
        texts.put(pos, newFtext);
        for (Player player : Server.getInstance().getOnlinePlayers().values())
        	player.level.addParticle(newFtext, player);
    }
	public static void AddAllText()
	{
		for (Player player : Server.getInstance().getOnlinePlayers().values())
		{
	        for( FloatingTextParticle text: texts.values())
	        	player.level.addParticle(text, player);
		}
	}
	public static void AddAllText(Player player)
	{
		for( FloatingTextParticle text: texts.values())
		{
        	player.level.addParticle(text, player);
        	player.sendMessage(text.getText());
		}
	}
}