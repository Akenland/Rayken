package com.kylenanakdewa.rayken.items;

import com.kylenanakdewa.core.common.CommonColors;
import com.kylenanakdewa.core.common.Utils;

import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

/**
 * CustomItemListener
 * @author Kyle Nanakdewa
 */
public class CustomItemListener implements Listener {

    /**
     * Determines if an item is a custom item.
     */
    public static boolean isCustomItem(ItemStack item){
        if(item.getItemMeta().hasDisplayName()) return true;
        if(item.getItemMeta().hasLore()) return true;
        if(item.getItemMeta().isUnbreakable()) return true;
        for(ItemFlag flag : ItemFlag.values()){
            if(item.getItemMeta().hasItemFlag(flag)) return true;
        }
        if(hasIllegalEnchantments(item)) return true;

        if(item.getItemMeta() instanceof PotionMeta && ((PotionMeta)item.getItemMeta()).hasCustomEffects()) return true;

        return false;
    }

    /**
     * Determines if an item has illegal enchantments.
     */
    public static boolean hasIllegalEnchantments(ItemStack item){
        for(int level : item.getEnchantments().values()){
            if(level>5) return true;
        }
        return false;
    }


    /**
     * Warns use of an illegal item.
     */
    public static boolean blockIllegalItem(Player player, ItemStack item){
        if(player.hasPermission("magic.itemoverride")) return false;

        if(hasIllegalEnchantments(item)){
            Utils.sendActionBar(player, CommonColors.ERROR+"You can't use this item!");
            player.sendMessage(CommonColors.INFO+"This item requires conversion before it can be used. Bring it to the Conversion Center at spawn.");
            String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString();
            Utils.notifyAdmins(player.getDisplayName()+CommonColors.INFO+" has an illegal item: "+itemName);
            return true;
        }
        return false;
    }
    /**
     * Warns use of an illegal item in hand.
     */
    public static boolean blockIllegalItem(Player player){
        ItemStack mainItem = player.getInventory().getItemInMainHand();
        ItemStack sideItem = player.getInventory().getItemInOffHand();

        return (blockIllegalItem(player, mainItem) || blockIllegalItem(player, sideItem));
    }


    /**
     * Prevents use of items with illegal enchantments.
     */
    @EventHandler
    public void blockIllegalAttacks(EntityDamageByEntityEvent event){
        if(event.isCancelled()) return;
        if(!event.getDamager().getType().equals(EntityType.PLAYER) 
        && !event.getCause().equals(DamageCause.ENTITY_ATTACK) 
        && !event.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK) 
        && !event.getCause().equals(DamageCause.PROJECTILE)) return;

        if(blockIllegalItem((Player)event.getDamager())) event.setCancelled(true);
    }
    @EventHandler
    public void blockIllegalBows(EntityShootBowEvent event){
        if(event.isCancelled()) return;
        if(!event.getEntity().getType().equals(EntityType.PLAYER)) return;

        if(blockIllegalItem((Player)event.getEntity(), event.getBow())) event.setCancelled(true);
    }
    @EventHandler
    public void blockIllegalInteract(PlayerInteractEvent event){
        if(event.isCancelled()) return;
        if(blockIllegalItem(event.getPlayer(), event.getItem())) event.setCancelled(true);
    }
    @EventHandler
    public void blockIllegalEquip(InventoryClickEvent event){
        if(event.isCancelled()) return;
        if(!event.getSlotType().equals(InventoryType.SlotType.ARMOR)) return;

        if(blockIllegalItem((Player)event.getWhoClicked(), event.getCursor())) event.setCancelled(true);
    }
    @EventHandler
    public void blockIllegalItemDispense(BlockDispenseEvent event){
        if(event.isCancelled()) return;
        if(hasIllegalEnchantments(event.getItem())) event.setCancelled(true);
    }
    @EventHandler
    public void blockIllegalStorage(InventoryMoveItemEvent event){
        if(event.isCancelled()) return;
        if(!event.getSource().getType().equals(InventoryType.PLAYER)) return;
        if(!event.getDestination().getType().equals(InventoryType.ENDER_CHEST) && !event.getDestination().getType().equals(InventoryType.SHULKER_BOX) && !isTARDISChest(event.getDestination())) return;

        if(blockIllegalItem((Player)event.getSource().getHolder(), event.getItem())) event.setCancelled(true);
    }
    @EventHandler
    public void blockIllegalDuplication(InventoryClickEvent event){
        if(event.isCancelled()) return;
        if(!event.getAction().equals(InventoryAction.CLONE_STACK)) return;

        if(blockIllegalItem((Player)event.getWhoClicked(), event.getCurrentItem())) event.setCancelled(true);

        else if(isCustomItem(event.getCurrentItem()) && !event.getWhoClicked().hasPermission("magic.itemoverride")) event.setCancelled(true);
    }
    @EventHandler
    public void blockIllegalItemConsume(PlayerItemConsumeEvent event){
        if(event.isCancelled()) return;
        if(isCustomItem(event.getItem())) event.setCancelled(true);
    }
    @EventHandler
    public void blockIllegalSplashPotion(PotionSplashEvent event){
        if(event.isCancelled()) return;
        if(isCustomItem(event.getPotion().getItem())) event.setCancelled(true);
    }


    /**
     * Checks if an inventory is in a TARDIS.
     */
    private boolean isTARDISChest(Inventory inventory){
        if(!(inventory.getHolder() instanceof Container)) return false;
        return ((Container)inventory.getHolder()).getWorld().getName().contains("TARDIS");
    }

}