package fr.will33.armsrush.manager;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArmsRushGameException;
import fr.will33.armsrush.model.*;
import fr.will33.armsrush.task.ArmsScoreboard;
import fr.will33.armsrush.task.GameTask;
import fr.will33.armsrush.task.LobbyTask;
import fr.will33.armsrush.utils.MapUtil;
import fr.will33.armsrush.utils.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;

public class GameManager {

    private Arena arena;
    private BossBar bossBar;
    private final List<Kit> kits = new ArrayList<>();
    private final List<Mob> mobs = new ArrayList<>();
    private LobbyTask lobbyTask;
    private GameTask gameTask;

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
        this.checkPlayerNoTeam();
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

        this.mobs.forEach(mob -> this.getArena().getSpawnTime().put(mob, mob.spawnEverySeconds()));
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
                ChatColor.translateAlternateColorCodes('&', ArmsRush.getInstance().getConfig().getString("bossBar.display").replace("{timer}", TimerUtil.format(ArmsRush.getInstance().getConfigurationManager().getGameDuration()))),
                BarColor.WHITE,
                BarStyle.SOLID
        );

        for(TeamEnum teamEnum : TeamEnum.values()){
            this.getArena().getPlayersInTeam(teamEnum).forEach(pls -> {
                APlayer aPlayer = this.getArena().getAPlayers().get(pls);
                if(aPlayer.getKit() != null){
                    aPlayer.getKit().giveKit(pls);
                }
                this.bossBar.addPlayer(pls);

                Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                ArmsScoreboard armsScoreboard = new ArmsScoreboard(scoreboard);
                armsScoreboard.create(ChatColor.translateAlternateColorCodes('&', ArmsRush.getInstance().getConfig().getString("scoreboard.title")));
                aPlayer.setArmsScoreboard(armsScoreboard);
                pls.setScoreboard(scoreboard);
            });
        }

        this.gameTask = new GameTask(ArmsRush.getInstance());
        this.gameTask.runTaskTimer(ArmsRush.getInstance(), 20, 20);
    }

    public void stopGame(){
        if(this.getArena().getStatut() == Arena.Statut.LOBBY) return;
        if(this.lobbyTask != null){
            this.lobbyTask.cancel();
            this.lobbyTask = null;
        }
        if(this.gameTask != null){
            this.gameTask.cancel();
            this.gameTask = null;
        }

        this.bossBar.removeAll();
        this.bossBar = null;
        this.getArena().setPortalIsOpen(false);

        for(APlayer aPlayer : this.getArena().getAPlayers().values()){
            if(aPlayer.getPlayer().getGameMode() != GameMode.SPECTATOR){
                aPlayer.setButin((int) (aPlayer.getButin() * 0.75));
            }
        }

        List<APlayer> top = new ArrayList<>(this.getArena().getAPlayers().values());
        Map<TeamEnum, Integer> points = new HashMap<>();
        for(APlayer aPlayer : top){
            TeamEnum teamEnum = this.getArena().getTeam(aPlayer.getPlayer());
            int point = points.getOrDefault(teamEnum, 0);
            point += aPlayer.getButin();
            points.put(teamEnum, point);
        }
        points = MapUtil.sortByValue(points);

        this.getArena().getPlayersInGame().forEach(pls -> pls.sendMessage(ChatColor.translateAlternateColorCodes('&', ArmsRush.getInstance().getConfig().getString("messages.top.head"))));
        int index = 1;
        for(Map.Entry<TeamEnum, Integer> teamTop : points.entrySet()){
            if(index > 5) break;
            this.getArena().getPlayersInGame().forEach(pls -> pls.sendMessage(ChatColor.translateAlternateColorCodes('&', ArmsRush.getInstance().getConfig().getString("messages.top.body").replace("{team_color}", "&" + teamTop.getKey().getColor().getChar()).replace("{team}", ArmsRush.getInstance().getConfig().getString("messages.team." + teamTop.getKey().name().toLowerCase())).replace("{butin}", String.valueOf(teamTop.getValue())))));
            index ++;
        }

        for(TeamEnum teamEnum : TeamEnum.values()){
            this.getArena().getPlayersInTeam(teamEnum).forEach(pls -> {
                APlayer aPlayer = this.getArena().getAPlayers().get(pls);
                pls.getInventory().clear();
                pls.getInventory().setArmorContents(null);
                pls.setGameMode(GameMode.SURVIVAL);
                aPlayer.getArmsScoreboard().remove();
                aPlayer.setArmsScoreboard(null);
            });
            this.getArena().getPlayersInTeam(teamEnum).clear();
        }
        this.getArena().getArena().getWorld().getEntities().stream().filter(entity -> entity instanceof Item item && this.getArena().getArena().isIn(item.getLocation())).forEach(Entity::remove);

        this.getArena().getSpawnTime().clear();
        this.getArena().getAPlayers().clear();
        this.getArena().setStatut(Arena.Statut.CLOSED);
    }

    /**
     * Check and spawn mobs if necessary
     * @param gameTimeInSecond Elapsed game time in second
     */
    public void checkSpawnMobs(int gameTimeInSecond){
        for(Map.Entry<Mob, Integer> entry : this.getArena().getSpawnTime().entrySet()){
            Mob mob = entry.getKey();
            if(mob.startSpawnAt() <= gameTimeInSecond){
                int time = this.getArena().getSpawnTime().getOrDefault(mob, mob.spawnEverySeconds());
                if(time == 0){
                    Random random = new Random();
                    int nbr = random.nextInt(mob.minNumberPerWave(), mob.maxNumberPerWave());
                    for(int i = 0; i <= nbr; i ++){
                        Location location = this.getArena().getMobSpawn().get(random.nextInt(this.getArena().getMobSpawn().size()));
                        mob.spawn(location);
                    }
                    this.getArena().getSpawnTime().put(mob, mob.spawnEverySeconds());
                } else {
                    this.getArena().getSpawnTime().put(mob, time-1);
                }
            }
        }
    }

    /**
     * Set a team for all players who have not selected a team
     */
    private void checkPlayerNoTeam(){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getInventory().contains(ArmsRush.getInstance().getConfigurationManager().getTeamItemStack()) && !this.getArena().getPlayersInGame().contains(player)){
                List<TeamEnum> available = new ArrayList<>();
                for(TeamEnum teamEnum : TeamEnum.values()){
                    if(this.getArena().getPlayersInTeam(teamEnum).size() < ArmsRush.getInstance().getConfigurationManager().getMaxPlayerInTeam()){
                        available.add(teamEnum);
                    }
                }
                if(available.isEmpty()){
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                } else {
                    if(!this.getArena().getAPlayers().containsKey(player)){
                        this.getArena().getAPlayers().put(player, new APlayer(player));
                    }
                    TeamEnum playerTeam = this.getArena().getTeam(player);
                    TeamEnum teamEnum = available.get(new Random().nextInt(available.size()));
                    Optional.ofNullable(playerTeam).ifPresent(t -> this.getArena().getPlayersInTeam(t).remove(player));
                    this.getArena().getPlayersInTeam(teamEnum).add(player);
                }
            }
        }
    }

    /**
     * Retrieve lobby task
     * @return
     */
    public LobbyTask getLobbyTask() {
        return lobbyTask;
    }

    /**
     * Retrieve game task
     * @return
     */
    public GameTask getGameTask() {
        return gameTask;
    }

    /**
     * Retrieve the bossbar instance
     * @return
     */
    public @Nullable BossBar getBossBar() {
        return bossBar;
    }

    /**
     * Retrieve mobs list
     * @return
     */
    public List<Mob> getMobs() {
        return mobs;
    }
}
