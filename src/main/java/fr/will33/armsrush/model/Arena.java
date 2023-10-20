package fr.will33.armsrush.model;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arena {

    private final Cuboid arena, portal;
    private final List<Location> mobSpawn = new ArrayList<>();
    private final Map<TeamEnum, List<Location>> spawnLocations = new HashMap<>();
    private final Map<TeamEnum, List<Player>> players = new HashMap<>();
    private final Map<Player, Kit> playerKit = new HashMap<>();
    private final Map<Player, Integer> playersButin = new HashMap<>();
    private Statut statut = Statut.LOBBY;

    public Arena(@NotNull Cuboid arena, @NotNull Cuboid portal) {
        this.arena = Preconditions.checkNotNull(arena);
        this.portal = Preconditions.checkNotNull(portal);
        for(TeamEnum teamEnum : TeamEnum.values()){
            this.spawnLocations.put(teamEnum, new ArrayList<>());
            this.players.put(teamEnum, new ArrayList<>());
        }
    }

    /**
     * Récupérer la zone de jeu
     * @return
     */
    public @NotNull Cuboid getArena() {
        return this.arena;
    }

    /**
     * Récupérer les points de spawn d'une équipe
     * @param teamEnum
     * @return
     */
    public List<Location> getSpawn(TeamEnum teamEnum){
        return this.spawnLocations.get(teamEnum);
    }

    /**
     * Récupérer tous les points de spawn
     * @return
     */
    public Map<TeamEnum, List<Location>> getSpawnLocations() {
        return spawnLocations;
    }

    /**
     * Récupérer les points de spawn des mobs
     * @return
     */
    public List<Location> getMobSpawn() {
        return mobSpawn;
    }

    /**
     * Récupérer les joueurs dans une équipe
     * @param teamEnum
     * @return
     */
    public List<Player> getPlayersInTeam(TeamEnum teamEnum){
        return this.players.get(teamEnum);
    }

    /**
     * Récupérer l'équipe d'un joueur
     * @param player Instance du joueur
     * @return
     */
    public @Nullable TeamEnum getTeam(Player player){
        TeamEnum teamEnum = null;
        for(Map.Entry<TeamEnum, List<Player>> entry : this.players.entrySet()){
            if(entry.getValue().contains(player)){
                teamEnum = entry.getKey();
                break;
            }
        }
        return teamEnum;
    }

    /**
     * Récupérer le kit des joueurs
     * @return
     */
    public Map<Player, Kit> getPlayerKit() {
        return playerKit;
    }

    /**
     * Récupérer le butin des joueurs
     * @return
     */
    public Map<Player, Integer> getPlayersButin() {
        return playersButin;
    }

    /**
     * Récupérer le statut de l'arène
     * @return
     */
    public @NotNull Statut getStatut() {
        return this.statut;
    }

    /**
     * Définir le statut de l'arène
     * @param statut Nouveau statut
     */
    public void setStatut(@NotNull Statut statut) {
        this.statut = Preconditions.checkNotNull(statut);
    }

    public static enum Statut {
        LOBBY, LAUNCH, INGAME;
    }
}
