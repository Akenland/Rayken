package com.kylenanakdewa.rayken;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.rayken.listeners.ExpListener;
//import com.kylenanakdewa.RealmsMagic.Listeners.SpellCastListener;

public class MagicPlugin extends JavaPlugin {

	@Deprecated
	public static MagicPlugin plugin;

	@Override
	public void onEnable(){
		plugin = this;

		// Register event listeners
		getServer().getPluginManager().registerEvents(new ExpListener(), this);
		//getServer().getPluginManager().registerEvents(new SpellCastListener(), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		switch(cmd.getName().toLowerCase()){
		// Main magic command
		case "magic":
			// If no args, return basic information
			if(args.length==0) {
				sender.sendMessage(Utils.messageText+"Rayken "+getDescription().getVersion()+" by Kyle Nanakdewa");
				sender.sendMessage(Utils.messageText+"Magic based around the lore of Akenland");
				return true;
			}
			
			// Check permissions if command is from a player
			if(sender instanceof Player && !sender.hasPermission("magic."+args[0])){
				sender.sendMessage(Utils.errorText+"You can't use this Magic command! Ask an "+ChatColor.DARK_PURPLE+"Admin"+Utils.errorText+" for help.");
				return true;
			}
			
			// If there's more than one arg and perms are good, move on to subcommands
			switch(args[0]){
				
			case "xp":
				if(args.length==1 && sender instanceof Player) return new MagicUser((Player) sender).displayExp(sender);
				if(args.length==2 && sender.hasPermission("magic.xp.others")){
					Player player = Utils.getPlayer(args[1]);
					if(player==null){Utils.sendActionBar(sender, Utils.errorText+"Player not found"); return false;}
					return new MagicUser(player).displayExp(sender);
				}
			}
			
		// Selecting a spell
		/*case "spell":
			if(sender instanceof Player) return new MagicUser((Player) sender).setActiveSpell(args);
			return false;*/
			
		// Command not set up
		default:
			sender.sendMessage(Utils.errorText+"This command is not set up in Rayken. Yell at Kyle if you want it fixed.");
			return false;
		}
	}
}
