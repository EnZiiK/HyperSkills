package me.reb4ck.hyperskills.managers;

import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.TempUser;
import me.reb4ck.hyperskills.api.events.SkillsLevelUPEvent;
import me.reb4ck.hyperskills.api.events.SkillsXPGainEvent;
import me.reb4ck.hyperskills.listener.perks.DoubleItemPerks;
import me.reb4ck.hyperskills.objects.*;
import me.reb4ck.hyperskills.objects.HyperSound;
import me.reb4ck.hyperskills.objects.perks.Perk;
import me.reb4ck.hyperskills.objects.xp.BlockXP;
import me.reb4ck.hyperskills.utils.StringUtils;
import me.reb4ck.hyperskills.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class SkillManager {
    public final Map<UUID, TempUser> tempUsers = new HashMap<>();
    private final HyperSkills plugin;
    private final Map<UUID, PlayerSkills> skillsCache = new HashMap<>();
    private int task;
    public int playersQuantity;

    public SkillManager(HyperSkills plugin) {
        this.plugin = plugin;
        this.loadPlayerDataOnEnable();
        this.updateTop10();
    }

    public void disable(){
        savePlayerDataOnDisable();
        stopUpdating();
    }

    private void stopUpdating() {
        this.plugin.sendDebug("Stopping updating Top - Skills", DebugType.LOG);
        Bukkit.getScheduler().cancelTask(task);
    }

    private void updateTop10() {
        this.task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            Bukkit.getServer().getOnlinePlayers().forEach(p -> savePlayerData(p, false, false));
            this.updateSkillsTop();
        }, 1200L, 1200L * plugin.getConfiguration().refreshRankingMinutes);
    }

    public void savePlayerData(Player player, boolean removeFromCache, boolean async) {
        UUID uuid = player.getUniqueId();
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                PlayerSkills playerSkills = skillsCache.getOrDefault(uuid, null);
                if(playerSkills != null)
                    this.plugin.getPluginDatabase().savePlayerSkills(Bukkit.getOfflinePlayer(uuid), playerSkills);

                if (removeFromCache)
                    skillsCache.remove(player.getUniqueId());

                this.plugin.sendDebug(String.format("Saved data of player %s to database.", player.getName()), DebugType.LOG);
            });
        } else {
            PlayerSkills playerSkills = skillsCache.getOrDefault(uuid, null);
            if(playerSkills != null)
                this.plugin.getPluginDatabase().savePlayerSkills(Bukkit.getOfflinePlayer(uuid), playerSkills);

            if (removeFromCache)
                skillsCache.remove(player.getUniqueId());

            this.plugin.sendDebug(String.format("Saved data of player %s to database.", player.getName()), DebugType.LOG);
        }
    }

    private void savePlayerDataOnDisable() {
        this.plugin.sendDebug("[PLUGIN DISABLE] Saving all player data", DebugType.LOG);
        for (UUID uuid : skillsCache.keySet()) {
            PlayerSkills playerSkills = skillsCache.getOrDefault(uuid, null);
            if(playerSkills != null)
                this.plugin.getPluginDatabase().savePlayerSkills(Bukkit.getOfflinePlayer(uuid), playerSkills);
        }
        skillsCache.clear();
        this.plugin.sendDebug("[PLUGIN DISABLE] Saved all player data to database - skills", DebugType.LOG);
    }

    public void addIntoTable(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            this.plugin.getPluginDatabase().addIntoSkillsDatabase(player);
        });
    }

    private void loadPlayerDataOnEnable() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::loadPlayerData);
    }

    public void loadPlayerData(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String strSkills = plugin.getPluginDatabase().getPlayerSkills(player);
            PlayerSkills playerSkills = strSkills != null ? plugin.getGson().fromStringSkills(strSkills) : new PlayerSkills();
            skillsCache.put(player.getUniqueId(), playerSkills);
            this.plugin.sendDebug(String.format("Loaded Skills of player %s from database", player.getName()), DebugType.LOG);
        });
    }

    public Integer getLevel(UUID uuid, SkillType key) {
        return skillsCache.getOrDefault(uuid, new PlayerSkills()).getLevel(key);
    }

    public Double getXP(UUID uuid, SkillType key) {
        return skillsCache.getOrDefault(uuid, new PlayerSkills()).getXP(key);
    }


    public Integer getLevel(OfflinePlayer p, SkillType key) {
        return skillsCache.getOrDefault(p.getUniqueId(), new PlayerSkills()).getLevel(key);
    }

    public double getXP(OfflinePlayer p, SkillType key) {
        return skillsCache.getOrDefault(p.getUniqueId(), new PlayerSkills()).getXP(key);
    }

    public void setLevel(OfflinePlayer p, SkillType key, int level) {
        if (p.isOnline())
            skillsCache.getOrDefault(p.getUniqueId(), new PlayerSkills()).setLevel(key, level);
    }

    public void setXP(OfflinePlayer p, SkillType key, double xp) {
        if (p.isOnline())
            skillsCache.getOrDefault(p.getUniqueId(), new PlayerSkills()).setXP(key, xp);
    }

    public void addLevel(OfflinePlayer p, SkillType key, int level) {
        if (p.isOnline())
            skillsCache.getOrDefault(p.getUniqueId(), new PlayerSkills()).addLevel(key, level);
    }

    public void addXP(UUID uuid, SkillType skillType, double xp) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            if (xp != 0D) {
                PlayerSkills playerSkills = plugin.getSkillManager().getPlayerSkills(player);
                xp*= Utils.getMultiplier(player, skillType);
                if (playerSkills.getLevel(skillType) < plugin.getConfiguration().maxSkillLevel) {
                    SkillsXPGainEvent event = new SkillsXPGainEvent(player, skillType, xp);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled())
                        return;
                    playerSkills.addXP(skillType, xp);
                    if(plugin.getConfiguration().actionBarXP)
                        plugin.getActionBarManager().sendXPActionBar(player, skillType, xp);
                    checkLevelUp(player, skillType);
                }
            }
    }

    public void setXP(UUID uuid, SkillType skillType, double xp) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            if (xp != 0D) {
                PlayerSkills playerSkills = plugin.getSkillManager().getPlayerSkills(player);
                if (playerSkills.getLevel(skillType) < plugin.getConfiguration().maxSkillLevel) {
                    playerSkills.setXP(skillType, xp);
                    if(plugin.getConfiguration().actionBarXP)
                        plugin.getActionBarManager().sendXPActionBar(player, skillType, xp);
                    checkLevelUp(player, skillType);
                }
            }
    }


    public void checkLevelUp(Player player, SkillType skillType){
        PlayerSkills playerSkills = plugin.getSkillManager().getPlayerSkills(player);
        int level = playerSkills.getLevel(skillType);
        Double currentXP = playerSkills.getXP(skillType);
        Double maxXP = plugin.getRequirements().getLevelRequirement(skillType, level);
        new HyperSound(plugin.getConfiguration().gainXPSound, 1, 1).play(player);
        if (currentXP >= maxXP) {
            SkillsLevelUPEvent event = new SkillsLevelUPEvent(player, skillType, level+1);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            new HyperSound(plugin.getConfiguration().levelUPSound, 1, 1).play(player);
            playerSkills.addLevel(skillType, 1);
            if(currentXP - maxXP > 0)
                playerSkills.setXP(skillType, currentXP - maxXP);
            else
                playerSkills.setXP(skillType, 0d);
            levelUp(player, skillType, level);
        }
    }

    private void levelUp(Player player, SkillType skill, int level) {
        List<String> commands = plugin.getRewards().getCommandRewards(skill, level);
        if (commands != null)
            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName())));
        List<String> messages = plugin.getMessages().getLevelUPMessage();
        if (messages != null) {
            for (String line : messages) {
                if (line.contains("%level_rewards%")) {
                    for(String placeholderLine : plugin.getRewards().getRewardPlaceholders(skill, level)){
                        player.sendMessage(StringUtils.color(placeholderLine.replaceAll("%previous_level%", Utils.toRoman(level))
                                .replaceAll("%level%", Utils.toRoman(level + 1))
                                .replaceAll("%next_level%", Utils.toRoman(level + 1))));
                    }
                } else {
                    player.sendMessage(StringUtils.color(line.replaceAll("%previous_level%", Utils.toRoman(level))
                            .replaceAll("%level%", Utils.toRoman(level + 1))
                            .replaceAll("%next_level%", Utils.toRoman(level + 1))
                            .replaceAll("%money_reward%", Utils.toRoman(0))));

                }
            }
        }
    }

    public PlayerSkills getPlayerSkills(OfflinePlayer p) {
        if (!p.isOnline()) {
            return new PlayerSkills();
        } else {
            return skillsCache.getOrDefault(p.getUniqueId(), new PlayerSkills());
        }
    }

    public void resetData(UUID uuid) {
        if (skillsCache.containsKey(uuid))
            skillsCache.put(uuid, new PlayerSkills());
    }

    public PlayerSkills getPlayerSkills(UUID uuid) {
        return skillsCache.getOrDefault(uuid, new PlayerSkills());
    }

    private void updateSkillsTop() {
        this.plugin.sendDebug("Starting updating Skills Top", DebugType.LOG);
        playersQuantity = 0;
        Set<UUID> uuids = plugin.getPluginDatabase().getAllPlayers();
        Map<SkillType, List<RankPlayer>> rankPlayerMap = new HashMap<>();
        for(SkillType skillType : SkillType.values()) {
            List<RankPlayer> rankPlayers = new ArrayList<>();
            for (UUID uuid : uuids) {
                PlayerSkills playerSkills = getDatabaseSkills(uuid);
                if(playerSkills != null && playerSkills.getLevel(skillType) > plugin.getConfiguration().levelToRank)
                    rankPlayers.add(new RankPlayer(uuid, skillType, playerSkills.getSkillValue(skillType)));
            }
            playersQuantity=+rankPlayers.size();
            rankPlayerMap.put(skillType, rankPlayers);
        }
        for(SkillType skillType : rankPlayerMap.keySet()){
            List<RankPlayer> rankPlayers = rankPlayerMap.get(skillType);
            rankPlayers.sort((o1, o2) -> (int) (o2.getPoints() - o1.getPoints()));
            int i = 1;
            for(RankPlayer rankPlayer : rankPlayers){
                PlayerSkills playerSkills = getDatabaseSkills(rankPlayer.getUuid());
                if(playerSkills == null) continue;
                playerSkills.setRankPosition(skillType, i);
                i++;
            }
        }
        this.plugin.sendDebug("Skill Top updated!", DebugType.LOG);
    }

    private PlayerSkills getDatabaseSkills(UUID uuid){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if(skillsCache.containsKey(uuid)){
            return skillsCache.get(uuid);
        }else{
            String strSkills = plugin.getPluginDatabase().getPlayerSkills(offlinePlayer);
            if (strSkills != null)
                return plugin.getGson().fromStringSkills(strSkills);
        }
        return null;
    }

    public int getOptionalPlayers(){
        if(skillsCache.size() == 0)
            return 1;
        return skillsCache.size();
    }

    public void manageBlockPoints(Player player, Block bl, Material mat, boolean multiplyRewards){
        byte data = bl.getData();
        String key = mat.toString();
        if(!plugin.getSkillPoints().skillBlocksXP.containsKey(key)) return;
        BlockXP skillXP = plugin.getSkillPoints().skillBlocksXP.get(key);
        if(data != skillXP.getMaterialData() && skillXP.getMaterialData() != -1) return;
        SkillType skillType = skillXP.getSkillType();
        Skill skill = plugin.getSkills().getAllSkills().get(skillType);
        if (!skill.isEnabled()) return;
        if (player.getGameMode().equals(GameMode.CREATIVE) && !skill.isXpInCreative()) return;
        if(Utils.isInBlockedWorld(bl.getWorld().getName(), skillType)) return;
        if(Utils.isInBlockedRegion(bl.getLocation(), skillType)) return;
        double xp = key.contains("SUGAR_CANE") || key.contains("CACTUS") ? skillXP.getXp() * Utils.getBlockQuantity(bl, mat) : skillXP.getXp();
        if(xp <= 0) return;
        if(multiplyRewards){
            double percentage = -1;
            if(skillType == SkillType.Farming)
                percentage = plugin.getApi().getTotalPerk(player.getUniqueId(), Perk.Crop_Chance);
            else if(skillType == SkillType.Mining)
                percentage = plugin.getApi().getTotalPerk(player.getUniqueId(), Perk.Ore_Chance);
            else if(skillType == SkillType.Foraging)
                percentage = plugin.getApi().getTotalPerk(player.getUniqueId(), Perk.Log_Chance);
            if(percentage == -1) return;
            if(Utils.hasSkillTouch(player)) return;
            DoubleItemPerks.multiplyRewards(player, skillType, bl, percentage, plugin.getAddonsManager().isEcoEnchants() && plugin.getAddonsManager().getEcoEnchants().hasEnchantment(player.getItemInHand(), "telekinesis"));
        }
        plugin.getSkillManager().addXP(player.getUniqueId(), skillType, xp);
    }

}