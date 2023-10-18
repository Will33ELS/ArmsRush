package fr.will33.armsrush.model;

import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArenaConfigurationException;
import fr.will33.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaConfiguration {

    private Cuboid arena, portal;
    private final List<Location> mobSpawn = new ArrayList<>();
    private final Map<TeamEnum, List<Location>> spawns = new HashMap<>();
    private Step step = Step.GLOBAL_ZONE;

    public ArenaConfiguration() {
        for(TeamEnum teamEnum : TeamEnum.values()){
            this.spawns.put(teamEnum, new ArrayList<>());
        }
    }

    /**
     * Récupérer la zone de jeu configuré
     * @return
     */
    public Cuboid getArena() {
        return arena;
    }

    /**
     * Définir la zone de jeu
     * @param arena
     */
    public void setArena(Cuboid arena) {
        this.arena = arena;
    }

    /**
     * Récupérer la zone du portail
     * @return
     */
    public Cuboid getPortal() {
        return portal;
    }

    /**
     * Définir la zone du portail
     * @param portal
     */
    public void setPortal(Cuboid portal) {
        this.portal = portal;
    }

    /**
     * Récupérer le point de spawn de toutes les équipes
     * @return
     */
    public Map<TeamEnum, List<Location>> getSpawns(){
        return this.spawns;
    }

    /**
     * Récupérer les points de spawn des mobs
     * @return
     */
    public List<Location> getMobSpawn() {
        return mobSpawn;
    }

    /**
     * Récupérer l'étape de la configuration
     * @return
     */
    public Step getStep() {
        return step;
    }

    /**
     * Définir l'étape de la configuration
     * @param step
     */
    public void setStep(Step step) {
        this.step = step;
    }

    /**
     * Finish arena setup
     * @throws ArenaConfigurationException
     */
    public void finish() throws ArenaConfigurationException {
        if(this.arena == null || this.portal == null || this.mobSpawn.isEmpty()){
            throw new ArenaConfigurationException(ArmsRush.getInstance().getConfig().getString("messages.config.incompleteConfig"));
        }
        for(Map.Entry<TeamEnum, List<Location>> entry : this.spawns.entrySet()){
            if(entry.getValue().isEmpty()){
                throw new ArenaConfigurationException(ArmsRush.getInstance().getConfig().getString("messages.config.incompleteConfig"));
            }
        }
        FileConfiguration config = ArmsRush.getInstance().getConfig();
        config.set("arena.world", this.getArena().pos1().getWorld().getName());
        config.set("arena.arenaZone", this.getArena().toString());
        config.set("arena.portalZone", this.getPortal().toString());
        for(TeamEnum teamEnum : TeamEnum.values()){
            config.set("arena.spawn." + teamEnum.name().toLowerCase(), this.getSpawns().get(teamEnum).stream().map(location -> LocationUtil.toString(location)).toList());
        }
        config.set("arena.mobsSpawnLocation", this.getMobSpawn().stream().map(LocationUtil::toString).toList());
        ArmsRush.getInstance().saveConfig();
        Arena arena = new Arena(this.getArena(), this.getPortal());
        arena.getSpawnLocations().putAll(this.spawns);
        arena.getMobSpawn().addAll(this.getMobSpawn());
        ArmsRush.getInstance().getGameManager().setArena(arena);
    }

    public static enum Step{
        GLOBAL_ZONE, PORTAL_ZONE, WHITE_SPAWN_POINTS, GRAY_SPAWN_POINTS, RED_SPAWN_POINTS, ORANGE_SPAWN_POINTS, YELLOW_SPAWN_POINTS, GREEN_SPAWN_POINTS, CYAN_SPAWN_POINTS, PURPLE_SPAWN_POINTS, MOBS_SPAWN_POINTS;
    }
}
