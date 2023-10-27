package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.TeamEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerRespawn implements Listener {

    private final ArmsRush instance;

    public PlayerRespawn(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        if(this.instance.getGameManager().getArena().getPlayersInGame().contains(player)){
            TeamEnum teamEnum = this.instance.getGameManager().getArena().getTeam(player);
            event.setRespawnLocation(this.instance.getGameManager().getArena().getSpawnLocations().get(teamEnum).get(0));
        }
    }

}
