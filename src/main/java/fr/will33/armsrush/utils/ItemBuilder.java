package fr.will33.armsrush.utils;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemBuilder {

    protected ItemStack itemStack;
    protected ItemMeta itemMeta;
    private final Material material;
    private final Integer amount;
    private final String displayName;
    private final List<String> lore;

    /**
     * Build an itemstack
     * @param material Material of the item
     * @param amount Amount of the item
     * @param displayName Displayname of the item
     * @param lore Lore of the item
     */
    public ItemBuilder(@NonNull Material material, @NonNull Integer amount, @Nullable String displayName, @Nullable List<String> lore) {
        this.material = Preconditions.checkNotNull(material);
        this.amount = Preconditions.checkNotNull(amount);
        this.displayName = displayName;
        this.lore = lore;
        this.itemStack = new ItemStack(this.material, this.amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    /**
     * Recover ItemStack instance
     * @return
     */
    public ItemStack toItemStack(){
        this.itemMeta = this.itemStack.getItemMeta();
        Optional.ofNullable(this.displayName).ifPresent(displayName -> this.itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName)));
        Optional.ofNullable(this.lore).ifPresent(lore -> this.itemMeta.setLore(lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).toList()));
        this.itemMeta.addItemFlags(ItemFlag.values());
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
