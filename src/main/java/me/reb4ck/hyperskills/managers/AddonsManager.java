package me.reb4ck.hyperskills.managers;

import lombok.Getter;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.addons.*;
import me.reb4ck.hyperskills.implementations.EconomyPluginImpl;
import me.reb4ck.hyperskills.implementations.ManagerImpl;
import me.reb4ck.hyperskills.listener.minions.JetMinionsListener;
import me.reb4ck.hyperskills.listener.minions.UltraMinionsListener;
import me.reb4ck.hyperskills.listener.mmoitems.AbilityUseEventListener;
import me.reb4ck.hyperskills.listener.skills.*;
import me.reb4ck.hyperskills.addons.ResidenceSupportAPIManager;
import me.reb4ck.hyperskills.utils.StringUtils;

import me.reb4ck.regionwrapper.implementations.RegionPluginImpl;
import me.reb4ck.regionwrapper.worldguard.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.List;

@Getter
public class AddonsManager extends ManagerImpl {
    private MMOItemsAPIManager mmoItems;
    private RegionPluginImpl regionPlugin;
    private MythicMobsAPIManager mythicMobs;
    private EconomyPluginImpl economyPlugin;
    private ClipPlaceholderAPIManager placeholderAPI;
    private MVDWPlaceholderAPIManager mvdwPlaceholderAPI;
    private CitizensAPIManager citizensAPIManager;
    private EcoEnchantsAPIManager ecoEnchants;

    public AddonsManager(HyperSkills plugin) {
        super(plugin);
        load();
    }

    @Override
    public void load() {
        String name = plugin.getDescription().getName();
        if (isPlugin("HyperRunes")) {
            registerListeners(new HyperRunesListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperRunes!"));
        }
        if (isPlugin("HyperCrafting")) {
            registerListeners(new HyperCraftingListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperCrafting!"));
        }
        if (isPlugin("HyperEnchants")) {
            registerListeners(new HyperEnchantingListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperEnchants!"));
        }
        if (isPlugin("HyperAnvil")) {
            registerListeners(new HyperAnvilListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperAnvil!"));
        }
        if (isPlugin("Autosell") && getPluginAuthor("AutoSell").contains("Drawethree")) {
            registerListeners(new AutoSellListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into Autosell!"));
        }
        if (isPlugin("HyperRegions")) {
            registerListeners(new HyperRegionListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e[" + name + "] &aSuccessfully hooked into HyperRegions!"));
        }
        if (isPlugin("MMOItems") && isPlugin("MythicLib")){
            mmoItems = new MMOItemsAPIManager("MMOItems & MythicLib");
            registerListeners(new AbilityUseEventListener());
        }
        if (isPlugin("Residence")) {
            regionPlugin = new ResidenceSupportAPIManager("Residence");
        }else if(isPlugin("WorldGuard")){
            if(isPlugin("WorldEdit") || isPlugin("FastAsyncWorldEdit")){
                regionPlugin = new WorldGuard();
                Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e["+name+"] &aSuccessfully hooked into WorldEdit && WorldGuard!"));
            }
        }else if(isPlugin("UltraRegions")){
            regionPlugin = new UltraRegionsAPIManager("UltraRegions");
        }
        if(isPlugin("MythicMobs")){
            mythicMobs = new MythicMobsAPIManager("MythicMobs");
        }
        if(isPlugin("Vault"))
            economyPlugin = new VaultAPIManager("Vault");
        if(isPlugin("Citizens"))
            citizensAPIManager = new CitizensAPIManager("Citizens");
        if(isPlugin("PlaceholderAPI")){
            placeholderAPI = new ClipPlaceholderAPIManager(plugin);
            placeholderAPI.register();
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e["+name+"] &aSuccessfully hooked into PlaceholderAPI!"));
        }
        if(isPlugin("MVDWPlaceholderAPI")){
            mvdwPlaceholderAPI = new MVDWPlaceholderAPIManager(plugin);
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e["+name+"] &aSuccessfully hooked into MVDWPlaceholderAPI!"));
        }
        if(isPlugin("JetsMinions") && plugin.getConfiguration().jetMinions){
            registerListeners(new JetMinionsListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e["+name+"] &aSuccessfully hooked into JetMinions!"));
        }
        if(isPlugin("UltraMinions") && plugin.getConfiguration().ultraMinions){
            registerListeners(new UltraMinionsListener(plugin));
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e["+name+"] &aSuccessfully hooked into UltraMinions!"));
        }
        if(isPlugin("EcoEnchants"))
            ecoEnchants = new EcoEnchantsAPIManager("EcoEnchants");
    }

    public void registerListeners(Listener... listener) {
        for (Listener l : listener)
            Bukkit.getPluginManager().registerEvents(l, HyperSkills.getInstance());
    }

    private boolean isPlugin(String name){
        return Bukkit.getServer().getPluginManager().getPlugin(name) != null;
    }

    private String getPluginVersion(String name){
        return Bukkit.getServer().getPluginManager().getPlugin(name).getDescription().getVersion();
    }

    private List<String> getPluginAuthor(String name){
        return Bukkit.getServer().getPluginManager().getPlugin(name).getDescription().getAuthors();
    }

    public boolean isMMOItems(){
        return mmoItems != null;
    }

    public boolean isEcoEnchants(){
        return ecoEnchants != null;
    }

}