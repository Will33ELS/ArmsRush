package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArmsRushConfigurationException;
import fr.will33.armsrush.gui.KitsGUI;
import fr.will33.armsrush.gui.TeamGUI;
import fr.will33.guimodule.GuiModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class PlayerInteract implements Listener {

    private final ArmsRush instance;

    public PlayerInteract(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).contains(event.getAction())){
            Optional.ofNullable(event.getItem()).ifPresent(item -> {
                if(item.isSimilar(this.instance.getConfigurationManager().getTeamItemStack())){
                    event.setCancelled(true);
                    try {
                        GuiModule.getGuiModule().getGuiManager().openInventory(event.getPlayer(), new TeamGUI(this.instance));
                    } catch (ArmsRushConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                } else if(item.isSimilar(this.instance.getConfigurationManager().getKitsItemStack())){
                    event.setCancelled(true);
                    GuiModule.getGuiModule().getGuiManager().openInventory(event.getPlayer(), new KitsGUI(this.instance));
                }
            });
        }
    }
}
