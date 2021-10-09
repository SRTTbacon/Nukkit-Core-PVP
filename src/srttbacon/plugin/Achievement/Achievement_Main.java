package srttbacon.plugin.Achievement;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Save_Load;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Achievement_Class;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.Window;

public class Achievement_Main
{
	public static class All_Achievement_Save
	{
		public Vector3 Pos;
		public String Name;
		public String Hint;
	}
	public static ArrayList<All_Achievement_Save> All_Achievement_List = new ArrayList<All_Achievement_Save>();
	public static void Commands(Player p, Command cmd, String[] args)
	{
		Player_Data p_data = Sub_Code.Get_Player_Data(p);
		if (cmd.getName().equalsIgnoreCase("Set_Achievement"))
		{
			if (args.length == 0)
			{
				p.sendMessage("引数1に称号名を指定してください。");
				return;
			}
			for (All_Achievement_Save aas: All_Achievement_List)
			{
				if (aas.Name.contains(args[0]))
				{
					p.sendMessage("既に似ている称号が存在します。該当称号名:" + aas.Name);
					return;
				}
			}
			for (Achievement_Class ac: Sub_Code.Achievement_List)
			{
				if (ac.Player_Name == p.getName())
				{
					p.sendMessage("既にコマンドが実行されています。");
					return;
				}
			}
			if  (args[0].contains(","))
			{
				p.sendMessage("','(カンマ)が含まれているため実行できませんでした。");
				return;
			}
			Achievement_Class ac = new Achievement_Class();
			ac.Player_Name = p.getName();
			ac.Tick = Sub_Code.Server_Main.getTick();
			ac.Achievement_Name = args[0];
			if (args.length >= 2)
			{
				if  (args[1].contains(","))
				{
					p.sendMessage("','(カンマ)が含まれているため実行できませんでした。");
					return;
				}
				ac.Hint = args[1];
			}
			else
			    ac.Hint = "ヒントなし";
			Sub_Code.Achievement_List.add(ac);
			p.sendMessage("設定しました。ダイヤモンドのツルハシで看板を指定してください。");
		}
		else if (cmd.getName().equalsIgnoreCase("Get_Achievements"))
		{
			if (All_Achievement_List.size() == 0)
			{
				p.sendMessage("登録されている称号はありません。");
				return;
			}
			for (All_Achievement_Save as: All_Achievement_List)
				p.sendMessage(as.Name);
		}
		else if (cmd.getName().equalsIgnoreCase("Get_My_Achievements"))
		{
			if (p_data.Has_Achievements.size() == 0)
			{
				p.sendMessage("所持している称号はありません。");
				return;
			}
			p.sendMessage(TextFormat.GREEN + "---現在所持している称号名---");
			for (String Name: p_data.Has_Achievements)
				p.sendMessage(Name);
		}
		else if (cmd.getName().equalsIgnoreCase("Delete_Achievements"))
		{
			if (args.length == 0)
			{
				p.sendMessage("引数1に称号名を指定してください。");
				return;
			}
			for (All_Achievement_Save aas: All_Achievement_List)
			{
				if (aas.Name.contains(args[0]))
				{
					Sub_Code.Server_Main.broadcastMessage(p_data.player.getDisplayName() + TextFormat.GREEN + "が称号:" + aas.Name + TextFormat.GREEN + "を削除しました。");
					All_Achievement_List.remove(aas);
					for (Player_Data p_data_1: Sub_Code.Players_Info)
					{
						boolean IsExist = false;
						for (String temp: p_data_1.Has_Achievements)
						{
							if (temp.contains(args[0]))
							{
								p_data.Has_Achievements.remove(temp);
								IsExist = true;
								break;
							}
						}
						if (IsExist)
							break;
					}
					return;
				}
			}
			p.sendMessage(args[0] + "という称号は存在しません。");
		}
		else if (cmd.getName().equalsIgnoreCase("Delete_My_Achievement"))
		{
			if (args.length == 0)
			{
				p.sendMessage("引数1に称号名を指定してください。");
				return;
			}
			for (String temp: p_data.Has_Achievements)
			{
				if (temp.contains(args[0]))
				{
					p_data.Has_Achievements.remove(temp);
					if (p_data.Achievement_Now.contains(args[0]))
						p_data.Achievement_Now = "";
					p.sendMessage(TextFormat.GREEN + "称号:" + temp + TextFormat.GREEN + "をプレイヤーデータから削除しました。");
					return;
				}
			}
			p.sendMessage(args[0] + "という名前の称号は存在しません。");
		}
		else if (cmd.getName().equalsIgnoreCase("Save_Main"))
		{
			Save_Load.Save_Main();
			Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がメイン設定を保存しました。");
		}
		else if (cmd.getName().equalsIgnoreCase("Load_Main"))
		{
			Save_Load.Load_Main();
			Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がメイン設定をロードしました。");
		}
	}
	public static void onInteract(PlayerInteractEvent e)
	{
		Item item = e.getItem();
		Player p = e.getPlayer();
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			Block b = e.getBlock();
			int b_x = (int)b.x;
			int b_y = (int)b.y;
			int b_z = (int)b.z;
			if (item.getId() == Item.DIAMOND_PICKAXE)
			{
				if (b.getId() == Block.WALL_SIGN || b.getId() == Block.SIGN_POST)
				{
					boolean IsExist = false;
					Achievement_Class Achi = null;
					for (Achievement_Class ac: Sub_Code.Achievement_List)
					{
						if (ac.Player_Name == p.getName())
						{
							Achi = ac;
							IsExist = true;
						}
					}
					if (!IsExist || Achi == null)
					{
						p.sendMessage("先に/Set_Achivementコマンドを実行してください。");
						return;
					}
					for (All_Achievement_Save aas: All_Achievement_List)
					{
						if (aas.Pos.x == b_x && aas.Pos.y == b_y && aas.Pos.z == b_z)
						{
							p.sendMessage("その看板は既に称号が割り当てられています。称号名:" + aas.Name);
							return;
						}
					}
					All_Achievement_Save all_achi = new All_Achievement_Save();
					all_achi.Name = Achi.Achievement_Name;
					all_achi.Hint = Achi.Hint;
					all_achi.Pos = new Vector3(b_x, b_y, b_z);
					All_Achievement_List.add(all_achi);
					for (int Number = 0; Number < Sub_Code.Achievement_List.size(); Number++)
					{
						if (Sub_Code.Achievement_List.get(Number).Player_Name == p.getName() && Sub_Code.Achievement_List.get(Number).Tick + 10 < Sub_Code.Server_Main.getTick())
						{
							Sub_Code.Achievement_List.remove(Number);
							break;
						}
					}
					Window.Achievement_Show(p, All_Achievement_List.get(All_Achievement_List.size() - 1));
				}
			}
			else if (b.getId() == Block.WALL_SIGN || b.getId() == Block.SIGN_POST)
			{
				for (All_Achievement_Save aas: All_Achievement_List)
				{
					if (aas.Pos.x == b_x && aas.Pos.y == b_y && aas.Pos.z == b_z)
					{
						Player_Data p_data = Sub_Code.Get_Player_Data(p);
						if (!p_data.Has_Achievements.contains(aas.Name))
						{
							p_data.Has_Achievements.add(aas.Name);
							p.level.addSound(p.getLocation(), Sound.RANDOM_LEVELUP, 0.75f, 1.0f);
							p.sendMessage(TextFormat.GREEN + "称号:" + aas.Name + TextFormat.GREEN + "をゲットしました。");
						}
						break;
					}
				}
			}
		}
		else if (e.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			if (item.getId() == Item.DIAMOND_PICKAXE)
			{
				for (int Number = 0; Number < Sub_Code.Achievement_List.size(); Number++)
				{
					if (Sub_Code.Achievement_List.get(Number).Player_Name == p.getName() && Sub_Code.Achievement_List.get(Number).Tick + 10 < Sub_Code.Server_Main.getTick())
					{
						Sub_Code.Achievement_List.remove(Number);
						p.sendMessage("称号の割り当てをキャンセルしました。");
						break;
					}
				}
			}
		}
	}
}