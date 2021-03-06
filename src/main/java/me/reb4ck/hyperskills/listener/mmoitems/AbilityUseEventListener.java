package me.reb4ck.hyperskills.listener.mmoitems;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.abilities.Ability;
import me.reb4ck.hyperskills.utils.ItemStatsUtils;
import net.Indyuce.mmoitems.api.event.AbilityUseEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class AbilityUseEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAbilityUse(AbilityUseEvent e){
        Player player = e.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(itemStack);
            Double mmoCost = ItemStatsUtils.getMMOManaCost(nbtItem);
            double current = HyperSkills.getInstance().getApi().getTotalAbility(player.getUniqueId(), Ability.Intelligence);
            if(mmoCost > 0){
                if(current > mmoCost) {
                    Bukkit.getScheduler().runTaskLater(HyperSkills.getInstance(), () -> player.setFoodLevel(100), 3);
                    HyperSkills.getInstance().getApi().removeAbility(player.getUniqueId(), Ability.Intelligence, mmoCost);
                }else {
                    e.setCancelled(true);
                }
            }
        }
    }
}
