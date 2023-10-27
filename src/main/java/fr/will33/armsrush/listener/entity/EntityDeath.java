package fr.will33.armsrush.listener.entity;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.APlayer;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDeath implements Listener {

    private final ArmsRush instance;

    public EntityDeath(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Mob mob){
            if(mob.hasMetadata("butin")){
                int butin = mob.getMetadata("butin").get(0).asInt();
                if(mob.getKiller() != null && this.instance.getGameManager().getArena().getPlayersInGame().contains(mob.getKiller())){
                    Player player = mob.getKiller();
                    APlayer aPlayer = this.instance.getGameManager().getArena().getAPlayers().get(player);
                    aPlayer.setButin(aPlayer.getButin() + butin);
                }
            }
        }
    }

}
