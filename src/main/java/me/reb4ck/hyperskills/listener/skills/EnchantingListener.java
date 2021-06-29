package me.reb4ck.hyperskills.listener.skills;

import lombok.AllArgsConstructor;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.Skill;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

@AllArgsConstructor
public class EnchantingListener implements Listener {

    private final HyperSkills plugin;

    @EventHandler
    public void enchantingSkill(EnchantItemEvent e) {
        SkillType skillType = SkillType.Enchanting;
        if (e.isCancelled()) return;
        Skill skill = plugin.getSkills().getAllSkills().get(skillType);
        if (!skill.isEnabled()) return;
        Player player = e.getEnchanter();
        if (player.getGameMode().equals(GameMode.CREATIVE) && !skill.isXpInCreative()) return;
        if (Utils.isInBlockedWorld(e.getEnchantBlock().getWorld().getName(), skillType)) return;
        if (Utils.isInBlockedRegion(e.getEnchantBlock().getLocation(), skillType)) return;
        plugin.getSkillManager().addXP(player.getUniqueId(), skillType, plugin.getSkillPoints().enchantingXP);
    }

}
