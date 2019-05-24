package com.kylenanakdewa.rayken.items.enchantments.sholk;

import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Attacker gains health on successful hits.
 *
 * @author Kyle Nanakdewa
 */
public class Life extends DamageMagicEnchantment {

    public Life(int level) {
        super("Life", Branch.SHOLK, level);
    }


    @Override
    protected boolean preUseCheck(Event event, ItemStack item) {
        // This enchantment only applies to events where an entity is damaged by another entity.
        return event instanceof EntityDamageByEntityEvent;
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        // 10% of damage, per level
        double healthGained = event.getDamage() * level * 0.1;

        LivingEntity entity = (LivingEntity)event.getDamager();
        double newHealth = Math.min(entity.getHealth()+healthGained, entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        entity.setHealth(newHealth);

        Utils.notifyAdmins(name+" "+level+" (weight "+getWeight()+") regained "+healthGained+" from "+event.getDamage()+" damage");
        return event.getDamage();
    }

    @Override
    public double getWeight() {
        return level*5;
    }

}