package com.kylenanakdewa.rayken.items.enchantments;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.Branch;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * A Magic Enchantment.
 * @author Kyle Nanakdewa
 */
public abstract class MagicEnchantment {

	/** All Magic Enchantments registered on this server. */
	private static final Map<String, Class<? extends MagicEnchantment>> enchantmentRegistry = new HashMap<String, Class<? extends MagicEnchantment>>();
	/** Magic Enchantments by friendly name. */
	private static final Map<String, String> enchantmentFriendlyMap = new HashMap<String, String>();

	/**
	 * Gets a Magic Enchantment by ID.
	 * @param id the unique identifier for the enchantment
	 * @param level the level to create
	 * @return a new enchantment instance, or null if the enchantment ID does not exist
	 */
	public static MagicEnchantment getById(String id, int level){
		id = id.toUpperCase().trim();
		if(!enchantmentRegistry.containsKey(id)) return null;

		try {
			return enchantmentRegistry.get(id).getConstructor(int.class).newInstance(level);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Gets a Magic Enchantment by friendly name.
	 * @param name the friendly name, like "Ice Aspect"
	 * @param level the level to create
	 * @return a new enchantment instance, or null if the enchantment does not exist
	 */
	public static MagicEnchantment getByFriendlyName(String name, int level){
		name = ChatColor.stripColor(name).toUpperCase().trim();
		if(!enchantmentFriendlyMap.containsKey(name)) return null;

		return MagicEnchantment.getById(enchantmentFriendlyMap.get(name), level);
	}
	/**
	 * Gets a Magic Enchantment by friendly name, with the level.
	 * If level cannot be found, it will default to 1.
	 * @param name the friendly name, like "Ice Aspect II"
	 * @return a new enchantment instance, or null if the enchantment does not exist
	 */
	public static MagicEnchantment getByFriendlyName(String name){
		int level = 1;

		name = ChatColor.stripColor(name).toLowerCase().trim();

		// Get the level
		int lastSpaceIndex = name.lastIndexOf(" ");
		if(lastSpaceIndex > 0){
			switch (name.substring(lastSpaceIndex)){
				case "I": level=1; break;
				case "II": level=2; break;
				case "III": level=3; break;
				case "IV": level=4; break;
				case "V": level=5; break;
				case "VI": level=6; break;
				case "VII": level=7; break;
				case "VIII": level=8; break;
				case "IX": level=9; break;
				case "X": level=10; break;
				default: level=1; break;
			}
		}

		return MagicEnchantment.getByFriendlyName(name.substring(0, lastSpaceIndex), level);
	}


	/**
	 * Register a Magic Enchantment on the server.
	 * @param enchantmentClass the class for your enchantment
	 * @param id the unique ID for your enchantment, ideally formatted like DAMAGE_LOWHEALTH
	 * @param name the friendly display name for your enchantment
	 */
	public static void registerEnchantment(Class<? extends MagicEnchantment> enchantmentClass, String id, String name){
		enchantmentRegistry.putIfAbsent(id.toUpperCase(), enchantmentClass);
		enchantmentFriendlyMap.putIfAbsent(name.toUpperCase(), id.toUpperCase());

		/*try {
			enchantmentClass.getConstructor(int.class).newInstance(1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}*/

		Utils.notifyAdmins("Registered "+id+" - "+name);
		Utils.notifyAdmins(enchantmentRegistry.size()+" enchantments registered.");
		enchantmentRegistry.keySet().forEach(key -> Utils.notifyAdmins(" - "+key));
	}


    /** The identifier for this magic enchantment. */
    final protected String id;
    /** The friendly name for this magic enchantment. */
    final protected String name;
    /** The branch of this magic enchantment. */
    final protected Branch branch;

    /** The level of this magic enchantment. */
    protected int level;


	protected MagicEnchantment(String id, String name, Branch branch, int level){
        this.id = id.toUpperCase();
        this.name = name;
        this.branch = branch;
		this.level = level;

		registerEnchantment(getClass(), id, name);
	}


	/**
	 * Gets the name of this magic enchantment.
	 * @return the name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Gets the magic branch of this magic enchantment.
	 * @return the branch of magic
	 */
	public Branch getBranch(){
		return branch;
	}

	/**
	 * Gets the level of this magic enchantment.
	 * @return the level
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * Gets the weight of this magic enchantment.
	 * A single item can have a total weight of up to 25.
	 * @return the weight of this enchantment
	 */
	public abstract double getWeight();


	/**
	 * Completes the appropriate action for this enchantment, if the conditions for it are met.
	 * @param event an event involving an entity with this item equipped
	 */
	public final void use(Event event, ItemStack item){
		// Do the pre-use check to see if this magic enchantment will actually be used
		if(!preUseCheck(event, item)) return;

		// Complete the action (defined by the subclass)
		action(event, item);
	}

	/**
	 * Checks when the item holding this enchantment is used.
	 * Can be used to determine whether the enchantment should take effect.
	 * @param event the event fired, could be any EntityEvent or PlayerEvent
	 * @return true if the enchantment should be used, or false if nothing should happen
	 */
	protected abstract boolean preUseCheck(Event event, ItemStack item);

	/**
	 * Complete the appropriate action for this enchantment.
	 * @param event the event that triggered this action, could be any EntityEvent or PlayerEvent
	 */
	protected abstract void action(Event event, ItemStack item);
}