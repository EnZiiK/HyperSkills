package me.reb4ck.hyperskills.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.reb4ck.hyperskills.managers.IndicatorType;

@Getter
@AllArgsConstructor
public class DamageIndicator {
    private final IndicatorType indicatorType;
    private final boolean active;
    private final boolean degrade;
    private final String indicator;
}
