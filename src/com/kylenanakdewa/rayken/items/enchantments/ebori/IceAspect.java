package com.kylenanakdewa.rayken.items.enchantments.ebori;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.InflictEffectMagicEnchantment;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Ice Aspect Ebori Enchantment
 * 
 * Slows target.
 * 
 * @author Kyle Nanakdewa
 */
public class IceAspect extends InflictEffectMagicEnchantment {

    public IceAspect(int level) {
        super("Ice Aspect", Branch.EBORI, level, new PotionEffect(PotionEffectType.SLOW, level*40, (int)Math.floor(level*0.2)));
    }

    @Override
    public double getWeight() {
        return level;
    }

}