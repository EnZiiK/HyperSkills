package me.reb4ck.hyperskills.addons;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import me.reb4ck.hyperskills.implementations.SoftDependImpl;
import me.reb4ck.regionwrapper.implementations.RegionPluginImpl;
import org.bukkit.Location;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResidenceSupportAPIManager  extends SoftDependImpl implements RegionPluginImpl {
    public ResidenceSupportAPIManager(String displayName) {
        super(displayName);
    }

    @Override
    public boolean isInRegion(Location location, List<String> regions) {
        ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(location);
        return res != null && regions.contains(res.getName());
    }

    @Override
    public boolean isInRegion(Location location, String region) {
        ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(location);
        return res != null && region.contains(res.getName());
    }

    @Override
    public Set<String> getRegions(Location location) {
        ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(location);
        return res != null ? new HashSet<>(Collections.singletonList(res.getName())) : new HashSet<>();
    }
}
