package me.reb4ck.hyperskills.listener.skills;

import com.cryptomorin.xseries.XMaterial;
import lombok.AllArgsConstructor;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.perks.PlayerPerks;
import me.reb4ck.hyperskills.objects.xp.BlockXP;
import me.reb4ck.hyperskills.objects.perks.Perk;
import me.reb4ck.hyperskills.objects.Skill;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.listener.perks.DoubleItemPerks;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@AllArgsConstructor
public class BlockBreakListener implements Listener {

    private final HyperSkills plugin;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void miningSpeed(PlayerInteractEvent e) {
        if(e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block bl = e.getClickedBlock();
        Player player = e.getPlayer();
        if(bl == null) return;
        if (bl.hasMetadata("COLLECTED")) return;
        if(e.isCancelled()) return;
        double miningSpeed = plugin.getApi().getTotalPerk(player.getUniqueId(), Perk.Mining_Speed);
        if(miningSpeed < 1) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 40, Math.max((int) (miningSpeed * 0.002645), 1)));

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreakSkills(BlockBreakEvent e) {
        Block bl = e.getBlock();
        Player player = e.getPlayer();
        try {
            if (bl.hasMetadata("COLLECTED")) return;
            if(e.isCancelled()) return;
            Material mat = bl.getType();
            plugin.getSkillManager().manageBlockPoints(player, bl, mat, true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void cutSheep(PlayerShearEntityEvent e) {
        if (e.getEntity() instanceof Sheep) plugin.getSkillManager().addXP(e.getPlayer().getUniqueId(), SkillType.Farming, plugin.getSkillPoints().farming_Cut_Wool);
    }
}
