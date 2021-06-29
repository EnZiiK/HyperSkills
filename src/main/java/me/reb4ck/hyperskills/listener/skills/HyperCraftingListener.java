package me.reb4ck.hyperskills.listener.skills;

import lombok.AllArgsConstructor;
import me.reb4ck.hypercrafting.api.events.HyperCraftEvent;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.Skill;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class HyperCraftingListener implements Listener {
    private final HyperSkills plugin;

    @EventHandler
    public void mainCombat(HyperCraftEvent e) {
        SkillType skillType = SkillType.Carpentry;
        Skill skill = plugin.getSkills().getAllSkills().get(skillType);
        Player player = e.getPlayer();
        if (!skill.isEnabled()) return;
        if (player.getGameMode().equals(GameMode.CREATIVE) && !skill.isXpInCreative()) return;
        if (Utils.isInBlockedWorld(player.getWorld().getName(), skillType)) return;
        if (Utils.isInBlockedRegion(player.getLocation(), skillType)) return;
        plugin.getSkillManager().addXP(player.getUniqueId(), skillType, plugin.getSkillPoints().carpentryXP);
    }
}
