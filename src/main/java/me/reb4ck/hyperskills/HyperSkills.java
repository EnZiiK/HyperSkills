package me.reb4ck.hyperskills;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.reb4ck.helper.UltimatePlugin;
import me.reb4ck.helper.database.Credentials;
import me.reb4ck.helper.database.DatabaseType;
import me.reb4ck.hyperskills.api.HyperSkillsAPI;
import me.reb4ck.hyperskills.api.HyperSkillsAPIImpl;
import me.reb4ck.hyperskills.armorequipevent.ArmorListener;
import me.reb4ck.hyperskills.commands.CommandManager;
import me.reb4ck.hyperskills.configs.*;
import me.reb4ck.hyperskills.database.Database;
import me.reb4ck.hyperskills.database.implementations.MySQLDatabase;
import me.reb4ck.hyperskills.database.implementations.SQLiteDatabase;
import me.reb4ck.hyperskills.listener.*;
import me.reb4ck.hyperskills.listener.perks.*;
import me.reb4ck.hyperskills.listener.skills.*;
import me.reb4ck.hyperskills.managers.*;
import me.reb4ck.hyperskills.objects.DebugType;
import me.reb4ck.hyperskills.serializer.GSON;
import me.reb4ck.hyperskills.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class HyperSkills extends UltimatePlugin {
    private static HyperSkills INSTANCE;
    private Config configuration;
    private Messages messages;
    private Rewards rewards;
    private Inventories inventories;
    private Requirements requirements;
    private SkillsPoints skillPoints;
    private UltimateItems ultimateItems;
    private HyperSkillsAPI api;
    private Database pluginDatabase;
    private Skills skills;
    private NormalItems normalItems;
    private CommandManager commandManager;
    private AddonsManager addonsManager;
    private SkillManager skillManager;
    private ManaManager manaManager;
    private AbilitiesManager abilitiesManager;
    private PerksManager perksManager;
    private ActionBarManager actionBarManager;
    private ResetDataManager resetDataManager;
    private SpeedManager speedManager;
    private HealthManager healthManager;
    private GSON gson;

    public static HyperSkills getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.gson = new GSON();
        loadConfigs();
        Credentials credentials = Credentials.fromConfig(configuration.getConfig());
        this.pluginDatabase = credentials.getDatabaseType() == DatabaseType.MYSQL ? new MySQLDatabase(this, credentials) : new SQLiteDatabase(this, credentials);
        this.api = new HyperSkillsAPIImpl(this);
        this.skillManager = new SkillManager(this);
        this.perksManager = new PerksManager(this);
        this.abilitiesManager = new AbilitiesManager(this);
        this.addonsManager = new AddonsManager(this);
        this.commandManager = new CommandManager(this);
        this.actionBarManager = new ActionBarManager(this);
        this.resetDataManager = new ResetDataManager(this);
        this.manaManager = new ManaManager(this);
        this.speedManager = new SpeedManager(this);
        this.healthManager = new HealthManager(this);
        registerListeners(new DamageListener(this), new ArmorListener(new ArrayList<>()), new BlockBreakListener(this), new ArmorSetupListener(), new ArmorEquipListener(), new AlchemyListener(this), XMaterial.getVersion() == 8 ? new MobKillListener_Legacy(this) : new MobKillListener(this), new PlayerJoinLeaveListener(this), new EnchantingListener(this), new FishingListener(this), new AlchemyPerks(), new DefenseListener(this), new EnchantingPerks(), new BlockPlaceListener(), new ItemStatsListener(this), new InventoryClickListener());
        Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e"+getDescription().getName()+" Has been enabled! &fVersion: " + getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e"+ getDescription().getName() +" Thanks for using my plugin!  &f~Reb4ck"));
    }


    @Override
    public void onDisable() {
        if(skillManager != null) skillManager.disable();
        if(perksManager != null) perksManager.disable();
        if(abilitiesManager != null) abilitiesManager.disable();
        if(pluginDatabase != null) pluginDatabase.close();
        Bukkit.getServer().getOnlinePlayers().forEach(HumanEntity::closeInventory);
        ultimateItems.save();
        getLogger().info(getDescription().getName() + " Disabled!");
    }

    public void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public void sendErrorMessage(Exception e) {
        e.printStackTrace();
    }


    public void loadConfigs() {
        normalItems = new NormalItems(this, "normalitems", false, true);
        ultimateItems = new UltimateItems(this, "ultimateitems", true, true);
        configuration = new Config(this, "config", true, false);
        messages = new Messages(this, "messages", true, false);
        rewards = new Rewards(this, "rewards", true, false);
        requirements = new Requirements(this, "requirements", true, false);
        skillPoints = new SkillsPoints(this, "skillspoints", true, false);
        inventories = new Inventories(this, "inventories", true, false);
        skills = new Skills(this, "skills", true, false);
    }

    public void reloadConfigs() {
        normalItems.reload();
        ultimateItems.reload();
        configuration.reload();
        messages.reload();
        rewards.reload();
        requirements.reload();
        skillPoints.reload();
        inventories.reload();
        skills.reload();
    }

    public void sendDebug(String message, DebugType debugType){
        if(!configuration.debug) return;
        if(debugType == DebugType.LOG)
            getLogger().info(message);
        else
            Bukkit.getConsoleSender().sendMessage(StringUtils.color(message));
    }

    @Override
    public String getPluginName(){
        return getDescription().getName();
    }
}
