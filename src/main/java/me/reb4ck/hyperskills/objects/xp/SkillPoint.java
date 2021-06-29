package me.reb4ck.hyperskills.objects.xp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.reb4ck.hyperskills.objects.SkillType;

@AllArgsConstructor
@Getter
public class SkillPoint {
    private final String id;
    private final SkillType skillType;
    private final Double xp;
}
