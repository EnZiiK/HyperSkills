package me.reb4ck.hyperskills.objects.xp;

import lombok.Getter;
import me.reb4ck.hyperskills.objects.SkillType;
import me.reb4ck.hyperskills.objects.xp.SkillPoint;

@Getter
public class MobXP extends SkillPoint {

    private final int data;

    public MobXP(String id, SkillType skillType, Double xp, int data) {
        super(id, skillType, xp);
        this.data = data;
    }
}
