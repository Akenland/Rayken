package com.kylenanakdewa.rayken.items.enchantments.warg;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Pack Ranger Warg Enchantment
 *
 * Weapon damage increases when attacking a wolf's target.
 *
 * @author Kyle Nanakdewa
 */
public class PackRanger extends DamageMagicEnchantment {

    public PackRanger(int level) {
        super("Pack Ranger", Branch.WARG, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        double damage = event.getDamage();

        Entity target = event.getEntity();
        int wolfCount = 0;

        for(Entity entity : event.getEntity().getNearbyEntities(16, 4, 16)){
            if(entity instanceof Wolf){
                Wolf wolf = (Wolf)entity;
                if(wolf.getTarget().equals(target)) wolfCount++;
            }
        }

        // 10% extra damage per wolf, multiplied by level
        double addedDamage = level * wolfCount * 0.1;

        return damage + addedDamage;
    }

    @Override
    public double getWeight() {
        return level*4;
    }

}