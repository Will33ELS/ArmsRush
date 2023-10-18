package fr.will33.armsrush.listener.inventory;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class InventoryClick implements Listener {

    private final ArmsRush instance;

    public InventoryClick(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Optional.ofNullable(event.getCurrentItem()).ifPresent(item -> {
            if(item.isSimilar(this.instance.getConfigurationManager().getKitsItemStack()) || item.isSimilar(this.instance.getConfigurationManager().getTeamItemStack())){
                event.setCancelled(true);
            }
        });
    }
}
