package com.kylenanakdewa.rayken.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.kylenanakdewa.core.common.CommonColors;
import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.Branch;
import com.kylenanakdewa.rayken.MagicUser;
import com.kylenanakdewa.rayken.items.enchantments.MagicEnchantment;
import com.kylenanakdewa.rayken.utils.EnchantmentUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * CustomItem
 * @author Kyle Nanakdewa
 */
public class CustomItem {

    /** The ItemStack holding this custom item. */
    private final ItemStack itemStack;
    /** The item metadata. */
    private final ItemMeta itemMeta;

    /** The item description. */
    private List<String> description;
    /** The item's crafter. */
    private String crafter;
    /** The Magic Enchantments on this item. */
    private Set<MagicEnchantment> enchantments;

    public CustomItem(ItemStack item) {
        itemStack = item;

        // Set up item metadata
        if (itemStack.hasItemMeta())
            itemMeta = itemStack.getItemMeta();
        else {
            itemMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            itemStack.setItemMeta(itemMeta);
        }

        // Get description, enchantments, level
        description = getDescription();
        crafter = getCrafter();
        enchantments = getMagicEnchantments();
    }


    /**
     * Gets the Bukkit ItemStack.
     */
    public ItemStack getItem() {
        return itemStack;
    }

    /**
     * Gets the Bukkit ItemMeta.
     */
    public ItemMeta getItemMeta() {
        return itemMeta;
    }


    /**
     * Gets the name of this item. Will attempt to return the display name, then the
     * localized name, then the material name.
     */
    public String getName() {
        return itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : itemMeta.hasLocalizedName() ? itemMeta.getLocalizedName() : itemStack.getType().toString();
    }

    /**
     * Sets the name of this item. This sets the display name, as would be set at an
     * anvil. Books use seperate data for their titles.
     */
    public void setName(String name) {
        itemMeta.setDisplayName(name);
    }

    /**
     * Updates the lore of the item. Content is ordered: Enchantment, Description,
     * Crafter, Level. If vanilla enchants are hidden, description will come first.
     */
    public void updateLore() {
        List<String> lore = new ArrayList<String>();
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
            lore.addAll(description);
            if(!description.isEmpty()) lore.add(ChatColor.RESET.toString()+ChatColor.RESET);
            lore.addAll(getEnchantmentStrings(true));
            if(!getEnchantmentStrings(true).isEmpty()) lore.add(ChatColor.RESET.toString()+ChatColor.RESET);
        } else {
            lore.addAll(getEnchantmentStrings(false));
            lore.add(ChatColor.RESET.toString()+ChatColor.RESET);
            lore.addAll(description);
            if(!description.isEmpty()) lore.add(ChatColor.RESET.toString()+ChatColor.RESET);
        }
        if(crafter!=null){
            lore.add(crafter);
            lore.add(ChatColor.RESET.toString()+ChatColor.RESET);
        }
        lore.add(getItemLevel().getLevelString());

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Gets the description of this item. The description is displayed at the top of
     * the lore section, above enchantments.
     * 
     * @return the description, or an empty list if there is no description
     */
    public List<String> getDescription() {
        if(!itemMeta.hasLore()) return new ArrayList<String>();

        List<String> desc = new ArrayList<String>(itemMeta.getLore());
        desc.removeIf(line -> line.length() > 2 && line.startsWith(ChatColor.RESET.toString()));

        return desc;
    }

    /**
     * Sets the description of this item.
     */
    public void setDescription(List<String> description) {
        this.description = description;
        updateLore();
    }

    /**
     * Gets the magic enchantments on this item.
     */
    public Set<MagicEnchantment> getMagicEnchantments() {
        if(enchantments!=null) return enchantments;

        Set<MagicEnchantment> enchantments = new HashSet<MagicEnchantment>();
        if(!itemMeta.hasLore()) return enchantments;

        for (String line : itemMeta.getLore()) {
            if(line.length()>2 && line.startsWith(ChatColor.RESET + ChatColor.LIGHT_PURPLE.toString() + ChatColor.GRAY)) {
                MagicEnchantment enchant = MagicEnchantment.getByFriendlyName(line);
                if(enchant!=null) enchantments.add(enchant);
            }
        }
        return enchantments;
    }

    /**
     * Adds a magic enchantment to this item.
     */
    public void addMagicEnchantment(MagicEnchantment enchantment) {
        // Remove existing enchantment
        enchantments.removeIf(ench -> ench.getName().equalsIgnoreCase(enchantment.getName()));

        if(enchantment.getLevel() > 0) enchantments.add(enchantment);
        updateLore();
    }

    /**
     * Removes a magic enchantment from this item.
     */
    public void removeMagicEnchantment(MagicEnchantment enchantment) {
        enchantments.remove(enchantment);
        updateLore();
    }

    /**
     * Gets a string list of enchantments.
     */
    public List<String> getEnchantmentStrings(boolean includeVanilla) {
        List<String> enchants = new ArrayList<String>();

        if(includeVanilla){
            for(Entry<Enchantment,Integer> ench : itemStack.getEnchantments().entrySet())
                enchants.add(ChatColor.RESET + EnchantmentUtils.getEnchantmentFriendlyName(ench.getKey(), ench.getValue()));
        }

        for(MagicEnchantment ench : enchantments)
            enchants.add(ChatColor.RESET+ChatColor.LIGHT_PURPLE.toString()+ChatColor.GRAY + EnchantmentUtils.getEnchantmentFriendlyName(ench));

        return enchants;
    }

    /**
     * Gets the crafter of this item.
     * @return the crafter's name, or null if not set
     */
    public String getCrafter() {
        if(!itemMeta.hasLore()) return null;
        for(String line : itemMeta.getLore()){
            if(line.length() > 2 && line.startsWith(ChatColor.RESET + ChatColor.GOLD.toString() + ChatColor.GRAY)){
                return line.split("Crafted by ")[1];
            }
        }
        return null;
    }

    /**
     * Sets the crafter of this item.
     */
    public void setCrafter(String crafter) {
        this.crafter = ChatColor.RESET + ChatColor.GOLD.toString() + ChatColor.GRAY + ChatColor.ITALIC + "Crafted by " + crafter;
        updateLore();
    }

    /**
     * Item level.
     */
    public class ItemLevel {
        private double mainLevel;
        private Map<Branch,Integer> branchLevels;
        private ItemLevel() {
            // Base item level
            mainLevel = EnchantmentUtils.getMaterialBaseLevel(itemStack.getType());

            // Vanilla Enchantment weights
            for(Entry<Enchantment, Integer> ench : itemStack.getEnchantments().entrySet()){
                mainLevel += EnchantmentUtils.getEnchantmentWeight(ench.getKey(), ench.getValue());
            }

            // Magic Enchantment weights
            branchLevels = new HashMap<Branch,Integer>();
            for(Branch branch : Branch.values()) branchLevels.put(branch, 0);
            for(MagicEnchantment ench : CustomItem.this.getMagicEnchantments()){
                int weight = (int)Math.ceil(ench.getWeight());
                mainLevel += weight;
                branchLevels.put(ench.getBranch(), branchLevels.get(ench.getBranch()) + weight);
            }
        }

        /** Gets the main level of this item. */
        public int getMainLevel() {
            return (int)Math.ceil(mainLevel);
        }
        /** Gets the level of the specified branch. */
        public int getBranchLevel(Branch branch) {
            return branchLevels.get(branch);
        }
        /** Gets the total levels (weight) of the magic branches. */
        public int getTotalBranchLevels() {
            int levels = 0;
            for(int level : branchLevels.values()) levels += level;
            return levels;
        }
        /** Gets the levels as a single string. */
        public String getLevelString() {
            String string = ChatColor.RESET+ChatColor.DARK_GRAY.toString()+"Level "+getMainLevel()+" ";
            for(Branch branch : Branch.values()) {
                if(branchLevels.get(branch) > 0) string += branch.getColor().toString() + branchLevels.get(branch) + " ";
            }
            return string;
        }
    }
    /**
     * Gets the item level.
     */
    public ItemLevel getItemLevel(){
        return new ItemLevel();
    }

    /**
     * Triggers all magic enchantments on this item.
     */
    public void triggerEnchantments(Event event){
        // Check player levels
        if(event instanceof PlayerEvent || (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager().getType().equals(EntityType.PLAYER))){
            Player player = event instanceof PlayerEvent ? ((PlayerEvent)event).getPlayer() : (Player)((EntityDamageByEntityEvent) event).getDamager();
            MagicUser magicUser =  new MagicUser(player);
            ItemLevel levels = getItemLevel();
            boolean playerLevelTooLow = false;

            if(magicUser.getLevelWarg() >= levels.branchLevels.get(Branch.WARG)) enchantments.forEach(ench -> {
                if(ench.getBranch().equals(Branch.WARG)) ench.use(event, itemStack);
            }); else playerLevelTooLow = true;
            if(magicUser.getLevelEbori() >= levels.branchLevels.get(Branch.EBORI)) enchantments.forEach(ench -> {
                if(ench.getBranch().equals(Branch.EBORI)) ench.use(event, itemStack);
            }); else playerLevelTooLow = true;
            if(magicUser.getLevelSholk() >= levels.branchLevels.get(Branch.SHOLK)) enchantments.forEach(ench -> {
                if(ench.getBranch().equals(Branch.SHOLK)) ench.use(event, itemStack);
            }); else playerLevelTooLow = true;
            if(magicUser.getLevelRhun() >= levels.branchLevels.get(Branch.RHUN)) enchantments.forEach(ench -> {
                if(ench.getBranch().equals(Branch.RHUN)) ench.use(event, itemStack);
            }); else playerLevelTooLow = true;

            if(playerLevelTooLow){
                Utils.sendActionBar(player, ChatColor.RED+"You don't understand this item's magic!");
                Utils.notifyAdmins(player.getDisplayName()+CommonColors.INFO+" does not have a high enough level to use "+getName());
            }
        }
        else enchantments.forEach(ench -> ench.use(event, itemStack));
    }
}