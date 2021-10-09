package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.level.Sound;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Particle;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Pyro
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Pyro";
		Data.CT = 0;
		Data.Skill_Now = "Pyro";
	}
	public static void Activate(PlayerInteractEvent e)
	{
		Player_Data From_Player = Sub_Code.Get_Player_Data(e.getPlayer());
		if (e.getItem().getId() == Item.BLAZE_POWDER && From_Player.CT <= 0)
		{
			PlaySound.PlaySoundAll(From_Player, "system.skill_active", From_Player.player.getPosition());
        	int Index = From_Player.player.getInventory().getHeldItemIndex();
        	From_Player.IsStartupSkill = Index;
        	Item item_back = From_Player.player.getInventory().getItemInHand().clone();
        	From_Player.Before_Skill_Item = From_Player.player.getInventory().getItemInHand().clone();
        	Enchantment enc = Enchantment.get(Enchantment.ID_TRIDENT_IMPALING);
        	item_back.addEnchantment(enc);
        	From_Player.player.getInventory().setItem(From_Player.player.getInventory().getHeldItemIndex(), item_back);
        	new Thread(new Runnable() {
	            @Override
	            public void run()
	            {
	            	try {
	    				Thread.sleep(750);
	    			} catch (InterruptedException e1) {
	    				// TODO 自動生成された catch ブロック
	    				e1.printStackTrace();
	    			}
	            	if (From_Player.IsStartupSkill == -1)
	            		return;
	    			int p_x = (int)From_Player.player.x;
	    			int p_y = (int)From_Player.player.y;
	    			int p_z = (int)From_Player.player.z;
	            	From_Player.player.getInventory().setItem(Index, From_Player.Before_Skill_Item);
	            	From_Player.Before_Skill_Item = null;
	            	From_Player.IsStartupSkill = -1;
					int Player_Count = 0;
	    			for (Player player : Server.getInstance().getOnlinePlayers().values())
	    			{
	    				if (player.getName() == From_Player.player.getName())
	    					continue;
	    				if (player.x > p_x - 5 && player.x < p_x + 5 && player.y > p_y - 4 && player.y < p_y + 5 && player.z > p_z - 5 && player.z < p_z + 5)
	    				{
	    					Player_Data To_Data = Sub_Code.Get_Player_Data(player);
	    					if (To_Data.Team_Color != From_Player.Team_Color || To_Data.Team_Color == Sub_Code.Team.None)
	    					{
	    						player.setOnFire(8);
	    						player.sendMessage(TextFormat.RED + "Pyroのスキル効果を受けました。");
								Player_Count++;
	    					}	
	    				}
	    			}
	    			if (Player_Count == 0)
	    				From_Player.CT = 5;
	    			else
	    			{
						if (Player_Count >= 3)
						    Sub_Code.Skill_Achievement(From_Player, "Pyro");
	    				From_Player.player.level.addSound(From_Player.player.getPosition(), Sound.MOB_BLAZE_SHOOT, 1.0f, 1.0f, From_Player.player);
	    				Particle.Particle_Circle(From_Player.player, From_Player.player.getPosition().setY(From_Player.player.y + 0.75), ParticleEffect.BASIC_FLAME, 2);
						PlaySound.PlaySoundAll(From_Player, "voice." + From_Player.Voice_Name + ".bow_attack", From_Player.player.getPosition());
	    			}
	    	        if (From_Player.CT > 0)
	    	        	ScoreBoard_Show.Add_CT_Text(From_Player);
	            }
        	}).start();
			int Index2 = Skill_Main.Skill_Name.indexOf("Pyro");
			From_Player.CT = Skill_Main.Skill_CT.get(Index2);	
	        if (From_Player.CT > 0)
	        	ScoreBoard_Show.Add_CT_Text(From_Player);
		}
	}
	public static void Get_Item(Player p)
	{ 
		Effect effect = Effect.getEffect(Effect.FIRE_RESISTANCE);
		effect.setDuration(99999);
		p.addEffect(effect);
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
	    Item Skill_Item = Item.get(Item.BLAZE_POWDER);
	    Skill_Item.setCustomName("Pyro");
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