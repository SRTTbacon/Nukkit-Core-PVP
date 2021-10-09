package srttbacon.plugin.Class;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Skills.Archer;

public class Player_Data
{
	public ArrayList<String> Has_Skills = new ArrayList<String>();
	public ArrayList<String> Has_Achievements = new ArrayList<String>();
	public String Skill_Now = "Miner";
	public String Achievement_Now = "";
	public String No_Drop_Item = "";
	public String Voice_Name = "hanai_risa";
	public int PT = 0;
	public int CT = 0;
	public int FallDamageTimer = 0;
	public int Skill_Time = 0;
	public int Scanner_Effect_Time = 0;
	public int No_Move_Time = 0;
	public int Kill_Count = 0;
	public int Kill_Continue_Count = 0;
	public int Death_Count = 0;
	public int Core_Break_Count = 0;
	public int Damage_Time = 0;
	public int Warrior_Damage_Time = 0;
	public int IsStartupSkill = -1;
	public int Before_Max_Health = 20;
	public int Core_Heal_Count = 0;
	public int Get_Iron_Count = 0;
	public int Warrior_Kill_Count = 0;
	public int CPS = 0;
	public float Voice_Volume = 0.0f;
	public float SE_Volume = 0.6f;
	public float Voice_Pitch = 1.0f;
	public boolean IsWindowShowing = false;
	public boolean IsSkillGot = false;
	public boolean IsLobbyMode = true;
	public boolean IsNinjaSkillUsing = false;
	public boolean IsBerserkerUsing = false;
	public boolean IsNoMoveMode = false;
	public Item Before_Skill_Item = null;
	public Sub_Code.Team Team_Color = Sub_Code.Team.None;
	public Archer.Archer_Type Archer_Type = Archer.Archer_Type.ライトニング;
	public Player player = null;
}