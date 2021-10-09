package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Handyman
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Handyman";
		Data.CT = 0;
		Data.Skill_Now = "Handyman";
	}
	public static void Activate(PlayerInteractEvent e)
	{
		Player_Data From_Player = Sub_Code.Get_Player_Data(e.getPlayer());
		if (e.getItem().getCustomName().contains("Handyman") && From_Player.CT <= 0)
		{
			int p_x = (int)From_Player.player.x;
			int p_y = (int)From_Player.player.y;
			int p_z = (int)From_Player.player.z;
			boolean IsExist = false;
			Player To_TP_Player = null;
			int Block_Distance = 0;
			for (Player player : Server.getInstance().getOnlinePlayers().values())
			{
				if (From_Player.player.getName() == player.getName())
					continue;
				if (player.x > p_x - 50 && player.x < p_x + 50 && player.y > p_y - 20 && player.y < p_y + 20 && player.z > p_z - 50 && player.z < p_z + 50)
				{
					Player_Data p_Data = Sub_Code.Get_Player_Data(player);
					if (p_Data.Team_Color == From_Player.Team_Color)
					{
						IsExist = true;
						int Distance = (int)Math.abs(player.x) + (int)Math.abs(player.z);
						if (Block_Distance == 0 || Block_Distance > Distance)
						{
							To_TP_Player = player;
							Block_Distance = Distance;
						}
					}
				}
			}
			if (IsExist)
			{
				Player to_player = To_TP_Player;
				new Thread(new Runnable() {
		            @Override
		            public void run()
		            {
		            	PlaySound.PlaySoundAll(From_Player, "system.skill_active", From_Player.player.getPosition());
		            	int Index = From_Player.player.getInventory().getHeldItemIndex();
		            	From_Player.IsStartupSkill = Index;
		            	Item item_back = From_Player.player.getInventory().getItemInHand().clone();
		            	From_Player.Before_Skill_Item = From_Player.player.getInventory().getItemInHand().clone();
		            	Enchantment enc = Enchantment.get(Enchantment.ID_TRIDENT_IMPALING);
		            	item_back.addEnchantment(enc);
		            	From_Player.player.getInventory().setItem(From_Player.player.getInventory().getHeldItemIndex(), item_back);
		            	try {
							Thread.sleep(1200);
						} catch (InterruptedException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
		            	if (From_Player.IsStartupSkill == -1)
		            		return;
		            	From_Player.player.getInventory().setItem(Index, From_Player.Before_Skill_Item);
		            	From_Player.Before_Skill_Item = null;
		            	From_Player.IsStartupSkill = -1;
						From_Player.player.teleport(to_player.getLocation());
						to_player.sendMessage(From_Player.player.getDisplayName() + TextFormat.GREEN + "が自身にテレポートしました。");
						From_Player.player.sendMessage(to_player.getDisplayName() + TextFormat.GREEN + "へテレポートしました。");
		            }
				}).start();;
				int Index2 = Skill_Main.Skill_Name.indexOf("Handyman");
				From_Player.CT = Skill_Main.Skill_CT.get(Index2);
			}
			else
			{
				From_Player.CT = 5;
				From_Player.player.sendMessage(TextFormat.GREEN + "近くに味方プレイヤーはいないようだ...");
			}
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
	    Item Skill_Item = Item.get(Item.SLIMEBALL);
	    Skill_Item.setCustomName("Handyman");
		p.getInventory().setItem(0, (ItemTool) ItemTool.get(Item.WOODEN_SWORD));
		p.getInventory().setItem(1, (ItemTool) ItemTool.get(Item.WOODEN_AXE));
		p.getInventory().setItem(2, (ItemTool) ItemTool.get(Item.STONE_PICKAXE));
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