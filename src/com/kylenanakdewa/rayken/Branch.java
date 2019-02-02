package com.kylenanakdewa.rayken;

import net.md_5.bungee.api.ChatColor;

public enum Branch {
	// Main branches
	WARG (ChatColor.DARK_GREEN, 1.6f, "Warg"),
	EBORI (ChatColor.DARK_AQUA, 1.8f, "Ebori"),
	SHOLK (ChatColor.DARK_RED, 2.0f, "Sholk"),
	RHUN (ChatColor.YELLOW, 1.4f, "Rhun"),
	AUNIX (ChatColor.DARK_PURPLE, 1.2f, "Aunix");


	private final ChatColor branchColor;
	private final float soundPitch;
	private final String name;

	Branch(ChatColor branchColor, float soundPitch, String name){
		this.branchColor = branchColor;
		this.soundPitch = soundPitch;
		this.name = name; // TODO - get name from config
	}

	public ChatColor getColor(){
		return branchColor;
	}

	public float getSoundPitch(){
		return soundPitch;
	}

	public String getName(){
		return branchColor+name;
	}
}
