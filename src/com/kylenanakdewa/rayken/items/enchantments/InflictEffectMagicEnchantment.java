package com.kylenanakdewa.rayken.items.enchantments;

import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.Branch;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * A Magic Enchantment that inflicts a PotionEffect on a hit target.
 * @author Kyle Nanakdewa
 */
public abstract class InflictEffectMagicEnchantment extends MagicEnchantment {

    /** The potion effect to be inflicted. */
    protected final PotionEffect effect;

    protected InflictEffectMagicEnchantment(String name, Branch branch, int level, PotionEffect effect) {
        super(name, branch, level);
        this.effect = effect;
    }


    @Override
    protected boolean preUseCheck(Event event, ItemStack item) {
        // This enchantment only applies to events where an entity is damaged by another entity.
        return event instanceof EntityDamageByEntityEvent;
    }

    @Override
    protected void action(Event event, ItemStack item) {
        EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent)event;
        if(!(damageEvent.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity)damageEvent.getEntity();
        entity.addPotionEffect(effect);

        Utils.notifyAdmins(name+" "+level+" (weight "+getWeight()+") inflicted "+effect.getType().getName()+" "+(effect.getAmplifier()+1)+" for "+effect.getDuration()+" ticks");
    }

}