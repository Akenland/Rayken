package com.kylenanakdewa.rayken.items.enchantments.ebori;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.InflictEffectMagicEnchantment;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Levitate Ebori Enchantment
 * 
 * Targets briefly levitate, before falling.
 * 
 * @author Kyle Nanakdewa
 */
public class Levitate extends InflictEffectMagicEnchantment {

    public Levitate(int level) {
        super("Levitate", Branch.EBORI, level, new PotionEffect(PotionEffectType.LEVITATION, ((level-1)*3+5)*20, (int)Math.floor(level * 0.2)));
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}