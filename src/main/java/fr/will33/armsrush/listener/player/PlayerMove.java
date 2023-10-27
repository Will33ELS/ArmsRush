package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.APlayer;
import fr.will33.armsrush.model.Arena;
import fr.will33.armsrush.utils.TimerUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class PlayerMove implements Listener {

    private final ArmsRush instance;

    public PlayerMove(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(this.instance.getGameManager().getArena().getStatut() == Arena.Statut.LAUNCH && this.instance.getGameManager().getArena().getPlayersInGame().contains(player)){
            event.setCancelled(true);
        }
        if(this.instance.getGameManager().getArena().getStatut() == Arena.Statut.INGAME && this.instance.getGameManager().getArena().getPortal().isIn(event.getTo()) && this.instance.getGameManager().getArena().isPortalIsOpen()){
            if(player.getGameMode() != GameMode.SPECTATOR && this.instance.getGameManager().getArena().getPlayersInGame().contains(player)) {
                APlayer aPlayer = this.instance.getGameManager().getArena().getAPlayers().get(player);
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.portal.success"))
                        .replace("{butin}", String.valueOf(aPlayer.getButin()))
                        .replace("{time}", TimerUtil.format(this.instance.getConfigurationManager().getGameDuration() - this.instance.getGameManager().getGameTask().getGameDurationRemaining())
                        ));
            }
        }
    }
}
