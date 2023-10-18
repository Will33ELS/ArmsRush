package fr.will33.armsrush.model;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public record Cuboid(Location pos1, Location pos2) {

    public Cuboid(@NotNull Location pos1, @NotNull Location pos2) {
        Preconditions.checkNotNull(pos1);
        Preconditions.checkNotNull(pos2);
        Preconditions.checkArgument(pos1.getWorld().equals(pos2.getWorld()), "La position 1 et 2 doivent être dans le même monde !");
        this.pos1 = new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()), Math.min(pos1.getBlockY(), pos2.getBlockY()), Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
        this.pos2 = new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(), pos2.getBlockY()), Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }

    /**
     * Check if location is in cuboid
     *
     * @param location Instance of the location
     * @return
     */
    public boolean isIn(@NotNull Location location) {
        Preconditions.checkNotNull(location);
        return location.getWorld() == this.pos1.getWorld() && location.getX() >= this.pos1.getBlockX() && location.getX() <= this.pos2.getBlockX() && location.getY() >= this.pos1.getBlockY() && location.getY() <= this.pos2.getBlockY() && location.getZ() >= this.pos1.getBlockZ() && location.getZ() <= this.pos2.getBlockZ();
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s:%s,%s,%s", this.pos1.getBlockX(), this.pos1.getBlockY(), this.pos1.getBlockZ(), this.pos2.getBlockX(), this.pos2.getBlockY(), this.pos2.getBlockZ());
    }

    /**
     * Load cuboid from String
     *
     * @param world Instance of the world
     * @param value String format x1,y1,z1:x2,y2,z2
     * @return Instance of the cuboid
     */
    public static Cuboid load(@NotNull World world, @NotNull String value) {
        Preconditions.checkNotNull(world);
        Preconditions.checkNotNull(value);
        String[] pos = value.split(":");
        String[] pos1 = pos[0].split(",");
        String[] pos2 = pos[1].split(",");
        return new Cuboid(new Location(world, Integer.parseInt(pos1[0]), Integer.parseInt(pos1[1]), Integer.parseInt(pos1[2])), new Location(world, Integer.parseInt(pos2[0]), Integer.parseInt(pos2[1]), Integer.parseInt(pos2[2])));
    }
}
