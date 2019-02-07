package com.kylenanakdewa.rayken.items.enchantments.sholk;

import java.util.Random;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.MagicEnchantment;

import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A Magic Enchantment that breaks a block when a fired arrow hits it.
 * 
 * @author Kyle Nanakdewa
 */
public class Shatter extends MagicEnchantment {

    public Shatter(int level) {
        super("Shatter", Branch.SHOLK, level);
    }

    @Override
    protected boolean preUseCheck(Event event, ItemStack item) {
        // This enchantment only applies to a bow, has random chance
        return event instanceof EntityShootBowEvent && new Random().nextDouble()<level/10 && ((EntityShootBowEvent)event).getEntity().hasPermission("rayken.unsafe");
    }

    @Override
    protected void action(Event event, ItemStack item) {
        EntityShootBowEvent shootEvent = (EntityShootBowEvent) event;

        ShatterListener.addProjectile((Projectile)shootEvent.getProjectile());
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}