package fr.will33.armsrush.task;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.APlayer;
import fr.will33.armsrush.utils.TimerUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GameTask extends BukkitRunnable {

    private final ArmsRush instance;
    private int gameDurationRemaining, portailOpenTime;

    public GameTask(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
        this.gameDurationRemaining = this.instance.getConfigurationManager().getGameDuration();
        this.portailOpenTime = this.instance.getConfigurationManager().getPortalOpenTime();
    }

    @Override
    public void run() {
        if(this.gameDurationRemaining == 0 || this.instance.getGameManager().getArena().getPlayerAlive() == 0){
            this.cancel();
            this.instance.getGameManager().stopGame();
        } else {
            this.instance.getGameManager().checkSpawnMobs(this.gameDurationRemaining);
            this.instance.getGameManager().getBossBar().setTitle(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("bossBar.display").replace("{timer}", TimerUtil.format(this.gameDurationRemaining))));

            for(Map.Entry<Player, APlayer> entry : this.instance.getGameManager().getArena().getAPlayers().entrySet()){
                if(this.portailOpenTime >= 0) {
                    entry.getValue().getArmsScoreboard().setLine(1, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("scoreboard.portal")).replace("{portal}", TimerUtil.format(this.portailOpenTime)));
                }
                entry.getValue().getArmsScoreboard().setLine(2, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("scoreboard.butin")).replace("{butin}", String.valueOf(entry.getValue().getButin())));
                entry.getValue().getArmsScoreboard().setLine(3, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("scoreboard.playerInGame")).replace("{players}", String.valueOf(this.instance.getGameManager().getArena().getPlayerAlive())));
            }

            if(this.portailOpenTime == 0){
                this.instance.getGameManager().getArena().setPortalIsOpen(true);
                for(Player pls : this.instance.getGameManager().getArena().getPlayersInGame()){
                    pls.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.portal.open")));
                }
                this.portailOpenTime = -1;
            } else if(this.portailOpenTime > 0){
                this.portailOpenTime --;
            }
            this.gameDurationRemaining --;
        }
    }

    /**
     * Retrieve remaining time
     * @return
     */
    public int getGameDurationRemaining() {
        return gameDurationRemaining;
    }
}
