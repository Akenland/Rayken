package com.kylenanakdewa.rayken.items.enchantments.warg;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Charge Warg Enchantment
 * 
 * Weapon damage increases when sprinting.
 * 
 * @author Kyle Nanakdewa
 */
public class Charge extends DamageMagicEnchantment {

    public Charge(int level) {
        super("DAMAGE_SPRINTING", "Charge", Branch.WARG, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        double damage = event.getDamage();
        return ((Player)event.getDamager()).isSprinting() ? damage + level*2 : damage;
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}