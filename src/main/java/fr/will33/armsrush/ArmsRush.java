package fr.will33.armsrush;

import fr.will33.armsrush.commands.ArmsRushCommand;
import fr.will33.armsrush.commands.ConfigurationCommand;
import fr.will33.armsrush.commands.PreStartCommand;
import fr.will33.armsrush.exception.ArmsRushConfigurationException;
import fr.will33.armsrush.manager.ConfigurationManager;
import fr.will33.armsrush.manager.GameManager;
import fr.will33.armsrush.manager.ListenerManager;
import fr.will33.guimodule.GuiModule;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ArmsRush extends JavaPlugin {

    private GameManager gameManager;
    private ConfigurationManager configurationManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.gameManager = new GameManager();
        this.configurationManager = new ConfigurationManager();
        if(!new File(this.getDataFolder(), "kits.yml").exists()) {
            this.saveResource("kits.yml", false);
        }
        try {
            this.configurationManager.loadConfiguration(this.gameManager, this.getConfig());
            this.configurationManager.loadKits(this.gameManager, YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "kits.yml")));
        } catch (ArmsRushConfigurationException e) {
            throw new RuntimeException(e);
        }

        new GuiModule(this);
        new ListenerManager().registerListeners(this);

        this.getCommand("armsrush").setExecutor(new ArmsRushCommand(this));
        this.getCommand("configuration").setExecutor(new ConfigurationCommand(this));
        this.getCommand("prestart").setExecutor(new PreStartCommand());
    }

    @Override
    public void onDisable() {
        this.getGameManager().stopGame();
    }

    /**
     * Récupérer la gestion de la configuration
     * @return
     */
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
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
