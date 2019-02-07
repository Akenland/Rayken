package com.kylenanakdewa.rayken.items.enchantments.sholk;

import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.MagicEnchantment;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Attacker gains health on successful hits.
 * 
 * @author Kyle Nanakdewa
 */
public class Life extends MagicEnchantment {

    protected Life(int level) {
        super("Life", Branch.SHOLK, level);
    }


    @Override
    protected boolean preUseCheck(Event event, ItemStack item) {
        // This enchantment only applies to events where an entity is damaged by another entity.
        return event instanceof EntityDamageByEntityEvent;
    }

    @Override
    protected void action(Event event, ItemStack item) {
        EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent)event;

        // 5% of damage, per level
        double healthGained = damageEvent.getDamage()*level*0.05;

        LivingEntity entity = (LivingEntity)damageEvent.getDamager();
        entity.setHealth(entity.getHealth()+healthGained);

        Utils.notifyAdmins(name+" "+level+" (weight "+getWeight()+") regained "+healthGained+" from "+damageEvent.getDamage()+" damage");
    }

    @Override
    public double getWeight() {
        return level*5;
    }

}