package srttbacon.plugin.Class;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Game;
import srttbacon.plugin.Lobby;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.ScoreBoard.ScoreBoard_Data;
import srttbacon.plugin.Class.ScoreBoard.api.ScoreboardAPI;
import srttbacon.plugin.Class.ScoreBoard.network.DisplaySlot;

public class ScoreBoard_Show
{
	//スコアボード メイン
	public static ArrayList<ScoreBoard_Data> Score_Data = new ArrayList<ScoreBoard_Data>();
	public static void Show_All()
	{
		for(Player player : Server.getInstance().getOnlinePlayers().values())
			Show_Player(Sub_Code.Get_Player_Data(player));
	}
	public static void Show_Player(Player_Data p)
	{
		if (p == null)
			return;
		int Index = -1;
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			if (Data.Player_Name == p.player.getName())
			{
				Index = Number;
				break;
			}
		}
		ScoreBoard_Data ScoreData = null;
		if (Index == -1)
		{
			ScoreBoard_Data Data = new ScoreBoard_Data();
			Data.Player_Name = p.player.getName();
			Score_Data.add(Data);
			ScoreData = Score_Data.get(Score_Data.size() - 1);
		}
		else
			ScoreData = Score_Data.get(Index);
		if (ScoreData.Blue_Team_Text != null)
		{
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Blue_Team_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Red_Team_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Team_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Phase_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Game_Time_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.CT_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Map_Name_Text);
		}
		if (ScoreData.Player_Count_Text != null)
		{
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Player_Count_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Player_Skill_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Player_Point_Text);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.Game_Count_Text);
		}
		ScoreboardAPI.removeScorebaord(p.player, ScoreData.scoreboard);
		ScoreData.scoreboardDisplay = ScoreData.scoreboard.addDisplay( DisplaySlot.SIDEBAR, "スコアボード", "    BattleCry.xyz    " );
		if (Game.Map_Now == null)
		{
			ScoreData.Player_Count_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "サーバー人数:" + Server.getInstance().getOnlinePlayers().values().size(), 0);
			ScoreData.Player_Skill_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "職業:" + p.Skill_Now, 1);
			ScoreData.Player_Point_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "所持PT:" + p.PT, 2);
			ScoreData.Game_Count_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "ゲーム開始まで: xxx", 3);
		}
		else
		{
			ScoreData.Map_Name_Text = ScoreData.scoreboardDisplay.addLine("マップ名:" + Game.Map_Now.Map_Name, -1);
			ScoreData.Blue_Team_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.BLUE + "青チーム" + TextFormat.WHITE + " - コアHP:150", 0);
			ScoreData.Red_Team_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.RED + "赤チーム" + TextFormat.WHITE + " - コアHP:150", 1);
			ScoreData.Team_Text = ScoreData.scoreboardDisplay.addLine("§b", 2);
			ScoreData.Phase_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "フェーズ:1", 3);
			ScoreData.Game_Time_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "ゲーム時間 : 00:00", 4);
			ScoreData.Player_Skill_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "職業:" + p.Skill_Now, 5);
			ScoreData.CT_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "CT: 0", 6);
		}
		ScoreboardAPI.setScoreboard(p.player, ScoreData.scoreboard);
	}
	public static void Add_CT_Text(Player_Data player)
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			if (Data.Player_Name == player.player.getName())
			{
				Data.scoreboardDisplay.removeEntry(Data.CT_Text);
				Data.CT_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "CT: " + player.CT, 5);
				break;
			}
		}
	}
	public static void Change_Skill(Player_Data player)
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			if (Data.Player_Name == player.player.getName())
			{
				Data.scoreboardDisplay.removeEntry(Data.Player_Skill_Text);
				if (Game.Map_Now == null)
				    Data.Player_Skill_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "職業:" + player.Skill_Now, 1);
				else
					Data.Player_Skill_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "職業:" + player.Skill_Now, 5);
				break;
			}
		}
	}
	public static void Change_PT(Player_Data player)
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			if (Data.Player_Name == player.player.getName())
			{
				Data.scoreboardDisplay.removeEntry(Data.Player_Point_Text);
				if (Game.Map_Now == null)
				    Data.Player_Point_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "所持PT:" + player.PT, 2);
				break;
			}
		}
	}
	public static void Change_Team(Player_Data player)
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			if (Data.Player_Name == player.player.getName())
			{
				Data.scoreboardDisplay.removeEntry(Data.Team_Text);
				if (Game.Map_Now != null)
				{
					if (player.Team_Color == Sub_Code.Team.Blue)
					    Data.Team_Text = Data.scoreboardDisplay.addLine(TextFormat.GREEN + "-あなたは" + TextFormat.BLUE + "青チーム" + TextFormat.GREEN + "です-", 2);
					else if (player.Team_Color == Sub_Code.Team.Red)
					    Data.Team_Text = Data.scoreboardDisplay.addLine(TextFormat.GREEN + "-あなたは" + TextFormat.RED + "赤チーム" + TextFormat.GREEN + "です-", 2);
					else
					    Data.Team_Text = Data.scoreboardDisplay.addLine("§b", 2);
				}
				break;
			}
		}
	}
	public static void Update_Game_Count_Down()
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			Data.scoreboardDisplay.removeEntry(Data.Game_Count_Text);
			if (Lobby.Game_Start_Count_Down == Lobby.Game_Start_Count_Down_Max)
				Data.Game_Count_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "ゲーム開始まで: xxx", 3);
			else
			    Data.Game_Count_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "ゲーム開始まで: " + Lobby.Game_Start_Count_Down, 3);
		}
	}
	public static void Update_Player_Count()
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			if (Game.Map_Now == null)
			{
				Data.scoreboardDisplay.removeEntry(Data.Player_Count_Text);
				Data.Player_Count_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "サーバー人数:" + Server.getInstance().getOnlinePlayers().values().size(), 0);
			}
		}
	}
	public static void Hide_All()
	{
		for(Player player : Server.getInstance().getOnlinePlayers().values())
			Hide_Player(player);
	}
	public static void Hide_Player(Player p)
	{
		int Index = -1;
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			if (Data.Player_Name == p.getName())
			{
				Index = Number;
				break;
			}
		}
		if (Index == -1)
			return;
		ScoreBoard_Data ScoreData = Score_Data.get(Index);
		ScoreboardAPI.removeScorebaord(p, ScoreData.scoreboard);
	}
	public static void Update_Blue_Team()
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			Data.scoreboardDisplay.removeEntry(Data.Blue_Team_Text);
			Data.Blue_Team_Text = Data.scoreboardDisplay.addLine(TextFormat.BLUE + "青チーム" + TextFormat.WHITE + " - コアHP:" + Game.Blue_Team_HP, 0);
		}
	}
	public static void Update_Red_Team()
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			Data.scoreboardDisplay.removeEntry(Data.Red_Team_Text);
			Data.Red_Team_Text = Data.scoreboardDisplay.addLine(TextFormat.RED + "赤チーム" + TextFormat.WHITE + " - コアHP:" + Game.Red_Team_HP, 1);
		}
	}
	public static void Update_Phase()
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			Data.scoreboardDisplay.removeEntry(Data.Phase_Text);
			Data.Phase_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "フェーズ:" + Game.Phase, 3);
		}
	}
	public static void Update_Game_Time()
	{
		if (Game.Map_Now == null)
			return;
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			ScoreBoard_Data Data = Score_Data.get(Number);
			Data.scoreboardDisplay.removeEntry(Data.Game_Time_Text);
			Data.Game_Time_Text = Data.scoreboardDisplay.addLine(TextFormat.WHITE + "ゲーム時間 - " + Game.Time_Text, 4);
		}
	}
	public static void Update_CT()
	{
		for (int Number = 0; Number < Score_Data.size(); Number++)
		{
			int Index = -1;
			for (int Number_01 = 0; Number_01 < Sub_Code.Players_Info.size(); Number_01++)
			{
				if (Sub_Code.Players_Info.get(Number_01).player.getName() == Score_Data.get(Number).Player_Name)
				{
					Index = Number_01;
					break;
				}
			}
			if (Index == -1)
				continue;
			Player_Data P_Info = Sub_Code.Players_Info.get(Index);
			ScoreBoard_Data ScoreData = Score_Data.get(Number);
			ScoreData.scoreboardDisplay.removeEntry(ScoreData.CT_Text);
			if (P_Info.CT > 0)
			{
				ScoreData.CT_Text = ScoreData.scoreboardDisplay.addLine(TextFormat.WHITE + "CT: " + P_Info.CT, 5);
			}
		}
	}
}