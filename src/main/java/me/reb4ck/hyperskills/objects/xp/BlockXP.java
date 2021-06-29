package me.reb4ck.hyperskills.objects.xp;

import lombok.Getter;
import me.reb4ck.hyperskills.objects.SkillType;

@Getter
public class BlockXP extends SkillPoint{
    private final byte materialData;
    public BlockXP(String id, SkillType skillType, Double xp, byte materialData) {
        super(id, skillType, xp);
        this.materialData = materialData;
    }
}
