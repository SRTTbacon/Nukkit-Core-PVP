package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Warrior
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Warrior";
		Data.CT = 0;
		Data.Skill_Now = "Warrior";
	}
	public static void Activate(EntityDamageByEntityEvent e, Player_Data Data)
	{
		Player From_Player = Data.player;
		boolean IsWarriorSkill = From_Player.getInventory().getItemInHand().getCustomName().contains("Warrior");
		if (From_Player.getInventory().getItemInHand().getId() == Item.BLAZE_ROD && IsWarriorSkill && Data.CT == 0)
		{
			if (e.getEntity() instanceof Player)
			{
				Player_Data enemy = Sub_Code.Get_Player_Data((Player)e.getEntity());
				int TickTime = Sub_Code.Server_Main.getTick();
				if (enemy.Warrior_Damage_Time + 200 < TickTime)
				{
					if (enemy.player.getHealth() <= 8.0f)
					{
						Data.Warrior_Kill_Count++;
						if (Data.Warrior_Kill_Count >= 2)
					        Sub_Code.Skill_Achievement(Data, "Warrior");
					}
					else
					    PlaySound.PlaySoundAll(Data, "voice." + Data.Voice_Name + ".bow_attack", Data.player.getPosition());
					e.setDamage(8.0f);
					int Index2 = Skill_Main.Skill_Name.indexOf("Warrior");
			        Data.CT = Skill_Main.Skill_CT.get(Index2);
			        enemy.player.sendMessage(TextFormat.RED + "Warriorのスキルダメージを受けました。");
			        enemy.Warrior_Damage_Time = Sub_Code.Server_Main.getTick();
				}
				else
				{
					From_Player.sendMessage(enemy.player.getDisplayName() + TextFormat.RED + "はWarriorのスキルを既に受けているためダメージを与えられませんでした。");
					Data.CT = 5;
				}
		        if (Data.CT > 0)
		        	ScoreBoard_Show.Add_CT_Text(Data);
			}
		}
		else
		{
			if (From_Player.getInventory().getItemInHand().getId() == Item.BLAZE_ROD && IsWarriorSkill)
				Data.Warrior_Kill_Count = 0;
			e.setDamage(e.getDamage() + 1.0f);
		}
	}
	public static void Get_Item(Player p)
	{
		PlayerInventory inventory = p.getInventory();
		Skill_Main.Player_Invectory_Move(p);
		ItemColorArmor helmet = (ItemColorArmor)Item.get(Item.LEATHER_CAP);
		ItemColorArmor chestamor = (ItemColorArmor)Item.get(Item.LEATHER_TUNIC);
		ItemColorArmor pants = (ItemColorArmor)Item.get(Item.LEATHER_PANTS);
	    ItemColorArmor boots = (ItemColorArmor)Item.get(Item.LEATHER_BOOTS);
	    Player_Data data = Sub_Code.Get_Player_Data(p);
	    BlockColor color = Sub_Code.Get_Team_Color(data.Team_Color);
	    helmet.setColor(color);
	    chestamor.setColor(color);
	    pants.setColor(color);
	    boots.setColor(color);
	    Item Skill_Item = Item.get(Item.BLAZE_ROD);
	    Skill_Item.setCustomName("Warrior");
		p.getInventory().setItem(0, (ItemTool) ItemTool.get(Item.STONE_SWORD));
		p.getInventory().setItem(1, (ItemTool) ItemTool.get(Item.WOODEN_AXE));
		p.getInventory().setItem(2, (ItemTool) ItemTool.get(Item.WOODEN_PICKAXE));
		p.getInventory().setItem(4, Skill_Item);
		if (inventory.getHelmet().getId() == Item.AIR)
		    p.getInventory().setHelmet((Item)helmet);
		if (inventory.getChestplate().getId() == Item.AIR)
		    p.getInventory().setChestplate((Item)chestamor);
		if (inventory.getLeggings().getId() == Item.AIR)
		    p.getInventory().setLeggings((Item)pants);
		if (inventory.getBoots().getId() == Item.AIR)
	        p.getInventory().setBoots((Item)boots);
	    Set_Skill(p);
	}
}