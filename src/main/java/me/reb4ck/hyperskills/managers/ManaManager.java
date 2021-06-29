package me.reb4ck.hyperskills.managers;

import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.api.events.PlayerEnterEvent;
import me.reb4ck.hyperskills.objects.abilities.PlayerMana;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManaManager implements Listener {
    private final Map<UUID, PlayerMana> manaCache = new HashMap<>();

    public ManaManager(HyperSkills plugin) {
        plugin.registerListeners(this);
        loadAllPlayers();
    }

    @EventHandler
    private void onJoin(PlayerEnterEvent e){
        if(!HyperSkills.getInstance().getConfiguration().manaSystem) return;
        UUID uuid = e.getPlayer().getUniqueId();
        manaCache.put(uuid, new PlayerMana(e.getPlayer()));
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e){
        if(!HyperSkills.getInstance().getConfiguration().manaSystem) return;
        UUID uuid = e.getPlayer().getUniqueId();
        if(!manaCache.containsKey(uuid)) return;
        manaCache.get(uuid).stop();
        manaCache.remove(uuid);
    }

    @EventHandler
    private void onLeave(PlayerKickEvent e){
        if(!HyperSkills.getInstance().getConfiguration().manaSystem) return;
        UUID uuid = e.getPlayer().getUniqueId();
        if(!manaCache.containsKey(uuid)) return;
        manaCache.get(uuid).stop();
        manaCache.remove(uuid);
    }

    private void loadAllPlayers(){
        Bukkit.getOnlinePlayers().forEach(player -> manaCache.put(player.getUniqueId(), new PlayerMana(player)));
    }
}
