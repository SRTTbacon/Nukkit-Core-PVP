package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.PlaySound;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Archer
{
	public enum Archer_Type
	{
		ライトニング,
		テレポート,
		フレイム,
		ブラインド,
		イモビライザー,
		パワー_ヒール,
	}
	public class Archer_Skill
	{
		public String Player_Name;
		public Item Bow;
	}
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Archer Bow";
		Data.CT = 0;
		Data.Skill_Now = "Archer";
	}
	public static void Skill_Select(PlayerInteractEvent e)
	{
		if (e.getItem().getId() == Item.MAGMA_CREAM && e.getItem().getCustomName().contains("Skill_Select"))
		{
			Player_Data p_data = Sub_Code.Get_Player_Data(e.getPlayer());
			if (p_data.IsWindowShowing)
			return;
			FormWindowSimple gui = new FormWindowSimple("Archer : スキル選択", "現在選択中のスキル : " + Sub_Code.Get_Player_Data(e.getPlayer()).Archer_Type.name());
			for (Archer_Type Types: Archer_Type.values())
				gui.addButton(new ElementButton("スキル : " + Types.name()));
		    PlaySound.PlaySoundOne(p_data, "system.menu", p_data.player.getPosition());
			p_data.player.showFormWindow(gui);
			p_data.IsWindowShowing = true;
		}
	}
	public static void Bow_Hit(ProjectileHitEvent e)
	{
		Entity entity = e.getEntity();
		int Index = -1;
		for (int Number = 0; Number < Sub_Code.Players_Info.size(); Number++)
		{
			if (Sub_Code.Players_Info.get(Number).player.getName() == entity.getNameTag())
			{
				Index = Number;
				break;
			}
		}
		if (Index != -1)
		{
			Player_Data Data = Sub_Code.Players_Info.get(Index);
	        int e_x = (int)entity.x; int e_y = (int)entity.y; int e_z = (int)entity.z;
			if (Data.Archer_Type == Archer_Type.ライトニング)
			{
				AddEntityPacket light = new AddEntityPacket();
		        light.type = 93;
		        light.entityRuntimeId = Entity.entityCount++;
		        light.yaw = (float) entity.getYaw();
		        light.pitch = (float) entity.getPitch();
		        light.x = entity.getFloorX();
		        light.y = entity.getFloorY();
		        light.z = entity.getFloorZ();
		        Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), light);
				int Attack_Count = 0;
		        for (Player player : Server.getInstance().getOnlinePlayers().values())
		        {
			        player.level.addSound(new Vector3(light.x, light.y, light.z), Sound.RANDOM_EXPLODE, 1.0f, 1.0f);
			        if (player.getName() == Data.player.getName())
		            	continue;
		        	if (player.x > light.x - 5 && player.x < light.x + 5 && player.y > light.y - 5 && player.y < light.y + 5 && player.z > light.z - 5 && player.z < light.z + 5)
		        	{
		        		Player_Data p_Data = Sub_Code.Get_Player_Data(player);
		        		if (p_Data.Team_Color != Data.Team_Color || p_Data.Team_Color == Sub_Code.Team.None)
		        		{
		        			EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(Data.player, player, DamageCause.ENTITY_ATTACK, 8.0f);
		        			player.attack(event);
							Attack_Count++;
		        		}
		        	}
		        }
				if (Attack_Count >= 3)
				    Sub_Code.Skill_Achievement(Data, "Archer");
			}
			else if (Data.Archer_Type == Archer_Type.テレポート)
			{
				if (Data.player.x - 50 > e_x || Data.player.x + 50 < e_x || Data.player.z - 50 > e_z || Data.player.z + 50 < e_z)
				{
					Data.player.sendMessage(TextFormat.GREEN + "距離が遠いためテレポートできない...");
					Data.CT = 7;
					ScoreBoard_Show.Add_CT_Text(Data);
					entity.kill();
					return;
				}
				Vector3 temp = new Vector3(entity.x, entity.y, entity.z);
				Data.player.teleport(entity.getPosition());
				new Thread(new Runnable() {
		            @Override
		            public void run()
		            {
		            	try {
							Thread.sleep(75);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Data.player.level.addSound(temp, Sound.MOB_ENDERMEN_PORTAL, 1.0f, 1.0f);
		            }
		        }).start();;
			}
			else
			{
				boolean IsExist = false;
				for (Player_Data p: Sub_Code.Players_Info)
				{
					if (p.player.getName() == Data.player.getName())
		            	continue;
			        int p_x = (int)p.player.x; int p_y = (int)p.player.y; int p_z = (int)p.player.z;
			        if (e_x == p_x && e_y - 2 <= p_y && e_y + 2 >= p_y && e_z == p_z)
			        {
			        	if (p.Team_Color != Data.Team_Color)
			        	{
				        	if (Data.Archer_Type == Archer_Type.ブラインド)
				        	{
					        	Effect effect = Effect.getEffect(Effect.BLINDNESS);
					        	effect.setDuration(140);
					        	p.player.addEffect(effect);
								IsExist = true;
								p.player.sendMessage(TextFormat.RED + "Archerのスキル効果を受けてしまった...");
				        	}
				        	else if (Data.Archer_Type == Archer_Type.フレイム)
				        	{
				        		p.player.setOnFire(7);
								IsExist = true;
								p.player.sendMessage(TextFormat.RED + "Archerのスキル効果を受けてしまった...");
				        	}
				        	else if (Data.Archer_Type == Archer_Type.イモビライザー)
				        	{
				        		p.No_Move_Time = 0;
								p.IsNoMoveMode = true;
								IsExist = true;
								p.player.sendMessage(TextFormat.RED + "Archerのスキル効果を受けてしまった...");
				        	}
			        	}
			        	else
			        	{
			        		if (Data.Archer_Type == Archer_Type.パワー_ヒール)
			        		{
			        			Effect effect1 = Effect.getEffect(Effect.STRENGTH);
			        			effect1.setAmplifier(0);
					        	effect1.setDuration(140);
					        	Effect effect2 = Effect.getEffect(Effect.REGENERATION);
					        	effect1.setAmplifier(0);
					        	effect2.setDuration(140);
					        	p.player.addEffect(effect1);
					        	p.player.addEffect(effect2);
								IsExist = true;
								if (p.player.getName() == Data.player.getName())
								    p.player.sendMessage(TextFormat.GREEN + "自身にスキル効果を与えた！");
								else
								    p.player.sendMessage(TextFormat.GREEN + "味方Archerからのスキル効果を受けた！");
			        		}
			        	}
			        }
				}
				if (!IsExist)
				{
					Data.CT = 7;
					ScoreBoard_Show.Add_CT_Text(Data);
				}
			}
	        entity.kill();
		}
		else if (entity.getNameTag() == "Bow_Normal")
            entity.kill();
    }
	public static void Bow_Shot(EntityShootBowEvent e)
	{
		if (e.getEntity() instanceof Player && e.getBow().getId() == Item.BOW);
		{
			Player_Data data = Sub_Code.Get_Player_Data((Player)e.getEntity());
			PlaySound.PlaySoundAll(data, "voice." + data.Voice_Name + ".bow_attack", data.player.getPosition());
			if (data.Skill_Now == "Archer" && data.CT == 0)
			{
				if (data.Archer_Type == Archer_Type.テレポート && data.IsStartupSkill == -1)
				{
					e.getProjectile().setNameTag("Bow_Normal");
					return;
				}
				e.getProjectile().setNameTag(data.player.getName());
				int Index2 = Skill_Main.Skill_Name.indexOf("Archer");
		        data.CT = Skill_Main.Skill_CT.get(Index2);
		        if (data.CT > 0)
		        	ScoreBoard_Show.Add_CT_Text(data);
			}
			else
				e.getProjectile().setNameTag("Bow_Normal");
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
	    Item Skill_Item = Item.get(Item.BOW);
	    Skill_Item.setCustomName("Archer Bow");
		Item Skill_Select_Item = Item.get(Item.MAGMA_CREAM);
		Skill_Select_Item.setCustomName("Skill_Select");
	    Item Arrow = Item.get(Item.ARROW, 0, 64);
		p.getInventory().setItem(0, (ItemTool) ItemTool.get(Item.WOODEN_SWORD));
		p.getInventory().setItem(1, (ItemTool) ItemTool.get(Item.WOODEN_AXE));
		p.getInventory().setItem(2, (ItemTool) ItemTool.get(Item.WOODEN_PICKAXE));
		p.getInventory().setItem(4, Skill_Item);
		p.getInventory().setItem(5, Skill_Select_Item);
	    p.getInventory().setItem(10, Arrow);
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