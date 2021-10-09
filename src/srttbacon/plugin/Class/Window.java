package srttbacon.plugin.Class;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Game;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Achievement.Achievement_Main;
import srttbacon.plugin.Achievement.Achievement_Main.All_Achievement_Save;
import srttbacon.plugin.Achievement.Battle_Achievement;
import srttbacon.plugin.Achievement.Battle_Achievement_Class;
import srttbacon.plugin.Skills.Archer.Archer_Type;
import srttbacon.plugin.Skills.Skill_Main;

public class Window
{
	public static class Achievement_Selector
	{
		public String Player_Name;
		public Achievement_Main.All_Achievement_Save Achievement_Save_Data;
	}
	private static ArrayList<Achievement_Selector> Achievement_Selector_List = new ArrayList<Achievement_Selector>();
	public static void onFormResponse(PlayerFormRespondedEvent e)
	{
		Player p = e.getPlayer();
		Player_Data p_data = Sub_Code.Get_Player_Data(p);
	    if (e.getWindow() instanceof FormWindowSimple)
	    {
	        FormWindowSimple gui = (FormWindowSimple) e.getWindow();
			if (gui.getResponse() == null || gui.wasClosed())
			{
				p_data.IsWindowShowing = false;
				PlaySound.PlaySoundOne(p_data, "system.close", p.getPosition());
				return;
			}
			PlaySound.PlaySoundOne(p_data, "system.select", p.getPosition());
	        if (gui.getTitle().contains("職業ショップ"))
	        {
		        String responseName = gui.getResponse().getClickedButton().getText();
		        String Name_Only = responseName.substring(0, responseName.indexOf('：') - 1);
		        int Index = Skill_Main.Skill_Name.indexOf(Name_Only);
		        if (p_data.PT < Skill_Main.Skill_Cost.get(Index))
		        {
		        	p.sendMessage(TextFormat.GREEN + "PTが足りません。");
		        	return;
		        }
		        if (p_data.Has_Skills.contains(Name_Only))
		        	p.sendMessage(TextFormat.GREEN + "職業を" + Name_Only + "に変更しました。");
		        else
		        {
		        	p.sendMessage(TextFormat.GREEN + "職業:" + Name_Only + "を購入しました。");
		        	p_data.PT -= Skill_Main.Skill_Cost.get(Index);
		        	p_data.Has_Skills.add(Name_Only);
		        }
		        Skill_Main.Set_Skill(p_data, Skill_Main.Skill_Name.get(Index));
		        p_data.player.getInventory().clearAll();
		        if (p_data.Team_Color != Sub_Code.Team.None && !p_data.IsLobbyMode)
		        {
			        Skill_Main.Get_Skill_Item(p_data);
			        if (p_data.Team_Color == Sub_Code.Team.Blue)
			        	p_data.player.teleport(Game.Map_Now.Blue_Team_Spawn_Pos);
			        else if (p_data.Team_Color == Sub_Code.Team.Red)
			        	p_data.player.teleport(Game.Map_Now.Red_Team_Spawn_Pos);
		        }
	        }
	        else if (gui.getTitle().contains("称号切り替え"))
	        {
	        	String responseName = gui.getResponse().getClickedButton().getText();
	        	if (responseName.contains("称号を持っていません。") || responseName.contains("(選択済み)"))
	        		return;
	        	else if (responseName.contains("称号なし"))
	        	{
	        		p_data.Achievement_Now = "";
	        		p.sendMessage(TextFormat.GREEN + "称号を無効にしました。");
	        	}
	        	else
	        	{
	        	    p_data.Achievement_Now = responseName;
	        	    p.sendMessage(TextFormat.GREEN + "称号を" + p_data.Achievement_Now + TextFormat.GREEN + "に変更しました。");
	        	}
	        	Sub_Code.Set_Player_Name(p_data);
	        }
	        else if (gui.getTitle().contains("称号の色を指定"))
	        {
	        	String responseName = "";
	        	try
	        	{
	        		responseName = gui.getResponse().getClickedButton().getText();
	        	}
	        	catch (Exception e1)
	        	{
	        		for (Achievement_Selector as: Achievement_Selector_List)
	        			if (as.Player_Name == p.getName())
	        			{
	        				Achievement_Selector_List.remove(as);
	        				break;
	        			}
	        		p.sendMessage("称号の割り当てを解除しました。");
	        		return;
	        	}
	        	for (Achievement_Selector as: Achievement_Selector_List)
	        	{
	        		if (as.Player_Name == p.getName())
	        		{
	        			for (Achievement_Main.All_Achievement_Save as2: Achievement_Main.All_Achievement_List)
	        			{
	        				if (responseName.contains(as2.Name))
	        				{
	        					as2.Name = responseName;
	        					Sub_Code.Server_Main.broadcastMessage(p.getDisplayName() + TextFormat.GREEN + "が称号を割り当てました。" + TextFormat.WHITE + "称号名:" + as2.Name);
	        					break;
	        				}
	        			}
	        			Achievement_Selector_List.remove(as);
	        			return;
	        		}
	        	}	
	        }
			else if (gui.getTitle().contains("Archer : スキル選択"))
			{
	        	String responseName = gui.getResponse().getClickedButton().getText();
				String Skill_Type_Name = responseName.substring(responseName.indexOf(":") + 2);
				p_data.Archer_Type = Archer_Type.valueOf(Skill_Type_Name);
				p.sendMessage(TextFormat.GREEN + "Archerのスキルを" + Skill_Type_Name + "に変更しました。");
				p_data.IsWindowShowing = false;
			}
			else if (gui.getTitle().contains("称号ヒント : メニュー"))
			{
	        	String responseName = gui.getResponse().getClickedButton().getText();
				if (responseName.contains("看板で得られる称号"))
				    Achievement_Hint_Sign_Show(p_data);
				else if (responseName.contains("条件達成や特定の行動で得られる称号"))
				    Achievement_Hint_Action_Show(p_data);
			}
			else if (gui.getTitle().contains("称号ヒント : 看板"))
			{
				String responseName = gui.getResponse().getClickedButton().getText();
				if (responseName.contains("戻る"))
				    Achievement_Hint_Show(p);
				else
					Achievement_Hint_Sign_Text_Show(p_data, gui.getResponse().getClickedButtonId());
			}
			else if (gui.getTitle().contains("称号ヒント : 条件&行動"))
			{
				String responseName = gui.getResponse().getClickedButton().getText();
				if (responseName.contains("戻る"))
				    Achievement_Hint_Show(p);
				else
					Achievement_Hint_Action_Text_Show(p_data, gui.getResponse().getClickedButtonId());
			}
	    }
		else if (e.getWindow() instanceof FormWindowCustom)
		{
			FormWindowCustom gui_custom = (FormWindowCustom) e.getWindow();
			if (gui_custom.getResponse() == null || gui_custom.wasClosed())
			{
				PlaySound.PlaySoundOne(p_data, "system.close", p.getPosition());
				return;
			}
			PlaySound.PlaySoundOne(p_data, "system.select", p.getPosition());
			if (gui_custom.getTitle().contains("サウンド設定"))
			{
				/*p_data.player.sendMessage(gui_custom.getResponse().getDropdownResponse(0).getElementContent() + " | " + gui_custom.getResponse().getSliderResponse(0)
				 + " | " + gui_custom.getResponse().getSliderResponse(1) + " | " + gui_custom.getResponse().getSliderResponse(2));*/
				p_data.Voice_Name = gui_custom.getResponse().getDropdownResponse(0).getElementContent().toLowerCase();
				p_data.Voice_Volume = gui_custom.getResponse().getSliderResponse(1) / 100.0f;
				p_data.Voice_Pitch = gui_custom.getResponse().getSliderResponse(2) / 100.0f;
				p_data.SE_Volume = gui_custom.getResponse().getSliderResponse(3) / 100.0f;
			}
		}
		else if (e.getWindow() instanceof FormWindowModal)
		{
			FormWindowModal gui_modal = (FormWindowModal)e.getWindow();
			if (gui_modal.getResponse() == null || gui_modal.wasClosed() || gui_modal.getResponse().getClickedButtonText() == "閉じる")
			{
				PlaySound.PlaySoundOne(p_data, "system.close", p.getPosition());
				return;
			}
			PlaySound.PlaySoundOne(p_data, "system.select", p.getPosition());
			if (gui_modal.getTitle().contains("称号ヒント : 看板") && gui_modal.getResponse().getClickedButtonText() == "戻る")
				Achievement_Hint_Sign_Show(p_data);
			else if (gui_modal.getTitle().contains("称号ヒント : 条件&行動") && gui_modal.getResponse().getClickedButtonText() == "戻る")
			    Achievement_Hint_Action_Show(p_data);
		}
	}
	public static void Entity_Right_Click(PlayerInteractEntityEvent e, Player_Data p_data)
	{
		Player p = e.getPlayer();
		Entity entity = e.getEntity();
		if (entity instanceof EntityVillager)
		{
			if (entity.getNameTag().contains("職業ショップ"))
			{
				FormWindowSimple gui = new FormWindowSimple("職業ショップ", "所持PT:" + p_data.PT);
				for (int Number = 0; Number < Skill_Main.Skill_Name.size(); Number++)
				{
					if (Skill_Main.Skill_IsEnable.get(Number))
					{
						if (p_data.Has_Skills.contains(Skill_Main.Skill_Name.get(Number)))
					        gui.addButton(new ElementButton(Skill_Main.Skill_Name.get(Number) + " ： " + Skill_Main.Skill_Cost.get(Number) + "PT(購入済み)"));
						else
					        gui.addButton(new ElementButton(Skill_Main.Skill_Name.get(Number) + " ： " + Skill_Main.Skill_Cost.get(Number) + "PT"));
					}
				}
				PlaySound.PlaySoundOne(p_data, "system.menu", p.getPosition());
			    p.showFormWindow(gui);
			}
			else if (entity.getNameTag().contains("称号切り替え"))
			{
				FormWindowSimple gui;
				if (p_data.Achievement_Now == "")
					gui = new FormWindowSimple("称号切り替え", "現在の称号:なし");
				else
					gui = new FormWindowSimple("称号切り替え", "現在の称号:" + p_data.Achievement_Now);
				if (p_data.Has_Achievements.size() > 0)
				{
					gui.addButton(new ElementButton("称号なし"));
					for (String Achi: p_data.Has_Achievements)
					{
						if (Achi == p_data.Achievement_Now)
							gui.addButton(new ElementButton(Achi + " (選択済み)"));
						else
							gui.addButton(new ElementButton(Achi));
					}
				}
				else
					gui.addButton(new ElementButton("称号を持っていません。"));
				PlaySound.PlaySoundOne(p_data, "system.menu", p.getPosition());
			    p.showFormWindow(gui);
			}
			else if (entity.getNameTag().contains("称号ヒント"))
			{
				PlaySound.PlaySoundOne(p_data, "system.menu", p.getPosition());
				Achievement_Hint_Show(p);
			}
		}
	}
	public static void Sound_Setting_Show(Player_Data p)
	{
		Form_Window.CustomForm form = new Form_Window.CustomForm("サウンド設定");
		int Index = 0;
		for (int Number = 0; Number < Sub_Code.Voice_Type_List.size(); Number++)
		{
			if (p.Voice_Name.equalsIgnoreCase(Sub_Code.Voice_Type_List.get(Number)))
			{
				Index = Number;
				break;
			}
		}
		form.Add_ComboBox("音声の種類", Sub_Code.Voice_Type_List, Index);
		form.Add_Slider("音声(音量) ", 0.0f, 100.0f, (p.Voice_Volume * 100));
		form.Add_Slider("音声(ピッチ) ", 50f, 150.0f, (p.Voice_Pitch * 100));
		form.Add_Slider("効果音 ", 0.0f, 100.0f, (p.SE_Volume * 100));
		form.Show(p.player);
	}
	public static void Achievement_Show(Player p, Achievement_Main.All_Achievement_Save Achievement_Save_Data)
	{
		FormWindowSimple gui = new FormWindowSimple("称号の色を指定", "該当の称号:" + Achievement_Save_Data.Name);
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.AQUA + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.BLACK + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.BLUE + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.DARK_AQUA + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.DARK_BLUE + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.DARK_GREEN + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.DARK_PURPLE + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.DARK_RED + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.GOLD + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.GRAY + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.GREEN + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.LIGHT_PURPLE + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.MINECOIN_GOLD + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.RED + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.WHITE + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		gui.addButton(new ElementButton(TextFormat.WHITE + "【" + TextFormat.YELLOW + Achievement_Save_Data.Name + TextFormat.WHITE + "】"));
		Achievement_Selector as = new Achievement_Selector();
		as.Player_Name = p.getName();
		as.Achievement_Save_Data = Achievement_Save_Data;
		Achievement_Selector_List.add(as);
	    p.showFormWindow(gui);
	}
	public static void Achievement_Hint_Show(Player p)
	{
		FormWindowSimple gui = new FormWindowSimple("称号ヒント : メニュー", "");
		gui.addButton(new ElementButton("看板で得られる称号"));
		gui.addButton(new ElementButton("条件達成や特定の行動で得られる称号"));
		p.showFormWindow(gui);
	}
	public static void Achievement_Hint_Sign_Show(Player_Data p)
	{
		FormWindowSimple gui = new FormWindowSimple("称号ヒント : 看板", "");
		gui.addButton(new ElementButton("戻る"));
		int Index = 1;
		for (All_Achievement_Save Temp: Achievement_Main.All_Achievement_List)
		{
			if (!Temp.Hint.contains("ヒントなし"))
			{
				if (p.Has_Achievements.contains(Temp.Name))
				    gui.addButton(new ElementButton(Index + " :" + Temp.Name));
				else
				    gui.addButton(new ElementButton(Index + " :" + TextFormat.WHITE + "【" + Temp.Name.substring(3, 5) + "?????(未取得)" + TextFormat.WHITE + "】"));
				Index++;
			}
		}
		gui.addButton(new ElementButton("戻る"));
		p.player.showFormWindow(gui);
	}
	public static void Achievement_Hint_Sign_Text_Show(Player_Data p, int Button_Index)
	{
		int Index = 0;
		Button_Index--;
		for (All_Achievement_Save Temp: Achievement_Main.All_Achievement_List)
		{
			if (!Temp.Hint.contains("ヒントなし"))
			{
				if (Index == Button_Index)
				{
					Form_Window.ModalForm form = new Form_Window.ModalForm("称号ヒント : 看板", "戻る", "閉じる");
					form.Set_Content_Text("ヒント : " + Temp.Hint);
					form.Show(p.player);
					break;
				}
				Index++;
			}
		}
	}
	public static void Achievement_Hint_Action_Show(Player_Data p)
	{
		FormWindowSimple gui = new FormWindowSimple("称号ヒント : 条件&行動", "");
		gui.addButton(new ElementButton("戻る"));
		int Index = 1;
		for (Battle_Achievement_Class Temp: Battle_Achievement.Batlle_All_Achievement)
		{
			if (p.Has_Achievements.contains(TextFormat.WHITE + "【" + Temp.Name + TextFormat.WHITE + "】"))
			    gui.addButton(new ElementButton(Index + " :" + TextFormat.WHITE + "【" + Temp.Name + TextFormat.WHITE + "】"));
			else
			    gui.addButton(new ElementButton(Index + " :" + TextFormat.WHITE + "【" + Temp.Name.substring(0, 2) + "?????(未取得)" + TextFormat.WHITE + "】"));
			Index++;
		}
		for (Battle_Achievement_Class Temp: Battle_Achievement.PT_All_Achievement)
		{
			if (p.Has_Achievements.contains(TextFormat.WHITE + "【" + Temp.Name + TextFormat.WHITE + "】"))
			    gui.addButton(new ElementButton(Index + " :" + TextFormat.WHITE + "【" + Temp.Name + TextFormat.WHITE + "】"));
			else
			    gui.addButton(new ElementButton(Index + " :" + TextFormat.WHITE + "【" + Temp.Name.substring(0, 2) + "?????(未取得)" + TextFormat.WHITE + "】"));
			Index++;
		}
		for (Battle_Achievement_Class Temp: Battle_Achievement.Skill_All_Achievement)
		{
			if (p.Has_Achievements.contains(TextFormat.WHITE + "【" + Temp.Name + TextFormat.WHITE + "】"))
			    gui.addButton(new ElementButton(Index + " :" + TextFormat.WHITE + "【" + Temp.Name + TextFormat.WHITE + "】"));
			else
			    gui.addButton(new ElementButton(Index + " :" + TextFormat.WHITE + "【" + Temp.Name.substring(0, 2) + "?????(未取得)" + TextFormat.WHITE + "】"));
			Index++;
		}
		gui.addButton(new ElementButton("戻る"));
		p.player.showFormWindow(gui);
	}
	public static void Achievement_Hint_Action_Text_Show(Player_Data p, int Button_Index)
	{
		Button_Index--;
		Battle_Achievement_Class Temp = null;
		if (Button_Index >= Battle_Achievement.Batlle_All_Achievement.size())
		    Button_Index -= Battle_Achievement.Batlle_All_Achievement.size();
		else
		    Temp = Battle_Achievement.Batlle_All_Achievement.get(Button_Index);
		if (Temp == null && Button_Index >= Battle_Achievement.PT_All_Achievement.size())
		    Button_Index -= Battle_Achievement.PT_All_Achievement.size();
		else if (Temp == null)
		    Temp = Battle_Achievement.PT_All_Achievement.get(Button_Index);
		if (Temp == null && Button_Index >= Battle_Achievement.Skill_All_Achievement.size())
		    Button_Index -= Battle_Achievement.Skill_All_Achievement.size();
		else if (Temp == null)
		    Temp = Battle_Achievement.Skill_All_Achievement.get(Button_Index);
		if (Temp != null)
		{
			Form_Window.ModalForm form = new Form_Window.ModalForm("称号ヒント : 条件&行動", "戻る", "閉じる");
			if (p.Has_Achievements.contains(TextFormat.WHITE + "【" + Temp.Name + TextFormat.WHITE + "】"))
			    form.Set_Content_Text("ヒント : " + Temp.Answer);
			else
			    form.Set_Content_Text("ヒント : " + Temp.Hint);
			form.Show(p.player);
		}
	}
}