package com.kylenanakdewa.rayken.items.enchantments;

import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.Branch;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * A Magic Enchantment that affects the damage of an attack.
 * @author Kyle Nanakdewa
 */
public abstract class DamageMagicEnchantment extends MagicEnchantment {

    protected DamageMagicEnchantment(String id, String name, Branch branch, int level) {
        super(id, name, branch, level);
    }


    @Override
    protected boolean preUseCheck(Event event, ItemStack item) {
        // This enchantment only applies to events where an entity is damaged by another entity.
        return event instanceof EntityDamageByEntityEvent;
    }

    @Override
    protected void action(Event event, ItemStack item) {
        EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent)event;
        damageEvent.setDamage(getNewDamage(damageEvent, item));

        Utils.notifyAdmins(name+" changed damage from "+damageEvent.getDamage()+" to "+getNewDamage(damageEvent, item));
    }


    /**
     * Gets the new damage, after the effects of this enchantment.
     * Use event.getDamage() to get the damage before this enchantment is processed.
     * @param event the damage event
     */
    protected abstract double getNewDamage(EntityDamageByEntityEvent event, ItemStack item);

}