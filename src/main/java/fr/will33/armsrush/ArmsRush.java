package fr.will33.armsrush;

import fr.will33.armsrush.manager.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ArmsRush extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();


    }

    /**
     * Récupérer la gestion de la partie
     * @return
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Récupérer l'instance du plugin
     * @return
     */
    public static ArmsRush getInstance(){
        return ArmsRush.getPlugin(ArmsRush.class);
    }

}
