package me.reb4ck.hyperskills.database;

import me.reb4ck.helper.UltimatePlugin;
import me.reb4ck.helper.implementations.DatabaseImpl;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.PlayerSkills;
import me.reb4ck.hyperskills.objects.abilities.Ability;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.objects.abilities.PlayerAbilities;
import me.reb4ck.hyperskills.objects.perks.PlayerPerks;
import org.bukkit.OfflinePlayer;

import java.util.Set;
import java.util.UUID;

public abstract class Database extends DatabaseImpl {

	public Database(UltimatePlugin plugin) {
		super(plugin);
	}

	public abstract Set<UUID> getAllPlayers();

	public abstract void addIntoSkillsDatabase(OfflinePlayer player);

	public abstract void addIntoPerksDatabase(OfflinePlayer player);

	public abstract void addIntoAbilitiesDatabase(OfflinePlayer player);

	public abstract String getPlayerSkills(OfflinePlayer player);

	public abstract void savePlayerSkills(OfflinePlayer player, PlayerSkills playerSkills);

	public abstract String getPlayerAbilities(OfflinePlayer player);

	public abstract void savePlayerAbilities(OfflinePlayer player, PlayerAbilities playerAbilities);

	public abstract String getPlayerPerks(OfflinePlayer player);

	public abstract void savePlayerPerks(OfflinePlayer player, PlayerPerks playerPerks);

}
