package fr.will33.armsrush.manager;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.exception.ArenaConfigurationException;
import fr.will33.armsrush.exception.ArmsRushConfigurationException;
import fr.will33.armsrush.model.Arena;
import fr.will33.armsrush.model.ArenaConfiguration;
import fr.will33.armsrush.model.Cuboid;
import fr.will33.armsrush.model.TeamEnum;
import fr.will33.armsrush.utils.ItemBuilder;
import fr.will33.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigurationManager {

    private Player ownerConfig;
    private int gameDuration, maxPlayerInTeam;
    private ArenaConfiguration arenaConfiguration;
    private ItemStack teamItemStack, kitsItemStack;

    /**
     * Load configuration
     * @throws ArmsRushConfigurationException
     */
    public void loadConfiguration(@NotNull GameManager gameManager, @NotNull FileConfiguration configuration) throws ArmsRushConfigurationException {
        Preconditions.checkNotNull(gameManager);
        Preconditions.checkNotNull(configuration);
        World world = Bukkit.getWorld(configuration.getString("arena.world"));
        if(world == null) throw new ArenaConfigurationException("Le monde n'existe pas ou n'est pas chargé.");
        Arena arena = new Arena(Cuboid.load(world, configuration.getString("arena.arenaZone")), Cuboid.load(world, configuration.getString("arena.portalZone")));
        for(TeamEnum teamEnum : TeamEnum.values()){
            configuration.getStringList("arena.spawn." + teamEnum.name().toLowerCase()).forEach(location -> arena.getSpawn(teamEnum).add(LocationUtil.fromString(location)));

        }
        arena.getMobSpawn().addAll(configuration.getStringList("arena.mobsSpawnLocation").stream().map(LocationUtil::fromString).toList());
        gameManager.setArena(arena);
        this.gameDuration = configuration.getInt("config.gameDurationInSeconds");
        if(this.gameDuration <= 0) throw new ArmsRushConfigurationException("La durée d'une partie doit être supérieur à 0 !");
        this.maxPlayerInTeam = configuration.getInt("config.maxPlayerInTeam");
        if(this.maxPlayerInTeam <= 0) throw new ArmsRushConfigurationException("Le nombre maximum de joueur par équipe doit être supérieur à 0 !");
        this.teamItemStack = this.loadItems(configuration.getString("prestart.teamItem.material"), configuration.getString("prestart.teamItem.displayname"), configuration.getStringList("prestart.teamItem.lore"));
        this.kitsItemStack = this.loadItems(configuration.getString("prestart.kitsItem.material"), configuration.getString("prestart.kitsItem.displayname"), configuration.getStringList("prestart.kitsItem.lore"));
    }

    /**
     * Load item from the configuration
     * @param material Name of the material
     * @param displayName Displayname of the item
     * @param lore Lore of the item
     * @return Instance of the item
     * @throws ArmsRushConfigurationException Material error
     */
    public ItemStack loadItems(String material, String displayName, List<String> lore) throws ArmsRushConfigurationException {
        Material mat;
        try{
            mat = Material.valueOf(material.toUpperCase());
        } catch (IllegalArgumentException err){
            throw new ArmsRushConfigurationException("This material (" + material + ") does not exist. Please check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html");
        }
        return new ItemBuilder(mat, 1, ChatColor.translateAlternateColorCodes('&', displayName), lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).toList()).toItemStack();
    }

    /**
     * Récupérer le joueur qui configure l'arène
     * @return
     */
    public @Nullable Player getOwnerConfig() {
        return ownerConfig;
    }

    /**
     * Récupérer la configuration en cours de l'arène
     * @return
     */
    public @Nullable ArenaConfiguration getArenaConfiguration() {
        return arenaConfiguration;
    }

    /**
     * Récupérer la durée de la partie
     * @return
     */
    public int getGameDuration() {
        return gameDuration;
    }

    /**
     * Récupérer le nombre de joueurs maximum dans chaque équipe
     * @return
     */
    public int getMaxPlayerInTeam() {
        return maxPlayerInTeam;
    }

    /**
     * Récupérer l'item qui permet d'ouvrir le menu de sélection d'équipe
     * @return
     */
    public ItemStack getTeamItemStack() {
        return teamItemStack;
    }

    /**
     * Récupérer l'item qui permet d'ouvrir le menu de sélection de kit
     * @return
     */
    public ItemStack getKitsItemStack() {
        return kitsItemStack;
    }
}
