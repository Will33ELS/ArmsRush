package fr.will33.armsrush.commands;

import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.model.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PreStartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            ArmsRush instance = ArmsRush.getInstance();
            if(instance.getGameManager().getArena().getStatut() != Arena.Statut.LOBBY){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages.preStart.inProgress")));
            } else {
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getInventory().addItem(instance.getConfigurationManager().getTeamItemStack(), instance.getConfigurationManager().getKitsItemStack());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages.preStart.go")));
            }
        }
        return false;
    }
}
