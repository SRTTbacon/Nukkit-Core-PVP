package srttbacon.plugin.Class.ScoreBoard.api;

import cn.nukkit.Player;
import srttbacon.plugin.Class.ScoreBoard.network.Scoreboard;

public class ScoreboardAPI
{
    public static Scoreboard createScoreboard()
    {
        return new Scoreboard();
    }
    public static void setScoreboard(Player player, Scoreboard scoreboard)
    {
        scoreboard.showFor(player);
    }
    public static void removeScorebaord(Player player, Scoreboard scoreboard)
    {
        scoreboard.hideFor(player);
    }
}