package me.reb4ck.hyperskills.gui;

import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.Item;
import me.reb4ck.hyperskills.objects.Skill;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.utils.InventoryUtils;
import me.reb4ck.hyperskills.utils.StringUtils;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TopGUI implements GUI {

    private final UUID uuid;

    public TopGUI(UUID uuid) {
        this.uuid = uuid;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            Player player = (Player) e.getWhoClicked();
            if (e.getSlot() == HyperSkills.getInstance().getInventories().getCloseButton().slot) {
                player.closeInventory();
            }else if(e.getSlot() == HyperSkills.getInstance().getInventories().getHideRanking().slot){
                player.openInventory(new MainGUI(player.getUniqueId()).getInventory());
            }else if(e.getSlot() == HyperSkills.getInstance().getInventories().getMainMenuStatsItem().slot){
                player.openInventory(new ProfileGUI(player).getInventory());
            } else {
                for (Skill skill : HyperSkills.getInstance().getSkills().getAllSkills().values()) {
                    if (skill.getSlot() == e.getSlot() && skill.isEnabled())
                        player.openInventory(new SubGUI(player.getUniqueId(), skill.getSkillType(), 1, Utils.getMultiplier(player, skill.getSkillType())).getInventory());
                }
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, HyperSkills.getInstance().getInventories().getTopMenuSize(), StringUtils.color(HyperSkills.getInstance().getInventories().getTopMenuTitle()));
        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().getBackground()));
        for (SkillType skillType : SkillType.values()) {
            int level = HyperSkills.getInstance().getSkillManager().getLevel(uuid, skillType);
            Skill skill = HyperSkills.getInstance().getSkills().getAllSkills().get(skillType);
            if(!skill.isEnabled()) continue;
            inventory.setItem(skill.getSlot(), InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().getTopMenuSkillItem(), Utils.getTopPlaceHolders(uuid, skillType), skillType, level+1));
        }
        if(HyperSkills.getInstance().getInventories().isMainMenuBackEnabled())
            inventory.setItem(HyperSkills.getInstance().getInventories().getMainMenuBack().slot, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().getMainMenuBack()));

        //SWORD
        inventory.setItem(HyperSkills.getInstance().getInventories().getHideRanking().slot, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().getHideRanking()));
        //STATS
        Item item = new Item(HyperSkills.getInstance().getInventories().getTopMenuStatsItem());
        item.headOwner = Bukkit.getOfflinePlayer(uuid).getName();
        inventory.setItem(item.slot, InventoryUtils.makeItem(item, Utils.getMainStatsPlaceholders(uuid)));
        //CLOSE
        inventory.setItem((HyperSkills.getInstance().getInventories()).getCloseButton().slot, InventoryUtils.makeItem((HyperSkills.getInstance().getInventories()).getCloseButton()));
        return inventory;
    }
}
