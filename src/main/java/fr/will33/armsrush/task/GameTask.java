package fr.will33.armsrush.task;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.utils.TimerUtil;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class GameTask extends BukkitRunnable {

    private final ArmsRush instance;
    private int gameDurationRemaining;

    public GameTask(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
        this.gameDurationRemaining = this.instance.getConfigurationManager().getGameDuration();
    }

    @Override
    public void run() {
        if(this.gameDurationRemaining == 0){
            this.cancel();
            this.instance.getGameManager().stopGame();
        } else {
            this.instance.getGameManager().getBossBar().setTitle(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("bossBar").replace("{timer}", TimerUtil.format(this.gameDurationRemaining))));



            this.gameDurationRemaining --;
        }
    }
}
