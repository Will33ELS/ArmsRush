package fr.will33.armsrush.model;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.task.ArmsScoreboard;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class APlayer {

    private final Player player;
    private ArmsScoreboard armsScoreboard;
    private Integer butin = 0;
    private Kit kit;

    public APlayer(@NotNull Player player) {
        this.player = Preconditions.checkNotNull(player);
    }

    /**
     * Retrieve player instance
     * @return
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * Retrieve the scoreboard instance
     * @return
     */
    public @Nullable ArmsScoreboard getArmsScoreboard() {
        return armsScoreboard;
    }

    /**
     * Define scoreboard
     * @param armsScoreboard Instance of the scoreboard
     * @return
     */
    public APlayer setArmsScoreboard(@Nullable ArmsScoreboard armsScoreboard) {
        this.armsScoreboard = armsScoreboard;
        return this;
    }

    /**
     * Retrieve butin
     * @return
     */
    public int getButin() {
        return butin;
    }

    /**
     * Define butin
     * @param butin New butin value
     * @return
     */
    public APlayer setButin(Integer butin) {
        this.butin = butin;
        return this;
    }

    /**
     * Retrieve the kit
     * @return
     */
    public @Nullable Kit getKit() {
        return kit;
    }

    /**
     * Define the selected kit
     * @param kit Instance of the kit
     * @return
     */
    public APlayer setKit(@Nullable Kit kit) {
        this.kit = kit;
        return this;
    }
}
