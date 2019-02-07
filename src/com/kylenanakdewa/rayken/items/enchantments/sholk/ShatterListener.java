package com.kylenanakdewa.rayken.items.enchantments.sholk;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Shatter Listener
 * @author Kyle Nanakdewa
 */
public final class ShatterListener implements Listener {

    /** The projectiles that should break a block. */
    private final static Set<Projectile> projectiles = new HashSet<Projectile>();

    /** Adds a projectile that was fired from a Shatter bow. */
    static void addProjectile(Projectile projectile){
        projectiles.add(projectile);
    }

    
    @EventHandler
    public void onHit(ProjectileHitEvent event){
        if(event.getHitBlock()==null || !projectiles.remove(event.getEntity())) return;

        double chance;

        switch (event.getHitBlock().getType()) {
            case GLASS: case THIN_GLASS: case STAINED_GLASS: case STAINED_GLASS_PANE: case LEAVES: case LEAVES_2:
                chance = 0.8;
                break;
            case FENCE: case FENCE_GATE:
                chance = 0.5;
                break;
            default:
                chance = 0.2;
                break;
        }

        if(!(new Random().nextDouble()<chance)) event.getHitBlock().breakNaturally();

    }

}