package jaredbgreat.dropstopper;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * This plugin will block drops, or alternately replace them.
 *
 * @author JaredBGreat (Jared Blackburn)
 */
public class MobDropStopper extends JavaPlugin  {
    Set<Material> toRemove;
    Map<Material, Material> replacements;
    
    private CommandHandler cmd;
    private DropStopper   doer;
    
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        doer = new DropStopper(this);
        cmd  = new CommandHandler(this);
        getServer().getPluginManager().registerEvents(doer, this);
        getCommand("mobdropstopper").setExecutor(cmd);
        configure();
    }
    
    
    void configure() {
        final FileConfiguration conf = getConfig();        
        toRemove = EnumSet.noneOf(Material.class);
        replacements = new EnumMap<Material, Material>(Material.class);
        List<String> items = conf.getStringList("remove");
        if(items != null && !items.isEmpty()) {
            for(String item : items) {
                if(!item.equalsIgnoreCase("null") && !item.trim().isEmpty()) {
                    toRemove.add(Material.matchMaterial(item));
                }
            }
        }
        ConfigurationSection replaceConfig 
                = conf.getConfigurationSection("replace");
        if(replaceConfig == null || replaceConfig.getKeys(false).isEmpty()) {
            return;
        }        
        for(String key : replaceConfig.getKeys(false)) {
            String replacement = replaceConfig.getString(key);
            replacements.put(Material.matchMaterial(key), Material.matchMaterial(replacement));
        }
    }
    
    
    @Override
    public void onDisable() {}
    
    
    boolean addRemoval(String item) {
        allowDrop(item);
        Material toAdd = Material.getMaterial(item.toUpperCase());
        if(toAdd == null) {
            return false;
        }
        toRemove.add(toAdd);
        return true;
    }
    
    
    boolean addReplacement(String item1, String item2) {
        allowDrop(item1);
        Material toReplace = Material.getMaterial(item1.toUpperCase());
        Material replacement = Material.getMaterial(item2.toUpperCase());
        if(toReplace == null || replacement == null) {
            return false;
        }
        replacements.put(toReplace, replacement);
        return true;
    }
    
    
    boolean allowDrop(String item) {
        Material toAllow = Material.getMaterial(item.toUpperCase());
        if(toAllow == null) {
            return false;
        }
        toRemove.remove(toAllow);
        replacements.remove(toAllow);
        return true;
    }
    
    
    private ArrayList<String> makeStringList() {
        ArrayList<String> out = new ArrayList<String>(toRemove.size());
        for(Material mat : toRemove) {
            out.add(mat.name());
        }
        return out;
    }
    
    
    private HashMap<String, String> makeStringMap() {
        HashMap<String, String> out = new HashMap<String, String>(replacements.size());
        for(Material mat : replacements.keySet()) {
            out.put(mat.name(), replacements.get(mat).name());
        }        
        return out;
    }
    
    
    void saveMyConfig() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("remove", makeStringList());
            config.set("replace", makeStringMap());
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            Logger.getLogger(MobDropStopper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // Not yet implemented -- or fully planned
    void loadMyConfig() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("remove", makeStringList());
            config.set("replace", makeStringMap());
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            Logger.getLogger(MobDropStopper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
