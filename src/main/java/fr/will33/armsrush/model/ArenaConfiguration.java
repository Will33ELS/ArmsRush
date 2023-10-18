package fr.will33.armsrush.model;

import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArenaConfigurationException;
import fr.will33.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ArenaConfiguration {

    private Cuboid arena, portal;
    private final List<Location> blueSpawn = new ArrayList<>(),
            redSpawn = new ArrayList<>(),
            mobSpawn = new ArrayList<>();
    private Step step = Step.GLOBAL_ZONE;

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
     * Récupérer les points de spawn de l'équipe bleue
     * @return
     */
    public List<Location> getBlueSpawn() {
        return blueSpawn;
    }

    /**
     * Récupérer les points de spawn de l'équipe rouge
     * @return
     */
    public List<Location> getRedSpawn() {
        return redSpawn;
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
        if(this.arena == null || this.portal == null || this.blueSpawn.isEmpty() || this.redSpawn.isEmpty() || this.mobSpawn.isEmpty()){
            throw new ArenaConfigurationException(ArmsRush.getInstance().getConfig().getString("messages.config.incompleteConfig"));
        }
        FileConfiguration config = ArmsRush.getInstance().getConfig();
        config.set("arena.world", this.getArena().pos1().getWorld().getName());
        config.set("arena.arenaZone", this.getArena().toString());
        config.set("arena.portalZone", this.getPortal().toString());
        config.set("arena.blueSpawnLocation", this.getBlueSpawn().stream().map(LocationUtil::toString).toList());
        config.set("arena.redSpawnLocation", this.getRedSpawn().stream().map(LocationUtil::toString).toList());
        config.set("arena.mobsSpawnLocation", this.getMobSpawn().stream().map(LocationUtil::toString).toList());
        ArmsRush.getInstance().saveConfig();
        Arena arena = new Arena(this.getArena(), this.getPortal());
        arena.getBlueSpawn().addAll(this.getBlueSpawn());
        arena.getRedSpawn().addAll(this.getRedSpawn());
        arena.getMobSpawn().addAll(this.getMobSpawn());
        ArmsRush.getInstance().getGameManager().setArena(arena);
    }

    public static enum Step{
        GLOBAL_ZONE, PORTAL_ZONE, BLUE_SPAWN_POINTS, RED_SPAWN_POINTS, MOBS_SPAWN_POINTS;
    }
}
