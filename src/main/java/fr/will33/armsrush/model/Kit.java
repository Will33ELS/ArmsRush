package fr.will33.armsrush.model;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Kit (String name, Material display, List<ItemStack> items, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots){

    /**
     * Give kit to player
     * @param player Instance of the player
     */
    public void giveKit(Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        this.items().forEach(item -> player.getInventory().addItem(item));
        player.getInventory().setHelmet(this.helmet());
        player.getInventory().setChestplate(this.chestplate());
        player.getInventory().setLeggings(this.leggings());
        player.getInventory().setBoots(this.boots());
    }

}
