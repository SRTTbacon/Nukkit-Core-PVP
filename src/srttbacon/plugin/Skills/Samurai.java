package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Samurai
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "隠れ身の術";
		Data.CT = 0;
		Data.Skill_Now = "忍者";
	}
	public static void Activate(PlayerInteractEvent pe)
	{
		Player p = pe.getPlayer();
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (pe.getItem().getId() == Item.GOLD_HORSE_ARMOR && pe.getItem().getCustomName().contains("隠れ身の術") && Data.CT <= 0)
		{
			PlaySound.PlaySoundAll(Data, "system.skill_active", Data.player.getPosition());
        	int Index = Data.player.getInventory().getHeldItemIndex();
        	Data.IsStartupSkill = Index;
        	Item item_back = Data.player.getInventory().getItemInHand().clone();
        	Data.Before_Skill_Item = Data.player.getInventory().getItemInHand().clone();
        	Enchantment enc = Enchantment.get(Enchantment.ID_TRIDENT_IMPALING);
        	item_back.addEnchantment(enc);
        	Data.player.getInventory().setItem(Data.player.getInventory().getHeldItemIndex(), item_back);
        	new Thread(new Runnable() {
	            @Override
	            public void run()
	            {
	            	try {
	    				Thread.sleep(1000);
	    			} catch (InterruptedException e1) {
	    				// TODO 自動生成された catch ブロック
	    				e1.printStackTrace();
	    			}
	            	if (Data.IsStartupSkill == -1)
	            		return;
	            	Data.player.getInventory().setItem(Index, Data.Before_Skill_Item);
	            	Data.Before_Skill_Item = null;
	            	Data.IsStartupSkill = -1;
					Data.Skill_Time = 0;
	    			for(Player player : Server.getInstance().getOnlinePlayers().values())
	    			{
	    				Player_Data p_data = Sub_Code.Get_Player_Data(player);
	    				if (p_data.Team_Color != Data.Team_Color && p_data.Skill_Now != "Scanner")
	    				    player.hidePlayer(Data.player);
	    			}
	    	        Data.IsNinjaSkillUsing = true;
	    	        ScoreBoard_Show.Add_CT_Text(Data);
	            }
        	}).start();
			int Index2 = Skill_Main.Skill_Name.indexOf("忍者");
			Data.CT = Skill_Main.Skill_CT.get(Index2);	
	        if (Data.CT > 0)
	        	ScoreBoard_Show.Add_CT_Text(Data);
		}
	}
	public static void Get_Item(Player p)
	{ 
		Effect e = Effect.getEffect(Effect.SPEED);
		e.setAmplifier(0);
		e.setDuration(99999);
		p.addEffect(e);
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
	    ItemTool Sword_Item = (ItemTool) ItemTool.get(Item.IRON_SWORD);
	    Sword_Item.setCustomName("日本刀");
	    Item Skill_Item = Item.get(Item.GOLD_HORSE_ARMOR);
	    Skill_Item.setCustomName("隠れ身の術");
		p.getInventory().setItem(0, Sword_Item);
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