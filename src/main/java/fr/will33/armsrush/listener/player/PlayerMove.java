package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

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
    }
}
