package me.reb4ck.hyperskills.listener.skills;

import lombok.AllArgsConstructor;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.Skill;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;

@AllArgsConstructor
public class AlchemyListener implements Listener {

    private final HyperSkills plugin;

    @EventHandler
    public void alchemySkill(BrewEvent e) {
        Block brew = e.getBlock();
        Location standL = brew.getLocation();
        SkillType skillType = SkillType.Alchemy;
        if (e.isCancelled())
            return;
        Skill skill = plugin.getSkills().getAllSkills().get(skillType);
        if (!skill.isEnabled())
            return;
        if (Utils.isInBlockedWorld(standL.getWorld().getName(), skillType))
            return;
        if (Utils.isInBlockedRegion(standL, skillType))
            return;
        for (Player player : brew.getWorld().getPlayers()) {
            Location playerL = player.getLocation();
            if (playerL.distance(standL) < 3) {
                if(player.getGameMode().equals(GameMode.CREATIVE) && !skill.isXpInCreative()) return;
                plugin.getSkillManager().addXP(player.getUniqueId(), SkillType.Alchemy, plugin.getSkillPoints().alchemyXP);
                return;
            }
        }
    }
}
