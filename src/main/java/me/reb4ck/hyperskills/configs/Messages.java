package me.reb4ck.hyperskills.configs;

import lombok.Getter;
import me.reb4ck.helper.UltimatePlugin;
import me.reb4ck.helper.files.YAMLFile;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

public class Messages extends YAMLFile {

    private HashMap<String, String> messages;

    @Getter
    private List<String> levelUPMessage;

    public Messages(UltimatePlugin plugin, String name, boolean defaults, boolean save) {
        super(plugin, name, defaults, save);
        loadMessages();
    }


    @Override
    public void reload(){
        super.reload();
        this.loadMessages();
    }


    private void loadMessages() {
        messages = new HashMap<>();
        levelUPMessage = getConfig().getStringList("levelUPMessage");
        for (String key : getConfig().getConfigurationSection("messages").getKeys(false))
            messages.put(key, StringUtils.color(getConfig().getString("messages." + key)));
    }

    public String getMessage(String key) {
        return messages.get(key);
    }
}
