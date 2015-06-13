package jaredbgreat.dropstopper;


import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
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
    
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        toRemove = EnumSet.noneOf(Material.class);
        replacements = new EnumMap<Material, Material>(Material.class);
        List<String> items = getConfig().getStringList("remove");
        if(items != null && !items.isEmpty()) {
            for(String item : items) {
                if(!item.equalsIgnoreCase("null") && !item.trim().isEmpty()) {
                    toRemove.add(Material.matchMaterial(item));
                }
            }
        }
        ConfigurationSection replaceConfig 
                = getConfig().getConfigurationSection("replace");
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
