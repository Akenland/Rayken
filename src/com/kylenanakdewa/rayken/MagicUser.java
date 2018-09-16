package com.kylenanakdewa.rayken;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.kylenanakdewa.rayken.listeners.ExpListener;
import com.kylenanakdewa.rayken.utils.ExpUtils;
import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.core.common.savedata.PlayerSaveDataSection;
import com.kylenanakdewa.core.characters.players.PlayerCharacter;

/**
 * Represents a player that can use Rayken magic.
 * @author Kyle Nanakdewa
 */
public class MagicUser extends PlayerSaveDataSection {
	
	//// HashMap to store active spell
	//public static HashMap<String,Spell> activeSpell = new HashMap<String,Spell>();
	
	
	//// Information about this magic user	
	//Map<String,Object> unlockedSpells;
	//Map<String,Object> unlockedSkills;

	int levelWarg;
	double expWarg;
	int levelEborite;
	double expEborite;
	int levelSholk;
	double expSholk;
	int levelRhun;
	double expRhun;


	public MagicUser(PlayerCharacter player){
		super(player, MagicPlugin.plugin);
		loadData();
		saveExp();
	}
	public MagicUser(OfflinePlayer player){
		super(player, MagicPlugin.plugin);
		loadData();
		saveExp();
	}
	/*public MagicUser(OfflinePlayer player){
		super(player);
		loadData();
		saveExp();
	}*/


	// Load magic user stats
	void loadData(){
		//if(data.contains("magicuser.spells")) unlockedSpells = data.getConfigurationSection("magicuser.spells").getValues(false);
		//if(data.contains("magicuser.skills")) unlockedSkills = data.getConfigurationSection("magicuser.skills").getValues(false);

		if(data.contains("exp")){
			levelWarg = data.getInt("exp.warg.level");
			expWarg = data.getDouble("exp.warg.exp");
			levelEborite = data.getInt("exp.eborite.level");
			expEborite = data.getDouble("exp.eborite.exp");
			levelSholk = data.getInt("exp.sholk.level");
			expSholk = data.getDouble("exp.sholk.exp");
			levelRhun = data.getInt("exp.rhun.level");
			expRhun = data.getDouble("exp.rhun.exp");
		}
	}

	// Save magic user stats
	void saveData(){
		// If no data (all levels are 0), don't save
		if(levelWarg+levelEborite+levelSholk+levelRhun==0) return;

		// Save the levels and exp
		data.set("exp.warg.level", levelWarg);
		data.set("exp.warg.exp", expWarg);
		data.set("exp.eborite.level", levelEborite);
		data.set("exp.eborite.exp", expEborite);
		data.set("exp.sholk.level", levelSholk);
		data.set("exp.sholk.exp", expSholk);
		data.set("exp.rhun.level", levelRhun);
		data.set("exp.rhun.exp", expRhun);
		save();
	}

	
	
	// Save exp currently in buffers
	public void saveExp(){
		// Save the exp from the buffers
		Double gainedExpWarg = ExpListener.wargExpBuffer.get(character.getUsername());
		expWarg += gainedExpWarg!=null ? gainedExpWarg : 0;
		Double gainedExpEborite = ExpListener.eboriteExpBuffer.get(character.getUsername());
		expEborite += gainedExpEborite!=null ? gainedExpEborite : 0;
		Double gainedExpSholk = ExpListener.sholkExpBuffer.get(character.getUsername());
		expSholk += gainedExpSholk!=null ? gainedExpSholk : 0;
		Double gainedExpRhun = ExpListener.rhunExpBuffer.get(character.getUsername());
		expRhun += gainedExpRhun!=null ? gainedExpRhun : 0;
		
		//Utils.notifyAdmins(Utils.infoText+"[Magic Debug] Gained XP for "+character.getName()+" includes: Warg "+gainedExpWarg+" XP, Eborite "+gainedExpEborite+" XP, Sholk "+gainedExpSholk+" XP, Rhun "+gainedExpRhun+" XP.");		
		
		// Empty the buffers
		ExpListener.wargExpBuffer.remove(character.getUsername());
		ExpListener.eboriteExpBuffer.remove(character.getUsername());
		ExpListener.sholkExpBuffer.remove(character.getUsername());
		ExpListener.rhunExpBuffer.remove(character.getUsername());
		// Calculate levels
		convertExpToLevels();
	}
	
	// Convert exp to levels
	void convertExpToLevels(){
		for(Branch branch : Branch.values()){
			int level = 0; // Represents the level of this branch
			double exp = 0.0; // Represents the exp of this branch

			// Load the branch level and exp that we're working on
			switch(branch){
			case WARG:
				level = levelWarg; exp = expWarg; break;
			case EBORITE:
				level = levelEborite; exp = expEborite; break;
			case SHOLK:
				level = levelSholk; exp = expSholk; break;
			case RHUN:
				level = levelRhun; exp = expRhun; break;
			default:
				break;
			}

			while(branch==Branch.WARG || branch==Branch.EBORITE || branch==Branch.SHOLK || branch==Branch.RHUN){
				double expRequired = ExpUtils.getExpToLevelUp(level); // represents how much exp is needed to get to next level

				// If not enough xp to advance to next level, converting is finished
				if(expRequired > exp) break;

				// Otherwise, subtract it, add a level, and repeat
				exp -= expRequired;
				level++;
			}
			
			// Make sure exp is nicely rounded
			exp = Math.round(exp*100.0)/100.0;
			
			// Once that finishes, save the new level and exp in this magicuser
			// Load the branch level and exp that we're working on
			switch(branch){
			case WARG:
				levelWarg = level; expWarg = exp; break;
			case EBORITE:
				levelEborite = level; expEborite = exp; break;
			case SHOLK:
				levelSholk = level; expSholk = exp; break;
			case RHUN:
				levelRhun = level; expRhun = exp; break;
			default:
				break;
			}
		}
		// Once all branches are done, save the file
		saveData();
	}
	
	
	/*// Set the active spell for this magicuser
	@Deprecated
	boolean setActiveSpell(Spell spell){
		// Make sure player is online
		if(!isOnline()) return false;
		Player player = (Player)getPlayer();

		// Check if the player has unlocked the spell, and they have permission to use it
		if(unlockedSpells!=null && unlockedSpells.containsKey(spell.getName().toLowerCase()) && spell.hasPermission(player)){
			activeSpell.put(player.getName(), spell);
			Utils.sendActionBar(player, spell.getBranch().getColor()+spell.getName()+" is ready!");
			return true;
		}
		// If they don't, return false
		Utils.sendActionBar(player,Utils.errorText+"You don't know this spell!");
		return false;
	}
	@Deprecated
	boolean setActiveSpell(String spellName, int level){
		// Make sure player is online
		if(!isOnline()) return false;
		Player player = (Player)getPlayer();

		spellName = spellName.toLowerCase();
		
		if(unlockedSpells!=null && unlockedSpells.containsKey(spellName)){
			// Get the level
			int playerLevel = (int) this.unlockedSpells.get(spellName);
			level = (level!=0 && level<=playerLevel) ? level : playerLevel;
			
			// Get the spell and make it active
			return setActiveSpell(SpellUtils.getSpell(spellName, level));
		}
		
		activeSpell.remove(getName());
		Utils.sendActionBar(player, "No spell selected");
		return true;
	}
	// Setting active spell via command
	@Deprecated
	boolean setActiveSpell(String[] args){
		// Make sure player is online
		if(!isOnline()) return false;
		Player player = (Player)getPlayer();

		if(args.length==0){
			if(unlockedSpells!=null){
				// List spells
				player.sendMessage(Utils.infoText+"--- Your Spells ---");
				for(Map.Entry<String, Object> spell : unlockedSpells.entrySet()){
					player.sendMessage(Utils.messageText+"- "+spell.getKey()+" "+spell.getValue());
				}
				player.sendMessage(Utils.infoText+"- Type "+ChatColor.LIGHT_PURPLE+"/spell <spell name>"+Utils.infoText+" to activate a spell, then right-click with an empty hand to cast it.");
				return true;
			}
			Utils.sendActionBar(player, "You don't know any spells!");
			return false;
		}
		if(args.length==1){
			// Change spell
			return setActiveSpell(args[0], 0);
		}
		if(args.length==2){
			// Change spell with chosen level
			return setActiveSpell(args[0], Integer.parseInt(args[1]));
		}
		Utils.sendActionBar(player, Utils.errorText+"Incorrect arguments");
		return false;
	}*/
	
	
	// Display this magicuser's xp
	boolean displayExp(CommandSender sender){
		sender.sendMessage(Utils.infoText+"--- Magic XP: "+character.getName()+Utils.infoText+" ---");
		sender.sendMessage(Branch.WARG.getColor()+"- "+Branch.WARG.getName()+" Level "+levelWarg+", "+expWarg+" XP");
		sender.sendMessage(Branch.EBORITE.getColor()+"- "+Branch.EBORITE.getName()+" Level "+levelEborite+", "+expEborite+" XP");
		sender.sendMessage(Branch.SHOLK.getColor()+"- "+Branch.SHOLK.getName()+" Level "+levelSholk+", "+expSholk+" XP");
		sender.sendMessage(Branch.RHUN.getColor()+"- "+Branch.RHUN.getName()+" Level "+levelRhun+", "+expRhun+" XP");
		
		return true;
	}
}
