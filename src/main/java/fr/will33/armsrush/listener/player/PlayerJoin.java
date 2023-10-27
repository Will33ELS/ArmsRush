package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoin implements Listener {

    private final ArmsRush instance;

    public PlayerJoin(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.getInventory().removeItem(
                this.instance.getConfigurationManager().getKitsItemStack(),
                this.instance.getConfigurationManager().getTeamItemStack()
        );
    }

}
