package com.kylenanakdewa.rayken.items.enchantments.warg;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Last Word Warg Enchantment
 * 
 * Weapon damage increases at low health.
 * 
 * @author Kyle Nanakdewa
 */
public class LastWord extends DamageMagicEnchantment {

    public LastWord(int level) {
        super("DAMAGE_LOWHEALTH", "Last Word", Branch.WARG, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        double damage = event.getDamage();
        double health = ((LivingEntity)event.getDamager()).getHealth();
        if(health>10) return damage;

        // Up to 2*level, reduced by health
        double addedDamage = level*2 - (health-1)*0.2;

        return event.getDamage()+addedDamage;
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}