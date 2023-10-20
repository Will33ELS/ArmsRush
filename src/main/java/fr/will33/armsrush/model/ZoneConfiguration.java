package fr.will33.armsrush.model;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArenaConfigurationException;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ZoneConfiguration {

    private Location pos1, pos2;
    private final Confirm after;

    public ZoneConfiguration(@NonNull Confirm after) {
        Preconditions.checkNotNull(after);
        this.after = after;
    }

    /**
     * Recover position 1
     * @return
     */
    public Location getPos1() {
        return pos1;
    }

    /**
     * Define position 1
     * @param pos1 Instance of the location
     */
    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    /**
     * Recover position 2
     * @return
     */
    public Location getPos2() {
        return pos2;
    }

    /**
     * Define position 2
     * @param pos2 Instance of the location
     */
    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    /**
     * Confirm select
     * @throws ArenaConfigurationException
     */
    public void confirm() throws ArenaConfigurationException {
        if(this.pos1 == null || this.pos2 == null){
            throw new ArenaConfigurationException(ArmsRush.getInstance().getConfig().getString("messages.zone.positionNull"));
        }
        if(!this.pos1.getWorld().equals(this.pos2.getWorld())){
            throw new ArenaConfigurationException(ArmsRush.getInstance().getConfig().getString("messages.zone.worldError"));
        }
        this.after.confirm(new Cuboid(this.pos1, this.pos2));
    }

    public static interface Confirm{
        void confirm(Cuboid cuboid);
    }
}