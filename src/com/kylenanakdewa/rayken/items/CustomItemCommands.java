package com.kylenanakdewa.rayken.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kylenanakdewa.core.common.CommonColors;
import com.kylenanakdewa.core.common.Error;
import com.kylenanakdewa.core.common.Utils;
import com.kylenanakdewa.core.common.prompts.Prompt;
import com.kylenanakdewa.rayken.items.enchantments.MagicEnchantment;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Commands for Custom Items.
 * @author Kyle Nanakdewa
 */
public final class CustomItemCommands implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.hasPermission("rayken.customitem")) return Error.NO_PERMISSION.displayChat(sender);

        Player player = (Player)sender;

        // Get the item
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if(itemStack==null || itemStack.getType().equals(Material.AIR)){
            Utils.sendActionBar(player, CommonColors.ERROR+"You must be holding an item.");
        }
        CustomItem customItem = new CustomItem(itemStack);

        // Info command
        if(args.length==0 || (args.length==1 && args[0].equalsIgnoreCase("info"))){
            Prompt info = new Prompt();
            info.addQuestion(CommonColors.INFO+" --- Item: "+CommonColors.MESSAGE+customItem.getName()+CommonColors.INFO+" ---");
            customItem.getDescription().forEach(line -> info.addQuestion(line));
            info.addQuestion(CommonColors.INFO+"- Crafter: "+customItem.getCrafter());
            info.addQuestion(CommonColors.INFO+"- "+customItem.getItemLevel().getLevelString());
            info.addQuestion(CommonColors.INFO+" -- "+ChatColor.DARK_PURPLE+CommonColors.INFO+" --");
            customItem.getEnchantmentStrings(true).forEach(line -> info.addQuestion(line));

            info.display(sender);
            return true;
        }

        // Update command
        if(args.length==1 && args[0].equalsIgnoreCase("update")){
            customItem.updateLore();
            Utils.sendActionBar(sender, "Custom item updated.");
            player.getInventory().setItemInMainHand(itemStack);
            return true;
        }

        // Name command
        if(args.length>=2 && args[0].equalsIgnoreCase("name")){
            // Merge all remaining args into a single string
            List<String> lastArgs = new ArrayList<String>(Arrays.asList(args));
            lastArgs.remove(0);
            String name = ChatColor.translateAlternateColorCodes('&', String.join(" ", lastArgs));

            customItem.setName(name);
            Utils.sendActionBar(sender, "Item renamed to "+name);
            player.getInventory().setItemInMainHand(itemStack);
            return true;
        }

        // Description command
        if(args.length>=2 && args[0].equalsIgnoreCase("description")){
            // Merge all remaining args into a single string
            List<String> lastArgs = new ArrayList<String>(Arrays.asList(args));
            lastArgs.remove(0);
            String data = ChatColor.translateAlternateColorCodes('&', String.join(" ", lastArgs));

            List<String> description = Arrays.asList(data.split("\\n"));

            customItem.setDescription(description);
            Utils.sendActionBar(sender, "Item description set.");
            player.getInventory().setItemInMainHand(itemStack);
            return true;
        }

        // Crafter command
        if(args.length>=2 && args[0].equalsIgnoreCase("crafter")){
            // Merge all remaining args into a single string
            List<String> lastArgs = new ArrayList<String>(Arrays.asList(args));
            lastArgs.remove(0);
            String crafter = ChatColor.translateAlternateColorCodes('&', String.join(" ", lastArgs));

            customItem.setCrafter(crafter);
            Utils.sendActionBar(sender, "Item's crafter set to "+crafter);
            player.getInventory().setItemInMainHand(itemStack);
            return true;
        }

        // Item Flag command
        if(args.length>=2 && args[0].equalsIgnoreCase("flag")){
            String flagName = args[1];
            ItemFlag flag = ItemFlag.valueOf(flagName);

            if(flag==null) return Error.INVALID_ARGS.displayActionBar(sender);

            if(args.length==3){
                switch (args[2].toLowerCase()) {
                    case "true": case "add":
                        customItem.getItemMeta().addItemFlags(flag);
                        Utils.sendActionBar(sender, "Item flag added.");
                        player.getInventory().setItemInMainHand(itemStack);
                        return true;
                    case "false": case "remove":
                        customItem.getItemMeta().removeItemFlags(flag);
                        Utils.sendActionBar(sender, "Item flag removed.");
                        player.getInventory().setItemInMainHand(itemStack);
                        return true;
                    default:
                        break;
                }
            }
            Utils.sendActionBar(sender, "Item flag present: "+customItem.getItemMeta().hasItemFlag(flag));
            return true;
        }

        // Unbreakable command
        if(args.length>=1 && args[0].equalsIgnoreCase("unbreakable")){
            if(args.length==2){
                switch (args[2].toLowerCase()) {
                    case "true":
                        customItem.getItemMeta().setUnbreakable(true);
                        Utils.sendActionBar(sender, "Item is now unbreakable.");
                        player.getInventory().setItemInMainHand(itemStack);
                        return true;
                    case "false":
                        customItem.getItemMeta().setUnbreakable(false);
                        Utils.sendActionBar(sender, "Item is breakable.");
                        player.getInventory().setItemInMainHand(itemStack);
                        return true;
                    default:
                        break;
                }
            }
            Utils.sendActionBar(sender, "Item unbreakable: "+customItem.getItemMeta().isUnbreakable());
            return true;
        }

        // Magic Enchantment command
        if(args.length==3 && args[0].equalsIgnoreCase("enchant")){
            MagicEnchantment enchantment = MagicEnchantment.getById(args[1], Integer.parseInt(args[2]));
            if(enchantment==null) return Error.INVALID_ARGS.displayActionBar(sender);

            customItem.addMagicEnchantment(enchantment);
            Utils.sendActionBar(sender, "Level of "+enchantment.getName()+" set to "+enchantment.getLevel());
            player.getInventory().setItemInMainHand(itemStack);
            return true;
        }

        return Error.INVALID_ARGS.displayActionBar(sender);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length<=1) return Arrays.asList("info", "update", "name", "description", "crafter", "flag", "unbreakable", "enchant");
        if(args.length<=2){
            if(args[0].equalsIgnoreCase("flag")){
                List<String> flags = new ArrayList<String>();
                for(ItemFlag flag : ItemFlag.values()) flags.add(flag.name());
                return flags;
            }
            if(args[0].equalsIgnoreCase("unbreakable") || (args.length<=3 && args[0].equalsIgnoreCase("flag"))){
                return Arrays.asList("true", "false");
            }
            if(args[0].equalsIgnoreCase("enchant")){
                return new ArrayList<String>(MagicEnchantment.getIds());
            }
        }
        return Arrays.asList("");
    }

}