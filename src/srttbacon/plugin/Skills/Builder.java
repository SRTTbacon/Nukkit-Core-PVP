package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import srttbacon.plugin.Game;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;
import srttbacon.plugin.Map.Map_Not_Break_Pos;

public class Builder
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Builder";
		Data.CT = 0;
		Data.Skill_Now = "Builder";
	}
	public static void Activate(PlayerInteractEvent pe)
	{
		if (pe.getItem().getId() == Item.CRAFTING_TABLE && pe.getItem().getCustomName().contains("Builder"))
		{
			Player p = pe.getPlayer();
			Vector3 Focus_Pos;
			boolean IsPlayerPos = false;
			if (pe.getBlock().getId() != Block.AIR)
			{
				Focus_Pos = pe.getBlock().getLocation();
				Focus_Pos.y = p.getLocation().y;
			}
			else
			{
				IsPlayerPos = true;
				Focus_Pos = p.getLocation();
				Focus_Pos.y = p.getLocation().y - 1;
			}
			Player_Data Data = Sub_Code.Get_Player_Data(p);
			if (Data == null || Data.CT > 0 || Data.IsLobbyMode)
				return;
			Location pl = p.getLocation();
	        Location loc = new Location(p.x - Math.sin(p.yaw / 180 * Math.PI) * Math.cos(p.pitch / 180 * Math.PI) * 12, p.y - Math.sin(p.pitch / 180 * Math.PI) * 10, p.z + Math.cos(p.yaw / 180 * Math.PI) * Math.cos(p.pitch / 180 * Math.PI) * 12);
	        double kyori = loc.distance(pl);
	        double y = loc.getY();
	        loc.setY(y + 1.0D);
	        Vector3 Angle = loc.subtract(pl).normalize().multiply(kyori / 4.5D);
	        int Angle_X = (int)Angle.x;
	        int Angle_Z = (int)Angle.z;
	        if (Angle_X == 2)
	        	Angle_X = 1;
	        else if (Angle_X == -2)
	        	Angle_X = -1;
	        if (Angle_Z == 2)
	        	Angle_Z = 1;
	        else if (Angle_Z == -2)
	        	Angle_Z = -1;
	        if (Angle_X == 1 && Angle_Z == 0)
	        {
	        	if (IsPlayerPos)
	        	    Focus_Pos = Focus_Pos.add(1, 1, 0);
	        	for (int Number = 0; Number < 3; Number++)
	        		for (int Number1 = -1; Number1 < 2; Number1++)
	        			Can_Put_Block(p, new Vector3(Focus_Pos.x, Focus_Pos.y + Number, Focus_Pos.z + Number1));
	        }
	        else if (Angle_X == -1 && Angle_Z == 0)
	        {
	        	if (IsPlayerPos)
	        	    Focus_Pos = Focus_Pos.add(-1, 1, 0);
	        	for (int Number = 0; Number < 3; Number++)
	        		for (int Number1 = -1; Number1 < 2; Number1++)
	        			Can_Put_Block(p, new Vector3(Focus_Pos.x, Focus_Pos.y + Number, Focus_Pos.z + Number1));
	        }
	        else if (Angle_Z == 1 && Angle_X == 0)
	        {
	        	if (IsPlayerPos)
	        	    Focus_Pos = Focus_Pos.add(0, 1, 1);
	        	for (int Number = 0; Number < 3; Number++)
	        		for (int Number1 = -1; Number1 < 2; Number1++)
	        			Can_Put_Block(p, new Vector3(Focus_Pos.x + Number1, Focus_Pos.y + Number, Focus_Pos.z));
	        }
	        else if (Angle_Z == -1 && Angle_X == 0)
	        {
	        	if (IsPlayerPos)
	        	    Focus_Pos = Focus_Pos.add(0, 1, -1);
	        	for (int Number = 0; Number < 3; Number++)
	        		for (int Number1 = -1; Number1 < 2; Number1++)
	        			Can_Put_Block(p, new Vector3(Focus_Pos.x + Number1, Focus_Pos.y + Number, Focus_Pos.z));
	        }
	        else if (Angle_X == 1 && Angle_Z == 1)
	        {
	        	if (IsPlayerPos)
	        	    Focus_Pos = Focus_Pos.add(1, 1, 1);
	        	else
	        		Focus_Pos = Focus_Pos.add(-1, 0, -1);
	        	for (int Number = 0; Number < 3; Number++)
	        		for (int Number1 = 0; Number1 < 3; Number1++)
	        		   Can_Put_Block(p, new Vector3(Focus_Pos.x + 2 - Number, Focus_Pos.y + Number1, Focus_Pos.z + Number));
	        }
	        else if (Angle_X == 1 && Angle_Z == -1)
	        {
	        	if (IsPlayerPos)
	        	    Focus_Pos = Focus_Pos.add(1, 1, -1);
	        	else
	        		Focus_Pos = Focus_Pos.add(-1, 0, 1);
	        	for (int Number = 0; Number < 3; Number++)
	        		for (int Number1 = 0; Number1 < 3; Number1++)
	        		   Can_Put_Block(p, new Vector3(Focus_Pos.x + 2 - Number, Focus_Pos.y + Number1, Focus_Pos.z  - Number));
	        }
	        else if (Angle_X == -1 && Angle_Z == -1)
	        {
	        	if (IsPlayerPos)
	        	    Focus_Pos = Focus_Pos.add(-1, 1, -1);
	        	else
	        		Focus_Pos = Focus_Pos.add(1, 0, 1);
	        	for (int Number = 0; Number < 3; Number++)
	        		for (int Number1 = 0; Number1 < 3; Number1++)
	        		   Can_Put_Block(p, new Vector3(Focus_Pos.x - 2 + Number, Focus_Pos.y + Number1, Focus_Pos.z - Number));
	        }
	        else if (Angle_X == -1 && Angle_Z == 1)
	        {
	        	if (IsPlayerPos)
	        	    Focus_Pos = Focus_Pos.add(-1, 1, 1);
	        	else
	        		Focus_Pos = Focus_Pos.add(1, 0, -1);
	        	for (int Number = 0; Number < 3; Number++)
	        		for (int Number1 = 0; Number1 < 3; Number1++)
	        		   Can_Put_Block(p, new Vector3(Focus_Pos.x - 2 + Number, Focus_Pos.y + Number1, Focus_Pos.z + Number));
	        }
	        else
	        	return;
	        int Index = Skill_Main.Skill_Name.indexOf("Builder");
	        Data.CT = Skill_Main.Skill_CT.get(Index);
	        ScoreBoard_Show.Add_CT_Text(Data);
		}
	}
	static void Can_Put_Block(Player p, Vector3 Pos)
	{
		for (Map_Not_Break_Pos NotBreakPos: Game.Map_Now.IsNotBreakPos)
		{
			if (Pos.x >= NotBreakPos.Start_Pos.x && Pos.x <= NotBreakPos.End_Pos.x && Pos.z >= NotBreakPos.Start_Pos.z && Pos.z <= NotBreakPos.End_Pos.z)
				return;;
		}
		if (p.level.getBlock(Pos).getId() == Block.AIR)
		{
			Block stone = Block.get(Block.STONE);
			stone.setDamage(1);
			p.level.setBlock(Pos, stone);
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
	    Item Skill_Item = Item.get(Item.CRAFTING_TABLE);
	    Skill_Item.setCustomName("Builder 3x3");
	    ItemBlock Skill_Stone_Item = new ItemBlock(Block.get(Block.STONE_BRICK), 0, 64);
	    Skill_Stone_Item.setCustomName("Builder 石レンガ");
	    ItemBlock Skill_Brick_Item = new ItemBlock(Block.get(Block.BRICKS_BLOCK), 0, 64);
	    Skill_Brick_Item.setCustomName("Builder レンガ");
	    ItemBlock Skill_WOOD_Item = new ItemBlock(Block.get(Block.WOOD), 0, 64);
	    Skill_WOOD_Item.setCustomName("Builder 木材");
	    ItemBlock Skill_Wool_Item = new ItemBlock(Block.get(Block.WOOL), 0, 64);
	    Skill_Wool_Item.setCustomName("Builder 羊毛");
		p.getInventory().setItem(0, (ItemTool) ItemTool.get(Item.WOODEN_SWORD));
		p.getInventory().setItem(1, (ItemTool) ItemTool.get(Item.STONE_AXE));
		p.getInventory().setItem(2, (ItemTool) ItemTool.get(Item.STONE_PICKAXE));
		p.getInventory().setItem(3, Skill_Stone_Item);
		p.getInventory().setItem(4, Skill_Brick_Item);
		p.getInventory().setItem(5, Skill_WOOD_Item);
		p.getInventory().setItem(6, Skill_Wool_Item);
		p.getInventory().setItem(8, Skill_Item);
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