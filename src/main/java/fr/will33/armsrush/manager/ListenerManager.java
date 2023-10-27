package fr.will33.armsrush.manager;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.listener.entity.EntityDamageByEntity;
import fr.will33.armsrush.listener.entity.EntityPickupItem;
import fr.will33.armsrush.listener.inventory.InventoryClick;
import fr.will33.armsrush.listener.player.*;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class ListenerManager {

    /**
     * Enregistrer les listeners
     * @param instance Instance du plugin
     */
    public void registerListeners(@NotNull ArmsRush instance){
        PluginManager pluginManager = Preconditions.checkNotNull(instance).getServer().getPluginManager();

        //ENTITY
        pluginManager.registerEvents(new EntityDamageByEntity(instance), instance);
        pluginManager.registerEvents(new EntityPickupItem(instance), instance);

        //INVENTORY
        pluginManager.registerEvents(new InventoryClick(instance), instance);

        //PLAYER
        pluginManager.registerEvents(new PlayerDeath(instance), instance);
        pluginManager.registerEvents(new PlayerDrop(instance), instance);
        pluginManager.registerEvents(new PlayerInteract(instance), instance);
        pluginManager.registerEvents(new PlayerJoin(instance), instance);
        pluginManager.registerEvents(new PlayerMove(instance), instance);
        pluginManager.registerEvents(new PlayerQuit(instance), instance);
        pluginManager.registerEvents(new PlayerRespawn(instance), instance);
    }

}
