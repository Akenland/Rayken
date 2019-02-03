package com.kylenanakdewa.rayken.items.enchantments.ebori;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Levitate Ebori Enchantment
 * 
 * Weapon damage increases when airborne.
 * 
 * @author Kyle Nanakdewa
 */
public class Levitate extends DamageMagicEnchantment {

    public Levitate(int level) {
        super("LEVITATE", "Levitate", Branch.EBORI, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        if(event.getEntity() instanceof LivingEntity){
            LivingEntity entity = (LivingEntity)event.getEntity();
            entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, ((level-1)*3+5)*20, (int)Math.floor(level * 0.2)));
        }

        return event.getDamage();
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}