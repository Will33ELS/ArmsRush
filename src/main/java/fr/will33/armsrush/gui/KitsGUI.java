package fr.will33.armsrush.gui;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.Kit;
import fr.will33.armsrush.utils.ItemBuilder;
import fr.will33.guimodule.gui.AbstractGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class KitsGUI extends AbstractGUI {
    private final ArmsRush instance;
    public KitsGUI(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @Override
    public void onDisplay(Player player) {
        int inventorySize = 6 * 9;
        if(this.instance.getGameManager().getKits().size() < 9){
            inventorySize = 9;
        } else if(this.instance.getGameManager().getKits().size() < 2 * 9){
            inventorySize = 9 * 2;
        } else if(this.instance.getGameManager().getKits().size() < 3 * 9){
            inventorySize = 9 * 3;
        } else if(this.instance.getGameManager().getKits().size() < 4 * 9){
            inventorySize = 9 * 4;
        } else if(this.instance.getGameManager().getKits().size() < 5 * 9){
            inventorySize = 9 * 5;
        }
        this.inventory = Bukkit.createInventory(null, inventorySize, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.kits.title")));
        this.onUpdate(player);
        player.openInventory(this.inventory);
    }

    @Override
    public void onUpdate(Player player) {
        int slot = 0;
        for(Kit kit : this.instance.getGameManager().getKits()){
            if(slot == this.inventory.getSize()) break;
            this.setSlotData(new ItemBuilder(kit.display(), 1, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.kits.displayFormat").replace("{kit}", kit.name())), this.instance.getConfig().getStringList("gui.kits.kit." + kit.name()).stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).toList()).toItemStack(), slot, kit.name());
            slot ++;
        }
    }

    @Override
    public void onClick(Player player, ItemStack itemStack, String action, ClickType clickType) {
        Optional.ofNullable(this.instance.getGameManager().getKit(action)).ifPresent(kit -> {
            this.instance.getGameManager().getArena().getPlayerKit().put(player, kit);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.kits.select").replace("{kit}", kit.name())));
        });
    }
}
