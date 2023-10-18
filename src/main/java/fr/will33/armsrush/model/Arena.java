package fr.will33.armsrush.model;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private final Cuboid arena, portal;
    private final List<Location> blueSpawn = new ArrayList<>(),
        redSpawn = new ArrayList<>(),
        mobSpawn = new ArrayList<>();
    private final List<Player> bluePlayers = new ArrayList<>(),
        redPlayers = new ArrayList<>();
    private Statut statut = Statut.LOBBY;

    public Arena(@NotNull Cuboid arena, @NotNull Cuboid portal) {
        this.arena = Preconditions.checkNotNull(arena);
        this.portal = Preconditions.checkNotNull(portal);
    }

    /**
     * Récupérer la zone de jeu
     * @return
     */
    public @NotNull Cuboid getArena() {
        return this.arena;
    }

    /**
     * Récupérer les points de spawn de l'équipe bleue
     * @return
     */
    public @NotNull List<Location> getBlueSpawn() {
        return this.blueSpawn;
    }

    /**
     * Récupérer les points de spawn de l'équipe rouge
     * @return
     */
    public @NotNull List<Location> getRedSpawn() {
        return this.redSpawn;
    }

    /**
     * Récupérer les points de spawn des mobs
     * @return
     */
    public List<Location> getMobSpawn() {
        return mobSpawn;
    }

    /**
     * Récupérer la liste des joueurs dans l'équipe bleue
     * @return
     */
    public @NotNull List<Player> getBluePlayers() {
        return this.bluePlayers;
    }

    /**
     * Récupérer la liste des joueurs dans l'équipe rouge
     * @return
     */
    public @NotNull List<Player> getRedPlayers() {
        return this.redPlayers;
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
        LOBBY, INGAME;
    }
}
