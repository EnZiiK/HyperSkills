package me.reb4ck.hyperskills.objects.abilities;

import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.ManaSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

public class PlayerMana {
    private int taskID;
    private final UUID uuid;

    public PlayerMana(Player player){
        this.uuid = player.getUniqueId();
        init();
    }

    private void init(){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ManaSettings manaSettings = HyperSkills.getInstance().getConfiguration().manaSettings;
        this.taskID = scheduler.scheduleAsyncRepeatingTask(HyperSkills.getInstance(), () -> {
            if (!manageMana(manaSettings))
                stop();
        }, 0L, manaSettings.getSecondAmount() * 20L);
    }

    private boolean manageMana(ManaSettings manaSettings) {
        if (!HyperSkills.getInstance().getConfiguration().manaSystem)
            return false;
        PlayerAbilities playerAbilities = HyperSkills.getInstance().getAbilitiesManager().getPlayerAbilities(uuid);
        double maxMana = HyperSkills.getInstance().getApi().getTotalAbility(uuid, Ability.Max_Intelligence);
        double currentMana = HyperSkills.getInstance().getApi().getTotalAbility(uuid, Ability.Intelligence);
        double amount = (manaSettings.getPercentagePerSecond() * maxMana) / 100;
        double newMana = currentMana + amount;
        if(currentMana > maxMana)
            playerAbilities.setAbility(Ability.Intelligence, maxMana);
        if(currentMana < maxMana)
            playerAbilities.setAbility(Ability.Intelligence, newMana > maxMana ? maxMana : newMana);
        return true;
    }

    public void stop(){
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
