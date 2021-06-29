package me.reb4ck.hyperskills.listener.skills;

import lombok.AllArgsConstructor;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.Skill;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.objects.xp.SkillPoint;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

@AllArgsConstructor
public class FishingListener implements Listener {
    private final HyperSkills plugin;

    @EventHandler
    public void fishingSkill(PlayerFishEvent e) {
        SkillType skillType = SkillType.Fishing;
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) || e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
            if (e.isCancelled())
                return;
            double xp = plugin.getSkillPoints().fishingXP;
            String key = e.getCaught() == null ? "" : Utils.getUpperValue(e.getCaught().getName());
            if(!key.equals("") && plugin.getSkillPoints().getFishingCaughtXP().containsKey(key)){
                SkillPoint skillPoint = plugin.getSkillPoints().getFishingCaughtXP().get(key);
                xp = skillPoint.getXp();
                skillType = skillPoint.getSkillType();
            }
            Player player = e.getPlayer();
            Skill skill = plugin.getSkills().getAllSkills().get(skillType);
            if (!skill.isEnabled()) return;
            if (player.getGameMode().equals(GameMode.CREATIVE) && !skill.isXpInCreative()) return;
            if (Utils.isInBlockedWorld(player.getWorld().getName(), skillType)) return;
            if (Utils.isInBlockedRegion(player.getLocation(), skillType)) return;
            plugin.getSkillManager().addXP(player.getUniqueId(), skillType, xp);
        }
    }
}
