package me.reb4ck.hyperskills.listener.skills;

import lombok.AllArgsConstructor;
import me.reb4ck.hyperanvil.api.events.AnvilUseEvent;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.Skill;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class HyperAnvilListener implements Listener {
    private final HyperSkills plugin;

    @EventHandler
    public void onFuseItems(AnvilUseEvent e){
        SkillType skillType = SkillType.Enchanting;
        if(e.isCancelled()) return;
        Player player = e.getPlayer();
        Skill skill = plugin.getSkills().getAllSkills().get(skillType);
        if (!skill.isEnabled()) return;
        if (player.getGameMode().equals(GameMode.CREATIVE) && !skill.isXpInCreative()) return;
        if (Utils.isInBlockedWorld(player.getWorld().getName(), skillType)) return;
        if (Utils.isInBlockedRegion(player.getLocation(), skillType)) return;
        plugin.getSkillManager().addXP(player.getUniqueId(), skillType, plugin.getSkillPoints().enchantingXP);
    }
}
