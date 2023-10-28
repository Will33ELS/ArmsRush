package fr.will33.armsrush.commands;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArmsRushGameException;
import fr.will33.armsrush.model.Arena;
import fr.will33.armsrush.model.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

public class ArmsRushCommand implements CommandExecutor {

    private final ArmsRush instance;

    public ArmsRushCommand(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            if(player.hasPermission("armsrush.admin")){
                if(strings.length == 0){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.armsrush.help")));
                } else if(strings[0].equalsIgnoreCase("addkit")){
                    if(strings.length != 3){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.armsrush.addkit.help")));
                    } else {
                        String kitName = strings[1];
                        Material material;
                        try {
                            material = Material.valueOf(strings[2].toUpperCase());
                        } catch (IllegalArgumentException err) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.armsrush.addkit.materialInvalid")));
                            return false;
                        }
                        if (this.instance.getGameManager().getKit(kitName) != null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.armsrush.addkit.alreadyExist")));
                        } else {
                            try{
                                Kit kit = new Kit(kitName, material, Arrays.stream(player.getInventory().getStorageContents()).toList(), player.getInventory().getHelmet(), player.getInventory().getChestplate(), player.getInventory().getLeggings(), player.getInventory().getBoots());
                                this.instance.getGameManager().getKits().add(kit);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.armsrush.addkit.success")));
                                this.instance.getConfigurationManager().saveKits(this.instance.getGameManager().getKits());
                            } catch (IOException err){
                                err.printStackTrace();
                            }
                        }
                    }
                } else if(strings[0].equalsIgnoreCase("delkit")){
                    if(strings.length != 2){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.armsrush.delkit.help")));
                    } else {
                        Kit kit = this.instance.getGameManager().getKit(strings[1]);
                        if(kit == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.armsrush.delkit.kitNotExist")));
                        } else {
                            try{
                                this.instance.getGameManager().getKits().remove(kit);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.armsrush.delkit.kitDeleted")));
                                this.instance.getConfigurationManager().saveKits(this.instance.getGameManager().getKits());
                            } catch (IOException err){
                                err.printStackTrace();
                            }
                        }
                    }
                } else if(strings[0].equalsIgnoreCase("start")){
                    if(this.instance.getGameManager().getArena().getStatut() != Arena.Statut.LOBBY){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages.start.inProgress")));
                    } else {
                        try {
                            this.instance.getGameManager().startLobby();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages.start.success")));
                        } catch (ArmsRushGameException e) {
                            player.sendMessage("§c" + e.getMessage());
                        }
                    }
                } else if(strings[0].equalsIgnoreCase("stop")){
                    if(this.instance.getGameManager().getArena().getStatut() == Arena.Statut.LOBBY || this.instance.getGameManager().getArena().getStatut() == Arena.Statut.CLOSED) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages.stop.noInProgress")));
                    } else {
                        this.instance.getGameManager().stopGame();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages.stop.success")));
                    }
                }
            }
        }
        return false;
    }
}
