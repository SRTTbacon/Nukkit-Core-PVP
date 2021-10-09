package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemDye;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.MainCode;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Particle;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Healer
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Healer";
		Data.CT = 0;
		Data.Skill_Now = "Healer";
	}
	public static void Activate(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Player_Data From_Player = Sub_Code.Get_Player_Data(p);
		if (e.getItem().getId() == Item.DYE && e.getItem().getCustomName().contains("Healer") && From_Player.CT <= 0)
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
	    				Thread.sleep(1200);
	    			} catch (InterruptedException e1) {
	    				// TODO 自動生成された catch ブロック
	    				e1.printStackTrace();
	    			}
	    			int p_x = (int)p.x;
	    			int p_y = (int)p.y;
	    			int p_z = (int)p.z;
	            	if (From_Player.IsStartupSkill == -1)
	            		return;
	            	From_Player.player.getInventory().setItem(Index, From_Player.Before_Skill_Item);
	            	From_Player.Before_Skill_Item = null;
	            	From_Player.IsStartupSkill = -1;
					int Heal_Count = 0;
	    			for (Player_Data p_Data: Sub_Code.Players_Info)
	    			{
	    				if (p_Data.player.x > p_x - 5 && p_Data.player.x < p_x + 5 && p_Data.player.y > p_y - 4 && p_Data.player.y < p_y + 5 && p_Data.player.z > p_z - 5 && p_Data.player.z < p_z + 5)
	    				{
	    					if (p_Data.Team_Color == From_Player.Team_Color || p_Data.Team_Color == Sub_Code.Team.None)
	    					{
	    						if (p_Data.player.getMaxHealth() <= 18)
	    							p_Data.player.setMaxHealth(p_Data.player.getMaxHealth() + 2);
	    						else if (p_Data.player.getMaxHealth() < 20)
	    							p_Data.player.setMaxHealth(20);
	    						p_Data.player.setHealth(p_Data.player.getHealth() + 6);
	    						Effect Effect_Regeneration = Effect.getEffect(Effect.REGENERATION);
	    						Effect_Regeneration.setAmplifier(1);
	    						Effect_Regeneration.setDuration(100);
	    						p_Data.player.addEffect(Effect_Regeneration);
	    						Particle.Particle_Circle(p, p.getPosition(), ParticleEffect.HEART);
	    						PlaySound.PlaySoundAll(p_Data, "skill.heal", p_Data.player, 0.5f);
	    						if (p_Data.player.getName() != p.getName())
	    							p_Data.player.sendMessage(TextFormat.GREEN + "味方ヒーラーから支援を受けた！");
	    						else
	    						{
	    							int random = MainCode.r.nextInt(3);
	    							if (random == 0)
	    								p_Data.player.sendMessage(TextFormat.GREEN + "凄まじい回復量だ...");
	    							else if (random == 1)
	    								p_Data.player.sendMessage(TextFormat.GREEN + "傷が癒えていく...");
	    							else if (random == 2)
	    								p_Data.player.sendMessage(TextFormat.GREEN + "スキル効果により痛みが引いていくようだ...");
	    						}
								Heal_Count++;
	    					}
	    				}
	    			}
					if (Heal_Count >= 4)
					    Sub_Code.Skill_Achievement(From_Player, "Healer");
					PlaySound.PlaySoundAll(From_Player, "voice." + From_Player.Voice_Name + ".bow_attack", From_Player.player.getPosition());
	            }
        	}).start();;
			int Index2 = Skill_Main.Skill_Name.indexOf("Healer");
			From_Player.CT = Skill_Main.Skill_CT.get(Index2);	
	        if (From_Player.CT > 0)
	        	ScoreBoard_Show.Add_CT_Text(From_Player);
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
	    ItemDye Skill_Item = new ItemDye(DyeColor.PINK);
	    Skill_Item.setCustomName("Healer");
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