package fr.will33.armsrush.gui;

import com.google.common.base.Preconditions;
import fr.will33.armsrush.ArmsRush;
import fr.will33.armsrush.exception.ArmsRushConfigurationException;
import fr.will33.armsrush.model.APlayer;
import fr.will33.armsrush.model.TeamEnum;
import fr.will33.armsrush.utils.ItemBuilder;
import fr.will33.guimodule.GuiModule;
import fr.will33.guimodule.gui.AbstractGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TeamGUI extends AbstractGUI {

    private final ArmsRush instance;
    private final ItemStack randomTeam;

    public TeamGUI(@NotNull ArmsRush instance) throws ArmsRushConfigurationException {
        this.instance = Preconditions.checkNotNull(instance);
        this.randomTeam = this.instance.getConfigurationManager().loadItems(
                this.instance.getConfig().getString("gui.team.random.material"),
                this.instance.getConfig().getString("gui.team.random.displayname"),
                this.instance.getConfig().getStringList("gui.team.random.lore")
        );
    }

    @Override
    public void onDisplay(Player player) {
        this.inventory = Bukkit.createInventory(null, 2 * 9, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.team.title")));
        this.onUpdate(player);
        player.openInventory(this.inventory);
    }

    @Override
    public void onUpdate(Player player) {
        int slot = 0;
        for(TeamEnum teamEnum : TeamEnum.values()){
            this.setSlotData(new ItemBuilder(Material.valueOf(teamEnum.name() + "_CONCRETE"), 1, ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.team." + teamEnum.name().toLowerCase() + ".displayname")), lore(teamEnum)).toItemStack(), slot, teamEnum.name());
            slot ++;
        }
        this.setSlotData(this.randomTeam, 13, "random");
    }

    @Override
    public void onClick(Player player, ItemStack itemStack, String action, ClickType clickType) {
        if(!this.instance.getGameManager().getArena().getAPlayers().containsKey(player)){
            this.instance.getGameManager().getArena().getAPlayers().put(player, new APlayer(player));
        }
        if("random".equals(action)){
            List<TeamEnum> available = new ArrayList<>();
            for(TeamEnum teamEnum : TeamEnum.values()){
                if(this.instance.getGameManager().getArena().getPlayersInTeam(teamEnum).size() < this.instance.getConfigurationManager().getMaxPlayerInTeam()){
                    available.add(teamEnum);
                }
            }
            if(available.isEmpty()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.team.noTeamAvailable")));
            } else {
                TeamEnum playerTeam = this.instance.getGameManager().getArena().getTeam(player);
                TeamEnum teamEnum = available.get(new Random().nextInt(available.size()));
                Optional.ofNullable(playerTeam).ifPresent(t -> this.instance.getGameManager().getArena().getPlayersInTeam(t).remove(player));
                this.instance.getGameManager().getArena().getPlayersInTeam(teamEnum).add(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.team.success")));
                refreshAllGUI();
            }
        } else {
            TeamEnum teamEnum = TeamEnum.valueOf(action);
            TeamEnum playerTeam = this.instance.getGameManager().getArena().getTeam(player);
            if ((playerTeam != null && playerTeam != teamEnum) && this.instance.getGameManager().getArena().getPlayersInTeam(teamEnum).size() >= this.instance.getConfigurationManager().getMaxPlayerInTeam()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.team.full")));
            } else if (playerTeam != teamEnum) {
                Optional.ofNullable(playerTeam).ifPresent(t -> this.instance.getGameManager().getArena().getPlayersInTeam(t).remove(player));
                this.instance.getGameManager().getArena().getPlayersInTeam(teamEnum).add(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("messages.team.success")));
                refreshAllGUI();
            }
        }
    }

    public static void refreshAllGUI(){
        for(Map.Entry<Player, AbstractGUI> guis : GuiModule.getGuiModule().getGuiManager().getGuis().entrySet()){
            if(guis.getValue() instanceof TeamGUI){
                guis.getValue().onUpdate(guis.getKey());
            }
        }
    }

    private List<String> lore(TeamEnum teamEnum){
        List<String> lore = new ArrayList<>();
        for(Player player : this.instance.getGameManager().getArena().getPlayersInTeam(teamEnum)){
            lore.add(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("gui.team.playerFormat").replace("{player}", player.getName())));
        }
        return lore;
    }
}
