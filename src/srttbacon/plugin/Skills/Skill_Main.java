package srttbacon.plugin.Skills;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Game;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Skill_Main
{
	public enum Skill_Buy
	{
		IsAlreadyBuy,
		Buy_OK,
		Not_Enough_Money,
		No_Player
	}
	public static ArrayList<String> Skill_Name = new ArrayList<String>();
	public static ArrayList<Integer> Skill_Cost = new ArrayList<Integer>();
	public static ArrayList<Boolean> Skill_IsEnable = new ArrayList<Boolean>();
	public static ArrayList<Integer> Skill_CT = new ArrayList<Integer>();
	public static void Init()
	{
		Skill_Name.add("忍者");
		Skill_Cost.add(0);
		Skill_CT.add(28);
		Skill_IsEnable.add(true);
		Skill_Name.add("Acrobat");
		Skill_Cost.add(0);
		Skill_CT.add(14);
		Skill_IsEnable.add(true);
		Skill_Name.add("Archer");
		Skill_Cost.add(0);
		Skill_CT.add(14);
		Skill_IsEnable.add(true);
		Skill_Name.add("Berserker");
		Skill_Cost.add(0);
		Skill_CT.add(25);
		Skill_IsEnable.add(true);
		Skill_Name.add("Builder");
		Skill_Cost.add(0);
		Skill_CT.add(13);
		Skill_IsEnable.add(true);
		Skill_Name.add("Handyman");
		Skill_Cost.add(0);
		Skill_CT.add(19);
		Skill_IsEnable.add(true);
		Skill_Name.add("Healer");
		Skill_Cost.add(0);
		Skill_CT.add(24);
		Skill_IsEnable.add(true);
		Skill_Name.add("Immobilizer");
		Skill_Cost.add(0);
		Skill_CT.add(23);
		Skill_IsEnable.add(true);
		Skill_Name.add("Miner");
		Skill_Cost.add(0);
		Skill_CT.add(17);
		Skill_IsEnable.add(true);
		Skill_Name.add("Pyro");
		Skill_Cost.add(0);
		Skill_CT.add(21);
		Skill_IsEnable.add(true);
		Skill_Name.add("Scanner");
		Skill_Cost.add(0);
		Skill_CT.add(23);
		Skill_IsEnable.add(true);
		Skill_Name.add("Scout");
		Skill_Cost.add(0);
		Skill_CT.add(5);
		Skill_IsEnable.add(true);
		Skill_Name.add("Vampire");
		Skill_Cost.add(0);
		Skill_CT.add(28);
		Skill_IsEnable.add(true);
		Skill_Name.add("Warrior");
		Skill_Cost.add(0);
		Skill_CT.add(24);
		Skill_IsEnable.add(true);
		
		Skill_Name.add("Bloodmage");
		Skill_Cost.add(150);
		Skill_CT.add(28);
		Skill_IsEnable.add(false);
		Skill_Name.add("Enchanter");
		Skill_Cost.add(150);
		Skill_CT.add(28);
		Skill_IsEnable.add(false);
		
		Skill_Name.add("Thor");
		Skill_Cost.add(200);
		Skill_CT.add(28);
		Skill_IsEnable.add(false);
		Skill_Name.add("Avenger");
		Skill_Cost.add(200);
		Skill_CT.add(28);
		Skill_IsEnable.add(false);
		
		Skill_Name.add("Spy");
		Skill_Cost.add(250);
		Skill_CT.add(28);
		Skill_IsEnable.add(false);
	}
	public static void Command(String cmd, Player player)
	{
		if (Game.Map_Now != null && player.isOp() && cmd.toLowerCase().contains("get_"))
		{
			player.sendMessage(TextFormat.RED + "試合中はこのコマンドを使用できません。");
			return;
		}
		else if (cmd.toLowerCase().contains("get_achievement"))
			return;
		else if (!cmd.toLowerCase().contains("get_"))
			return;
		for (Player player2: Server.getInstance().getOnlinePlayers().values())
			player.showPlayer(player2);
		player.removeAllEffects();
		Clear_Skill_Item(Sub_Code.Get_Player_Data(player));
		if (cmd.equalsIgnoreCase("Get_Acrobat"))
			Acrobat.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Archer"))
			Archer.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Berserker"))
			Berserker.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Builder"))
			Builder.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Handyman"))
			Handyman.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Healer"))
			Healer.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Immobilizer"))
			Immobilizer.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Miner"))
			Miner.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Ninja"))
			Samurai.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Pyro"))
			Pyro.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Scanner"))
			Scanner.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Scout"))
			Scout_Skill.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Vampire"))
			Vampire.Get_Item(player);
		else if (cmd.equalsIgnoreCase("Get_Warrior"))
			Warrior.Get_Item(player);
		ScoreBoard_Show.Change_Skill(Sub_Code.Get_Player_Data(player));
	}
	public static void Get_Skill_Item(Player_Data p)
	{
		p.Kill_Continue_Count = 0;
		Clear_Skill_Item(p);
		p.player.removeAllEffects();
		if (p.Skill_Now.contains("忍者"))
			Samurai.Get_Item(p.player);
		else if (p.Skill_Now.contains("Acrobat"))
			Acrobat.Get_Item(p.player);
		else if (p.Skill_Now.contains("Archer"))
			Archer.Get_Item(p.player);
		else if (p.Skill_Now.contains("Berserker"))
			Berserker.Get_Item(p.player);
		else if (p.Skill_Now.contains("Builder"))
			Builder.Get_Item(p.player);
		else if (p.Skill_Now.contains("Handyman"))
			Handyman.Get_Item(p.player);
		else if (p.Skill_Now.contains("Healer"))
			Healer.Get_Item(p.player);
		else if (p.Skill_Now.contains("Immobilizer"))
			Immobilizer.Get_Item(p.player);
		else if (p.Skill_Now.contains("Miner"))
			Miner.Get_Item(p.player);
		else if (p.Skill_Now.contains("Pyro"))
			Pyro.Get_Item(p.player);
		else if (p.Skill_Now.contains("Scanner"))
			Scanner.Get_Item(p.player);
		else if (p.Skill_Now.contains("Scout"))
			Scout_Skill.Get_Item(p.player);
		else if (p.Skill_Now.contains("Vampire"))
			Vampire.Get_Item(p.player);
		else if (p.Skill_Now.contains("Warrior"))
			Warrior.Get_Item(p.player);
	}
	public static void Clear_Skill_Item(Player_Data p)
	{
		PlayerInventory inventory = p.player.getInventory();
		try
		{
			for (int Number = 0; Number < inventory.getSize(); Number++)
			{
				Item item = inventory.getItem(Number);
				int Id = item.getId();
				if (Id == Item.LEATHER_CAP || Id == Item.LEATHER_TUNIC || Id == Item.LEATHER_PANTS || Id == Item.LEATHER_BOOTS || Id == Item.WOODEN_SWORD || Id == Item.WOODEN_AXE || Id == Item.WOODEN_PICKAXE)
					inventory.clear(Number);
				else if (p.No_Drop_Item != "" && item.getCustomName().contains(p.No_Drop_Item))
					inventory.clear(Number);
				else if (item.getCustomName().contains("Builder") || item.getCustomName().contains("日本刀"))
					inventory.clear(Number);
			}
		}
		catch (Exception e)
		{

		}
	}
	public static void Set_Skill(Player_Data p, String Skill_Name)
	{
		p.player.removeAllEffects();
		if (Skill_Name.contains("忍者"))
			Samurai.Set_Skill(p.player);
		else if (Skill_Name.contains("Acrobat"))
			Acrobat.Set_Skill(p.player);
		else if (Skill_Name.contains("Archer"))
			Archer.Set_Skill(p.player);
		else if (Skill_Name.contains("Berserker"))
			Berserker.Set_Skill(p.player);
		else if (Skill_Name.contains("Builder"))
			Builder.Set_Skill(p.player);
		else if (Skill_Name.contains("Handyman"))
			Handyman.Set_Skill(p.player);
		else if (Skill_Name.contains("Healer"))
			Healer.Set_Skill(p.player);
		else if (Skill_Name.contains("Immobilizer"))
			Immobilizer.Set_Skill(p.player);
		else if (Skill_Name.contains("Miner"))
			Miner.Set_Skill(p.player);
		else if (Skill_Name.contains("Pyro"))
			Pyro.Set_Skill(p.player);
		else if (Skill_Name.contains("Scanner"))
			Scanner.Set_Skill(p.player);
		else if (Skill_Name.contains("Scout"))
			Scout_Skill.Set_Skill(p.player);
		else if (Skill_Name.contains("Vampire"))
			Vampire.Set_Skill(p.player);
		else if (Skill_Name.contains("Warrior"))
			Warrior.Set_Skill(p.player);
		ScoreBoard_Show.Change_Skill(p);
		Sub_Code.Set_Player_Name(p);
	}
	public static void Entity_Damage(EntityDamageByEntityEvent e)
	{
		Player p = (Player)e.getDamager();
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		if (Data.Skill_Now == "Warrior")
			Warrior.Activate(e, Data);
		else if (Data.Skill_Now == "忍者")
			Data.Skill_Time = 6;
	}
	public static boolean Not_Put_Item(Item block)
	{
		if (block.getCustomName().contains("Builder 3x3"))
			return true;
		return false;
	}
	public static void ItemDrop(PlayerDropItemEvent e)
	{
		Player p = e.getPlayer();
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Item item = e.getItem();
		String a = item.getCustomName();
		int aa = item.getId();
		if (aa == Item.LEATHER_CAP || aa == Item.LEATHER_TUNIC || aa == Item.LEATHER_PANTS || aa == Item.LEATHER_BOOTS || aa == Item.WOODEN_SWORD || aa == Item.WOODEN_AXE || aa == Item.WOODEN_PICKAXE)
			e.setCancelled();
		else if (a != "" && a.contains(Data.No_Drop_Item))
			e.setCancelled();
		else if (a.contains("Builder 石レンガ") || a.contains("Builder レンガ") || a.contains("Builder 木材") || a.contains("Builder 羊毛") || a.contains("日本刀"))
			e.setCancelled();
	}
	public static void Interact(PlayerInteractEvent e)
	{
		if (e.getItem().isNull())
			return;
		Player player = e.getPlayer();
		Player_Data p_data = Sub_Code.Get_Player_Data(player);
		if (p_data.IsLobbyMode && !player.isOp())
			return;
		if (p_data.Skill_Now == "忍者")
			Samurai.Activate(e);
		else if (p_data.Skill_Now == "Acrobat")
			Acrobat.Activate(e);
		else if (p_data.Skill_Now == "Archer")
		    Archer.Skill_Select(e);
		else if (p_data.Skill_Now == "Berserker")
			Berserker.Activate(e);
		else if (p_data.Skill_Now == "Builder")
			Builder.Activate(e);
		else if (p_data.Skill_Now == "Handyman")
			Handyman.Activate(e);
		else if (p_data.Skill_Now == "Healer")
			Healer.Activate(e);
		else if (p_data.Skill_Now == "Immobilizer")
			Immobilizer.Activate(e);
		else if (p_data.Skill_Now == "Miner")
			Miner.Activate(e);
		else if (p_data.Skill_Now == "Pyro")
			Pyro.Activate(e);
		else if (p_data.Skill_Now == "Scanner")
			Scanner.Activate(e);
		else if (p_data.Skill_Now == "Scout")
			Scout_Skill.onInteract(e);
	}
	public static void Player_Interact(PlayerInteractEntityEvent e, Player_Data p_data)
	{
		if (e.getItem().isNull())
			return;
		if (p_data.IsLobbyMode && !p_data.player.isOp())
			return;
		if (!(e.getEntity() instanceof Player))
			return;
		if (p_data.Skill_Now == "Vampire")
			Vampire.Activate(e, p_data);
	}
	public static void Move(PlayerMoveEvent e)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(e.getPlayer());
		if (Data.IsNoMoveMode)
		{
			Location to = e.getFrom();
			to.y = e.getTo().y;
			to.pitch = e.getTo().getPitch();
			to.yaw = e.getTo().getYaw();
			e.setTo(to);
		}
	}
	public static void Player_Invectory_Move(Player p)
	{
		PlayerInventory inventory = p.getInventory();
		int[] list = {0,1,2,3,4,5,6,8};
		for (int Number_01: list)
		{
			if (inventory.getItem(Number_01).getId() != Item.AIR)
			{
				for (int Number = 9; Number < inventory.getSize(); Number++)
				{
					if (inventory.getItem(Number).getId() == Item.AIR)
					{
						inventory.setItem(Number, inventory.getItem(Number_01));
						break;
					}
				}
				inventory.setItem(Number_01, Item.get(Item.AIR));
			}
		}
	}
}