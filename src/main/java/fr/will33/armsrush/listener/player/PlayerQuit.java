package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.gui.TeamGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerQuit implements Listener {
    private final ArmsRush instance;

    public PlayerQuit(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        this.instance.getGameManager().getArena().getAPlayers().remove(player);
        Optional.ofNullable(this.instance.getGameManager().getArena().getTeam(player)).ifPresent(team -> {
            this.instance.getGameManager().getArena().getPlayersInTeam(team).remove(player);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            TeamGUI.refreshAllGUI();
        });
    }
}
