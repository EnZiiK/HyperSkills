package me.reb4ck.hyperskills.implementations;

import me.reb4ck.hyperskills.objects.SkillType;

public interface Levellable {
    void addXP(SkillType skill, Double xp);

    void removeXP(SkillType skill, Double xp);

    void addLevel(SkillType skill, Integer level);

    void removeLevel(SkillType skill, Integer level);

    void setLevel(SkillType skill, Integer level);

    void setXP(SkillType skill, Double xp);

    int getLevel(SkillType skill);

    Double getXP(SkillType skill);
}
