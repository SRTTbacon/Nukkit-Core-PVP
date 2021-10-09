package srttbacon.plugin.Class.ScoreBoard;

import srttbacon.plugin.Class.ScoreBoard.api.ScoreboardAPI;
import srttbacon.plugin.Class.ScoreBoard.network.DisplayEntry;
import srttbacon.plugin.Class.ScoreBoard.network.Scoreboard;
import srttbacon.plugin.Class.ScoreBoard.network.ScoreboardDisplay;

public class ScoreBoard_Data
{
	public Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
	public ScoreboardDisplay scoreboardDisplay;
	public DisplayEntry Blue_Team_Text;
	public DisplayEntry Red_Team_Text;
	public DisplayEntry Phase_Text;
	public DisplayEntry Game_Time_Text;
	public DisplayEntry CT_Text;
	public DisplayEntry Team_Text;
	public DisplayEntry Map_Name_Text;
	
	public DisplayEntry Next_Map_Text;
	public DisplayEntry Player_Count_Text;
	public DisplayEntry Player_Skill_Text;
	public DisplayEntry Player_Point_Text;
	public DisplayEntry Game_Count_Text;
	
	public String Player_Name = "";
}