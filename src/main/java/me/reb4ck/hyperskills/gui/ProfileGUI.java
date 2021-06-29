package me.reb4ck.hyperskills.gui;

import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.Item;
import me.reb4ck.hyperskills.utils.InventoryUtils;
import me.reb4ck.hyperskills.utils.StringUtils;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ProfileGUI implements GUI{

    private final UUID uuid;

    private final String headOwner;

    public ProfileGUI(Player player) {
        this.uuid = player.getUniqueId();
        this.headOwner = player.getDisplayName();
    }

    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            Player player = (Player) e.getWhoClicked();
            if (e.getSlot() == HyperSkills.getInstance().getInventories().getCloseButton().slot) {
                player.closeInventory();
            }else if(e.getSlot() == 48){
                player.openInventory(new MainGUI(uuid).getInventory());
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, HyperSkills.getInstance().getInventories().getProfileMenuSize(), StringUtils.color(HyperSkills.getInstance().getInventories().getProfileTitle()));
        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().getBackground()));
        for (Item item : HyperSkills.getInstance().getInventories().getProfileGUIItems())
            inventory.setItem(item.slot, InventoryUtils.makeItem(item, Utils.getStatsPlaceholders(uuid), headOwner));
        inventory.setItem(HyperSkills.getInstance().getInventories().getProfileBackButton().slot, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().getProfileBackButton()));
        inventory.setItem(HyperSkills.getInstance().getInventories().getCloseButton().slot, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().getCloseButton()));
        return inventory;
    }
}
