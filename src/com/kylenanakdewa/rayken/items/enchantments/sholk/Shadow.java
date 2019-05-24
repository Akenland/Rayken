package com.kylenanakdewa.rayken.items.enchantments.sholk;

import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.items.enchantments.DamageMagicEnchantment;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Shadow Sholk Enchantment
 *
 * Turn invisible on successful hits.
 *
 * @author Kyle Nanakdewa
 */
public class Shadow extends DamageMagicEnchantment {

    public Shadow(int level) {
        super("Shadow", Branch.SHOLK, level);
    }

    @Override
    protected double getNewDamage(EntityDamageByEntityEvent event, ItemStack item) {
        int duration = (int)Math.round(level * event.getDamage());

        LivingEntity attacker = (LivingEntity)event.getDamager();
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 0));

        return event.getDamage();
    }

    @Override
    public double getWeight() {
        return level*2;
    }

}