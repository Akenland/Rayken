package com.kylenanakdewa.rayken.items.enchantments.ebori;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * ConstructListener
 * @author Kyle Nanakdewa
 */
public final class ConstructListener implements Listener {

    /** The projectiles that should place a block. */
    private final static Set<Projectile> projectiles = new HashSet<Projectile>();

    /** Adds a projectile that was fired from a Construct bow. */
    static void addProjectile(Projectile projectile){
        projectiles.add(projectile);
    }

    
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onHit(ProjectileHitEvent event){
        if(event.getHitBlock()==null || !projectiles.remove(event.getEntity())) return;

        // Get the block the arrow is in
        Block block = event.getEntity().getLocation().getBlock();
        if(!block.getType().equals(Material.AIR)) return;

        // Get the block in the shooter's off hand
        ItemStack offHand = ((HumanEntity)event.getEntity().getShooter()).getInventory().getItemInOffHand();
        if(offHand==null || !offHand.getType().isBlock()) return;

        // Set the block
        block.setType(offHand.getType());
        block.setData(offHand.getData().getData());

        // Reduce the amount in shooter's off hand
        offHand.setAmount(offHand.getAmount()-1);
        ((HumanEntity)event.getEntity().getShooter()).getInventory().setItemInOffHand(offHand);
    }

}