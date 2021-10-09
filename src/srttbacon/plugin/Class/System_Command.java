package srttbacon.plugin.Class;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Game;
import srttbacon.plugin.Lobby;
import srttbacon.plugin.MainCode;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Map.Map_Setting;

public class System_Command
{
    public static void onCommand(Player p, Command cmd, String[] args)
    {
        //ゲーム開始
	    if (cmd.getName().equalsIgnoreCase("Start_Game"))
	    {
	    	if (Map_Setting.Map_Contents.size() == 0)
	    	{
	    		p.sendMessage("マップが登録されていません。");
	    	   return ;
	    	}
	    	Sub_Code.Server_Main.broadcastMessage(TextFormat.GREEN + "---ゲームを開始します---");
	    	Lobby.Game_Starting = true;
	    	Lobby.Teleport();
	    	for(Player player : Server.getInstance().getOnlinePlayers().values())
			{
	    	    player.setGamemode(0);
				//謎
				if (player.getName().equalsIgnoreCase("GuminoAme") || player.getName().equalsIgnoreCase("SRTTbacon7839"))
				{
					int random = MainCode.r.nextInt(3);
					if (random == 0)
				        player.sendMessage(TextFormat.RED + "殺戮の時間だ。愚かな人間よ、せいぜい逃げ惑うがよい...");
					else if (random == 1)
					    player.sendMessage(TextFormat.RED + "この感覚...ふっ、どうやら時間のようだな...");
					else if (random == 2)
					    player.sendMessage(TextFormat.RED + "さぁ、ゲームを始めよう...");
				}
			}
	    }
	    //ゲーム終了
	    else if (cmd.getName().equalsIgnoreCase("Stop_Game"))
		{
	    	if (Game.Map_Now != null)
	    		Game.Send_Victory_OR_Defeat(Sub_Code.Team.None);
		}
	    //ロビーから試合場所へ移動
	    else if (cmd.getName().equalsIgnoreCase("Game"))
	    {
	    	Player_Data p_data = Sub_Code.Get_Player_Data(p);
	    	if (p_data.IsLobbyMode)
	    	    Lobby.Player_Game_Start(p_data);
	    	else
	    		p.sendMessage(TextFormat.RED + "既に戦闘マップに移動しています。");
	    }
	    //フェーズを変更
	    else if (cmd.getName().equalsIgnoreCase("Set_Phase"))
	    {
	    	if (args.length == 0)
	    	{
	    		p.sendMessage(TextFormat.GREEN + "引数にフェーズ番号を指定してください。");
	    		return;
	    	}
	    	if (args[0].equalsIgnoreCase("1"))
	    	{
	    		Game.Time_Total_Seconds = 60;
	    		Game.Time_Seconds = 0;
	    		Game.Time_Minutes = 1;
	    	}
	    	else if (args[0].equalsIgnoreCase("2"))
	    	{
	    		Game.Time_Total_Seconds = 600;
	    		Game.Time_Seconds = 0;
	    		Game.Time_Minutes = 10;
	    	}
	    	else if (args[0].equalsIgnoreCase("3"))
	    	{
	    		Game.Time_Total_Seconds = 1200;
	    		Game.Time_Seconds = 0;
	    		Game.Time_Minutes = 20;
	    	}
	    	else if (args[0].equalsIgnoreCase("4"))
	    	{
	    		Game.Time_Total_Seconds = 1800;
	    		Game.Time_Seconds = 0;
	    		Game.Time_Minutes = 30;
	    	}
	    }
	    //プレイヤーPTを変更(許可が無い限り使用してはならない)
	    else if (cmd.getName().equalsIgnoreCase("Set_PT"))
	    {
	    	if (args.length == 0)
	    	{
	    		p.sendMessage(TextFormat.GREEN + "引数にPTを入れてください。");
	    		return;
	    	}
	    	try
	    	{
		    	Player_Data p_data = Sub_Code.Get_Player_Data(p);
		    	p_data.PT = Integer.valueOf(args[0]);
		    	ScoreBoard_Show.Change_PT(p_data);
		    	p.sendMessage(TextFormat.GREEN + "PTを設定しました。");
	    	}
	    	catch (Exception e)
	    	{
	    		p.sendMessage(TextFormat.RED + "エラーが発生しました。引数は数字のみにしてください。");
	    	}
	    }
	    else if (cmd.getName().equalsIgnoreCase("Set_Sky_Text"))
	    {
	    	if (args.length < 2)
	    	{
	    		p.sendMessage(TextFormat.GREEN + "引数1にタイトル、引数2に内容を入力してください。");
	    		return;
	    	}
	    	Sub_Code.Add_Text(p.getPosition(), args[0], args[1]);
        }
        else if (cmd.getName().equalsIgnoreCase("Sound_Setting"))
        {
            Window.Sound_Setting_Show(Sub_Code.Get_Player_Data(p));
        }
		else if (cmd.getName().equalsIgnoreCase("Achievement_Hint"))
		{
			PlaySound.PlaySoundOne(Sub_Code.Get_Player_Data(p), "system.menu", p.getPosition());
			Window.Achievement_Hint_Show(p);
		}
    }
}