package com.kylenanakdewa.rayken.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.items.enchantments.MagicEnchantment;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/**
 * EnchantmentUtils
 */
public final class EnchantmentUtils {

    private static final Map<String,String> ENCHANTMENT_FRIENDLY_NAMES;
    static {
        Map<String,String> map = new HashMap<String,String>();
        map.put("ARROW_DAMAGE", "Power");
        map.put("ARROW_FIRE", "Flame");
        map.put("ARROW_INFINITE", "Infinity");
        map.put("ARROW_KNOCKBACK", "Punch");
        map.put("BINDING_CURSE", ChatColor.RED+"Curse of Binding");
        map.put("CHANNELING", "Channeling");
        map.put("DAMAGE_ALL", "Sharpness");
        map.put("DAMAGE_ARTHROPODS", "Bane of Arthropods");
        map.put("DAMAGE_UNDEAD", "Smite");
        map.put("DEPTH_STRIDER", "Depth Strider");
        map.put("DIG_SPEED", "Efficiency");
        map.put("DURABILITY", "Unbreaking");
        map.put("FIRE_ASPECT", "Fire Aspect");
        map.put("FROST_WALKER", "Frost Walker");
        map.put("IMPALING", "Impaling");
        map.put("KNOCKBACK", "Knockback");
        map.put("LOOT_BONUS_BLOCKS", "Fortune");
        map.put("LOOT_BONUS_MOBS", "Looting");
        map.put("LOYALTY", "Loyalty");
        map.put("LUCK", "Luck");
        map.put("LURE", "Lure");
        map.put("MENDING", "Mending");
        map.put("OXYGEN", "Respiration");
        map.put("PROTECTION_ENVIRONMENTAL", "Protection");
        map.put("PROTECTION_EXPLOSIONS", "Blast Protection");
        map.put("PROTECTION_FALL", "Feather Falling");
        map.put("PROTECTION_FIRE", "Fire Protection");
        map.put("PROJECTILE_PROTECTION", "Projectile Protection");
        map.put("RIPTIDE", "Riptide");
        map.put("SILK_TOUCH", "Silk Touch");
        map.put("SWEEPING_EDGE", "Sweeping Edge");
        map.put("THORNS", "Thorns");
        map.put("VANISHING_CURSE", ChatColor.RED+"Curse of Vanishing");
        map.put("WATER_WORKER", "Aqua Affinity");
        ENCHANTMENT_FRIENDLY_NAMES = Collections.unmodifiableMap(map);
    }

    private static final Map<Integer,String> LEVEL_NUMERALS;
    static {
        Map<Integer,String> map = new HashMap<Integer,String>();
        map.put(1, "I");
        map.put(2, "II");
        map.put(3, "III");
        map.put(4, "IV");
        map.put(5, "V");
        map.put(6, "VI");
        map.put(7, "VII");
        map.put(8, "VIII");
        map.put(9, "IX");
        map.put(10, "X");
        LEVEL_NUMERALS = Collections.unmodifiableMap(map);
    }

    private static final Map<Material,Double> ITEM_BASE_LEVELS;
    static {
        Map<Material,Double> map = new HashMap<Material,Double>();

        // Weapons
        map.put(Material.WOOD_SWORD, 4.0);
        map.put(Material.WOOD_SPADE, 2.5);
        map.put(Material.WOOD_PICKAXE, 2.0);
        map.put(Material.WOOD_AXE, 7.0);

        map.put(Material.STONE_SWORD, 5.0);
        map.put(Material.STONE_SPADE, 3.5);
        map.put(Material.STONE_PICKAXE, 3.0);
        map.put(Material.STONE_AXE, 9.0);

        map.put(Material.IRON_SWORD, 6.0);
        map.put(Material.IRON_SPADE, 4.5);
        map.put(Material.IRON_PICKAXE, 4.0);
        map.put(Material.IRON_AXE, 9.0);

        map.put(Material.GOLD_SWORD, 4.0);
        map.put(Material.GOLD_SPADE, 2.5);
        map.put(Material.GOLD_PICKAXE, 2.0);
        map.put(Material.GOLD_AXE, 7.0);

        map.put(Material.DIAMOND_SWORD, 7.0);
        map.put(Material.DIAMOND_SPADE, 5.5);
        map.put(Material.DIAMOND_PICKAXE, 5.0);
        map.put(Material.DIAMOND_AXE, 9.0);

        map.put(Material.BOW, 9.0);

        // Armor
        map.put(Material.LEATHER_HELMET, 1.0);
        map.put(Material.LEATHER_CHESTPLATE, 3.0);
        map.put(Material.LEATHER_LEGGINGS, 2.0);
        map.put(Material.LEATHER_BOOTS, 1.0);

        map.put(Material.GOLD_HELMET, 2.0);
        map.put(Material.GOLD_CHESTPLATE, 5.0);
        map.put(Material.GOLD_LEGGINGS, 3.0);
        map.put(Material.GOLD_BOOTS, 1.0);

        map.put(Material.CHAINMAIL_HELMET, 2.0);
        map.put(Material.CHAINMAIL_CHESTPLATE, 5.0);
        map.put(Material.CHAINMAIL_LEGGINGS, 4.0);
        map.put(Material.CHAINMAIL_BOOTS, 1.0);

        map.put(Material.IRON_HELMET, 2.0);
        map.put(Material.IRON_CHESTPLATE, 6.0);
        map.put(Material.IRON_LEGGINGS, 5.0);
        map.put(Material.IRON_BOOTS, 2.0);

        map.put(Material.DIAMOND_HELMET, 3.0);
        map.put(Material.DIAMOND_CHESTPLATE, 8.0);
        map.put(Material.DIAMOND_LEGGINGS, 6.0);
        map.put(Material.DIAMOND_BOOTS, 3.0);

        ITEM_BASE_LEVELS = Collections.unmodifiableMap(map);
    }

    /**
     * Gets the friendly name for a vanilla enchantment.
     */
    public static String getEnchantmentFriendlyName(Enchantment enchantment, int level){
        String enchantmentName = ENCHANTMENT_FRIENDLY_NAMES.get(enchantment.getName());
        if(enchantmentName==null) enchantmentName = enchantment.getName();

        String levelString = LEVEL_NUMERALS.get(level);
        if(levelString==null) levelString = level+"";
        levelString = (level==1 && enchantment.getMaxLevel()==1) ? "" : " "+levelString;

        Utils.notifyAdmins(ChatColor.GRAY+enchantmentName+levelString);
        return ChatColor.GRAY+enchantmentName+levelString;
    }

    /**
     * Gets the friendly name for a custom enchantment.
     */
    public static String getEnchantmentFriendlyName(MagicEnchantment enchantment){
        String levelString = LEVEL_NUMERALS.get(enchantment.getLevel());
        if(levelString==null) levelString = enchantment.getLevel()+"";
        levelString = " "+enchantment.getBranch().getColor()+levelString;

        return ChatColor.GRAY+enchantment.getName()+levelString;
    }

    /**
     * Gets the base level (attack damage or armour points) for a material.
     */
    public static double getMaterialBaseLevel(Material material){
        return ITEM_BASE_LEVELS.containsKey(material) ? ITEM_BASE_LEVELS.get(material) : 0;
    }

    /**
     * Gets the weight of a vanilla enchantment.
     */
    public static double getEnchantmentWeight(Enchantment enchantment, int level){
        //// BOW
        // Power
        if(enchantment.getName().equals("ARROW_DAMAGE")){
            return Math.round(0.25*(1+level)*9);
        }
        // Flame
        if(enchantment.getName().equals("ARROW_FIRE")){
            return 4;
        }
        // Infinity
        if(enchantment.getName().equals("ARROW_INFINITE")){
            return 1;
        }
        // Punch
        if(enchantment.getName().equals("ARROW_KNOCKBACK")){
            return level*3;
        }

        //// MELEE
        // Sharpness
        if(enchantment.getName().equals("DAMAGE_ALL")){
            return 1+0.5*(level-1);
        }
        // Bane of Arthropods, Smite, Looting, Sweeping Edge
        if(enchantment.getName().equals("DAMAGE_ARTHROPODS") || enchantment.getName().equals("DAMAGE_UNDEAD") || enchantment.getName().equals("LOOT_BONUS_MOBS") || enchantment.getName().equals("SWEEPING_EDGE")){
            return level;
        }
        // Fire Aspect
        if(enchantment.getName().equals("FIRE_ASPECT")){
            return (4*level)-1;
        }
        // Knockback
        if(enchantment.getName().equals("KNOCKBACK")){
            return level*3;
        }

        //// TOOLS
        // Efficiency
        if(enchantment.getName().equals("DIG_SPEED") || enchantment.getName().equals("SILK_TOUCH")){
            return level;
        }
        // Fortune
        if(enchantment.getName().equals("LOOT_BONUS_BLOCKS")){
            return level*2;
        }


        //// ARMOUR
        // Protection
        if(enchantment.getName().equals("PROTECTION_ENVIRONMENTAL") 
        || enchantment.getName().equals("PROTECTION_EXPLOSIONS") 
        || enchantment.getName().equals("PROTECTION_PROJECTILE") 
        || enchantment.getName().equals("PROTECTION_FIRE") 
        || enchantment.getName().equals("PROTECTION_FALL") 
        || enchantment.getName().equals("OXYGEN")){
            return level*2;
        }
        // Aqua Affinity and Respiration
        if(enchantment.getName().equals("WATER_WORKER")){
            return 2;
        }
        // Frost Walker
        if(enchantment.getName().equals("FROST_WALKER")){
            return level*2+2;
        }
        // Depth Strider and Thorns
        if(enchantment.getName().equals("DEPTH_STRIDER") || enchantment.getName().equals("THORNS")){
            return level*3;
        }

        return 0;
    }
}