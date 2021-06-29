package me.reb4ck.hyperskills.gui;

import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.item.UltimateItem;
import me.reb4ck.hyperskills.utils.InventoryUtils;
import me.reb4ck.hyperskills.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;


public class ConfirmGUI implements GUI {

    private final UltimateItem ultimateItem;

    public ConfirmGUI(UltimateItem ultimateItem) {
        this.ultimateItem = ultimateItem;
    }

    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        int slot = e.getSlot();
        e.setCancelled(true);
        if (slot == 11) {
            HyperSkills.getInstance().getUltimateItems().removeItem(ultimateItem.getId());
            player.openInventory(new AllArmorsGUI(1).getInventory());
        } else if (slot == 15) {
            player.openInventory(new AllArmorsGUI(1).getInventory());
        }

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&8Confirm"));
        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().getBackground()));
        inventory.setItem(11, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().confirm));
        inventory.setItem(15, InventoryUtils.makeItem(HyperSkills.getInstance().getInventories().cancel));

        return inventory;
    }
}
