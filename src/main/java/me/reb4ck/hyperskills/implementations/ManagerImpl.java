package me.reb4ck.hyperskills.implementations;

import lombok.RequiredArgsConstructor;
import me.reb4ck.hyperskills.HyperSkills;

@RequiredArgsConstructor
public abstract class ManagerImpl {
    protected final HyperSkills plugin;
    public abstract void load();
    public void save(){}
}
