package fr.will33.armsrush.listener.entity;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.TeamEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntity implements Listener {
    private final ArmsRush instance;

    public EntityDamageByEntity(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager){
            TeamEnum victimTeam = this.instance.getGameManager().getArena().getTeam(player);
            TeamEnum damagerTeam = this.instance.getGameManager().getArena().getTeam(damager);
            if(victimTeam != null && damagerTeam == null){
                event.setCancelled(true);
            } else if(victimTeam != null && victimTeam.equals(damagerTeam)){
                event.setCancelled(true);
            }
        }
    }
}
