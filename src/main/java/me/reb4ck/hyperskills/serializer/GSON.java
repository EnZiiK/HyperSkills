package me.reb4ck.hyperskills.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.reb4ck.hyperskills.objects.PlayerSkills;
import me.reb4ck.hyperskills.objects.abilities.PlayerAbilities;
import me.reb4ck.hyperskills.objects.perks.PlayerPerks;
import me.reb4ck.hyperskills.serializer.adapters.AbilitiesAdapter;
import me.reb4ck.hyperskills.serializer.adapters.EnumTypeAdapter;
import me.reb4ck.hyperskills.serializer.adapters.PerksAdapter;
import me.reb4ck.hyperskills.serializer.adapters.SkillsAdapter;

public class GSON {

    private final Gson adapter;

    private final Gson playerAbilities;

    private final Gson playerPerks;

    public GSON() {
        this.playerAbilities = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping()
                .registerTypeAdapter(PlayerAbilities.class, new AbilitiesAdapter())
                .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY)
                .enableComplexMapKeySerialization()
                .create();
        this.adapter = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping()
                .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY)
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(PlayerSkills.class, new SkillsAdapter())
                .create();
        this.playerPerks = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping()
                .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY)
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(PlayerPerks.class, new PerksAdapter())
                .create();
    }

    public String toStringAbilities(PlayerAbilities pd) {
        return this.playerAbilities.toJson(pd, PlayerAbilities.class);
    }

    public PlayerAbilities fromStringAbilities(String data) {
        return playerAbilities.fromJson(data, PlayerAbilities.class);
    }

    public String toStringSkills(PlayerSkills pd) {
        return this.adapter.toJson(pd, PlayerSkills.class);
    }

    public PlayerSkills fromStringSkills(String data) {
        return adapter.fromJson(data, PlayerSkills.class);
    }

    public String toStringPerks(PlayerPerks pd) {
        return this.playerPerks.toJson(pd, PlayerPerks.class);
    }

    public PlayerPerks fromStringPerks(String data) {
        return playerPerks.fromJson(data, PlayerPerks.class);
    }

}