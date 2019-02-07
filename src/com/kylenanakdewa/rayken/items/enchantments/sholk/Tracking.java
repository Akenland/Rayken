package com.kylenanakdewa.rayken.items.enchantments.sholk;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.InflictEffectMagicEnchantment;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Targets briefly glow.
 * 
 * @author Kyle Nanakdewa
 */
public class Tracking extends InflictEffectMagicEnchantment {

    public Tracking(int level) {
        super("Tracking", Branch.SHOLK, level, new PotionEffect(PotionEffectType.GLOWING, level*20, 0));
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}