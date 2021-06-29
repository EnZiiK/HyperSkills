package me.reb4ck.hyperskills.addons;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.api.HyperSkillsAPI;
import me.reb4ck.hyperskills.objects.abilities.Ability;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.objects.perks.Perk;
import me.reb4ck.hyperskills.utils.Utils;

public class MVDWPlaceholderAPIManager {

    public MVDWPlaceholderAPIManager(HyperSkills plugin) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            return;
        }
        HyperSkillsAPI api = plugin.getApi();
        PlaceholderAPI.registerPlaceholder(plugin, "skills_current_health", e -> Utils.getHealth(e.getPlayer()) + "");
        for(Ability ability : Ability.values()) {
            PlaceholderAPI.registerPlaceholder(plugin, "skills_" + ability.getLowName() + "_total", e -> api.getTotalAbility(e.getPlayer().getUniqueId(), ability) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+ability.getLowName()+"_extra", e -> api.getExtraAbility(e.getPlayer().getUniqueId(), ability) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+ability.getLowName()+"_simple", e -> api.getSimpleAbility(e.getPlayer().getUniqueId(), ability) + "");
        }

        for(Perk perk : Perk.values()) {
            PlaceholderAPI.registerPlaceholder(plugin, "skills_" + perk.getLowName() + "_total", e -> api.getTotalPerk(e.getPlayer().getUniqueId(), perk) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+perk.getLowName()+"_extra", e -> api.getExtraPerk(e.getPlayer().getUniqueId(), perk) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+perk.getLowName()+"_simple", e -> api.getSimplePerk(e.getPlayer().getUniqueId(), perk) + "");
        }

        for(SkillType skillType : SkillType.values()) {
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+skillType.getLowName()+"_level", e -> api.getLevel(e.getPlayer().getUniqueId(), skillType) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+skillType.getLowName()+"_xp", e -> api.getLevel(e.getPlayer().getUniqueId(), skillType) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+skillType.getLowName()+"_progressbar", e -> Utils.getProgressBar(e.getPlayer().getUniqueId(), skillType) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+skillType.getLowName()+"_xp_left", e -> plugin.getRequirements().getLevelRequirement(skillType, api.getLevel(e.getPlayer().getUniqueId(), skillType)) - api.getXP(e.getPlayer().getUniqueId(), skillType) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+skillType.getLowName()+"_rank", e -> api.getRank(e.getPlayer().getUniqueId(), skillType) + "");
            PlaceholderAPI.registerPlaceholder(plugin, "skills_"+skillType.getLowName()+"_level_roman", e -> Utils.toRoman(api.getLevel(e.getPlayer().getUniqueId(), skillType)) + "");
        }
    }
}