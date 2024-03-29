package me.dave.enchantedfollowers.commands;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import me.dave.enchantedfollowers.Followers;

import java.util.ArrayList;
import java.util.List;

public class GetHexArmorCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Console cannot run this command!");
            return true;
        }
        String prefix = Followers.configManager.getPrefix();
        if (!player.hasPermission("follower.admin.dye")) {
            sender.sendMessage(prefix + "§7You have insufficient permissions.");
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(prefix + "§7Incorrect Usage, try §c/gethexarmor <material> <hexcolor>");
            return true;
        }
        Material material = Material.getMaterial(args[0].toUpperCase());
        String color = args[1].replace("#", "");
        if (material == null || (color.length() != 6 && !(color.length() == 7 && color.startsWith("#")))) {
            player.sendMessage(prefix + "§7Incorrect Usage, try §c/gethexarmor <material> <hexcolor>");
            return true;
        }
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof LeatherArmorMeta armorMeta)) {
            player.sendMessage(prefix + "§7Material has to be a form of Leather Armor.");
            return true;
        }
        armorMeta.setColor(getRGBFromHex(color));
        item.setItemMeta(armorMeta);
        player.getInventory().addItem(item);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

        List<String> tabComplete = new ArrayList<>();
        List<String> wordCompletion = new ArrayList<>();
        boolean wordCompletionSuccess = false;

        if (args.length == 1) {
            if (commandSender.hasPermission("follower.admin.gethexarmor")) {
                tabComplete.add("leather_helmet");
                tabComplete.add("leather_chestplate");
                tabComplete.add("leather_leggings");
                tabComplete.add("leather_boots");
                tabComplete.add("leather_horse_armor");
            }
        }

        for (String currTab : tabComplete) {
            int currArg = 0;
            if (currTab.startsWith(args[currArg])) {
                wordCompletion.add(currTab);
                wordCompletionSuccess = true;
            }
        }
        if (wordCompletionSuccess) return wordCompletion;
        return tabComplete;
    }

    private Color getRGBFromHex(String hexColour) {
        int red = Integer.valueOf(hexColour.substring(0, 2), 16);
        int green = Integer.valueOf(hexColour.substring(2, 4), 16);
        int blue = Integer.valueOf(hexColour.substring(4, 6), 16);
        return Color.fromRGB(red,green, blue);
    }
}
