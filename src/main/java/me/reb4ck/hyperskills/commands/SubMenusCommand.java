package me.reb4ck.hyperskills.commands;

import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.gui.SubGUI;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.utils.StringUtils;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SubMenusCommand extends HyperCommand {

    public SubMenusCommand() {
        super(Collections.singletonList("submenu"), "Opens skills submenu", "", true, "/Skills submenu");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (args.length > 1 && args.length < 4) {
            try {
                SkillType skill = SkillType.valueOf(args[1]);
                int page = 1;
                if (args.length == 3) {
                    page = Integer.parseInt(args[2]);
                }
                p.openInventory(new SubGUI(p.getUniqueId(), skill, page, Utils.getMultiplier(p, skill)).getInventory());
            } catch (IllegalArgumentException e) {
                p.sendMessage(StringUtils.color(HyperSkills.getInstance().getMessages().getMessage("invalidArguments").replace("%prefix%", HyperSkills.getInstance().getConfiguration().prefix)));
            }

        } else {
            p.sendMessage(StringUtils.color(HyperSkills.getInstance().getMessages().getMessage("invalidArguments").replace("%prefix%", HyperSkills.getInstance().getConfiguration().prefix)));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return null;
    }

}
