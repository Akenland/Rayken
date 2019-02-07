package com.kylenanakdewa.rayken.items.enchantments.ebori;

import java.util.Random;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.MagicEnchantment;

import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A Magic Enchantment that places a block when a fired arrow hits a block face.
 * 
 * @author Kyle Nanakdewa
 */
public class Construct extends MagicEnchantment {

    public Construct(int level) {
        super("Construct", Branch.EBORI, level);
    }

    @Override
    protected boolean preUseCheck(Event event, ItemStack item) {
        // This enchantment only applies to a bow, has random chance
        return event instanceof EntityShootBowEvent && new Random().nextDouble()<level/20 && ((EntityShootBowEvent)event).getEntity().hasPermission("rayken.unsafe");
    }

    @Override
    protected void action(Event event, ItemStack item) {
        EntityShootBowEvent shootEvent = (EntityShootBowEvent) event;

        ConstructListener.addProjectile((Projectile)shootEvent.getProjectile());
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}