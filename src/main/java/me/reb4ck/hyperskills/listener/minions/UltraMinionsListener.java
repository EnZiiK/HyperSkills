package me.reb4ck.hyperskills.listener.minions;

import io.github.Leonardo0013YT.UltraMinions.api.events.MinionCollectEvent;
import lombok.AllArgsConstructor;
import me.reb4ck.hyperskills.HyperSkills;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class UltraMinionsListener implements Listener {

    private final HyperSkills plugin;

    @EventHandler(priority = EventPriority.LOW)
    public void ultraMinionsEvent(MinionCollectEvent e) {
        if (!e.getItems().isEmpty())
            e.getItems().keySet().stream()
                .filter(item -> item != null && item.getType() != Material.AIR)
                .forEach(item -> plugin.getSkillManager().manageBlockPoints(e.getPlayer(), e.getPlayer().getLocation().getBlock(), item.getType(), false));
    }
}
