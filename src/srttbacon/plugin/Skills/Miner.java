package srttbacon.plugin.Skills;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import srttbacon.plugin.Game;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Player_Data;
import srttbacon.plugin.Class.ScoreBoard_Show;
import srttbacon.plugin.Map.Map_Not_Break_Pos;

public class Miner
{
	public static void Set_Skill(Player p)
	{
		Player_Data Data = Sub_Code.Get_Player_Data(p);
		if (Data == null)
			return;
		Data.No_Drop_Item = "Miner";
		Data.CT = 0;
		Data.Skill_Now = "Miner";
	}
	public static void Activate(PlayerInteractEvent pe)
	{
		if (pe.getItem().getId() == Item.SLIMEBALL && pe.getItem().getCustomName().contains("Miner"))
		{
			Player p = pe.getPlayer();
			Player_Data Data = Sub_Code.Get_Player_Data(p);
			if (Data == null || Data.CT > 0)
				return;
	        Location loc = new Location(p.x - Math.sin(p.yaw / 180 * Math.PI) * Math.cos(p.pitch / 180 * Math.PI) * 12, p.y - Math.sin(p.pitch / 180 * Math.PI) * 5, p.z + Math.cos(p.yaw / 180 * Math.PI) * Math.cos(p.pitch / 180 * Math.PI) * 12);
	        int Pos_Start_X = (int)p.x;
	        int Pos_Start_Y = (int)p.y;
	        int Pos_Start_Z = (int)p.z;
	        int Pos_End_X = (int)loc.x;
	        int Pos_End_Y = (int)loc.y;
	        int Pos_End_Z = (int)loc.z;
	        boolean IsMinus_X = false;
	        boolean IsMinus_Y = false;
	        boolean IsMinus_Z = false;
	        if (Pos_Start_X > Pos_End_X)
	        	IsMinus_X = true;
	        if (Pos_Start_Y > Pos_End_Y)
	        	IsMinus_Y = true;
	        else
	        	Pos_Start_Y--;
	        if (Pos_Start_Z > Pos_End_Z)
	        	IsMinus_Z = true;
	        boolean IsPut = false;
	        while (true)
	        {
	        	boolean IsOK_X = false;
	        	boolean IsOK_Y = false;
	        	boolean IsOK_Z = false;
	        	if (IsMinus_X)
	        	{
		        	if (Pos_Start_X > Pos_End_X)
		        		Pos_Start_X--;
		        	if (Pos_Start_X <= Pos_End_X)
		        		IsOK_X = true;
	        	}
	        	else
	        	{
	        		if (Pos_Start_X < Pos_End_X)
	        			Pos_Start_X++;
	        		if (Pos_Start_X >= Pos_End_X)
		        		IsOK_X = true;
	        	}
	        	if (IsMinus_Y)
	        	{
	        		if (Pos_Start_Y > Pos_End_Y)
	        			Pos_Start_Y--;
	        		if (Pos_Start_Y <= Pos_End_Y)
	        			IsOK_Y = true;
	        	}
	        	else
	        	{
	        		if (Pos_Start_Y < Pos_End_Y)
	        			Pos_Start_Y++;
	        		if (Pos_Start_Y >= Pos_End_Y)
	        			IsOK_Y = true;
	        	}
	        	if (IsMinus_Z)
	        	{
	        		if (Pos_Start_Z > Pos_End_Z)
	        			Pos_Start_Z--;
	        		if (Pos_Start_Z <= Pos_End_Z)
	        			IsOK_Z = true;
	        	}
	        	else
	        	{
	        		if (Pos_Start_Z < Pos_End_Z)
	        			Pos_Start_Z++;
	        		if (Pos_Start_Z >= Pos_End_Z)
	        			IsOK_Z = true;
	        	}
	        	if (IsOK_X && IsOK_Y && IsOK_Z)
	        		break;
	        	boolean IsPut_OK = true;
	        	for (Map_Not_Break_Pos NotBreakPos: Game.Map_Now.IsNotBreakPos)
				{
					if (Pos_Start_X >= NotBreakPos.Start_Pos.x && Pos_Start_X <= NotBreakPos.End_Pos.x && Pos_Start_Z >= NotBreakPos.Start_Pos.z && Pos_Start_Z <= NotBreakPos.End_Pos.z)
					{
						IsPut_OK = false;
						break;
					}
				}
	        	if (!IsPut_OK)
	        		continue;
	        	Vector3 Pos_Now = new Vector3(Pos_Start_X, Pos_Start_Y, Pos_Start_Z);
	        	if (p.level.getBlock(Pos_Now).getId() == Block.AIR)
	        	{
	        		Block stone = Block.get(Block.STONE);
	        		stone.setDamage(1);
	        		p.level.setBlock(Pos_Now, stone);
	        	}
	        	IsPut = true;
	        }
	        int Index = Skill_Main.Skill_Name.indexOf("Miner");
	        if (IsPut)
	            Data.CT = Skill_Main.Skill_CT.get(Index);
	        else
	        	Data.CT = 5;
	        if (Data.CT > 0)
	        	ScoreBoard_Show.Add_CT_Text(Data);
		}
	}
	public static void Get_Item(Player p)
	{
		Effect e = Effect.getEffect(Effect.HASTE);
		e.setAmplifier(0);
		e.setDuration(99999);
		p.addEffect(e);
		PlayerInventory inventory = p.getInventory();
		Skill_Main.Player_Invectory_Move(p);
		ItemColorArmor helmet = (ItemColorArmor)Item.get(Item.LEATHER_CAP);
		ItemColorArmor chestplate = (ItemColorArmor)Item.get(Item.LEATHER_TUNIC);
		ItemColorArmor pants = (ItemColorArmor)Item.get(Item.LEATHER_PANTS);
	    ItemColorArmor boots = (ItemColorArmor)Item.get(Item.LEATHER_BOOTS);
	    Player_Data data = Sub_Code.Get_Player_Data(p);
	    BlockColor color = Sub_Code.Get_Team_Color(data.Team_Color);
	    helmet.setColor(color);
	    chestplate.setColor(color);
	    pants.setColor(color);
	    boots.setColor(color);
	    Item Skill_Item = Item.get(Item.SLIMEBALL);
	    Skill_Item.setCustomName("Miner");
		p.getInventory().setItem(0, (ItemTool) ItemTool.get(Item.STONE_SWORD));
		p.getInventory().setItem(1, (ItemTool) ItemTool.get(Item.STONE_AXE));
		p.getInventory().setItem(2, (ItemTool) ItemTool.get(Item.IRON_PICKAXE));
		p.getInventory().setItem(4, Skill_Item);
		if (inventory.getHelmet().getId() == Item.AIR)
		    p.getInventory().setHelmet((Item)helmet);
		if (inventory.getChestplate().getId() == Item.AIR)
		    p.getInventory().setChestplate((Item)chestplate);
		if (inventory.getLeggings().getId() == Item.AIR)
		    p.getInventory().setLeggings((Item)pants);
		if (inventory.getBoots().getId() == Item.AIR)
	        p.getInventory().setBoots((Item)boots);
	    Set_Skill(p);
	}
}