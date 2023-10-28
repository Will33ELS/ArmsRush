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
        if(commandSender instanceof Player player && player.hasPermission("armsrush.admin")){
            ArmsRush instance = ArmsRush.getInstance();
            if(instance.getGameManager().getArena().getStatut() != Arena.Statut.CLOSED){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages.preStart.inProgress")));
            } else {
                instance.getGameManager().getArena().setStatut(Arena.Statut.LOBBY);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages.preStart.open")));
            }
        }
        return false;
    }
}
