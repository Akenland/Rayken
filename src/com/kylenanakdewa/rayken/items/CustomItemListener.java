package com.kylenanakdewa.rayken.items;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.kylenanakdewa.core.common.CommonColors;
import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.MagicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

/**
 * CustomItemListener
 * @author Kyle Nanakdewa
 */
public final class CustomItemListener implements Listener {

    /**
     * Determines if an item is a custom item.
     */
    public static boolean isCustomItem(ItemStack item){
        if(item.hasItemMeta()){
            //if(item.getItemMeta().hasDisplayName()) return true;
            if(item.getItemMeta().hasLore()) return true;
            if(item.getItemMeta().isUnbreakable()) return true;
            for(ItemFlag flag : ItemFlag.values()){
                if(item.getItemMeta().hasItemFlag(flag)) return true;
            }
            if(item.getItemMeta() instanceof PotionMeta && ((PotionMeta)item.getItemMeta()).hasCustomEffects()) return true;
        }

        if(hasIllegalEnchantments(item)) return true;

        return false;
    }

    /**
     * Determines if an item has illegal enchantments.
     */
    public static boolean hasIllegalEnchantments(ItemStack item){
        if(item.getType().equals(Material.COMMAND) || item.getType().equals(Material.COMMAND_CHAIN) || item.getType().equals(Material.COMMAND_REPEATING) || item.getType().equals(Material.BARRIER) || item.getType().equals(Material.BEDROCK)) return true;

        for(Entry<Enchantment, Integer> enchantment : item.getEnchantments().entrySet()){
            // If enchantment is over level 5, it is illegal
            if(enchantment.getValue()>5) return true;
            // If enchantment is higher than the max level, it is illegal
            if(enchantment.getValue() > enchantment.getKey().getMaxLevel()) return true;
            // If enchantment does not apply to this item, it is illegal
            if(!enchantment.getKey().getItemTarget().includes(item)) return true;
        }
        return false;
    }


    /** Custom Item cache. */
    private static final Map<ItemStack,CustomItem> customItemCache = new HashMap<ItemStack,CustomItem>();

    /**
     * Custom Item handler.
     */
    private static CustomItem getCustomItem(ItemStack item) {
        CustomItem custom = customItemCache.get(item);
        if(custom==null){
            custom = new CustomItem(item);
            customItemCache.put(item, custom);
        }
        return custom;
    }
    /**
     * Clear cache when server is empty.
     */
    @EventHandler
    public void clearCustomItemCache(PlayerQuitEvent event) {
        if(Bukkit.getOnlinePlayers().isEmpty()) customItemCache.clear();
    }
    /**
     * Custom Item interact handler.
     */
    @EventHandler
    public void customItemInteract(PlayerInteractEvent event) {
        if(event.isCancelled() || event.getAction().equals(Action.PHYSICAL) || !event.hasItem() || event.getItem()==null || event.getItem().getType().isBlock()) return;
        if(isCustomItem(event.getItem())){
            getCustomItem(event.getItem()).triggerEnchantments(event);
        }
    }
    @EventHandler
    public void customItemEnchant(EnchantItemEvent event) {
        if(event.isCancelled()) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(MagicPlugin.plugin, () -> getCustomItem(event.getItem()).setCrafter(event.getEnchanter().getDisplayName()));
    }
    @EventHandler
    public void customItemDamage(EntityDamageByEntityEvent event) {
        if(event.isCancelled() || !event.getDamager().getType().equals(EntityType.PLAYER)) return;

        Player player = (Player)event.getDamager();
        ItemStack mainItem = player.getInventory().getItemInMainHand();
        ItemStack sideItem = player.getInventory().getItemInOffHand();

        if(mainItem!=null && isCustomItem(mainItem)) Utils.sendActionBar(player, player.getDisplayName()+" used "+getCustomItem(mainItem).getName());
        if(sideItem!=null && isCustomItem(sideItem)) Utils.sendActionBar(player, player.getDisplayName()+" used "+getCustomItem(sideItem).getName());

        if(mainItem!=null && isCustomItem(mainItem)) getCustomItem(mainItem).triggerEnchantments(event);
        if(sideItem!=null && isCustomItem(sideItem)) getCustomItem(sideItem).triggerEnchantments(event);
    }


    /**
     * Warns admins of custom items.
     */
    public static boolean warnCustomItem(Player player, ItemStack item){
        if(player.hasPermission("magic.itemoverride") || item==null || !item.getType().isItem()) return false;

        if(isCustomItem(item)){
            player.sendMessage(CommonColors.INFO+"This item may require conversion. Bring it to the Conversion Center at spawn.");
            String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString();
            Utils.notifyAdmins(player.getDisplayName()+CommonColors.INFO+" has a custom item: "+itemName);
            return true;
        }
        return false;
    }


    @EventHandler
    public void warnItemPickup(EntityPickupItemEvent event){
        if(event.isCancelled()) return;
        if(!event.getEntity().getType().equals(EntityType.PLAYER)) return;

        warnCustomItem((Player)event.getEntity(), event.getItem().getItemStack());
    }
    @EventHandler
    public void warnItemMove(InventoryMoveItemEvent event){
        if(event.isCancelled()) return;
        if(!event.getSource().getType().equals(InventoryType.PLAYER)) return;

        warnCustomItem((Player)event.getSource().getHolder(), event.getItem());
    }


    /**
     * Warns use of an illegal item.
     */
    public static boolean blockIllegalItem(Player player, ItemStack item){
        if(player.hasPermission("magic.itemoverride") || item==null) return false;

        if(hasIllegalEnchantments(item)){
            Utils.sendActionBar(player, CommonColors.ERROR+"You can't use this item!");
            player.sendMessage(CommonColors.INFO+"This item requires conversion before it can be used. Bring it to the Conversion Center at spawn.");
            String itemName = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getType().toString();
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
        if(event.isCancelled() || !event.getDamager().getType().equals(EntityType.PLAYER)) return;
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
        if(event.isCancelled() || event.getAction().equals(Action.PHYSICAL)) return;
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
        blockIllegalItem(event.getPlayer(), event.getItem());
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