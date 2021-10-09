package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemDye;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.MainCode;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Particle;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Vampire
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "吸血";
		Data.CT = 0;
		Data.Skill_Now = "Vampire";
	}
	public static void Activate(PlayerInteractEntityEvent e, Player_Data p_data)
	{
		if (e.getItem().getId() == Item.DYE && e.getItem().getCustomName().contains("吸血") && p_data.CT <= 0)
		{
			Player_Data enemy_data = Sub_Code.Get_Player_Data((Player)e.getEntity());
			int Index2 = Skill_Main.Skill_Name.indexOf("Vampire");
			if (enemy_data.Team_Color == p_data.Team_Color)
			    return;
			if (p_data.player.getMaxHealth() < 30)
			{
				if (enemy_data.player.getMaxHealth() >= 16)
				{
					enemy_data.player.setMaxHealth(enemy_data.player.getMaxHealth() - 2);
					PlaySound.PlaySoundOne(enemy_data, "skill.vampire", p_data.player.getPosition());
					enemy_data.player.sendMessage(TextFormat.RED + "血を吸い取られてしまった...");
					p_data.player.setMaxHealth(p_data.player.getMaxHealth() + 2);
					p_data.player.level.addSound(p_data.player.getPosition(), Sound.RANDOM_POTION_BREWED, 1.0f, 1.0f, p_data.player);
					int random = MainCode.r.nextInt(3);
					if (random == 0)
						p_data.player.sendMessage(TextFormat.RED + "もっと血が...血が欲しい...");
					else if (random == 1)
						p_data.player.sendMessage(TextFormat.RED + "やはりニンゲンの血は格別だ...");
					else if (random == 2)
						p_data.player.sendMessage(TextFormat.RED + "キンッキンに冷えてやがる...！");
					p_data.CT = Skill_Main.Skill_CT.get(Index2);	
			        if (p_data.CT > 0)
			        	ScoreBoard_Show.Add_CT_Text(p_data);
				}
				else
				{
					p_data.player.sendMessage(TextFormat.GREEN + "そのプレイヤーは血が足りていないようだ...");
					p_data.CT = 5;
			        if (p_data.CT > 0)
			        	ScoreBoard_Show.Add_CT_Text(p_data);
				}
			}
			else
			{
				EntityDamageEvent event = new EntityDamageEvent(p_data.player, DamageCause.ENTITY_ATTACK, 2.0f);
				enemy_data.player.attack(event);
				PlaySound.PlaySoundOne(enemy_data, "skill.vampire", p_data.player.getPosition());
				if (p_data.player.getHealth() - 2 <= p_data.player.getMaxHealth())
					p_data.player.setHealth(p_data.player.getHealth() + 2);
				else
					p_data.player.setHealth(p_data.player.getMaxHealth());
				Particle.Particle_Circle(p_data.player, p_data.player.getPosition(), ParticleEffect.HEART);
				p_data.player.level.addSound(p_data.player.getPosition(), Sound.RANDOM_POTION_BREWED, 1.0f, 1.0f, p_data.player);
				int random = MainCode.r.nextInt(3);
				if (random == 0)
					p_data.player.sendMessage(TextFormat.RED + "もっと血が...血が欲しい...");
				else if (random == 1)
					p_data.player.sendMessage(TextFormat.RED + "やはりニンゲンの血は格別だ...");
				else if (random == 2)
					p_data.player.sendMessage(TextFormat.RED + "キンッキンに冷えてやがる...！");
				Sub_Code.Skill_Achievement(p_data, "Vampire");
				p_data.CT = Skill_Main.Skill_CT.get(Index2);	
		        if (p_data.CT > 0)
		        	ScoreBoard_Show.Add_CT_Text(p_data);
			}
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
	    ItemDye Skill_Item = new ItemDye(DyeColor.RED);
	    Skill_Item.setCustomName("吸血");
		p.getInventory().setItem(0, (ItemTool) ItemTool.get(Item.WOODEN_SWORD));
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