package com.kylenanakdewa.rayken.items.enchantments.ebori;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.MagicPlugin;
import com.kylenanakdewa.rayken.items.enchantments.MagicEnchantment;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * A Magic Enchantment that places a block when a fired arrow hits a block face.
 *
 * @author Kyle Nanakdewa
 */
public class Rainmaker extends MagicEnchantment {

    public Rainmaker(int level) {
        super("Rainmaker", Branch.EBORI, level);
    }

    @Override
    protected boolean preUseCheck(Event event, ItemStack item) {
        // This enchantment only applies to a bow, has random chance
        return event instanceof EntityShootBowEvent;
    }

    @Override
    protected void action(Event event, ItemStack item) {
        EntityShootBowEvent shootEvent = (EntityShootBowEvent) event;
        PlayerInventory inv = ((HumanEntity)shootEvent.getEntity()).getInventory();

        for(int i=1; i<level+1; i++){
            Bukkit.getScheduler().scheduleSyncDelayedTask(MagicPlugin.plugin, () -> {
                if(reduceItemCount(Material.ARROW, inv))
                    shootEvent.getEntity().launchProjectile(Arrow.class);
            }, i*10);
        }
    }

    private static boolean reduceItemCount(Material material, Inventory inventory){
        int index = inventory.first(material);
        if(index < 0) return false;

        ItemStack item = inventory.getItem(index);
        item.setAmount(item.getAmount()-1);
        return true;
    }

    @Override
    public double getWeight() {
        return level*8;
    }

}