package srttbacon.plugin.Achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.level.Sound;
import cn.nukkit.utils.TextFormat;
import srttbacon.plugin.Sub_Code;
import srttbacon.plugin.Class.Player_Data;

public class Battle_Achievement
{
	public static ArrayList<Battle_Achievement_Class> Batlle_All_Achievement = new ArrayList<Battle_Achievement_Class>();
	public static ArrayList<Battle_Achievement_Class> PT_All_Achievement = new ArrayList<Battle_Achievement_Class>();
	public static ArrayList<Battle_Achievement_Class> Skill_All_Achievement = new ArrayList<Battle_Achievement_Class>();
	static Map<String, Integer> Skill_Achievement_Index = new HashMap<>();
	public static void Init()
	{
		Batlle_All_Achievement.add(new Battle_Achievement_Class(TextFormat.RED + "キラー", "敵プレイヤーを連続でnキルすると得られる", "敵プレイヤーを連続で10キルすると得られる"));
		Batlle_All_Achievement.add(new Battle_Achievement_Class(TextFormat.DARK_RED + "殺人鬼", "敵プレイヤーを連続でnキルすると得られる", "敵プレイヤーを連続で15キルすると得られる"));
		Batlle_All_Achievement.add(new Battle_Achievement_Class(TextFormat.LIGHT_PURPLE + "悪魔", "敵プレイヤーを連続でnキルすると得られる", "敵プレイヤーを連続で20キルすると得られる"));
		Batlle_All_Achievement.add(new Battle_Achievement_Class(TextFormat.DARK_PURPLE + "殺戮の天使", "敵プレイヤーを連続でnキルすると得られる", "敵プレイヤーを連続で30キルすると得られる"));
		PT_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "小金持ち", "所持TPが一定以上に達した場合に得られる", "所持TPが5000以上になると得られる"));
		PT_All_Achievement.add(new Battle_Achievement_Class(TextFormat.DARK_AQUA + "大金持ち", "所持TPが一定以上に達した場合に得られる", "所持TPが10000以上になると得られる"));
		PT_All_Achievement.add(new Battle_Achievement_Class(TextFormat.AQUA + "大富豪", "所持TPが一定以上に達した場合に得られる", "所持TPが20000以上になると得られる"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "高所恐怖症", "職業:Acrobatを用いて条件を満たすと得られる", "職業:Acrobatを使用し、高所から3秒以上落下して着地する"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "雷神", "職業:Archerを用いて条件を満たすと得られる", "職業:Archerの雷スキルを使用し、一度に3人以上にダメージを与える"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "はやぶさ", "職業:Berserkerを用いて条件を満たすと得られる", "職業:Berserkerを使用して敵をキルした時に付与されるダメージ増加をレベル3にする"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "建築士", "職業:Builderを用いて条件を満たすと得られる", "職業:Builderを使用して配布されるブロックをすべて使い切る"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "修理屋", "職業:Handymanを用いて条件を満たすと得られる", "職業:Handymanを使用して自軍のコアをを20以上回復させる"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "治癒者", "職業:Healerを用いて条件を満たすと得られる", "職業:Healerを使用して1度に4人以上のプレイヤーを治癒する"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "虎の威", "職業:Immobilizerを用いて条件を満たすと得られる", "職業:Immobilizerを使用して1度に3人以上のプレイヤーを静止させる"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "採鉱", "職業:Minerを用いて条件を満たすと得られる", "職業:Minerを使用して鉄鉱石を2スタック以上集める"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "イフリート", "職業:Pyroを用いて条件を満たすと得られる", "職業:Pyroを使用して一度に3人以上の敵プレイヤーを燃やす"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "忍び", "職業:忍者を用いて条件を満たすと得られる", "職業:忍者のスキルを使用したまま敵コアを1回以上破壊する"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "観測者", "職業:Scannerを用いて条件を満たすと得られる", "職業:Scannerを使用して一度に5人以上索敵する"));
		Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "ドラキュラ", "職業:Vampireを用いて条件を満たすと得られる", "職業:Vampireを使用して最大体力をハート15にする"));
        Skill_All_Achievement.add(new Battle_Achievement_Class(TextFormat.YELLOW + "急襲", "職業:Warriorを用いて条件を満たすと得られる", "職業:Worriorのスキルを使用し、連続で2キルする"));
		for (int Number = 0; Number < Skill_All_Achievement.size(); Number++)
		{
			Battle_Achievement_Class Temp = Skill_All_Achievement.get(Number);
			String Skill_Name = Temp.Hint.substring(Temp.Hint.indexOf(":") + 1, Temp.Hint.indexOf("を"));
			Skill_Achievement_Index.put(Skill_Name, Number);
		}
	}
	public static String Get_Skill_Achievement_Name(String Skill_Name)
	{
		Battle_Achievement_Class Temp = Skill_All_Achievement.get(Skill_Achievement_Index.get(Skill_Name));
		return TextFormat.WHITE + "【" + Temp.Name + TextFormat.WHITE + "】";
	}
	public static int Get_Skill_Achievement_Index(String Skill_Name)
	{
		return Skill_Achievement_Index.get(Skill_Name);
	}
	public static void Get_Battle_Achievement(Player_Data p_data)
	{
		int GetAchievement = -1;
		if (p_data.Kill_Continue_Count >= 10 && !p_data.Has_Achievements.contains(TextFormat.WHITE + "【" + Batlle_All_Achievement.get(0).Name + TextFormat.WHITE + "】"))
		{
			GetAchievement = 0;
			Sub_Code.Server_Main.broadcastMessage(p_data.player.getDisplayName() + TextFormat.YELLOW + "が連続10キルを達成しました。やるね！");
		}
		else if (p_data.Kill_Continue_Count >= 15 && !p_data.Has_Achievements.contains(TextFormat.WHITE + "【" + Batlle_All_Achievement.get(1).Name + TextFormat.WHITE + "】"))
		{
			GetAchievement = 1;
			Sub_Code.Server_Main.broadcastMessage(p_data.player.getDisplayName() + TextFormat.YELLOW + "が連続15キルを達成しました。気を付けろ...こいつは強い...！");
		}
		else if (p_data.Kill_Continue_Count >= 20 && !p_data.Has_Achievements.contains(TextFormat.WHITE + "【" + Batlle_All_Achievement.get(2).Name + TextFormat.WHITE + "】"))
		{
			GetAchievement = 2;
			Sub_Code.Server_Main.broadcastMessage(p_data.player.getDisplayName() + TextFormat.YELLOW + "が連続20キルを達成しました。すごい...");
		}
		else if (p_data.Kill_Continue_Count >= 30 && !p_data.Has_Achievements.contains(TextFormat.WHITE + "【" + Batlle_All_Achievement.get(3).Name + TextFormat.WHITE + "】"))
		{
			GetAchievement = 3;
			Sub_Code.Server_Main.broadcastMessage(p_data.player.getDisplayName() + TextFormat.YELLOW + "が連続30キルを達成しました。もはやチート...？");
		}
		if (GetAchievement >= 0)
		{
			p_data.Has_Achievements.add(TextFormat.WHITE + "【" + Batlle_All_Achievement.get(GetAchievement).Name + TextFormat.WHITE + "】");
			p_data.player.level.addSound(p_data.player.getLocation(), Sound.RANDOM_LEVELUP, 0.75f, 1.0f, p_data.player);
			p_data.player.sendMessage(TextFormat.GREEN + "条件をクリアしたため、称号:" + TextFormat.WHITE + "【" + Batlle_All_Achievement.get(GetAchievement).Name + TextFormat.WHITE + "】" + TextFormat.GREEN + "を獲得しました。");
		}
	}
}