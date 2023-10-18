package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDrop implements Listener {

    private final ArmsRush instance;

    public PlayerDrop(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(this.instance.getConfigurationManager().getKitsItemStack().isSimilar(event.getItemDrop().getItemStack()) ||
                this.instance.getConfigurationManager().getTeamItemStack().isSimilar(event.getItemDrop().getItemStack())){
            event.setCancelled(true);
        }
    }
}
