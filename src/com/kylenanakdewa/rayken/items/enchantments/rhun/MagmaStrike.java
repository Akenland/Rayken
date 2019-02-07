package com.kylenanakdewa.rayken.items.enchantments.rhun;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Deals more damage to burning targets.
 * 
 * @author Kyle Nanakdewa
 */
public class MagmaStrike extends DamageMagicEnchantment {

    public MagmaStrike(int level) {
        super("Magma Strike", Branch.RHUN, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        double damage = event.getDamage();
        return event.getEntity().getFireTicks()>0 ? damage + (damage * level*0.05) : damage;
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}