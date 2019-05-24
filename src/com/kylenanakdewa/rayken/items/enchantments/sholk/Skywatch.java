package com.kylenanakdewa.rayken.items.enchantments.sholk;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Skywatch Sholk Enchantment
 *
 * Weapon damage increases against airborne targets.
 *
 * @author Kyle Nanakdewa
 */
public class Skywatch extends DamageMagicEnchantment {

    public Skywatch(int level) {
        super("Skywatch", Branch.SHOLK, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        double damage = event.getDamage();
        LivingEntity entity = (LivingEntity)event.getEntity();
        return isFalling(entity) ? damage * level : damage;
    }

    @Override
    public double getWeight() {
        return level*4;
    }

    private boolean isFalling(LivingEntity entity){
		// Entity must not be on ground
		return !entity.isOnGround()
		 // Entity must have air below them
		 && entity.getLocation().subtract(0, 1, 0).getBlock().isEmpty()
		 && entity.getLocation().subtract(0, 2, 0).getBlock().isEmpty()
		 // Entity must not be a player, or they must not be flying
		 && (!(entity instanceof Player) || !((Player)entity).isFlying());
    }
}