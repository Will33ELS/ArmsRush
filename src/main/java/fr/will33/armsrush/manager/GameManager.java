package fr.will33.armsrush.manager;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.model.Arena;
import org.jetbrains.annotations.NotNull;

public class GameManager {

    private Arena arena;

    public Arena getArena() {
        return arena;
    }

    public void setArena(@NotNull Arena arena) {
        this.arena = Preconditions.checkNotNull(arena);
    }
}
