package fr.will33.armsrush.manager;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.model.Arena;
import fr.will33.armsrush.model.Kit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private Arena arena;
    private final List<Kit> kits = new ArrayList<>();

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
}
