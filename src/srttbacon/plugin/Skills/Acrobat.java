package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.BlockColor;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Acrobat
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Acrobat";
		Data.CT = 0;
		Data.Skill_Now = "Acrobat";
	}
	public static void Activate(PlayerInteractEvent pe)
	{
		if (pe.getItem().getId() == Item.FEATHER && pe.getItem().getCustomName().contains("Acrobat"))
		{
			Player p = pe.getPlayer();
			Player_Data Data = Sub_Code.Get_Player_Data(p);
			if (Data == null || Data.CT > 0 || Data.IsNoMoveMode)
				return;
			new Thread(new Runnable() {
	            @Override
	            public void run() {
	            	int Index = p.getInventory().getHeldItemIndex();
	            	Data.IsStartupSkill = Index;
	            	//スキル発動(時間差あり)
	            	PlaySound.PlaySoundAll(Data, "system.skill_active", p.getPosition());
	            	Item item_back = p.getInventory().getItemInHand().clone();
	            	Data.Before_Skill_Item = p.getInventory().getItemInHand().clone();
	            	Enchantment enc = Enchantment.get(Enchantment.ID_TRIDENT_IMPALING);
	            	item_back.addEnchantment(enc);
	            	p.getInventory().setItem(p.getInventory().getHeldItemIndex(), item_back);
	            	try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
	            	if (Data.IsStartupSkill == -1)
	            		return;
	            	p.getInventory().setItem(Index, Data.Before_Skill_Item);
	            	Data.IsStartupSkill = -1;
	            	Data.Before_Skill_Item = null;
					Data.FallDamageTimer = 0;
					Data.Skill_Time = 0;
	            	//プレイヤーを向いている方向へ飛ばす
	    	        p.level.addSound(p, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.0f, 1.0f);
	    			Location pl = p.getLocation();
	    			//x.y.zそれぞれの最後のかける数を変更すると飛ぶ距離が変わります
	    	        Location loc = new Location(p.x - Math.sin(p.yaw / 180 * Math.PI) * Math.cos(p.pitch / 180 * Math.PI) * 12, p.y - Math.sin(p.pitch / 180 * Math.PI) * 9.5, p.z + Math.cos(p.yaw / 180 * Math.PI) * Math.cos(p.pitch / 180 * Math.PI) * 12);
	    	        double kyori = loc.distance(pl);
	    	        double y = loc.getY();
	    	        loc.setY(y + 1.0D);
	    	        p.setMotion(loc.subtract(pl).normalize().multiply(kyori / 4.5D));
	            }
	        }).start();
	        int Index = Skill_Main.Skill_Name.indexOf("Acrobat");
	        Data.CT = Skill_Main.Skill_CT.get(Index);
	        ScoreBoard_Show.Add_CT_Text(Data);
		}
	}
	public static boolean OnFallDamage(Player_Data p)
	{
		if (p.Skill_Now == "Acrobat")
		{
			if (p.Skill_Time >= 3 && p.Skill_Time <= 6)
				Sub_Code.Skill_Achievement(p, "Acrobat");
			p.Skill_Time = 10;
			if (p.FallDamageTimer < 6)
			    return false;
		}
		return true;
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
	    Item Skill_Item = Item.get(Item.FEATHER);
	    Skill_Item.setCustomName("Acrobat");
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