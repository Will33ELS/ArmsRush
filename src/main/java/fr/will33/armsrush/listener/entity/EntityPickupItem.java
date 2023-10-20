package fr.will33.armsrush.listener.entity;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.jetbrains.annotations.NotNull;

public class EntityPickupItem implements Listener {

    private final ArmsRush instance;

    public EntityPickupItem(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event){
        Item item = event.getItem();
        Entity entity = event.getEntity();
        if(item.hasMetadata("butin") && !(entity instanceof Player)){
            event.setCancelled(true);
        }

        if(entity instanceof Player player){
            if(this.instance.getGameManager().getArena().getTeam(player) == null) {
                event.setCancelled(true);
            } else {
                item.remove();
                event.setCancelled(true);
                int butin = item.getMetadata("butin").get(0).asInt();
                this.instance.getGameManager().getArena().getPlayersButin().put(player, this.instance.getGameManager().getArena().getPlayersButin().getOrDefault(player, 0) + butin);
            }
        }

    }
}
