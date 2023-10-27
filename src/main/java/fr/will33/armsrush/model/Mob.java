package fr.will33.armsrush.model;

import fr.will33.armsrush.ArmsRush;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Optional;

public record Mob (
        EntityType entityType,
        String displayName,
        Integer startSpawnAt,
        Integer spawnEverySeconds,
        Integer minNumberPerWave,
        Integer maxNumberPerWave,
        ItemStack helmet,
        ItemStack chestplate,
        ItemStack leggings,
        ItemStack boots,
        ItemStack itemInHand,
        Integer health,
        Integer butin
        )
{
    public void spawn(Location spawnLocation){
        org.bukkit.entity.Mob livingEntity = (org.bukkit.entity.Mob) spawnLocation.getWorld().spawnEntity(spawnLocation, this.entityType());
        Optional.ofNullable(this.displayName()).map(displayName -> ChatColor.translateAlternateColorCodes('&', displayName)).ifPresent(displayName -> {
            livingEntity.setCustomName(displayName);
            livingEntity.setCustomNameVisible(true);
        });
        Optional.ofNullable(this.helmet()).ifPresent(helmet -> livingEntity.getEquipment().setHelmet(helmet));
        Optional.ofNullable(this.chestplate()).ifPresent(helmet -> livingEntity.getEquipment().setChestplate(helmet));
        Optional.ofNullable(this.leggings()).ifPresent(helmet -> livingEntity.getEquipment().setLeggings(helmet));
        Optional.ofNullable(this.boots()).ifPresent(helmet -> livingEntity.getEquipment().setBoots(helmet));
        Optional.ofNullable(this.itemInHand()).ifPresent(helmet -> livingEntity.getEquipment().setItemInMainHand(helmet));
        livingEntity.setHealth(this.health());
        livingEntity.setPersistent(false);
        livingEntity.setMetadata("butin", new FixedMetadataValue(ArmsRush.getInstance(), this.butin()));
    }
}
