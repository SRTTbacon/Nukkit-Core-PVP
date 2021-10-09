package srttbacon.plugin.Skills;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityFishingHook;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BlockColor;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;

public class Scout_Skill
{
	public static  ArrayList<String> fishing_Name = new ArrayList<String>();
	public static ArrayList<EntityFishingHook> fishing_Hook = new ArrayList<EntityFishingHook>();
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Scout";
		Data.CT = 0;
		Data.Skill_Now = "Scout";
	}
	 public static void startFishing(Player player) {
	        CompoundTag nbt = new CompoundTag()
	                .putList(new ListTag<DoubleTag>("Pos")
	                        .add(new DoubleTag("", player.x))
	                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
	                        .add(new DoubleTag("", player.z)))
	                .putList(new ListTag<DoubleTag>("Motion")
	                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 + Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
	                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
	                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
	                .putList(new ListTag<FloatTag>("Rotation")
	                        .add(new FloatTag("", (float) player.yaw))
	                        .add(new FloatTag("", (float) player.pitch)));
	        double f = 1.2;
	        EntityFishingHook fishingHook = new EntityFishingHook(player.chunk, nbt, player);
	        fishingHook.setMotion(new Vector3(-Math.sin(Math.toRadians(player.yaw)) * Math.cos(Math.toRadians(player.pitch)) * f * f, -Math.sin(Math.toRadians(player.pitch)) * f * f,
	                Math.cos(Math.toRadians(player.yaw)) * Math.cos(Math.toRadians(player.pitch)) * f * f));
	        ProjectileLaunchEvent ev = new ProjectileLaunchEvent(fishingHook);
	        Sub_Code.Server_Main.getPluginManager().callEvent(ev);
	        if (ev.isCancelled()) {
	            fishingHook.kill();
	        } else {
	            fishingHook.spawnToAll();
	        }
	        fishing_Name.add(player.getName());
	        fishing_Hook.add(fishingHook);
	    }
	    public static void stopFishing(Player p)
	    {
	    	int Index = fishing_Name.indexOf(p.getName());
	    	if (Index == -1)
	    		return;
	    	EntityFishingHook aaa = fishing_Hook.get(Index);
	    	if (aaa.isCollided || aaa.onGround || aaa.isInsideOfWater())
	    	{
	    		Player_Data Data = Sub_Code.Get_Player_Data(p);
				if (Data != null && Data.CT == 0 && Data.Skill_Now == "Scout" && !Data.IsNoMoveMode)
				{
			    	Location l = aaa.getLocation();
			    	Location pl = p.getLocation();
			        double kyori = l.distance(pl);
			        double y = l.getY();
			        l.setY(y + 1.0D);
			        Vector3 vec = pl.getLocation();
			        p.setMotion(l.subtract(vec).normalize().multiply(kyori / 4.5D));
		    		int Index2 = Skill_Main.Skill_Name.indexOf("Scout");
			        Data.CT = Skill_Main.Skill_CT.get(Index2);
			        if (Data.CT > 0)
			        {
			        	ScoreBoard_Show.Add_CT_Text(Data);
			        }
				}
	    	}
	    	aaa.kill();
	        fishing_Name.remove(Index);
	        fishing_Hook.remove(Index);
	    }
	public static void onInteract(PlayerInteractEvent event)
	{
		Item item = event.getItem();
		if (Sub_Code.Get_Player_Data(event.getPlayer()).Skill_Now != "Scout")
			return;
	    if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR && item.getId() == Item.FISHING_ROD && item.getCustomName().contains("Scout"))
	    {
	        event.setCancelled();
	        if (fishing_Name.contains(event.getPlayer().getName()))
	        {
	            stopFishing(event.getPlayer());
	        }
	        else
	        {
	          startFishing(event.getPlayer());
	          item.setDamage(0);
	        }
	    }
	}
	public static void onItemHeld(PlayerItemHeldEvent event)
	{
	    int Index = fishing_Name.indexOf(event.getPlayer().getName());
	    if (Index == -1)
	    	return;
	    EntityFishingHook aaa = fishing_Hook.get(Index);
	    aaa.kill();
	    fishing_Name.remove(Index);
	    fishing_Hook.remove(Index);
	}
	public static void onQuit(PlayerQuitEvent event)
	{
		try
		{
			int Index = fishing_Name.indexOf(event.getPlayer().getName());
			if (Index == -1)
				return;
			fishing_Name.remove(Index);
	        fishing_Hook.remove(Index);
		}
		finally {};
	}
	public static void Get_Item(Player p)
	{
		PlayerInventory inventory = p.getInventory();
		Skill_Main.Player_Invectory_Move(p);
		ItemColorArmor helmet = (ItemColorArmor)Item.get(Item.LEATHER_CAP);
		ItemColorArmor pants = (ItemColorArmor)Item.get(Item.LEATHER_PANTS);
	    ItemColorArmor boots = (ItemColorArmor)Item.get(Item.LEATHER_BOOTS);
	    Player_Data data = Sub_Code.Get_Player_Data(p);
	    BlockColor color = Sub_Code.Get_Team_Color(data.Team_Color);
	    helmet.setColor(color);
	    pants.setColor(color);
	    boots.setColor(color);
	    Item Skill_Item = Item.get(Item.FISHING_ROD);
	    Skill_Item.setCustomName("Scout");
		p.getInventory().setItem(0, (ItemTool) ItemTool.get(Item.WOODEN_SWORD));
		p.getInventory().setItem(1, (ItemTool) ItemTool.get(Item.WOODEN_AXE));
		p.getInventory().setItem(2, (ItemTool) ItemTool.get(Item.WOODEN_PICKAXE));
		p.getInventory().setItem(4, Skill_Item);
		if (inventory.getHelmet().getId() == Item.AIR)
		    p.getInventory().setHelmet((Item)helmet);
		if (inventory.getLeggings().getId() == Item.AIR)
		    p.getInventory().setLeggings((Item)pants);
		if (inventory.getBoots().getId() == Item.AIR)
	        p.getInventory().setBoots((Item)boots);
	    Set_Skill(p);
	}
}