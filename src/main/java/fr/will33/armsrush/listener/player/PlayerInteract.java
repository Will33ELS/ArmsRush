package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArmsRushConfigurationException;
import fr.will33.armsrush.gui.KitsGUI;
import fr.will33.armsrush.gui.TeamGUI;
import fr.will33.armsrush.manager.ConfigurationManager;
import fr.will33.guimodule.GuiModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
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
        Player player = event.getPlayer();
        ConfigurationManager configurationManager = this.instance.getConfigurationManager();
        if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_HOE && player.equals(configurationManager.getOwnerConfig()) && event.getHand() == EquipmentSlot.HAND){
            if(configurationManager.getZoneConfiguration() != null && event.getClickedBlock() != null){
                if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    event.setCancelled(true);
                    configurationManager.getZoneConfiguration().setPos1(event.getClickedBlock().getLocation());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.zone.point1")));
                } else if(event.getAction() == Action.LEFT_CLICK_BLOCK){
                    event.setCancelled(true);
                    configurationManager.getZoneConfiguration().setPos2(event.getClickedBlock().getLocation());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.zone.point2")));
                }
                if(configurationManager.getZoneConfiguration().getPos1() != null && configurationManager.getZoneConfiguration().getPos2() != null){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.zone.success")));
                }
            }
        }


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
