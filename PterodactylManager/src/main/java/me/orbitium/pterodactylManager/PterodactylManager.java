package me.orbitium.pterodactylManager;

import me.orbitium.pterodactylManager.api.CommandSender;
import me.orbitium.pterodactylManager.api.PterodactylFileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class PterodactylManager extends JavaPlugin {

    private String sender;
    private String receiver;
    private String apiKey;
    //    public static PterodactylMain pterodactylMain;
    public PterodactylFileManager networkFileManager;
    public CommandSender commandSender;
    public static PterodactylManager instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfiguration config = getConfig();

        saveDefaultConfig();

        String apiKey = config.getString("api-key");
        String sender = config.getString("sender-id");
        String receiver = config.getString("receiver-id");

        if (apiKey == null) {
            getLogger().log(Level.SEVERE, "No API KEY found in the config!");
            return;
        }

        if (sender == null) {
            getLogger().log(Level.SEVERE, "No SENDER ID found in the config!");
            return;
        }

        if (receiver == null) {
            getLogger().log(Level.SEVERE, "No LOBBY ID found in the config!");
            return;
        }

        instance = this;

//        pterodactylMain = new PterodactylMain(sender, receiver, apiKey);
        this.apiKey = apiKey;
        this.sender = sender;
        this.receiver = receiver;
        networkFileManager = new PterodactylFileManager(apiKey, receiver);
        commandSender = new CommandSender();


        /*
        TODO: Lobby ve Battle arasındaki bağlantıyı sağla ki lobby onları sürekli yeniden başlatmasın.
         */
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getSender() {
        return sender;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getReceiver() {
        return receiver;
    }

    public PterodactylFileManager getNetworkFileManager() {
        return networkFileManager;
    }

    public static void prtNetworkError(String error) {
        instance.getLogger().log(Level.WARNING, error);
    }

    public static void prtInfo(String info) {
        instance.getLogger().log(Level.INFO, info);
    }
}
