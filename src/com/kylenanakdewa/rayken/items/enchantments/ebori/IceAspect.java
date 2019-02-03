package com.kylenanakdewa.rayken.items.enchantments.ebori;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Ice Aspect Ebori Enchantment
 * 
 * Slows target.
 * 
 * @author Kyle Nanakdewa
 */
public class IceAspect extends DamageMagicEnchantment {

    public IceAspect(int level) {
        super("SLOWING", "Ice Aspect", Branch.EBORI, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        if(event.getEntity() instanceof LivingEntity){
            LivingEntity entity = (LivingEntity)event.getEntity();
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, level*40, (int)Math.floor(level*0.2)));
        }

        return event.getDamage();
    }

    @Override
    public double getWeight() {
        return level;
    }

}