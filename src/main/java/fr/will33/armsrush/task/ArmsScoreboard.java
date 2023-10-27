package fr.will33.armsrush.task;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ArmsScoreboard {

    private final Scoreboard scoreboard;
    private Objective objective;
    public ArmsScoreboard(@NonNull Scoreboard scoreboard){
        Preconditions.checkNotNull(scoreboard);
        this.scoreboard = scoreboard;
    }

    /**
     * Create sidebar
     * @param title Title of the sidebar
     */
    public void create(@NonNull String title) {
        Preconditions.checkNotNull(title);
        this.objective = this.scoreboard.registerNewObjective("sb1", "sb2", title);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Set line
     * @param line Number of the line
     * @param lineContent Content of the line
     */
    public void setLine(int line, String lineContent) {
        Team team = this.scoreboard.getTeam(String.valueOf(line));
        if(team == null){
            team = this.scoreboard.registerNewTeam(String.valueOf(line));
            team.addEntry(ChatColor.values()[line].toString());
            this.objective.getScore(ChatColor.values()[line].toString()).setScore(0);
        }
        team.setPrefix(lineContent);
    }

    /**
     * Remove sidebar
     */
    public void remove() {
        this.objective.unregister();
        this.objective = null;
    }
}
