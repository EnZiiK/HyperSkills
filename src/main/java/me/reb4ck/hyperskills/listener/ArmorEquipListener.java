package me.reb4ck.hyperskills.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.armorequipevent.ArmorEquipEvent;
import me.reb4ck.hyperskills.utils.ItemStatsUtils;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ArmorEquipListener implements Listener {
    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {
        if(e.isCancelled()) return;
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        NBTItem armor;
        if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
            armor = new NBTItem(e.getNewArmorPiece());
            if(Utils.hasEffectInHand(armor.getItem())) return;
            ItemStatsUtils.getItemAbilities(armor, false).forEach((ability, value) -> HyperSkills.getInstance().getApi().addArmorAbility(uuid, ability, value));
            ItemStatsUtils.getItemPerks(armor, false).forEach((perk, value) -> HyperSkills.getInstance().getApi().addArmorPerk(uuid, perk, value));
        }
        if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR) {
            armor = new NBTItem(e.getOldArmorPiece());
            if(Utils.hasEffectInHand(armor.getItem())) return;
            ItemStatsUtils.getItemAbilities(armor, false).forEach((ability, value) -> HyperSkills.getInstance().getApi().removeArmorAbility(uuid, ability, value));
            ItemStatsUtils.getItemPerks(armor, false).forEach((perk, value) -> HyperSkills.getInstance().getApi().removeArmorPerk(uuid, perk, value));
        }
    }
}
