package srttbacon.plugin;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.food.Food;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;
import srttbacon.plugin.Map.Map_Setting;
import srttbacon.plugin.Map.Ore_Restore;

public class Loop extends NukkitRunnable
{
	public Loop()
	{
		
	}
	boolean Game_Starting = false;
	@Override
	public void run()
	{
		if (Server.getInstance().getOnlinePlayers().size() == 0)
			return;
		Player Random_Player = null;
		for (Player_Data player: Sub_Code.Players_Info)
		{
			Random_Player = player.player;
			if (player.IsLobbyMode)
				player.player.getFoodData().addFoodLevel(Food.beetroot);
			if (player.CT > 0)
				player.CT--;
			if (player.CPS >= 20)
			{
				player.player.sendMessage(TextFormat.RED + "1秒間のクリック数が多すぎます。このサーバーでは連打ツールを禁止しています。");
				Sub_Code.Server_Main.getLogger().info(player.player.getName() + "が連打ツールを使用している可能性があります。管理者は確認してください。");
			}
			player.FallDamageTimer++;
			player.Skill_Time++;
			player.CPS = 0;
			if (player.Skill_Now == "忍者" && player.Skill_Time > 5 && player.IsNinjaSkillUsing)
			{
				for (Player player2: Server.getInstance().getOnlinePlayers().values())
				    player2.showPlayer(player.player);
				player.IsNinjaSkillUsing = false;
			}
			else if (player.Skill_Now == "Berserker" && player.Skill_Time > 20 && player.IsBerserkerUsing)
			{
				player.player.setMaxHealth(player.Before_Max_Health);
				player.IsBerserkerUsing = false;
			}
			if (player.IsNoMoveMode)
			{
				if (player.No_Move_Time < 4)
					player.No_Move_Time++;
				else
					player.IsNoMoveMode = false;
			}
			if (!player.IsLobbyMode)
			    Sub_Code.Set_Player_Name(player);
		}
		if (Game.Map_Now != null)
		{
			Game.Time_Total_Seconds++;
			Game.Time_Seconds++;
			if (Game.Time_Seconds >= 60)
			{
				Game.Time_Seconds = 0;
				Game.Time_Minutes++;
			}
			if (Game.Time_Minutes >= 10 && Game.Phase < 2)
			{
				Game.Phase = 2;
				Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN + "【システム】 Phase2へ移行    敵のコアが破壊可能になりました。");
				for (Player player: Server.getInstance().getOnlinePlayers().values())
					player.level.addSound(player.getLocation(), Sound.MOB_ENDERDRAGON_GROWL, 0.2f, 1.0f, player);
				ScoreBoard_Show.Update_Phase();
			}
			if (Game.Time_Minutes >= 20 && Game.Phase < 3)
			{
				Game.Phase = 3;
				Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN + "【システム】 Phase3へ移行    ダイヤモンドの採取が可能になり、コアへのダメージが2倍になります。");
				for (Player player: Server.getInstance().getOnlinePlayers().values())
					player.level.addSound(player.getLocation(), Sound.MOB_ENDERDRAGON_GROWL, 0.2f, 1.0f, player);
				ScoreBoard_Show.Update_Phase();
			}
			if (Game.Time_Minutes >= 30 && Game.Phase < 4)
			{
				Game.Phase = 4;
				Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN + "【システム】 Phase4へ移行    コアへのダメージが3倍になり、15秒ごとにコアHPが1ずつ減少します。");
				for (Player player: Server.getInstance().getOnlinePlayers().values())
					player.level.addSound(player.getLocation(), Sound.MOB_ENDERDRAGON_GROWL, 0.2f, 1.0f, player);
				ScoreBoard_Show.Update_Phase();
			}
			if (Game.Game_End_Time > 0)
				Game.Game_End_Time++;
			if (Game.Game_End_Time > 15)
			{
				Game.Game_End_Time = 0;
				Game.Game_End();
			}
			if (Game.Phase == 4 && Game.Time_Seconds % 15 == 0)
			{
				if (Game.Blue_Team_HP > 1)
					Game.Blue_Team_HP--;
				if (Game.Red_Team_HP > 1)
					Game.Red_Team_HP--;
				ScoreBoard_Show.Update_Blue_Team();
				ScoreBoard_Show.Update_Red_Team();
			}
			for (int Number = 0; Number < Map_Setting.Map_Ore_Restore_List.size(); Number++)
			{
				Ore_Restore or = Map_Setting.Map_Ore_Restore_List.get(Number);
				or.Time--;
				if (or.Time <= 0)
				{
					int x = (int)or.block.x;
					int y = (int)or.block.y;
					int z = (int)or.block.z;
					Random_Player.level.setBlock(or.block.getLocation(), or.block);
					Map_Setting.Map_Ore_Restore_List.remove(Number);
					for (Vector3 pos: Map_Setting.Map_Put_Stone_List)
					{
						if (pos.x == x && pos.y == y && pos.z == z)
						{
							Map_Setting.Map_Put_Stone_List.remove(pos);
							break;
						}
					}
					Number--;
				}
			}
		}
		else
		{
			if (Game_Starting && Lobby.Game_Start_Count_Down < Lobby.Game_Start_Count_Down_Max && Server.getInstance().getOnlinePlayers().size() > 0)
				Lobby.Game_Start_Count_Down--;
			if (Lobby.Game_Starting)
			{
				Lobby.Game_Starting = false;
				Lobby.Game_Start_Count_Down = Lobby.Game_Start_Count_Down_Max;
				Lobby.Game_Start_Count_Down--;
				Game_Starting = true;
			}
			ScoreBoard_Show.Update_Game_Count_Down();
			if (Lobby.Game_Start_Count_Down <= 0)
			{
				if (!Sub_Code.Server_Main.isLevelLoaded("main"))
				{
					Sub_Code.Server_Main.loadLevel("main");
					Sub_Code.Server_Main.setDefaultLevel(Sub_Code.Server_Main.getLevelByName("main"));
				}
				Lobby.Game_Start_Count_Down = Lobby.Game_Start_Count_Down_Max;
				Game.Map_Now = Map_Setting.Map_Contents.get(MainCode.r.nextInt(Map_Setting.Map_Contents.size()));
				//Game.Map_Now = Map_Setting.Map_Contents.get(0);
				for (Player player: Server.getInstance().getOnlinePlayers().values())
				{
					player.setLevel(Sub_Code.Server_Main.getLevelByName("main"));
					player.switchLevel(Sub_Code.Server_Main.getLevelByName("main"));
				}
				if (Sub_Code.Server_Main.isLevelLoaded("main_back"))
				    Sub_Code.Server_Main.unloadLevel(Sub_Code.Server_Main.getLevelByName("main_back"));
				for (Player_Data player: Sub_Code.Players_Info)
				{
					Lobby.Player_Game_Start(player);
				}
				ScoreBoard_Show.Show_All();
			}
		}
		String Second_Text;
		String Minute_Text;
		if (Game.Time_Seconds < 10)
			Second_Text = "0" + Game.Time_Seconds;
		else
			Second_Text = String.valueOf(Game.Time_Seconds);
		if (Game.Time_Minutes < 10)
			Minute_Text = "0" + Game.Time_Minutes;
		else
			Minute_Text = String.valueOf(Game.Time_Minutes);
		Game.Time_Text = Minute_Text + ":" + Second_Text;
		if (Game.Map_Now == null)
			ScoreBoard_Show.Update_Player_Count();
		else
		    ScoreBoard_Show.Update_Game_Time();
		ScoreBoard_Show.Update_CT();
	}
}