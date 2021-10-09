package srttbacon.plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Achievement.Achievement_Main;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Map.Map_Not_Break_Pos;
import srttbacon.plugin.Map.Map_Setting;
import srttbacon.plugin.Map.Maps;

public class Save_Load
{
	public static void Save_Main()
	{
		try
		{
			boolean IsOneLineMode = false;
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Sub_Code.Plugin_Dir + "/Configs/Maps/Main.yaml"), "UTF-8"));
			if (Achievement_Main.All_Achievement_List.size() > 0)
			{
				fw.write("All_Achievement_List:");
				for (Achievement_Main.All_Achievement_Save Achi_Pos: Achievement_Main.All_Achievement_List)
				{
					if (!IsOneLineMode)
						fw.write(Achi_Pos.Pos.x + "," + Achi_Pos.Pos.y + "," + Achi_Pos.Pos.z + "," + Achi_Pos.Name + "," + Achi_Pos.Hint);
					else
						fw.write("/" + Achi_Pos.Pos.x + "," + Achi_Pos.Pos.y + "," + Achi_Pos.Pos.z + "," + Achi_Pos.Name + "," + Achi_Pos.Hint);
					IsOneLineMode = true;
				}
				IsOneLineMode = false;
				fw.write("\r\n");
			}
			if (Lobby.All_Teleport_List.size() > 0)
			{
				fw.write("All_Teleport_List:");
				for (Lobby.All_Teleport_Save Achi_Pos: Lobby.All_Teleport_List)
				{
					if (!IsOneLineMode)
						fw.write(Achi_Pos.Sign_Pos.x + "," + Achi_Pos.Sign_Pos.y + "," + Achi_Pos.Sign_Pos.z + "," + Achi_Pos.To_Pos.x + "," + Achi_Pos.To_Pos.y + "," + Achi_Pos.To_Pos.z + "," + Achi_Pos.To_Pos.yaw + "," + Achi_Pos.To_Pos.pitch + "," + Achi_Pos.ID);
					else
						fw.write("/" + Achi_Pos.Sign_Pos.x + "," + Achi_Pos.Sign_Pos.y + "," + Achi_Pos.Sign_Pos.z + "," + Achi_Pos.To_Pos.x + "," + Achi_Pos.To_Pos.y + "," + Achi_Pos.To_Pos.z + "," + Achi_Pos.To_Pos.yaw + "," + Achi_Pos.To_Pos.pitch + "," + Achi_Pos.ID);
					IsOneLineMode = true;
				}
				IsOneLineMode = false;
				fw.write("\r\n");
			}
			if (Lobby.Lobby_Teleport_Position == null)
				fw.write("Lobby_Teleport_Position:0,6,0,0,0");
			else
			    fw.write("Lobby_Teleport_Position:" + Lobby.Lobby_Teleport_Position.x + "," + Lobby.Lobby_Teleport_Position.y + "," + Lobby.Lobby_Teleport_Position.z + "," + Lobby.Lobby_Teleport_Position.getYaw() + "," + Lobby.Lobby_Teleport_Position.getPitch());
			fw.close();
		}
		catch (Exception e)
		{
			Sub_Code.Server_Main.getLogger().info("メイン情報のセーブ中にエラーが発生しました。内容:" + e.getMessage());
		}
	}
	public static void Load_Main()
	{
		try
		{
			if (Files.exists(Paths.get(Sub_Code.Plugin_Dir + "/Configs/Maps/Main.yaml")))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Sub_Code.Plugin_Dir + "/Configs/Maps/Main.yaml"), "UTF-8"));
				String line;
				Achievement_Main.All_Achievement_List.clear();
	            while ((line = br.readLine()) != null)
	            {
	                String Setting_Name = line.substring(0, line.indexOf(':'));
	                String Value = line.substring(line.indexOf(':') + 1);
	                if (Setting_Name.contains("All_Achievement_List"))
	                {
	                	if (Value.contains("/"))
	                	{
		                	String[] Pos = Value.split("/");
		                	for (String Pos_String: Pos)
		                	{
			                	Achievement_Main.All_Achievement_Save Achi_Pos = new Achievement_Main.All_Achievement_Save();
		                		String[] Achi_Pos_String = Pos_String.split(",");
		                		Achi_Pos.Pos = new Vector3(Double.valueOf(Achi_Pos_String[0]), Double.valueOf(Achi_Pos_String[1]), Double.valueOf(Achi_Pos_String[2]));
		                		Achi_Pos.Name = Achi_Pos_String[3];
								Achi_Pos.Hint = Achi_Pos_String[4];
		                		Achievement_Main.All_Achievement_List.add(Achi_Pos);
		                	}	
	                	}
	                	else
	                	{
		                	Achievement_Main.All_Achievement_Save Achi_Pos = new Achievement_Main.All_Achievement_Save();
	                		String[] Achi_Pos_String = Value.split(",");
	                		Achi_Pos.Pos = new Vector3(Double.valueOf(Achi_Pos_String[0]), Double.valueOf(Achi_Pos_String[1]), Double.valueOf(Achi_Pos_String[2]));
	                		Achi_Pos.Name = Achi_Pos_String[3];
							Achi_Pos.Hint = Achi_Pos_String[4];
	                		Achievement_Main.All_Achievement_List.add(Achi_Pos);
	                	}
	                }
	                else if (Setting_Name.contains("All_Teleport_List"))
	                {
	                	if (Value.contains("/"))
	                	{
		                	String[] Pos = Value.split("/");
		                	for (String Pos_String: Pos)
		                	{
			                	Lobby.All_Teleport_Save Tel_Pos = new Lobby.All_Teleport_Save();
		                		String[] Tel_Pos_String = Pos_String.split(",");
		                		Tel_Pos.Sign_Pos = new Vector3(Double.valueOf(Tel_Pos_String[0]), Double.valueOf(Tel_Pos_String[1]), Double.valueOf(Tel_Pos_String[2]));
		                		Tel_Pos.To_Pos = new Location(Double.valueOf(Tel_Pos_String[3]), Double.valueOf(Tel_Pos_String[4]), Double.valueOf(Tel_Pos_String[5]), Double.valueOf(Tel_Pos_String[6]), Double.valueOf(Tel_Pos_String[7]));
		                		Tel_Pos.ID = Integer.valueOf(Tel_Pos_String[8]);
		                		Lobby.All_Teleport_List.add(Tel_Pos);
		                	}	
	                	}
	                	else
	                	{
	                		Lobby.All_Teleport_Save Tel_Pos = new Lobby.All_Teleport_Save();
	                		String[] Tel_Pos_String = Value.split(",");
	                		Tel_Pos.Sign_Pos = new Vector3(Double.valueOf(Tel_Pos_String[0]), Double.valueOf(Tel_Pos_String[1]), Double.valueOf(Tel_Pos_String[2]));
	                		Tel_Pos.To_Pos = new Location(Double.valueOf(Tel_Pos_String[3]), Double.valueOf(Tel_Pos_String[4]), Double.valueOf(Tel_Pos_String[5]), Double.valueOf(Tel_Pos_String[6]), Double.valueOf(Tel_Pos_String[7]));
	                		Tel_Pos.ID = Integer.valueOf(Tel_Pos_String[8]);
	                		Lobby.All_Teleport_List.add(Tel_Pos);
	                	}
	                }
	                else if (Setting_Name.contains("Lobby_Teleport_Position"))
	                {
	                	String[] Pos = Value.split(",");
	                	Lobby.Lobby_Teleport_Position = new Location(Double.valueOf(Pos[0]), Double.valueOf(Pos[1]), Double.valueOf(Pos[2]), Double.valueOf(Pos[3]), Double.valueOf(Pos[4]));
	                }
	            }
	            br.close();
			}
		}
		catch (Exception e)
		{
			Sub_Code.Server_Main.getLogger().info("メイン情報のロード中にエラーが発生しました。内容:" + e.getMessage());
		}
	}
	public static void Save_All_Maps()
	{
		try
		{
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Sub_Code.Plugin_Dir + "/Configs/Maps/Map_Setting.yaml"), "UTF-8"));
			boolean First_Map = true;
			for (int Number = 0; Number < Map_Setting.Map_Contents.size(); Number++)
			{
				Maps map = Map_Setting.Map_Contents.get(Number);
				if (First_Map)
				{
					fw.write("Map_Name:" + map.Map_Name + "\r\n");
					First_Map = false;
				}
				else
					fw.write("\r\nMap_Name:" + map.Map_Name + "\r\n");
				boolean IsOneLineMode = false;
				if (map.IsNotBreakPos.size() > 0)
				{
					fw.write("IsNotBreakPos:");
					for (Map_Not_Break_Pos Pos: map.IsNotBreakPos)
					{
						if (!IsOneLineMode)
							fw.write(Pos.Start_Pos.x + "," + Pos.Start_Pos.y + "," + Pos.Start_Pos.z + "," + Pos.End_Pos.x + "," + Pos.End_Pos.y + "," + Pos.End_Pos.z);
						else
							fw.write("/" + Pos.Start_Pos.x + "," + Pos.Start_Pos.y + "," + Pos.Start_Pos.z + "," + Pos.End_Pos.x + "," + Pos.End_Pos.y + "," + Pos.End_Pos.z);
						IsOneLineMode = true;
					}
					IsOneLineMode = false;
					fw.write("\r\n");
				}
				if (map.Ore_Restore_Pos.size() > 0)
				{
					fw.write("Ore_Restore_Pos:");
					for (Map_Not_Break_Pos Pos: map.Ore_Restore_Pos)
					{
						if (!IsOneLineMode)
							fw.write(Pos.Start_Pos.x + "," + Pos.Start_Pos.y + "," + Pos.Start_Pos.z + "," + Pos.End_Pos.x + "," + Pos.End_Pos.y + "," + Pos.End_Pos.z);
						else
							fw.write("/" + Pos.Start_Pos.x + "," + Pos.Start_Pos.y + "," + Pos.Start_Pos.z + "," + Pos.End_Pos.x + "," + Pos.End_Pos.y + "," + Pos.End_Pos.z);
						IsOneLineMode = true;
					}
					IsOneLineMode = false;
					fw.write("\r\n");
				}
				fw.write("Blue_Team_Spawn_Pos:" + map.Blue_Team_Spawn_Pos.x + "," + map.Blue_Team_Spawn_Pos.y + "," + map.Blue_Team_Spawn_Pos.z + "," + map.Blue_Team_Spawn_Pos.getYaw() + "," + map.Blue_Team_Spawn_Pos.getPitch() + "\r\n");
				fw.write("Red_Team_Spawn_Pos:" + map.Red_Team_Spawn_Pos.x + "," + map.Red_Team_Spawn_Pos.y + "," + map.Red_Team_Spawn_Pos.z + "," + map.Red_Team_Spawn_Pos.getYaw() + "," + map.Red_Team_Spawn_Pos.getPitch() + "\r\n");
				fw.write("Blue_Team_Core_Position:" + map.Blue_Team_Core_Position.x + "," + map.Blue_Team_Core_Position.y + "," + map.Blue_Team_Core_Position.z + "\r\n");
				fw.write("Red_Team_Core_Position:" + map.Red_Team_Core_Position.x + "," + map.Red_Team_Core_Position.y + "," + map.Red_Team_Core_Position.z + "\r\n");
				fw.write("Map_Pos:" + map.Map_Pos.Start_Pos.x + "," + map.Map_Pos.Start_Pos.y + "," + map.Map_Pos.Start_Pos.z + "," + map.Map_Pos.End_Pos.x + "," + map.Map_Pos.End_Pos.y + "," + map.Map_Pos.End_Pos.z);
			}
			fw.close();
		}
		catch (Exception e)
		{
			Sub_Code.Server_Main.getLogger().info("マップ情報のセーブ中にエラーが発生しました。内容:" + e.getMessage());
		}
	}
	public static void Load_All_Maps()
	{
		try
		{
			if (Files.exists(Paths.get(Sub_Code.Plugin_Dir + "/Configs/Maps/Map_Setting.yaml")))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Sub_Code.Plugin_Dir + "/Configs/Maps/Map_Setting.yaml"), "UTF-8"));
				String line;
				Map_Setting.Map_Contents.clear();
	            while ((line = br.readLine()) != null)
	            {
	                String Setting_Name = line.substring(0, line.indexOf(':'));
	                String Value = line.substring(line.indexOf(':') + 1);
	                if (Setting_Name.contains("Map_Name"))
	                {
	                	Maps temp_map = new Maps();
	                	temp_map.Map_Name = Value;
	                	Map_Setting.Map_Contents.add(temp_map);
	                }
	                Maps map = Map_Setting.Map_Contents.get(Map_Setting.Map_Contents.size() - 1);
	                if (Setting_Name.contains("IsNotBreakPos"))
	                {
	                	if (Value.contains("/"))
	                	{
		                	String[] Pos = Value.split("/");
		                	for (String Pos_String: Pos)
		                	{
			                	Map_Not_Break_Pos Break_Pos = new Map_Not_Break_Pos();
		                		String[] Map_Break_Pos = Pos_String.split(",");
		                		Break_Pos.Start_Pos = new Vector3(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]));
		                		Break_Pos.End_Pos = new Vector3(Double.valueOf(Map_Break_Pos[3]), Double.valueOf(Map_Break_Pos[4]), Double.valueOf(Map_Break_Pos[5]));
		                		map.IsNotBreakPos.add(Break_Pos);
		                	}
	                	}
	                	else
	                	{
	                		Map_Not_Break_Pos Break_Pos = new Map_Not_Break_Pos();
	                		String[] Map_Break_Pos = Value.split(",");
	                		Break_Pos.Start_Pos = new Vector3(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]));
	                		Break_Pos.End_Pos = new Vector3(Double.valueOf(Map_Break_Pos[3]), Double.valueOf(Map_Break_Pos[4]), Double.valueOf(Map_Break_Pos[5]));
	                		map.IsNotBreakPos.add(Break_Pos);
	                	}
	                }
	                if (Setting_Name.contains("Ore_Restore_Pos"))
	                {
	                	if (Value.contains("/"))
	                	{
		                	String[] Pos = Value.split("/");
		                	for (String Pos_String: Pos)
		                	{
			                	Map_Not_Break_Pos Break_Pos = new Map_Not_Break_Pos();
		                		String[] Map_Break_Pos = Pos_String.split(",");
		                		Break_Pos.Start_Pos = new Vector3(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]));
		                		Break_Pos.End_Pos = new Vector3(Double.valueOf(Map_Break_Pos[3]), Double.valueOf(Map_Break_Pos[4]), Double.valueOf(Map_Break_Pos[5]));
		                		map.Ore_Restore_Pos.add(Break_Pos);
		                	}
	                	}
	                	else
	                	{
	                		Map_Not_Break_Pos Break_Pos = new Map_Not_Break_Pos();
	                		String[] Map_Break_Pos = Value.split(",");
	                		Break_Pos.Start_Pos = new Vector3(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]));
	                		Break_Pos.End_Pos = new Vector3(Double.valueOf(Map_Break_Pos[3]), Double.valueOf(Map_Break_Pos[4]), Double.valueOf(Map_Break_Pos[5]));
	                		map.Ore_Restore_Pos.add(Break_Pos);
	                	}
	                }
	                if (Setting_Name.contains("Blue_Team_Spawn_Pos"))
	                {
                		String[] Map_Break_Pos = Value.split(",");
                		map.Blue_Team_Spawn_Pos = new Location(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]), Double.valueOf(Map_Break_Pos[3]), Double.valueOf(Map_Break_Pos[4]));
	                }
	                if (Setting_Name.contains("Red_Team_Spawn_Pos"))
	                {
                		String[] Map_Break_Pos = Value.split(",");
                		map.Red_Team_Spawn_Pos = new Location(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]), Double.valueOf(Map_Break_Pos[3]), Double.valueOf(Map_Break_Pos[4]));
	                }
	                if (Setting_Name.contains("Blue_Team_Core_Position"))
	                {
                		String[] Map_Break_Pos = Value.split(",");
                		map.Blue_Team_Core_Position = new Vector3(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]));
	                }
	                if (Setting_Name.contains("Red_Team_Core_Position"))
	                {
                		String[] Map_Break_Pos = Value.split(",");
                		map.Red_Team_Core_Position = new Vector3(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]));
	                }
	                if (Setting_Name.contains("Map_Pos"))
	                {
	                	Map_Not_Break_Pos Break_Pos = new Map_Not_Break_Pos();
                		String[] Map_Break_Pos = Value.split(",");
                		Break_Pos.Start_Pos = new Vector3(Double.valueOf(Map_Break_Pos[0]), Double.valueOf(Map_Break_Pos[1]), Double.valueOf(Map_Break_Pos[2]));
                		Break_Pos.End_Pos = new Vector3(Double.valueOf(Map_Break_Pos[3]), Double.valueOf(Map_Break_Pos[4]), Double.valueOf(Map_Break_Pos[5]));
                		map.Map_Pos = Break_Pos;
	                }
	            }
	            br.close();
			}
		}
		catch (Exception e)
		{
			Sub_Code.Server_Main.getLogger().info("マップ情報をロードできませんでした。内容:" + e.getMessage());
		}
	}
	public static void Load_All_Player_Data()
	{
		for(Player player : Server.getInstance().getOnlinePlayers().values())
		{
			Load_Player_Data(player);
		}
	}
	public static void Save_All_Player_Data()
	{
		for(Player player : Server.getInstance().getOnlinePlayers().values())
		{
			Save_Player_Data(Sub_Code.Get_Player_Data(player));
		}
	}
	public static void Load_Player_Data(Player player)
	{
		try
		{
			for (int Number = 0; Number < Sub_Code.Players_Info.size(); Number++)
				if (Sub_Code.Players_Info.get(Number).player.getName() == player.getName())
					return;
			Player_Data pd = new Player_Data();
			if (Files.exists(Paths.get(Sub_Code.Plugin_Dir + "/Configs/" + player.getName() + ".yaml")))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Sub_Code.Plugin_Dir + "/Configs/" + player.getName() + ".yaml"), "UTF-8"));
				String line;
				while ((line = br.readLine()) != null)
				{
					String Setting_Name = line.substring(0, line.indexOf(':'));
	                String Value = line.substring(line.indexOf(':') + 1);
	                if (Setting_Name.contains("Skill_Now"))
	                	pd.Skill_Now = Value;
	                else if (Setting_Name.contains("Achievement_Now"))
	                	pd.Achievement_Now = Value;
	                else if (Setting_Name.contains("Player_PT"))
	                	pd.PT = Integer.valueOf(Value);
					else if (Setting_Name.contains("Voice_Name"))
					    pd.Voice_Name = Value;
					else if (Setting_Name.contains("Voice_Volume"))
					    pd.Voice_Volume = Float.valueOf(Value);
					else if (Setting_Name.contains("Voice_Pitch"))
					    pd.Voice_Pitch = Float.valueOf(Value);
					else if (Setting_Name.contains("SE_Volume"))
					    pd.SE_Volume = Float.valueOf(Value);
	                else if (Setting_Name.contains("Has_Skills"))
	                {
	                	ArrayList<String> Skill_List = new ArrayList<String>();
	                	if (Value.contains("/"))
	                	{
	                		String[] Skills = Value.split("/");
		                	for (String Skill_String: Skills)
		                		Skill_List.add(Skill_String);
	                	}
	                	else
	                		Skill_List.add(Value);
	                	pd.Has_Skills = Skill_List;
	                }
	                else if (Setting_Name.contains("Has_Achievements"))
	                {
	                	ArrayList<String> Achievement_List = new ArrayList<String>();
	                	if (Value.contains("/"))
	                	{
	                		String[] Achievements = Value.split("/");
		                	for (String Achievement_String: Achievements)
		                		Achievement_List.add(Achievement_String);
	                	}
	                	else
	                		Achievement_List.add(Value);
	                	pd.Has_Achievements = Achievement_List;
	                }
				}
				pd.player = player;
				br.close();
				player.setDisplayName(pd.Achievement_Now + TextFormat.WHITE + player.getName() + "(" + pd.Skill_Now + ")" );
				Sub_Code.Players_Info.add(pd);
				Sub_Code.Player_Info_Index.put(player.getName(), Sub_Code.Players_Info.size() - 1);
			}
			else
			{
				pd.Skill_Now = "Miner";
				pd.Achievement_Now = "";
				ArrayList<String> Skills = new ArrayList<String>();
				Skills.add("Miner");
				pd.Has_Skills = Skills;
				ArrayList<String> Achi = new ArrayList<String>();
				pd.Has_Achievements = Achi;
				pd.player = player;
				Sub_Code.Players_Info.add(pd);
				Sub_Code.Player_Info_Index.put(player.getName(), Sub_Code.Players_Info.size() - 1);
				player.setDisplayName(player.getName() + "(" + pd.Skill_Now + ")" );
				Save_Player_Data(pd);
			}	
		}
		catch (IOException e)
		{
			Sub_Code.Server_Main.getLogger().info("プレイヤー(" + player.getName() + ")の読み込み中にエラーが発生しました。\nエラー:" + e.getMessage());
		}
	}
	public static void Save_Player_Data(Player_Data player)
	{
		if (player == null)
			return;
		try
		{
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Sub_Code.Plugin_Dir + "/Configs/" + player.player.getName() + ".yaml"), "UTF-8"));
			fw.write("Skill_Now:" + player.Skill_Now + "\r\n");
			if (player.Achievement_Now != "")
			    fw.write("Achievement_Now:" + player.Achievement_Now + "\r\n");
			if (player.Has_Skills.size() > 0)
			{
				String Skill_Text = "";
				for (int Number = 0; Number < player.Has_Skills.size(); Number++)
				{
					if (Skill_Text == "")
						Skill_Text = player.Has_Skills.get(Number);
					else
						Skill_Text += "/" + player.Has_Skills.get(Number);
				}
				fw.write("Has_Skills:" + Skill_Text + "\r\n");
			}
			if (player.Has_Achievements.size() > 0)
			{
				String Achievement_Text = "";
				for (int Number = 0; Number < player.Has_Achievements.size(); Number++)
				{
					if (Achievement_Text == "")
						Achievement_Text = player.Has_Achievements.get(Number);
					else
						Achievement_Text += "/" + player.Has_Achievements.get(Number);
				}
				fw.write("Has_Achievements:" + Achievement_Text + "\r\n");
			}
			fw.write("Player_PT:" + String.valueOf(player.PT + "\r\n"));
			fw.write("Voice_Name:" + player.Voice_Name + "\r\n");
			fw.write("Voice_Volume:" + String.valueOf(player.Voice_Volume + "\r\n"));
			fw.write("Voice_Pitch:" + String.valueOf(player.Voice_Pitch + "\r\n"));
			fw.write("SE_Volume:" + String.valueOf(player.SE_Volume));
			fw.close();
		}
		catch (IOException e)
		{
			Sub_Code.Server_Main.getLogger().info("プレイヤー(" + player.player.getName() + ")の書き込み中にエラーが発生しました。\nエラー:" + e.getMessage());
		}
	}
}