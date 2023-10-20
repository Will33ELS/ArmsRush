package fr.will33.armsrush.commands;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArenaConfigurationException;
import fr.will33.armsrush.manager.ConfigurationManager;
import fr.will33.armsrush.model.ArenaConfiguration;
import fr.will33.armsrush.model.TeamEnum;
import fr.will33.armsrush.model.ZoneConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ConfigurationCommand implements CommandExecutor {

    private final ArmsRush instance;

    public ConfigurationCommand(@NotNull ArmsRush instance) {
        this.instance = Preconditions.checkNotNull(instance);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player && player.isOp()){
            FileConfiguration configuration = this.instance.getConfig();
            ConfigurationManager configurationManager = this.instance.getConfigurationManager();
            if(strings.length == 0){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.help")));
            } else {
                if(strings[0].equalsIgnoreCase("start")){
                    if(configurationManager.getArenaConfiguration() != null && !configurationManager.getOwnerConfig().equals(player)){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.configurationInProgress")));
                    } else if(configurationManager.getArenaConfiguration() == null && configurationManager.getOwnerConfig() == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.hoeGlobalCuboid")));
                        player.getInventory().setItem(0, new ItemStack(Material.GOLDEN_HOE));
                        configurationManager.setOwnerConfig(player);
                        configurationManager.setArenaConfiguration(new ArenaConfiguration());
                        configurationManager.setZoneConfiguration(new ZoneConfiguration((confirmGlobal) -> {
                            configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.GLOBAL_ZONE);
                            configurationManager.getArenaConfiguration().setArena(confirmGlobal);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.hoePortalCuboid")));
                            configurationManager.setZoneConfiguration(new ZoneConfiguration((confirmBlue) -> {
                                configurationManager.getArenaConfiguration().setPortal(confirmBlue);
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.WHITE_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.hoeRedCuboid")));
                            }));
                        }));
                    }
                } else {
                    if(player.equals(configurationManager.getOwnerConfig())) {
                        if (strings[0].equalsIgnoreCase("confirm")) {
                            if (configurationManager.getZoneConfiguration() != null) {
                                try {
                                    configurationManager.getZoneConfiguration().confirm();
                                } catch (ArenaConfigurationException err) {
                                    player.sendMessage("§c" + err.getMessage());
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.noConfirm")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addWhiteSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.WHITE_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.WHITE).add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopWhiteSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.WHITE_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.WHITE).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOneWhiteSpawn")));
                            } else {
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.GRAY_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.addGraySpawnPoint")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addGraySpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.GRAY_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.GRAY).add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopGraySpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.GRAY_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.GRAY).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOneGraySpawn")));
                            } else {
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.RED_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.addRedSpawnPoint")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addRedSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.RED_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.RED).add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopRedSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.RED_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.RED).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOneRedSpawn")));
                            } else {
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.ORANGE_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.addOrangeSpawnPoint")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addOrangeSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.ORANGE_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.ORANGE).add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopOrangeSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.ORANGE_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.ORANGE).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOneOrangeSpawn")));
                            } else {
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.YELLOW_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.addYellowSpawnPoint")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addYellowSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.YELLOW_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.YELLOW).add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopYellowSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.YELLOW_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.YELLOW).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOneYellowSpawn")));
                            } else {
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.GREEN_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.addGreenSpawnPoint")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addGreenSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.GREEN_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.GREEN).add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopGreenSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.GREEN_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.GREEN).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOneGreenSpawn")));
                            } else {
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.CYAN_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.addCyanSpawnPoint")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addCyanSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.CYAN_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.CYAN).add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopCyanSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.CYAN_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.CYAN).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOneCyanSpawn")));
                            } else {
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.PURPLE_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.addPurpleSpawnPoint")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addPurpleSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.PURPLE_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.PURPLE).add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopPurpleSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.PURPLE_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.PURPLE).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOnePurpleSpawn")));
                            } else {
                                configurationManager.getArenaConfiguration().setStep(ArenaConfiguration.Step.MOBS_SPAWN_POINTS);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.addMobsSpawnPoint")));
                            }
                        } else if(strings[0].equalsIgnoreCase("addMobsSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.MOBS_SPAWN_POINTS){
                            if(!configurationManager.getArenaConfiguration().getArena().isIn(player.getLocation())){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.notInZone")));
                            } else {
                                configurationManager.getArenaConfiguration().getMobSpawn().add(player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.spawnAdded")));
                            }
                        } else if(strings[0].equalsIgnoreCase("stopMobsSpawnPoint") && configurationManager.getArenaConfiguration().getStep() == ArenaConfiguration.Step.MOBS_SPAWN_POINTS){
                            if(configurationManager.getArenaConfiguration().getSpawns().get(TeamEnum.PURPLE).isEmpty()){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.minOneMobsSpawn")));
                            } else {
                                try{
                                    configurationManager.getArenaConfiguration().finish();
                                    configurationManager.setOwnerConfig(null);
                                    configurationManager.setArenaConfiguration(null);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.config.success")));
                                } catch (ArenaConfigurationException err){
                                    player.sendMessage("§c" + err.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
