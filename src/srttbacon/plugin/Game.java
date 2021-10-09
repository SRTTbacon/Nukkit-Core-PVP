package srttbacon.plugin;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;
import srttbacon.plugin.Map.Map_Setting;
import srttbacon.plugin.Map.Maps;

public class Game
{
	public final static int Max_Core_HP = 150;
	public final static int Victory_PT = 100;
	public final static int Defeat_PT = 50;
	public static int Time_Total_Seconds = 0;
	public static int Time_Seconds = 0;
	public static int Time_Minutes = 0;
	public static int Blue_Team_HP = 150;
	public static int Red_Team_HP = 150;
	public static int Blue_Team_Player_Count = 0;
	public static int Red_Team_Player_Count = 0;
	public static int Phase = 1;
	public static int Blue_Break_Time = 0;
	public static int Red_Break_Time = 0;
	public static int Not_Break_Time = 0;
	public static int Game_End_Time = 0;
	public static boolean IsNotBreakCore = false;
	public static String Time_Text = "00:00";
	public static Maps Map_Now = null;
	public static boolean Block_Break(BlockBreakEvent e)
	{
		if (Map_Now != null)
		{
			Block block = e.getBlock();
			if (block.getId() == Block.END_STONE)
			{
				if (IsNotBreakCore)
				{
					e.setCancelled();
					return true;
				}
				Sub_Code.Team Break_Team = Sub_Code.Team.None;
				if (Map_Now.Blue_Team_Core_Position != null)
				{
					if (Map_Now.Blue_Team_Core_Position.x == (int)block.x && Map_Now.Blue_Team_Core_Position.y == (int)block.y && Map_Now.Blue_Team_Core_Position.z == (int)block.z)
						Break_Team = Sub_Code.Team.Blue;
				}
				if (Map_Now.Red_Team_Core_Position != null)
				{
					if (Map_Now.Red_Team_Core_Position.x == (int)block.x && Map_Now.Red_Team_Core_Position.y == (int)block.y && Map_Now.Red_Team_Core_Position.z == (int)block.z)
						Break_Team = Sub_Code.Team.Red;
				}
				if (Break_Team == Sub_Code.Team.None)
					return false;
				Player p = e.getPlayer();
				int Tick_Now = Sub_Code.Server_Main.getTick();
				if (Game.Phase == 1)
				{
					if (Not_Break_Time + 40 < Tick_Now)
					{
						p.sendMessage(TextFormat.RED + "フェーズ1の間はコアを破壊できません。");
						Not_Break_Time = Tick_Now;
					}
					e.setCancelled();
					return true;
				}
				Player_Data p_data = Sub_Code.Get_Player_Data(p);
				if (p_data.Team_Color == Break_Team)
				{
					e.setCancelled();
					return true;
				}
				else if (p_data.Skill_Now == "忍者" && p_data.Skill_Time > 5 && p_data.IsNinjaSkillUsing)
				    Sub_Code.Skill_Achievement(p_data, "忍者");
				if (p_data != null)
				    p_data.Core_Break_Count++;
				if (Break_Team == Sub_Code.Team.Blue)
				{
					if (Blue_Break_Time + 40 < Tick_Now)
					{
					    Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + "が" + TextFormat.BLUE + "青チーム" + TextFormat.WHITE + "のコアを破壊しています。");
						Blue_Break_Time = Tick_Now;
					}
					if (p_data.Skill_Now == "Handyman")
					{
						if (MainCode.r.nextInt(3) == 0)
						{
							Red_Team_HP++;
							p_data.Core_Heal_Count++;
							if (p_data.Core_Heal_Count >= 20)
							    Sub_Code.Skill_Achievement(p_data, "Handyman");
							ScoreBoard_Show.Update_Red_Team();
							Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN+ "Handymanの能力によって" + TextFormat.RED + "赤チーム" + TextFormat.GREEN + "のコアが回復しました。");
						}
					}
					if (Game.Phase == 2)
					    Blue_Team_HP--;
					else if (Game.Phase == 3)
						Blue_Team_HP -= 2;
					else if (Game.Phase == 4)
						Blue_Team_HP -= 3;
					if (Blue_Team_HP < 0)
						Blue_Team_HP = 0;
					ScoreBoard_Show.Update_Blue_Team();
					for(Player player : Server.getInstance().getOnlinePlayers().values())
						if (Sub_Code.Get_Player_Data(player).Team_Color == Break_Team)
						    player.level.addSound(player.getLocation(), Sound.RANDOM_ANVIL_LAND, 0.25f, 1.0f, player);
					if (Blue_Team_HP <= 0)
						Send_Victory_OR_Defeat(Sub_Code.Team.Red);
				}
				else if (Break_Team == Sub_Code.Team.Red)
				{
					if (Red_Break_Time + 40 < Tick_Now)
					{
					    Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + "が" + TextFormat.RED + "赤チーム" + TextFormat.WHITE + "のコアを破壊しています。");
					    Red_Break_Time = Tick_Now;
					}
					if (p_data.Skill_Now == "Handyman")
					{
						if (MainCode.r.nextInt(3) == 0)
						{
							Blue_Team_HP++;
							ScoreBoard_Show.Update_Blue_Team();
							Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN+ "Handymanの能力によって" + TextFormat.BLUE + "青チーム" + TextFormat.GREEN + "のコアが回復しました。");
						}
					}
					if (Game.Phase == 2)
						Red_Team_HP--;
					else if (Game.Phase == 3)
						Red_Team_HP -= 2;
					else if (Game.Phase == 4)
						Red_Team_HP -= 3;
					if (Red_Team_HP < 0)
						Red_Team_HP = 0;
					ScoreBoard_Show.Update_Red_Team();
					for(Player player : Server.getInstance().getOnlinePlayers().values())
						if (Sub_Code.Get_Player_Data(player).Team_Color == Break_Team)
						    player.level.addSound(player.getLocation(), Sound.RANDOM_ANVIL_LAND, 0.25f, 1.0f, player);
					if (Red_Team_HP <= 0)
						Send_Victory_OR_Defeat(Sub_Code.Team.Blue);
				}
				if (!IsNotBreakCore)
				    e.setCancelled();
				return true;
			}
			return false;
		}
		return false;
	}
	public static void Send_Victory_OR_Defeat(Sub_Code.Team Victory_Team)
	{
		IsNotBreakCore = true;
		if (Victory_Team == Sub_Code.Team.Blue)
			Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN + "---試合終了 " + TextFormat.BLUE + "青チーム" + TextFormat.GREEN + "の勝利です！---");
		else if (Victory_Team == Sub_Code.Team.Red)
			Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN + "---試合終了 " + TextFormat.RED + "赤チーム" + TextFormat.GREEN + "の勝利です！---");
		else
			Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN + "---試合終了 管理者によって強制的に終了されました---");
		for(Player player : Server.getInstance().getOnlinePlayers().values())
		{
			Player_Data p_data = Sub_Code.Get_Player_Data(player);
			player.level.addSound(player.getLocation(), Sound.CAULDRON_EXPLODE, 0.95f, 1.0f);
			if (p_data.Team_Color == Victory_Team)
			{
				int Get_PT = Victory_PT + (int)(Math.round((double)p_data.Kill_Count / 2) + (int)(Math.round((double)p_data.Core_Break_Count / 2)));
				p_data.PT += Get_PT;
				if (Victory_Team == Sub_Code.Team.Blue)
					player.sendTitle(TextFormat.BLUE + "あなたのチーム" + TextFormat.WHITE + "の勝利です！", "報酬として" + Get_PT + "PTを得ました。", 10, 100, 20);
				else 
					player.sendTitle(TextFormat.RED + "あなたのチーム" + TextFormat.WHITE + "の勝利です！", "報酬として" + Get_PT + "PTを得ました。", 10, 100, 20);
			}
			else if (p_data.Team_Color != Sub_Code.Team.None)
			{
				int Get_PT = Defeat_PT + (int)(Math.round((double)p_data.Kill_Count / 2) + (int)(Math.round((double)p_data.Core_Break_Count / 2)));
				p_data.PT += Get_PT;
				if (Victory_Team == Sub_Code.Team.Blue)
					player.sendTitle(TextFormat.BLUE + "あなたのチーム" + TextFormat.WHITE + "の敗北です・・・", "報酬として" + Get_PT + "PTを得ました。", 10, 100, 20);
				else 
					player.sendTitle(TextFormat.RED + "あなたのチーム" + TextFormat.WHITE + "の敗北です・・・", "報酬として" + Get_PT + "PTを得ました。", 10, 100, 20);
			}
			else
			{
				int Get_PT = 10;
				p_data.PT += Get_PT;
				if (Victory_Team == Sub_Code.Team.Blue)
					player.sendTitle(TextFormat.BLUE + "あなたのチーム" + TextFormat.WHITE + "の敗北です・・・", "報酬として" + Get_PT + "PTを得ました。", 10, 100, 20);
				else 
					player.sendTitle(TextFormat.RED + "あなたのチーム" + TextFormat.WHITE + "の敗北です・・・", "報酬として" + Get_PT + "PTを得ました。", 10, 100, 20);
			}
		}
		Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN + "!---数秒後、ロビーへ移動します。---!");	
		Game_End_Time++;
	}
	public static void Game_End()
	{
		Lobby.Teleport();
		for(Player player : Server.getInstance().getOnlinePlayers().values())
		{
			Player_Data p_data = Sub_Code.Get_Player_Data(player);
			p_data.CT = 0;
			p_data.FallDamageTimer = 0;
			p_data.IsLobbyMode = true;
			p_data.IsNinjaSkillUsing = false;
			p_data.IsNoMoveMode = false;
			p_data.IsSkillGot = false;
			p_data.Kill_Count = 0;
			p_data.No_Drop_Item = "";
			p_data.No_Move_Time = 0;
			p_data.Scanner_Effect_Time = 0;
			p_data.Skill_Time = 0;
			p_data.Team_Color = Sub_Code.Team.None;
			for(Player player1 : Server.getInstance().getOnlinePlayers().values())
				player.showPlayer(player1);
			player.removeAllEffects();
			player.getInventory().clearAll();
			player.setExperience(0, 0);
			player.setMaxHealth(20);
			Sub_Code.Set_Player_Name(p_data);
		}
		Map_Setting.Map_Restore(Map_Now.Map_Name);
		Map_Now = null;
		Time_Text = "00:00";
		Phase = 1;
		Time_Total_Seconds = 0;
		Time_Seconds = 0;
		Time_Minutes = 0;
		Blue_Team_HP = Max_Core_HP;
		Red_Team_HP = Max_Core_HP;
		Blue_Team_Player_Count = 0;
		Red_Team_Player_Count = 0;
		IsNotBreakCore = false;
		ScoreBoard_Show.Show_All();
	}
}