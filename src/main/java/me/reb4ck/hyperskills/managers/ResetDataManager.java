package me.reb4ck.hyperskills.managers;

import lombok.RequiredArgsConstructor;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.objects.DataReset;

import java.util.*;

@RequiredArgsConstructor
public class ResetDataManager {
    private final HyperSkills plugin;
    private final Map<UUID, DataReset> players = new HashMap<>();

    public boolean addPlayer(UUID uuid, UUID executor){
        if(players.containsKey(uuid)) return false;
        players.put(uuid, new DataReset(uuid, executor));
        return true;
    }

    public void removePlayer(UUID uuid, boolean completed){
        if(!players.containsKey(uuid)) return;
        if(completed)
            players.get(uuid).stop(true);
        players.remove(uuid);
    }

    public void resetData(UUID uuid){
        removePlayer(uuid, true);
        plugin.getAbilitiesManager().resetData(uuid);
        plugin.getPerksManager().resetData(uuid);
        plugin.getSkillManager().resetData(uuid);
    }
}
