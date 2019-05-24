package com.kylenanakdewa.rayken;

import org.bukkit.ChatColor;

/**
 * The magic branches.
 * @author Kyle Nanakdewa
 */
public enum Branch {

	/** Survival magic branch. */
	WARG (ChatColor.DARK_GREEN, 1.6f, "Warg"),

	/** Manipulation magic branch. */
	EBORI (ChatColor.DARK_AQUA, 1.8f, "Ebori"),

	/** Gathering magic branch. */
	SHOLK (ChatColor.DARK_RED, 2.0f, "Sholk"),

	/** Destruction magic branch. */
	RHUN (ChatColor.YELLOW, 1.4f, "Rhùn"),

	/** Divine magic branch. */
	AUNIX (ChatColor.DARK_PURPLE, 1.2f, "Aunix");


	/** The color associated with this branch. */
	private final ChatColor branchColor;

	/** The sound pitch associated with this branch. */
	private final float soundPitch;

	/** The name of this branch. */
	private final String name;


	private Branch(ChatColor branchColor, float soundPitch, String name){
		this.branchColor = branchColor;
		this.soundPitch = soundPitch;
		this.name = name;
	}


	/** Gets the colour associated with this branch. */
	public ChatColor getColor(){
		return branchColor;
	}

	@Deprecated
	public float getSoundPitch(){
		return soundPitch;
	}

	/** Gets the name of this branch. */
	public String getName(){
		return branchColor+name;
	}
}
