package com.kylenanakdewa.rayken.items.enchantments;

import com.kylenanakdewa.rayken.Branch;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * A Magic Enchantment that adds an attribute to an item.
 * @author Kyle Nanakdewa
 */
public abstract class AttributeMagicEnchantment extends MagicEnchantment {

    /** The attribute type. */
    private Attribute attribute;
    /** The attribute amount. */
    private double amount;
    /** The attribute operation. */
    private AttributeModifier.Operation operation;


    protected AttributeMagicEnchantment(String name, Branch branch, int level, Attribute attributeType, double attributeAmount, AttributeModifier.Operation attributeOperation) {
        super(name, branch, level);
        attribute = attributeType;
        amount = attributeAmount;
        operation = attributeOperation;
    }


    @Override
    protected boolean preUseCheck(Event event, ItemStack item) {
        // Make sure the attribute exists
        // TODO
        return false;
    }

    @Override
    protected void action(Event event, ItemStack item) {
		// No action to perform
	}

}