package com.kylenanakdewa.rayken.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.kylenanakdewa.rayken.MagicUser;

public final class ExpListener implements Listener {
	
	//// Exp Gain buffers - store the exp gained here, that way we're not constantly writing data
	public static HashMap<String,Double> wargExpBuffer = new HashMap<String,Double>();
	public static HashMap<String,Double> eboriteExpBuffer = new HashMap<String,Double>();
	public static HashMap<String,Double> sholkExpBuffer = new HashMap<String,Double>();
	public static HashMap<String,Double> rhunExpBuffer = new HashMap<String,Double>();
		
	// Add to the exp buffers
	public void addToBuffer(HashMap<String,Double> buffer, Player player, double amount){
		String playerName = player.getName();
		Double currentExp = buffer.get(playerName);
		buffer.put(playerName, (currentExp==null ? amount : currentExp+amount));
	}
	// Add to all exp buffers
	public void addToBuffer(Player player, double amount){
		addToBuffer(wargExpBuffer, player, amount);
		addToBuffer(eboriteExpBuffer, player, amount);
		addToBuffer(sholkExpBuffer, player, amount);
		addToBuffer(rhunExpBuffer, player, amount);
	}
	
	// Transfer exp from buffer to file when player quits
	@EventHandler
	public void playerQuit(PlayerQuitEvent event){
		// Whenever MagicUser is instantiated, it automatically saves exp in the buffers, calculates levels, and saves to file
		new MagicUser(event.getPlayer());
	}
	
	
	// Normal Exp gain listener - add 1% of xp to each branch
	@EventHandler
	public void expGain(PlayerExpChangeEvent event){
		// Only do this if it's not 5xp, since that's the amount you get for normal mobs
		if(event.getAmount()>5 && event.getPlayer().getTotalExperience()<1500){
			// Exp gained per branch
			double expGained = event.getAmount()*0.01;

			// Add this to each buffer
			addToBuffer(event.getPlayer(), expGained);

			//event.getPlayer().sendMessage(Utils.infoText+"[Magic Debug] You collected "+event.getAmount()+" XP, "+expGained+" XP was added to each branch.");
		}
	}

	
	// Warg Exp gain - crafting, smelting
	// Smelting
	@EventHandler
	public void smeltingExpGain(FurnaceExtractEvent event){
		double expGained = 0.05 * event.getItemAmount();
		addToBuffer(wargExpBuffer, event.getPlayer(), expGained);
		
		//event.getPlayer().sendMessage(Utils.infoText+"You collected "+expGained+" Warg XP from smelting.");
	}
	// Using items
	@EventHandler
	public void itemBreakExpGain(PlayerItemBreakEvent event){
		double expGained = 2.0;
		addToBuffer(wargExpBuffer, event.getPlayer(), expGained);
		
		//event.getPlayer().sendMessage(Utils.infoText+"You collected "+expGained+" Warg XP from using items.");
	}
	
	
	// Eborite Exp gain - enchanting, brewing
	// Enchanting
	@EventHandler
	public void enchantExpGain(EnchantItemEvent event){
		// Set the exp gain based on enchantment level
		int expCost = event.getExpLevelCost();
		double expGained;
		if(expCost >=25){
			expGained = 8.0;
		} else if(expCost>=15){
			expGained = 6.0;
		} else {
			expGained = 4.0;
		}
		addToBuffer(eboriteExpBuffer, event.getEnchanter(), expGained);
		
		//event.getEnchanter().sendMessage(Utils.infoText+"You collected "+expGained+" Eborite XP from enchanting.");
	}
	
	
	// Sholk Exp gain - farming
	// Farming crops
	@EventHandler
	public void farmExpGain(BlockPlaceEvent event){
		// If the block placed on is soil, count as farming
		if(event.getBlockAgainst().getType()==Material.SOIL){
			double expGained = 0.05;
			addToBuffer(sholkExpBuffer, event.getPlayer(), expGained);
			
			//event.getPlayer().sendMessage(Utils.infoText+"You collected "+expGained+" Sholk XP from farming.");
		}
	}
	// Breeding
	@EventHandler
	public void breedingExpGain(EntityBreedEvent event){
		// If breeder is a player, give them the XP
		if(event.getBreeder() instanceof Player){
			Player player = (Player) event.getBreeder();
			double expGained = 1.0;
			addToBuffer(sholkExpBuffer, player, expGained);
			
			//player.sendMessage(Utils.infoText+"You collected "+expGained+" Sholk XP from breeding.");
		}
	}
	
	
	// Rhun Exp gain - killing
	private static HashMap<UUID,UUID> lastPlayerKilled = new HashMap<UUID,UUID>();
	@EventHandler
	public void huntingExpGain(EntityDeathEvent event){
		// Get the killer
		Player player = event.getEntity().getKiller();
		
		// If killer is not null, give them the XP
		if(player!=null && event.getEntityType()==EntityType.PLAYER){
			// If they killed a player, 4xp
			double expGained = event.getDroppedExp()*0.1;

			// Boost it 1.5x if it was caused by TNT
			if(event.getEntity().getLastDamageCause().getCause().equals(DamageCause.BLOCK_EXPLOSION)) expGained = expGained*1.5;
			// Halve gained XP if it's a repeated kill (to prevent abuse)
			if(lastPlayerKilled.get(player.getUniqueId()).equals(event.getEntity().getUniqueId())) expGained = expGained/2;

			addToBuffer(rhunExpBuffer, player, expGained);

			//player.sendMessage(Utils.infoText+"You collected "+expGained+" Rhun XP from hunting.");

			// Reduce XP for next kill
			lastPlayerKilled.put(player.getUniqueId(), event.getEntity().getUniqueId());
		}
	}
}
