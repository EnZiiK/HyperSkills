package me.reb4ck.hyperskills.listener.skills;

import com.cryptomorin.xseries.XMaterial;
import dev.drawethree.autosell.event.PlayerAutosellEvent;
import lombok.AllArgsConstructor;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.xp.BlockXP;
import me.reb4ck.hyperskills.objects.perks.Perk;
import me.reb4ck.hyperskills.objects.Skill;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.listener.perks.DoubleItemPerks;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class AutoSellListener implements Listener {

    private final HyperSkills plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAutoSell(PlayerAutosellEvent e) {
        Block bl = e.getBlock();
        Material mat = bl.getType();
        Player player = e.getPlayer();
        try {
            if (bl.hasMetadata("placeBlock"))
                return;
            if (e.isCancelled())
                return;
            byte data = bl.getData();
            String key = mat.toString();
            if (!plugin.getSkillPoints().skillBlocksXP.containsKey(key)) return;
            BlockXP skillXP = plugin.getSkillPoints().skillBlocksXP.get(key);
            if (data != skillXP.getMaterialData() && skillXP.getMaterialData() != -1) return;
            SkillType skillType = skillXP.getSkillType();
            Skill skill = plugin.getSkills().getAllSkills().get(skillType);
            if (!skill.isEnabled()) return;
            if (player.getGameMode().equals(GameMode.CREATIVE) && !skill.isXpInCreative()) return;
            if (Utils.isInBlockedWorld(bl.getWorld().getName(), skillType)) return;
            if (Utils.isInBlockedRegion(bl.getLocation(), skillType)) return;
            double xp = mat == XMaterial.SUGAR_CANE.parseMaterial() || mat == XMaterial.CACTUS.parseMaterial() ? skillXP.getXp() * Utils.getBlockQuantity(bl, mat) : skillXP.getXp();
            if (xp <= 0) return;
            double percentage = -1;
            if (skillType == SkillType.Farming)
                percentage = plugin.getApi().getTotalPerk(player.getUniqueId(), Perk.Crop_Chance);
            else if (skillType == SkillType.Mining)
                percentage = plugin.getApi().getTotalPerk(player.getUniqueId(), Perk.Ore_Chance);
            else if (skillType == SkillType.Foraging)
                percentage = plugin.getApi().getTotalPerk(player.getUniqueId(), Perk.Log_Chance);
            if (percentage == -1) return;
            plugin.getSkillManager().addXP(player.getUniqueId(), skillType, xp);
            if (Utils.hasSkillTouch(player)) return;
            DoubleItemPerks.multiplyRewards(player, skillType, bl, percentage, plugin.getAddonsManager().isEcoEnchants() && plugin.getAddonsManager().getEcoEnchants().hasEnchantment(player.getItemInHand(), "telekinesis"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}