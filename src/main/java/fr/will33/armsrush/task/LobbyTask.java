package fr.will33.armsrush.task;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArmsRushGameException;
import fr.will33.armsrush.model.Arena;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class LobbyTask extends BukkitRunnable {

    private final ArmsRush instance;
    private int seconds = 10;

    public LobbyTask(@NotNull ArmsRush instance) throws ArmsRushGameException {
        this.instance = Preconditions.checkNotNull(instance);
        if(this.instance.getGameManager().getArena().getStatut() != Arena.Statut.LOBBY){
            throw new ArmsRushGameException("La partie n'est pas au statut LOBBY !");
        } else {
            this.instance.getGameManager().getArena().setStatut(Arena.Statut.LAUNCH);
        }
    }

    @Override
    public void run() {
        if(this.seconds == 0){
            this.cancel();
            this.instance.getGameManager().getArena().getPlayersInGame().forEach(pls ->
                    pls.spigot().sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.lobby.start")))));

           this.instance.getGameManager().startGame();
        } else {
            this.instance.getGameManager().getArena().getPlayersInGame().forEach(pls ->
                    pls.spigot().sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.lobby.startIn").replace("{seconds}", String.valueOf(this.seconds))))));
            this.seconds --;
        }
    }
}
