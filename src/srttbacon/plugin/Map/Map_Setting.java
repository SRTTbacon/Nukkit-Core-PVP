package srttbacon.plugin.Map;

import java.io.File;
import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Game;
import srttbacon.plugin.Lobby;
import srttbacon.plugin.Save_Load;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Block_Copy;
import srttbacon.plugin.Class.Block_Replace;
import srttbacon.plugin.Class.Map_Select;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Map_Setting
{
	public static ArrayList<Maps> Map_Contents = new ArrayList<Maps>();
	public static ArrayList<Ore_Restore> Map_Ore_Restore_List = new ArrayList<Ore_Restore>();
	public static ArrayList<Vector3> Map_Put_Stone_List = new ArrayList<Vector3>();
	public static boolean Set_Blue_Core_Mode = false;
	public static boolean Set_Red_Core_Mode = false;
	public static String Set_Core_Map_Name = "";
	public static void Block_Place(Player p, Block block)
	{
	     if (Set_Blue_Core_Mode && block.getId() == Block.END_STONE)
		 {
	    	 Set_Blue_Core_Mode = false;
			 Maps map = Sub_Code.Get_Map_By_Name(Set_Core_Map_Name);
	    	 if (map == null)
	    	 {
	    		 p.sendMessage("マップが見つからなかったため、操作はキャンセルされました。");
	    		 return;
	    	 }
			 map.Blue_Team_Core_Position = new Vector3((int)block.x, (int)block.y, (int)block.z);
			 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + "がマップ名:" + Set_Core_Map_Name + "|" + TextFormat.BLUE + "青チーム" + TextFormat.WHITE + "のコアを設定しました。");
			 return;
		 }
		 else if (Set_Red_Core_Mode && block.getId() == Block.END_STONE)
		 {
			 Set_Red_Core_Mode = false;
			 Maps map = Sub_Code.Get_Map_By_Name(Set_Core_Map_Name);
	    	 if (map == null)
	    	 {
	    		 p.sendMessage("マップが見つからなかったため、操作はキャンセルされました。");
	    		 return;
	    	 }
			 map.Red_Team_Core_Position = new Vector3((int)block.x, (int)block.y, (int)block.z);
			 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + "がマップ名:" + Set_Core_Map_Name + "|" + TextFormat.RED + "赤チーム" + TextFormat.WHITE + "のコアを設定しました。");
			 return;
		 }
	}
	public static void Right_Click(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Item item = e.getItem();
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			 if (item.getId() == Item.STONE_AXE)
			 {
				 for (int Number = 0; Number < Sub_Code.Block_Replace_List.size(); Number++)
				 {
					 if (Sub_Code.Block_Replace_List.get(Number).Player_Name.getName() == p.getName())
					 {
						 Sub_Code.Block_Replace_List.remove(Number);
						 break;
					 }
				 }
				 Block block = e.getBlock();
				 Block_Replace Temp = new Block_Replace();
				 Temp.Start_Position = new Vector3(block.x, block.y, block.z);
				 Temp.Player_Name = p;
				 Temp.Tick = Sub_Code.Server_Main.getTick();
				 Sub_Code.Block_Replace_List.add(Temp);
				 p.sendMessage(TextFormat.YELLOW + "置換開始位置:" + (int)block.x + ", " + (int)block.y + ", " + (int)block.z);
			 }
			 else if (item.getId() == Item.GOLD_AXE)
			 {
				 int Index = -1;
				 for (int Number = 0; Number < Sub_Code.Block_Copy_List.size(); Number++)
				 {
					 if (Sub_Code.Block_Copy_List.get(Number).Player_Name.getName() == p.getName())
					 {
						 Index = Number;
						 break;
					 }
				 }
				 if (Index == -1)
				 {
					 Block block = e.getBlock();
					 Block_Copy Temp = new Block_Copy();
					 Temp.Start_Position = new Vector3(block.x, block.y, block.z);
					 Temp.Player_Name = p;
					 Temp.Tick = Sub_Code.Server_Main.getTick();
					 Sub_Code.Block_Copy_List.add(Temp);
					 p.sendMessage(TextFormat.YELLOW + "コピー開始位置:" + (int)block.x + ", " + (int)block.y + ", " + (int)block.z); 
				 }
				 else
				 {
					 Block_Copy Before = Sub_Code.Block_Copy_List.get(Index);
					 Block block = e.getBlock();
					 Block_Copy Temp = new Block_Copy();
					 Temp.Start_Position = Before.Start_Position;
					 Temp.End_Position = new Vector3(block.x, block.y, block.z);
					 Temp.Player_Name = Before.Player_Name;
					 Temp.Tick = Sub_Code.Server_Main.getTick();
					 Sub_Code.Block_Copy_List.remove(Index);
					 Sub_Code.Block_Copy_List.add(Temp);
					 p.sendMessage("終了地点を設定しました。/Set_Copyコマンドを実行してください。");
				 }
			 }
			 else if (item.getId() == Item.DIAMOND_AXE)
			 {
				 int Index = -1;
				 for (int Number = 0; Number < Sub_Code.Map_Select_List.size(); Number++)
				 {
					 if (Sub_Code.Map_Select_List.get(Number).Player_Name == p.getName())
					 {
						 Index = Number;
						 break;
					 }
				 }
				 if (Index == -1)
				 {
					 Block block = e.getBlock();
					 Map_Select Temp = new Map_Select();
					 Temp.Start_Pos = block.getLocation();
					 Temp.Player_Name = p.getName();
					 Temp.Tick = Sub_Code.Server_Main.getTick();
					 Sub_Code.Map_Select_List.add(Temp);
					 p.sendMessage(TextFormat.YELLOW + "マップ開始位置:" + (int)block.x + ", " + (int)block.y + ", " + (int)block.z); 
				 }
				 else
				 {
					 Map_Select Before = Sub_Code.Map_Select_List.get(Index);
					 Block block = e.getBlock();
					 Map_Select Temp = new Map_Select();
					 Temp.Start_Pos = Before.Start_Pos;
					 Temp.End_Pos = block.getLocation();
					 Temp.Player_Name = Before.Player_Name;
					 Temp.Tick = Sub_Code.Server_Main.getTick();
					 Sub_Code.Map_Select_List.remove(Index);
					 Sub_Code.Map_Select_List.add(Temp);
					 p.sendMessage("終了地点を設定しました。/Set_New_Mapコマンドを実行してください。");
				 }
			 }
			 else if (item.getId() == Item.STONE_PICKAXE)
			 {
				 int Index = -1;
				 for (int Number = 0; Number < Sub_Code.Map_Not_Break_List.size(); Number++)
				 {
					 if (Sub_Code.Map_Not_Break_List.get(Number).Player_Name == p.getName())
					 {
						 Index = Number;
						 break;
					 }
				 }
				 if (Index == -1)
				 {
					 Block block = e.getBlock();
					 Map_Select Temp = new Map_Select();
					 Temp.Start_Pos = block.getLocation();
					 Temp.Player_Name = p.getName();
					 Temp.Tick = Sub_Code.Server_Main.getTick();
					 Sub_Code.Map_Not_Break_List.add(Temp);
					 p.sendMessage(TextFormat.YELLOW + "マップ保護の開始位置:" + (int)block.x + ", " + (int)block.y + ", " + (int)block.z); 
				 }
				 else
				 {
					 Map_Select Before = Sub_Code.Map_Not_Break_List.get(Index);
					 Block block = e.getBlock();
					 Map_Select Temp = new Map_Select();
					 Temp.Start_Pos = Before.Start_Pos;
					 Temp.End_Pos = block.getLocation();
					 Temp.Player_Name = Before.Player_Name;
					 Temp.Tick = Sub_Code.Server_Main.getTick();
					 Sub_Code.Map_Not_Break_List.remove(Index);
					 Sub_Code.Map_Not_Break_List.add(Temp);
					 p.sendMessage("終了地点を設定しました。/Set_Map_Protectコマンドを実行してください。");
				 }
			 }
			 else if (item.getId() == Item.GOLD_PICKAXE)
			 {
				 int Index = -1;
				 for (int Number = 0; Number < Sub_Code.Map_Ore_Restore_List.size(); Number++)
				 {
					 if (Sub_Code.Map_Ore_Restore_List.get(Number).Player_Name == p.getName())
					 {
						 Index = Number;
						 break;
					 }
				 }
				 if (Index == -1)
				 {
					 Block block = e.getBlock();
					 Map_Select Temp = new Map_Select();
					 Temp.Start_Pos = block.getLocation();
					 Temp.Player_Name = p.getName();
					 Temp.Tick = Sub_Code.Server_Main.getTick();
					 Sub_Code.Map_Ore_Restore_List.add(Temp);
					 p.sendMessage(TextFormat.YELLOW + "マップ修復の開始位置:" + (int)block.x + ", " + (int)block.y + ", " + (int)block.z); 
				 }
				 else
				 {
					 Map_Select Before = Sub_Code.Map_Ore_Restore_List.get(Index);
					 Block block = e.getBlock();
					 Map_Select Temp = new Map_Select();
					 Temp.Start_Pos = Before.Start_Pos;
					 Temp.End_Pos = block.getLocation();
					 Temp.Player_Name = Before.Player_Name;
					 Temp.Tick = Sub_Code.Server_Main.getTick();
					 Sub_Code.Map_Ore_Restore_List.remove(Index);
					 Sub_Code.Map_Ore_Restore_List.add(Temp);
					 p.sendMessage("終了地点を設定しました。/Set_Map_Restoreコマンドを実行してください。");
				 }
			 }
		}
		else if (e.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			if (item.getId() == Item.STONE_AXE)
			{
				for (int Number = 0; Number < Sub_Code.Block_Replace_List.size(); Number++)
				{
					if (Sub_Code.Block_Replace_List.get(Number).Player_Name.getName() == p.getName() && Sub_Code.Block_Replace_List.get(Number).Tick + 10 < Sub_Code.Server_Main.getTick())
					{
						Sub_Code.Block_Replace_List.remove(Number);
						p.sendMessage(TextFormat.YELLOW + "置き換えモードを解除しました。");
						break;
					}
				}
			}
			else if (item.getId() == Item.GOLD_AXE)
			{
				for (int Number = 0; Number < Sub_Code.Block_Copy_List.size(); Number++)
				{
					if (Sub_Code.Block_Copy_List.get(Number).Player_Name.getName() == p.getName() && Sub_Code.Block_Copy_List.get(Number).Tick + 10 < Sub_Code.Server_Main.getTick())
					{
						Sub_Code.Block_Copy_List.remove(Number);
						p.sendMessage(TextFormat.YELLOW + "コピーモードを解除しました。");
						break;
					}
				}
			}
			else if (item.getId() == Item.DIAMOND_AXE)
			{
				for (int Number = 0; Number < Sub_Code.Map_Select_List.size(); Number++)
				{
					if (Sub_Code.Map_Select_List.get(Number).Player_Name == p.getName() && Sub_Code.Map_Select_List.get(Number).Tick + 10 < Sub_Code.Server_Main.getTick())
					{
						Sub_Code.Map_Select_List.remove(Number);
						p.sendMessage(TextFormat.YELLOW + "マップ指定を解除しました。");
						break;
					}
				}
			}
			else if (item.getId() == Item.STONE_PICKAXE)
			{
				for (int Number = 0; Number < Sub_Code.Map_Not_Break_List.size(); Number++)
				{
					if (Sub_Code.Map_Not_Break_List.get(Number).Player_Name == p.getName() && Sub_Code.Map_Not_Break_List.get(Number).Tick + 10 < Sub_Code.Server_Main.getTick())
					{
						Sub_Code.Map_Not_Break_List.remove(Number);
						p.sendMessage(TextFormat.YELLOW + "マップ指定を解除しました。");
						break;
					}
				}
			}
			else if (item.getId() == Item.GOLD_PICKAXE)
			{
				for (int Number = 0; Number < Sub_Code.Map_Ore_Restore_List.size(); Number++)
				{
					if (Sub_Code.Map_Ore_Restore_List.get(Number).Player_Name == p.getName() && Sub_Code.Map_Ore_Restore_List.get(Number).Tick + 10 < Sub_Code.Server_Main.getTick())
					{
						Sub_Code.Map_Ore_Restore_List.remove(Number);
						p.sendMessage(TextFormat.YELLOW + "マップ指定を解除しました。");
						break;
					}
				}
			}
		}
	}
	public static void Map_Commands(Player p, Command cmd, String[] args)
	{
	       if (cmd.getName().equalsIgnoreCase("Set_Lobby_Point"))
	       {
	    	   Lobby.Lobby_Teleport_Position = p.getLocation();
	    	   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + "がロビーのスポーン地点を設定しました。");
	       }
	       else if (cmd.getName().equalsIgnoreCase("Lobby"))
	       {
	    	   if (Lobby.Lobby_Teleport_Position == null)
	    	   {
	    		   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + ":" + "/Lobbyコマンドの前に/SetLobbyPointを指定する必要があります。");
	    		   return;
	    	   }
	    	   Player_Data p_data = Sub_Code.Get_Player_Data(p);
	    	   if (p_data.Damage_Time + 100 > Sub_Code.Server_Main.getTick())
	    	   {
	    		   p.sendMessage(TextFormat.RED + "ダメージを受けて5秒間はロビーへ移動できません。");
	    		   return;
	    	   }
	    	   Lobby.Teleport(p);
			   if (p_data.IsLobbyMode)
			       p.sendMessage(TextFormat.GREEN + "自身をロビーへテレポート");
	       }
	       else if (cmd.getName().equalsIgnoreCase("Lobby_All"))
	       {
	    	   if (Lobby.Lobby_Teleport_Position == null)
	    	   {
	    		   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + ":" + "/Lobbyコマンドの前に/SetLobbyPointを指定する必要があります。");
	    		   return;
	    	   }
	    	   Lobby.Teleport();
	    	   Sub_Code.Server_Main.broadcastMessage(p.getName() + ":全員をロビーへテレポート");
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Replace"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Replace_List.size(); Number++)
	    	   {
	    		   Block_Replace Temp = Sub_Code.Block_Replace_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Replace(Number, false);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がワールドの編集をしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Replace_Only_Air"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Replace_List.size(); Number++)
	    	   {
	    		   Block_Replace Temp = Sub_Code.Block_Replace_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Replace_Only_Air(Number);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がワールドの編集をしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Replace_Air"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Replace_List.size(); Number++)
	    	   {
	    		   Block_Replace Temp = Sub_Code.Block_Replace_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Replace(Number, true);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がワールドの編集をしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Copy"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Copy_List.size(); Number++)
	    	   {
	    		   Block_Copy Temp = Sub_Code.Block_Copy_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Copy(Number, false);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がコピーしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Copy_With_Air"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Copy_List.size(); Number++)
	    	   {
	    		   Block_Copy Temp = Sub_Code.Block_Copy_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Copy(Number, true);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がコピーしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Copy_Rotation"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Copy_List.size(); Number++)
	    	   {
	    		   Block_Copy Temp = Sub_Code.Block_Copy_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Copy_Rotation(Number, false);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がコピーしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Copy_Rotation_With_Air"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Copy_List.size(); Number++)
	    	   {
	    		   Block_Copy Temp = Sub_Code.Block_Copy_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Copy_Rotation(Number, true);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がコピーしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Copy_Rotation_90"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Copy_List.size(); Number++)
	    	   {
	    		   Block_Copy Temp = Sub_Code.Block_Copy_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Copy_Rotation_90(Number, false);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がコピーしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	       else if (cmd.getName().equalsIgnoreCase("Set_Copy_Rotation_90_With_Air"))
	       {
	    	   for (int Number = 0; Number < Sub_Code.Block_Copy_List.size(); Number++)
	    	   {
	    		   Block_Copy Temp = Sub_Code.Block_Copy_List.get(Number);
	    		   if (Temp.Player_Name.getName() == p.getName() && Temp.End_Position != null)
	    		   {
	    			   int Count = Sub_Code.Set_Copy_Rotation_90(Number, true);
	    			   Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がコピーしました。ブロック数:" + Count);
	    		   }
	    		   else
	    			   p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	   }
	       }
	     else if (cmd.getName().equalsIgnoreCase("Set_New_Map"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    		 return;
	    	 }
	    	 for (int Number = 0; Number < Sub_Code.Map_Select_List.size(); Number++)
	    	 {
	    		 Map_Select Temp = Sub_Code.Map_Select_List.get(Number);
	    		 if (Temp.Player_Name == p.getName() && Temp.End_Pos != null)
	    		 {
	    			 boolean IsExist = false;
	    			 String Exist_Name = "";
	    	    	 for (int Number1 = 0; Number1 < Map_Setting.Map_Contents.size(); Number1++)
	    	    	 {
	    	    		 if (Map_Setting.Map_Contents.get(Number1).Map_Name.contains(args[0]))
	    	    		 {
	    	    			 IsExist = true;
	    	    			 Exist_Name = Map_Setting.Map_Contents.get(Number1).Map_Name;
	    	    			 break;
	    	    		 }
	    	    	 }
	    	    	 if (IsExist)
	    	    	 {
	    	    		 p.sendMessage("指定したマップ名は既に登録されています。\n該当マップ名:" + Exist_Name);
	    	    		 return;
	    	    	 }
	    	    	 srttbacon.plugin.Map.Maps map = new srttbacon.plugin.Map.Maps();
	    	    	 Map_Not_Break_Pos pos = new Map_Not_Break_Pos();
	    	    	 pos.Start_Pos = Temp.Start_Pos;
	    	    	 pos.End_Pos = Temp.End_Pos;
	    	    	 map.Map_Name = args[0];
	    	    	 map.Map_Pos = pos;
	    	    	 Map_Setting.Map_Contents.add(map);
	    			 Sub_Code.Map_Select_List.remove(Number);
	    			 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "が新たなマップを登録しました。\nマップ名:" + args[0]);
	    		 }
	    		 else
	    			 p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	 }
	     }
	     else if (cmd.getName().equalsIgnoreCase("Delete_Map"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    		 return;
	    	 }
	    	 for (int Number1 = 0; Number1 < Map_Setting.Map_Contents.size(); Number1++)
	    	 {
	    		 if (Map_Setting.Map_Contents.get(Number1).Map_Name.contains(args[0]))
	    		 {
	    			 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がマップ名:" + Map_Setting.Map_Contents.get(Number1).Map_Name + "を削除しました。");
	    			 Map_Setting.Map_Contents.remove(Number1);
	    			 break;
	    		 }
	    	 }
	     }
	     else if (cmd.getName().equalsIgnoreCase("Get_Map_Exist"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    		 return;
	    	 }
	    	 boolean IsExist = false;
	    	 String Name = "";
	    	 for (int Number1 = 0; Number1 < Map_Setting.Map_Contents.size(); Number1++)
	    	 {
	    		 if (Map_Setting.Map_Contents.get(Number1).Map_Name.contains(args[0]))
	    		 {
	    			 IsExist = true;
	    			 Name = Map_Setting.Map_Contents.get(Number1).Map_Name;
	    			 break;
	    		 }
	    	 }
	    	 if (IsExist)
	    		 p.sendMessage("マップ名:" + args[0] + "は存在します。該当マップ名:" + Name);
	    	 else
	    		 p.sendMessage("マップ名:" + args[0] + "は存在しません。");
	     }
	     else if (cmd.getName().equalsIgnoreCase("Get_Map_Names"))
	     {
	    	 if (Map_Setting.Map_Contents.size() == 0)
	    	 {
	    		 p.sendMessage("登録されているマップは1つもありません。");
	    		 return;
	    	 }
	    	 p.sendMessage("---マップ数:" + Map_Setting.Map_Contents.size() + "---");
	    	 for (Maps map: Map_Setting.Map_Contents)
	    	 {
	    		 p.sendMessage("マップ名:" + map.Map_Name);
	    	 }
	     }
	     else if (cmd.getName().equalsIgnoreCase("Set_Blue_Core"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    		 return;
	    	 }
	    	 if (Sub_Code.Get_Map_By_Name(args[0]) == null)
	    	 {
	    		 p.sendMessage("指定したマップ名は存在しません。");
	    		 return;
	    	 }
	    	 Set_Core_Map_Name = args[0];
	    	 Set_Blue_Core_Mode = true;
	    	 Set_Red_Core_Mode = false;
	    	 p.sendMessage(TextFormat.GREEN + "エンドストーンブロックを設置してください。");
	     }
	     else if (cmd.getName().equalsIgnoreCase("Set_Red_Core"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    		 return;
	    	 }
	    	 if (Sub_Code.Get_Map_By_Name(args[0]) == null)
	    	 {
	    		 p.sendMessage("指定したマップ名は存在しません。");
	    		 return;
	    	 }
	    	 Set_Core_Map_Name = args[0];
	    	 Set_Blue_Core_Mode = false;
	    	 Set_Red_Core_Mode = true;
	    	 p.sendMessage(TextFormat.GREEN + "エンドストーンブロックを設置してください。");
	     }
	     else if (cmd.getName().equalsIgnoreCase("Reset_Core"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    		 return;
	    	 }
	    	 Maps map = Sub_Code.Get_Map_By_Name(args[0]);
	    	 if (map == null)
	    	 {
	    		 p.sendMessage("指定したマップ名は存在しません。");
	    		 return;
	    	 }
	    	 map.Blue_Team_Core_Position = null;
	    	 map.Red_Team_Core_Position = null;
	    	 Game.Blue_Team_HP = Game.Max_Core_HP;
	    	 Game.Red_Team_HP = Game.Max_Core_HP;
	    	 ScoreBoard_Show.Update_Blue_Team();
	    	 ScoreBoard_Show.Update_Red_Team();
	    	 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がコア設定を初期化しました。");
	     }
	     else if (cmd.getName().equalsIgnoreCase("Set_Map_Blue_Spawn_Pos"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    		 return;
	    	 }
	    	 if (!Set_Map_Spawn_Pos(args, Sub_Code.Team.Blue, p))
	    		 p.sendMessage("指定したマップ名は存在しません。マップ名:" + args[0]);
	     }
	     else if (cmd.getName().equalsIgnoreCase("Set_Map_Red_Spawn_Pos"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    		 return;
	    	 }
	    	 if (!Set_Map_Spawn_Pos(args, Sub_Code.Team.Red, p))
	    		 p.sendMessage("指定したマップ名は存在しません。マップ名:" + args[0]);
	     }
	     else if (cmd.getName().equalsIgnoreCase("Set_Map_Protect"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    	     return; 
	    	 }
	    	 Maps map = Sub_Code.Get_Map_By_Name(args[0]);
	    	 if (map == null)
	    	 {
	    		 p.sendMessage("指定したマップ名は存在しません。");
	    		 return;
	    	 }
	    	 for (int Number = 0; Number < Sub_Code.Map_Not_Break_List.size(); Number++)
	    	 {
	    		 Map_Select Temp = Sub_Code.Map_Not_Break_List.get(Number);
	    		 if (Temp.Player_Name == p.getName() && Temp.End_Pos != null)
	    		 {
	    			 int Start_Pos_X = Math.min((int)Temp.Start_Pos.x, (int)Temp.End_Pos.x);
	    			 int Start_Pos_Z = Math.min((int)Temp.Start_Pos.z, (int)Temp.End_Pos.z);
	    			 int End_Pos_X = Math.max((int)Temp.Start_Pos.x, (int)Temp.End_Pos.x);
	    			 int End_Pos_Z = Math.max((int)Temp.Start_Pos.z, (int)Temp.End_Pos.z);
	    			 Map_Not_Break_Pos a = new Map_Not_Break_Pos();
	    			 a.Start_Pos = new Vector3(Start_Pos_X, 0, Start_Pos_Z);
	    			 a.End_Pos = new Vector3(End_Pos_X, 0, End_Pos_Z);
	    			 map.IsNotBreakPos.add(a);
	    			 Sub_Code.Map_Not_Break_List.remove(Temp);
	    			 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がマップ名:" + args[0] + "の保護位置を追加しました。");
	    			 break;
	    		 }
	    		 else
	    			 p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	    	 }
	     }
	     else if (cmd.getName().equalsIgnoreCase("Set_Map_Restore"))
	     {
	    	 if (args.length == 0)
	    	 {
	    		 p.sendMessage("マップ名を指定してください。");
	    	     return; 
	    	 }
	    	 Maps map = Sub_Code.Get_Map_By_Name(args[0]);
	    	 if (map == null)
	    	 {
	    		 p.sendMessage("指定したマップ名は存在しません。");
	    		 return;
	    	 }
	    	 boolean ore = false;
	    	 for (int Number = 0; Number < Sub_Code.Map_Ore_Restore_List.size(); Number++)
	    	 {
	    		 Map_Select Temp = Sub_Code.Map_Ore_Restore_List.get(Number);
	    		 if (Temp.Player_Name == p.getName() && Temp.End_Pos != null)
	    		 {
	    			 int Start_Pos_X = Math.min((int)Temp.Start_Pos.x, (int)Temp.End_Pos.x);
	    			 int Start_Pos_Y = Math.min((int)Temp.Start_Pos.y, (int)Temp.End_Pos.y);
	    			 int Start_Pos_Z = Math.min((int)Temp.Start_Pos.z, (int)Temp.End_Pos.z);
	    			 int End_Pos_X = Math.max((int)Temp.Start_Pos.x, (int)Temp.End_Pos.x);
	    			 int End_Pos_Y = Math.max((int)Temp.Start_Pos.y, (int)Temp.End_Pos.y);
	    			 int End_Pos_Z = Math.max((int)Temp.Start_Pos.z, (int)Temp.End_Pos.z);
	    			 Map_Not_Break_Pos a = new Map_Not_Break_Pos();
	    			 a.Start_Pos = new Vector3(Start_Pos_X, Start_Pos_Y, Start_Pos_Z);
	    			 a.End_Pos = new Vector3(End_Pos_X, End_Pos_Y, End_Pos_Z);
	    			 map.Ore_Restore_Pos.add(a);
	    			 Sub_Code.Map_Ore_Restore_List.remove(Temp);
	    			 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がマップ名:" + args[0] + "に修復機能を追加しました。");
	    			 ore = true;
	    			 break;
	    		 }
	    	 }
	    	 if (!ore)
	    		 p.sendMessage(TextFormat.GREEN + "先に開始位置または終了位置を指定する必要があります。");
	     }
	     else if (cmd.getName().equalsIgnoreCase("Save_Map"))
	     {
	    	 Save_Load.Save_All_Maps();
	    	 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がマップ情報をセーブしました。");
	     }
	     else if (cmd.getName().equalsIgnoreCase("Load_Map"))
	     {
	    	 Save_Load.Load_All_Maps();
	    	 Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がマップ情報をロードしました。");
	     }
	}
	private static boolean Set_Map_Spawn_Pos(String[] args, Sub_Code.Team Team_Color, Player p)
	{
		boolean IsExist = false;
		for (int Number1 = 0; Number1 < Map_Setting.Map_Contents.size(); Number1++)
   	    {
   		    Maps map = Map_Setting.Map_Contents.get(Number1);
   		    if (map.Map_Name.contains(args[0]))
   		    {
   			    IsExist = true;
   			    String Message = p.getDisplayName() + TextFormat.GREEN + "がマップ:" + args[0] + "の";
   				if (Team_Color == Sub_Code.Team.Blue)
   				{
   	   			    map.Blue_Team_Spawn_Pos = p.getLocation();
   	   			    Sub_Code.Server_Main.broadcastMessage(Message + TextFormat.BLUE + "青チーム" + TextFormat.GREEN + "のスポーン位置を設定しました。座標;" + (int)p.x + " | " + (int)p.y + " | " + (int) + p.z);
   				}
   				else if (Team_Color == Sub_Code.Team.Red)
   				{
   	   			    map.Red_Team_Spawn_Pos = p.getLocation();
   	   			    Sub_Code.Server_Main.broadcastMessage(Message + TextFormat.RED + "赤チーム" + TextFormat.GREEN + "のスポーン位置を設定しました。座標;" + (int)p.x + " | " + (int)p.y + " | " + (int)p.z);
   				}
   			    break;
   		    }
   	    }
		return IsExist;
	}
	public static void Map_Restore(String Map_Name)
	{
		Sub_Code.Server_Main.loadLevel("main_back");
		Sub_Code.Server_Main.setDefaultLevel(Sub_Code.Server_Main.getLevelByName("main_back"));
		for (Player player: Server.getInstance().getOnlinePlayers().values())
		{
			player.setLevel(Sub_Code.Server_Main.getLevelByName("main_back"));
		    player.switchLevel(Sub_Code.Server_Main.getLevelByName("main_back"));
		    player.teleport(Lobby.Lobby_Teleport_Position);
		}
		Sub_Code.Server_Main.unloadLevel(Sub_Code.Server_Main.getLevelByName("main"));
		File file1 = new File(Sub_Code.Plugin_Dir);
		//String Map_Dir = file1.getParentFile() + "/worlds/world/region";
		String Copy_Dir = file1.getParentFile() + "/worlds/Maps/" + Map_Name;
	    File Dir = new File(Copy_Dir);
	    if (!Dir.exists())
	    {
	    	Sub_Code.Server_Main.broadcastMessage("マップ名:" + Map_Name + "が存在しませんでした。管理者は/worlds/maps内を確認してください。");
	    	return;
	    }
	    // ファイルの一覧
	    /*for (File f: Dir.listFiles())
	    {
	        if(f.isFile())
	        {
	            try
	            {
					Files.copy(Paths.get(f.getAbsolutePath()), Paths.get(Map_Dir + "/" + f.getName()), StandardCopyOption.REPLACE_EXISTING);
				}
	            catch (IOException e)
	            {
					Sub_Code.Server_Main.getLogger().info(f.getName() + "のコピーに失敗しました。");
				}
	        }
	    }*/
	}
}