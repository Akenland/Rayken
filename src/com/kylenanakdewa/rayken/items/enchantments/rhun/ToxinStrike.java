package com.kylenanakdewa.rayken.items.enchantments.rhun;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.InflictEffectMagicEnchantment;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Poisons target.
 * 
 * @author Kyle Nanakdewa
 */
public class ToxinStrike extends InflictEffectMagicEnchantment {

    public ToxinStrike(int level) {
        super("Toxin Strike", Branch.RHUN, level, new PotionEffect(PotionEffectType.POISON, level*40, (int)Math.floor(level*0.2)));
    }

    @Override
    public double getWeight() {
        return 5 / Math.floor(25/Math.max(1, Math.pow(2, effect.getAmplifier()))) * (effect.getDuration()/10);
    }

}