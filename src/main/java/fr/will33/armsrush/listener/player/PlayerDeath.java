package fr.will33.armsrush.listener.player;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.APlayer;
import fr.will33.armsrush.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class PlayerDeath implements Listener {
    private final ArmsRush instance;

    public PlayerDeath(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        APlayer aPlayer = this.instance.getGameManager().getArena().getAPlayers().get(player);
        if(this.instance.getGameManager().getArena().getTeam(player) != null){
            if(player.getKiller() != null){
                event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.kill.broadcast").replace("{damager}", player.getKiller().getName()).replace("{victim}", player.getName())));
            }

            Bukkit.getScheduler().runTaskLater(this.instance, () -> {
                player.spigot().respawn();
                player.setGameMode(GameMode.SPECTATOR);
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
            }, 1L);
            if(aPlayer.getButin() > 0) {
                int butin = aPlayer.getButin() / 2;

                ItemBuilder itemBuilder = new ItemBuilder(this.instance.getConfigurationManager().getButinMaterial(), 1, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("butin.display")).replace("{butin}", String.valueOf(butin)), null);
                Item item = player.getWorld().dropItem(player.getLocation(), itemBuilder.toItemStack());
                item.setPersistent(false);
                item.setMetadata("butin", new FixedMetadataValue(this.instance, butin));
                aPlayer.setButin(butin);
            }
        }
    }
}
