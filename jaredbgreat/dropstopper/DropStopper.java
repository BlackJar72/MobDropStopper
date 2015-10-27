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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * This plugin will block drops, or alternately replace them.
 *
 * @author JaredBGreat (Jared Blackburn)
 */
public class DropStopper extends JavaPlugin implements Listener {
    private Set<Material> toRemove;
    private Map<Material, Material> replacements;
    
   private CommandHandler cmd;
    
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);      
        cmd = new CommandHandler(this);
        getCommand("mobdropstopper").setExecutor(cmd);
        configure();
    }
    
    
    void configure() {
        // FIXME: This exact same code previously worked but now doesn't
        //        (yes, same sever jar); either fix this or someone
        //        please fix Bukkit, Java, or the universe (to have a 
        //        consistent reality) -- I'm starting wonder which needs it.
        //
        //        Currently this leads to a NullPointerException from
        //        org.bukkit.configuration.file.YamlConfiguration.convertMapsToSections
        //        when the following line runs.  As a result, configs can't
        //        load and for practical purposes nothing saved.
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
    public void onDisable() {
        
    }
    
    
    @EventHandler
    public void onLootDropped(EntityDeathEvent event) {
        if(event.getEntity() instanceof Player) {
            return;
        } else if(event.getEntity() instanceof LivingEntity) {
            List<ItemStack> drops    = event.getDrops();
            List<ItemStack> removals = new ArrayList<ItemStack>();
            for(ItemStack drop : drops) {  
                Material item = drop.getType();
                if((replacements.containsKey(item))) {
                    drop.setType(replacements.get(item));
                }
                if(toRemove.contains(drop.getType())) {
                    removals.add(drop);
                }
            }
            drops.removeAll(removals);
        }
    }
    
    
    boolean addRemoval(String item) {
        Material toAdd = Material.getMaterial(item);
        if(toAdd == null) {
            return false;
        }
        toRemove.add(toAdd);
        return true;
    }
    
    
    boolean addReplacement(String item1, String item2) {
        Material toReplace = Material.getMaterial(item1);
        Material replacement = Material.getMaterial(item2);
        if(toReplace == null || replacement == null) {
            return false;
        }
        replacements.put(toReplace, replacement);
        return true;
    }
    
    
    boolean allowDrop(String item) {
        Material toAllow = Material.getMaterial(item);
        if(toAllow == null) {
            return false;
        }
        toRemove.remove(toAllow);
        replacements.put(toAllow, toAllow);
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
            Logger.getLogger(DropStopper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
