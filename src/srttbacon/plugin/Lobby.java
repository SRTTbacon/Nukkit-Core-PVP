package srttbacon.plugin;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.command.Command;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;
import srttbacon.plugin.Class.Teleport_Class;
import srttbacon.plugin.Skills.Skill_Main;

public class Lobby
{
	public static class All_Teleport_Save
	{
		public Location To_Pos;
		public Vector3 Sign_Pos;
		public int ID = 0;
	}
	public static List<All_Teleport_Save> All_Teleport_List = new ArrayList<All_Teleport_Save>();
	public static Location Lobby_Teleport_Position;
	public final static int Game_Start_Count_Down_Max = 20;
	public static int Game_Start_Count_Down = 20;
	public static boolean Game_Starting = false;
	public static void Teleport()
	{
		for(Player player : Server.getInstance().getOnlinePlayers().values())
		{
			player.teleport(Lobby_Teleport_Position);
			Sub_Code.Get_Player_Data(player).IsLobbyMode = true;
		}
	}
	public static void Teleport(Player player)
	{
		player.teleport(Lobby_Teleport_Position);
		Sub_Code.Get_Player_Data(player).IsLobbyMode = true;
	}
	public static void Sign_Board_Click(PlayerInteractEvent event)
	{
		Block block = event.getBlock();
		Player player = event.getPlayer();
		Player_Data p_data = Sub_Code.Get_Player_Data(player);
		if (p_data.IsLobbyMode)
		{
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (block.getId() == Block.SIGN_POST || block.getId() == Block.WALL_SIGN))
			{
				BlockEntitySign Sign = (BlockEntitySign) player.getLevel().getBlockEntity(block.getLocation());
				if (Sign != null && Sign.getText() != null)
				{
					String[] text = Sign.getText();
					if (text.length > 1)
					    if (text[1].contains("ゲーム開始"))
						    Player_Game_Start(Sub_Code.Get_Player_Data(player));
				}
			}
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			Item item = event.getItem();
			int b_x = (int)block.x;
			int b_y = (int)block.y;
			int b_z = (int)block.z;
			if (item.getId() == Item.NETHERITE_PICKAXE)
			{
				if (block.getId() == Block.WALL_SIGN || block.getId() == Block.SIGN_POST)
				{
					boolean IsExist = false;
					Teleport_Class tel = null;
					for (Teleport_Class ac: Sub_Code.Teleport_List)
					{
						if (ac.Player_Name == player.getName())
						{
							tel = ac;
							IsExist = true;
						}
					}
					if (!IsExist || tel == null)
					{
						player.sendMessage("先に/Set_Teleportコマンドを実行してください。");
						return;
					}
					for (All_Teleport_Save aas: All_Teleport_List)
					{
						if (aas.Sign_Pos.x == b_x && aas.Sign_Pos.y == b_y && aas.Sign_Pos.z == b_z)
						{
							player.sendMessage("その看板は既に移動先が割り当てられています。識別ID:" + aas.ID);
							return;
						}
					}
					All_Teleport_Save all_tel = new All_Teleport_Save();
					all_tel.To_Pos = tel.To_Pos;
					all_tel.Sign_Pos = new Vector3(b_x, b_y, b_z);
					all_tel.ID = MainCode.r.nextInt(100000);
					All_Teleport_List.add(all_tel);
					Sub_Code.Teleport_List.remove(tel);
					Sub_Code.Server_Main.broadcastMessage(player.getDisplayName() + TextFormat.GREEN + "がテレポート用看板を作成しました。識別ID:" + all_tel.ID);
				}
			}
			else if (block.getId() == Block.WALL_SIGN || block.getId() == Block.SIGN_POST)
			{
				for (All_Teleport_Save aas: All_Teleport_List)
				{
					if (aas.Sign_Pos.x == b_x && aas.Sign_Pos.y == b_y && aas.Sign_Pos.z == b_z)
					{
						player.teleport(aas.To_Pos);
						player.yaw = aas.To_Pos.yaw;
						player.pitch = aas.To_Pos.pitch;
						break;
					}
				}
			}
		}
	}
	public static void Player_Game_Start(Player_Data p)
	{
		if (Game.Map_Now == null)
		{
			p.player.sendMessage("まだ戦闘が開始されていません。");
			return;
		}
		if (p.Team_Color == Sub_Code.Team.None)
		{
			if (Game.Blue_Team_Player_Count == Game.Red_Team_Player_Count)
			{
				if (MainCode.r.nextInt(2) == 0)
					Set_Player_Team(p, Sub_Code.Team.Blue);
				else
					Set_Player_Team(p, Sub_Code.Team.Red);
			}
			else if (Game.Blue_Team_Player_Count < Game.Red_Team_Player_Count)
				Set_Player_Team(p, Sub_Code.Team.Blue);
			else
				Set_Player_Team(p, Sub_Code.Team.Red);
			Skill_Main.Get_Skill_Item(p);
		}
		else
		{
			if (p.Team_Color == Sub_Code.Team.Blue)
				p.player.teleport(Game.Map_Now.Blue_Team_Spawn_Pos);
			else
				p.player.teleport(Game.Map_Now.Red_Team_Spawn_Pos);
			Skill_Main.Get_Skill_Item(p);
		}
		p.IsLobbyMode = false;
	}
	private static void Set_Player_Team(Player_Data p, Sub_Code.Team Team_Color)
	{
		if (Team_Color == Sub_Code.Team.Blue)
		{
			p.Team_Color = Sub_Code.Team.Blue;
			p.player.sendMessage("あなたは" + TextFormat.BLUE + "青チーム" + TextFormat.WHITE + "です。");
			p.player.sendTitle("あなたは" + TextFormat.BLUE + "青チーム" + TextFormat.WHITE + "です。", TextFormat.RED + "赤チーム" + TextFormat.WHITE + "のコアを破壊しましょう。", 10, 60, 10);
			Game.Blue_Team_Player_Count++;
			p.player.teleport(Game.Map_Now.Blue_Team_Spawn_Pos);
			p.player.setSpawn(Game.Map_Now.Blue_Team_Spawn_Pos);
		}
		else
		{
			p.Team_Color = Sub_Code.Team.Red;
			p.player.sendMessage("あなたは" + TextFormat.RED + "赤チーム" + TextFormat.WHITE + "です。");
			p.player.sendTitle("あなたは" + TextFormat.RED + "赤チーム" + TextFormat.WHITE + "です。", TextFormat.BLUE + "青チーム" + TextFormat.WHITE + "のコアを破壊しましょう。", 10, 60, 10);
			Game.Red_Team_Player_Count++;
			p.player.teleport(Game.Map_Now.Red_Team_Spawn_Pos);
			p.player.setSpawn(Game.Map_Now.Red_Team_Spawn_Pos);
		}
		Sub_Code.Set_Player_Name(p);
		ScoreBoard_Show.Change_Team(p);
	}
	public static void Commands(Player p, Command cmd, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("Set_Teleport"))
		{
			for (int Number = 0; Number < Sub_Code.Teleport_List.size(); Number++)
			{
				if (Sub_Code.Teleport_List.get(Number).Player_Name == p.getName())
				{
					Sub_Code.Teleport_List.remove(Number);
					break;
				}
			}
			Teleport_Class Temp = new Teleport_Class();
			Temp.To_Pos = p.getLocation();
			Temp.Player_Name = p.getName();
			Temp.Tick = Sub_Code.Server_Main.getTick();
			Sub_Code.Teleport_List.add(Temp);
			p.sendMessage(TextFormat.YELLOW + "テレポート先を指定しました。テレポート元となる看板をネザライトのツルハシでタップしてください。"); 
		}
		else if (cmd.getName().equalsIgnoreCase("Delete_Teleport"))
		{
			if (args.length == 0)
			{
				p.sendMessage("引数1に識別IDを指定してください。");
				return;
			}
			for (All_Teleport_Save aas: All_Teleport_List)
			{
				if (aas.ID == Integer.valueOf(args[0]))
				{
					Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "がテレポート用看板を削除しました。識別ID:" + aas.ID);
					All_Teleport_List.remove(aas);
					return;
				}
			}
			p.sendMessage(TextFormat.GREEN + "識別ID:" + args[0] + "は存在しません。");
		}
		else if (cmd.getName().equalsIgnoreCase("Get_Teleports"))
		{
			if (All_Teleport_List.size() == 0)
			{
				p.sendMessage(TextFormat.GREEN + "現在登録されているテレポートはありません。");
				return;
			}
			for (All_Teleport_Save aas: All_Teleport_List)
				p.sendMessage(TextFormat.GREEN + "看板位置:" + (int)aas.Sign_Pos.x + ", " + (int)aas.Sign_Pos.y + ", " + (int)aas.Sign_Pos.z + " 識別ID:" + aas.ID);
		}
	}
}