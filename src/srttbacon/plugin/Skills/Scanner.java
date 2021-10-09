package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Scanner
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Scanner";
		Data.CT = 0;
		Data.Skill_Now = "Scanner";
	}
	public static void Activate(PlayerInteractEvent pe)
	{
		Player p = pe.getPlayer();
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null || Data.CT > 0)
			return;
		if (pe.getItem().getId() == Item.SPYGLASS)
		{
			int Index1 = Skill_Main.Skill_Name.indexOf("Scanner");
			Data.CT = Skill_Main.Skill_CT.get(Index1);
			ScoreBoard_Show.Add_CT_Text(Data);
			PlaySound.PlaySoundAll(Data, "system.skill_active", Data.player.getPosition());
        	int Index = Data.player.getInventory().getHeldItemIndex();
        	Data.IsStartupSkill = Index;
        	Item item_back = Data.player.getInventory().getItemInHand().clone();
        	Data.Before_Skill_Item = Data.player.getInventory().getItemInHand().clone();
        	Enchantment enc = Enchantment.get(Enchantment.ID_TRIDENT_IMPALING);
        	item_back.addEnchantment(enc);
        	Data.player.getInventory().setItem(Data.player.getInventory().getHeldItemIndex(), item_back);
        	new Thread(new Runnable() {
    			int Exist_Count = 0;
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
	    			int p_x = (int)p.x;
	    			int p_y = (int)p.y;
	    			int p_z = (int)p.z;
	            	Data.player.getInventory().setItem(Index, Data.Before_Skill_Item);
	            	Data.Before_Skill_Item = null;
	            	Data.IsStartupSkill = -1;
	    			for (Player player: Server.getInstance().getOnlinePlayers().values())
	    			{
	    				if (player.getName() == p.getName())
	    					continue;
	    				if (player.x > p_x - 50 && player.x < p_x + 50 && player.y > p_y - 20 && player.y < p_y + 20 && player.z > p_z - 50 && player.z < p_z + 50)
	    				{
	    					Player_Data p_Data = Sub_Code.Get_Player_Data(player);
	    					if (p_Data.Team_Color != Data.Team_Color || p_Data.Team_Color == Sub_Code.Team.None)
	    					{
								CompoundTag nbt = new CompoundTag()
								.putList(new ListTag<DoubleTag>("Pos")
										.add(new DoubleTag("", player.x))
										.add(new DoubleTag("", player.y + player.getEyeHeight()))
										.add(new DoubleTag("", player.z)))
								.putList(new ListTag<DoubleTag>("Motion")
										.add(new DoubleTag("", 0))
										.add(new DoubleTag("", player.y + 15))
										.add(new DoubleTag("", 0)))
								.putList(new ListTag<FloatTag>("Rotation")
										.add(new FloatTag("", (float) player.yaw))
										.add(new FloatTag("", (float) player.pitch)));
								if (p_Data.Skill_Now == "忍者" && p_Data.IsNinjaSkillUsing)
								    for (Player player1: Server.getInstance().getOnlinePlayers().values())
									    player1.showPlayer(p_Data.player);
	    						EntityFirework firework = new EntityFirework(player.chunk, nbt);
	    						firework.setFirework(Item.get(Item.FIREWORKS));
	    						firework.spawnToAll();
	    						player.sendMessage(TextFormat.RED + "敵チームから索敵されたようだ...");
	    						Exist_Count++;
	    					}
	    				}
	    			}
	            	if (Exist_Count > 0)
	    			{
	    	            Data.CT = Skill_Main.Skill_CT.get(Index);
	    				Effect e = Effect.getEffect(Effect.SPEED);
	    				e.setDuration(200);
	    				if (Exist_Count >= 5)
						{
							Sub_Code.Skill_Achievement(Data, "Scanner");
	    					e.setAmplifier(3);
					    }
	    				else
	    					e.setAmplifier(2);
	    				Data.player.addEffect(e);
	    			}
	    			else
	    				Data.CT = 5;
	            }
        	}).start();
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
	    Item Skill_Item = Item.get(Item.SPYGLASS);
	    Skill_Item.setCustomName("Scanner");
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