package fr.will33.armsrush.manager;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArmsRushGameException;
import fr.will33.armsrush.model.Arena;
import fr.will33.armsrush.model.Kit;
import fr.will33.armsrush.model.TeamEnum;
import fr.will33.armsrush.task.LobbyTask;
import fr.will33.armsrush.utils.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameManager {

    private Arena arena;
    private BossBar bossBar;
    private final List<Kit> kits = new ArrayList<>();
    private LobbyTask lobbyTask;

    /**
     * Récupérer l'arène de jeu
     * @return
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Définir l'arène de jeu
     * @param arena
     */
    public void setArena(@NotNull Arena arena) {
        this.arena = Preconditions.checkNotNull(arena);
    }

    /**
     * Retrieve kit from name
     * @param name Name of the kit
     * @return
     */
    public @Nullable Kit getKit(String name){
        return this.kits.stream().filter(kit -> kit.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Récupérer les kits disponibles
     * @return
     */
    public List<Kit> getKits() {
        return kits;
    }

    /**
     * Lancement du lobby
     * @throws ArmsRushGameException
     */
    public void startLobby() throws ArmsRushGameException {
        if(this.getArena().getStatut() != Arena.Statut.LOBBY) return;
        for(TeamEnum teamEnum : TeamEnum.values()){
            Random random = new Random();
            List<Player> players = this.getArena().getPlayersInTeam(teamEnum);
            List<Location> spawns = this.getArena().getSpawn(teamEnum);
            if(spawns.size() < players.size()){
                players.forEach(p -> p.teleport(spawns.get(random.nextInt(spawns.size()))));
            } else {
                players.forEach(p -> p.teleport(spawns.get(players.indexOf(p))));
            }
        }
        this.lobbyTask = new LobbyTask(ArmsRush.getInstance());
        this.lobbyTask.runTaskTimer(ArmsRush.getInstance(), 20, 20);
        this.getArena().getPlayersInGame().forEach(pls -> {
            pls.getInventory().clear();
            pls.getInventory().setArmorContents(null);
        });
    }

    /**
     * Lancement de la partie
     */
    public void startGame(){
        if(this.lobbyTask != null){
            this.lobbyTask.cancel();
            this.lobbyTask = null;
        }

        if(this.getArena().getStatut() == Arena.Statut.INGAME) return;
        this.getArena().setStatut(Arena.Statut.INGAME);

        this.bossBar = Bukkit.createBossBar(
                ChatColor.translateAlternateColorCodes('&', ArmsRush.getInstance().getConfig().getString("bossBar").replace("{timer}", TimerUtil.format(ArmsRush.getInstance().getConfigurationManager().getGameDuration()))),
                BarColor.WHITE,
                BarStyle.SOLID
        );

        for(TeamEnum teamEnum : TeamEnum.values()){
            this.getArena().getPlayersInTeam(teamEnum).forEach(pls -> {
                if(this.getArena().getPlayerKit().containsKey(pls)){
                    this.getArena().getPlayerKit().get(pls).giveKit(pls);
                }
                this.getArena().getPlayersButin().put(pls, 0);
                this.bossBar.addPlayer(pls);
            });
        }
    }

    public void stopGame(){
        if(this.lobbyTask != null){
            this.lobbyTask.cancel();
            this.lobbyTask = null;
        }
        this.bossBar.removeAll();
        this.bossBar = null;

        for(TeamEnum teamEnum : TeamEnum.values()){
            this.getArena().getPlayersInTeam(teamEnum).forEach(pls -> {
                pls.getInventory().clear();
                pls.getInventory().setArmorContents(null);
                pls.setGameMode(GameMode.SPECTATOR);
            });
            this.getArena().getPlayersInTeam(teamEnum).clear();
        }
        this.getArena().getPlayersButin().clear();
        this.getArena().getPlayerKit().clear();


    }

    /**
     * Récupérer la task de lancement
     * @return
     */
    public LobbyTask getLobbyTask() {
        return lobbyTask;
    }

    public @Nullable BossBar getBossBar() {
        return bossBar;
    }
}
