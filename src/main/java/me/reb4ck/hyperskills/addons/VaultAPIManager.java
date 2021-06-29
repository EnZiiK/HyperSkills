package me.reb4ck.hyperskills.addons;

import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.implementations.EconomyPluginImpl;
import me.reb4ck.hyperskills.implementations.SoftDependImpl;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class VaultAPIManager extends SoftDependImpl implements EconomyPluginImpl {

    private Economy economy;

    public VaultAPIManager(String displayName){
        super(displayName);
        RegisteredServiceProvider<Economy> rsp = HyperSkills.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return;
        economy = rsp.getProvider();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return economy.getBalance(offlinePlayer);
    }

    @Override
    public double getBalance(UUID uuid) {
        return getBalance(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public void withdraw(OfflinePlayer offlinePlayer, double amount) {
        economy.withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public void withdraw(UUID uuid, double amount) {
        withdraw(Bukkit.getOfflinePlayer(uuid), amount);
    }

    @Override
    public void deposit(OfflinePlayer offlinePlayer, double amount) {
        economy.depositPlayer(offlinePlayer, amount);
    }

    @Override
    public void deposit(UUID uuid, double amount) {
        deposit(Bukkit.getOfflinePlayer(uuid), amount);
    }
}
