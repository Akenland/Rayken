package com.kylenanakdewa.rayken.items.enchantments.warg;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Air Strike Warg Enchantment
 * 
 * Weapon damage increases when airborne.
 * 
 * @author Kyle Nanakdewa
 */
public class AirStrike extends DamageMagicEnchantment {

    public AirStrike(int level) {
        super("DAMAGE_AERIAL", "Air Strike", Branch.WARG, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        double damage = event.getDamage();
        Player player = (Player)event.getDamager();
        return !player.isOnGround() || player.isGliding() ? damage + level*4 : damage;
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}