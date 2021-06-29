package me.reb4ck.hyperskills.objects.xp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.reb4ck.hyperskills.objects.SkillType;

@AllArgsConstructor
@Getter
public class GainingXP {
    @Setter
    private boolean isGaining;
    private final SkillType skillType;
    private final double amount;
}
