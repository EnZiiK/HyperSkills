package me.reb4ck.hyperskills.addons;

import me.reb4ck.hyperskills.implementations.SoftDependImpl;
import org.bukkit.entity.Entity;

public class CitizensAPIManager extends SoftDependImpl {
    public CitizensAPIManager(String displayName) {
        super(displayName);
    }

    public boolean isNPCEntity(Entity entity){
        return entity.hasMetadata("NPC");
    }
}
