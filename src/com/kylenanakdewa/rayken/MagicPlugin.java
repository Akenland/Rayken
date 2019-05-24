package com.kylenanakdewa.rayken;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.kylenanakdewa.core.common.CommonColors;
import com.kylenanakdewa.core.common.Error;
import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.items.CustomItemCommands;
import com.kylenanakdewa.rayken.items.CustomItemListener;
import com.kylenanakdewa.rayken.items.enchantments.MagicEnchantment;
import com.kylenanakdewa.rayken.items.enchantments.ebori.Construct;
import com.kylenanakdewa.rayken.items.enchantments.ebori.ConstructListener;
import com.kylenanakdewa.rayken.items.enchantments.ebori.IceAspect;
import com.kylenanakdewa.rayken.items.enchantments.ebori.Levitate;
import com.kylenanakdewa.rayken.items.enchantments.ebori.Rainmaker;
import com.kylenanakdewa.rayken.items.enchantments.rhun.MagmaStrike;
import com.kylenanakdewa.rayken.items.enchantments.rhun.ToxinStrike;
import com.kylenanakdewa.rayken.items.enchantments.sholk.Life;
import com.kylenanakdewa.rayken.items.enchantments.sholk.Shadow;
import com.kylenanakdewa.rayken.items.enchantments.sholk.Shatter;
import com.kylenanakdewa.rayken.items.enchantments.sholk.ShatterListener;
import com.kylenanakdewa.rayken.items.enchantments.sholk.Skywatch;
import com.kylenanakdewa.rayken.items.enchantments.sholk.Tracking;
import com.kylenanakdewa.rayken.items.enchantments.warg.AirStrike;
import com.kylenanakdewa.rayken.items.enchantments.warg.Charge;
import com.kylenanakdewa.rayken.items.enchantments.warg.LastWord;
import com.kylenanakdewa.rayken.items.enchantments.warg.PackRanger;
import com.kylenanakdewa.rayken.listeners.ExpListener;

/**
 * Rayken
 * <p>
 * A magic plugin for Minecraft Bukkit, based around five branches: Survival, Manipulation, Gathering, Destruction, and Divine.
 * Designed for Akenland - RPG-inspired survival server.
 * @author Kyle Nanakdewa
 */
public final class MagicPlugin extends JavaPlugin {

	public static MagicPlugin plugin;

	@Override
	public void onEnable(){
		plugin = this;

		// Register event listeners
		getServer().getPluginManager().registerEvents(new ExpListener(), this);
		getServer().getPluginManager().registerEvents(new CustomItemListener(), this);
		getServer().getPluginManager().registerEvents(new ConstructListener(), this);
		getServer().getPluginManager().registerEvents(new ShatterListener(), this);


		// Register commands
		this.getCommand("customitem").setExecutor(new CustomItemCommands());


		// Register enchantments
		MagicEnchantment.registerEnchantment(PackRanger.class, "Pack Ranger");
		MagicEnchantment.registerEnchantment(LastWord.class, "Last Word");
		MagicEnchantment.registerEnchantment(Charge.class, "Charge");
		MagicEnchantment.registerEnchantment(AirStrike.class, "Air Strike");

		MagicEnchantment.registerEnchantment(Levitate.class, "Levitate");
		MagicEnchantment.registerEnchantment(IceAspect.class, "Ice Aspect");
		MagicEnchantment.registerEnchantment(Construct.class, "Construct");
		MagicEnchantment.registerEnchantment(Rainmaker.class, "Rainmaker");

		MagicEnchantment.registerEnchantment(Shatter.class, "Shatter");
		MagicEnchantment.registerEnchantment(Life.class, "Life");
		MagicEnchantment.registerEnchantment(Shadow.class, "Shadow");
		MagicEnchantment.registerEnchantment(Tracking.class, "Tracking");
		MagicEnchantment.registerEnchantment(Skywatch.class, "Skywatch");

		MagicEnchantment.registerEnchantment(ToxinStrike.class, "Toxin Strike");
		MagicEnchantment.registerEnchantment(MagmaStrike.class, "Magma Strike");


		// Set XP multiplier
		ExpListener.xpMultiplier = getConfig().getDouble("xp-multiplier", 1);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		switch(cmd.getName().toLowerCase()){
		// Main magic command
		case "magic":
			// If no args, return basic information
			if(args.length==0) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE+"Rayken "+getDescription().getVersion()+CommonColors.MESSAGE+" by Kyle Nanakdewa");
				sender.sendMessage(CommonColors.MESSAGE+"Magic based around the lore of Akenland.");
				sender.sendMessage(CommonColors.MESSAGE+"Website: http://akenland.com");
				sender.sendMessage(CommonColors.MESSAGE+"Email: plugins@akenland.com");
				return true;
			}

			// Check permissions if command is from a player
			if(sender instanceof Player && !sender.hasPermission("magic."+args[0])){
				sender.sendMessage(CommonColors.ERROR+"You can't use this Magic command! Ask an "+ChatColor.DARK_PURPLE+"Admin"+CommonColors.ERROR+" for help.");
				return true;
			}

			// If there's more than one arg and perms are good, move on to subcommands
			switch(args[0]){

			case "xp":
				if(args.length==1 && sender instanceof Player) return new MagicUser((Player) sender).displayExp(sender);
				if(args.length==2 && sender.hasPermission("magic.xp.others")){
					Player player = Utils.getPlayer(args[1]);
					if(player==null) return Error.PLAYER_NOT_FOUND.displayActionBar(sender);
					return new MagicUser(player).displayExp(sender);
				}
			}

		// Command not set up
		default:
			sender.sendMessage(CommonColors.ERROR+"This command is not set up in Rayken. Yell at Kyle if you want it fixed.");
			return false;
		}
	}
}
