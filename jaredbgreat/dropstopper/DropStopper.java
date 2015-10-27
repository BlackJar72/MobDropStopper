package jaredbgreat.dropstopper;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author jared
 */
public class DropStopper implements Listener {
    MobDropStopper owner;
    
    
    DropStopper(MobDropStopper owner) {
        this.owner = owner;
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
                if((owner.replacements.containsKey(item))) {
                    drop.setType(owner.replacements.get(item));
                }
                if(owner.toRemove.contains(drop.getType())) {
                    removals.add(drop);
                }
            }
            drops.removeAll(removals);
        }
    }
}
