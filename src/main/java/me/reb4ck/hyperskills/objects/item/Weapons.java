package me.reb4ck.hyperskills.objects.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.reb4ck.hyperskills.objects.abilities.Ability;
import me.reb4ck.hyperskills.objects.perks.Perk;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Weapons {
    private final Map<String, Map<Ability, Double>> weapons;
    private final Map<String, Map<Perk, Double>> weaponsPerks;
}
