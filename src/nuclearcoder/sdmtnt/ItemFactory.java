package nuclearcoder.sdmtnt;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemFactory {
	public static ItemStack[] getSpells() {
		return new ItemStack[] { getSpell1(), getSpell2() };
	}

	public static ItemStack getSpell1() {
		ItemStack result = new ItemStack(Material.BOW);
		ItemMeta meta = result.getItemMeta();

		meta.setDisplayName(ChatColor.RESET + "Sort 1 - Boule de Feu");
		meta.setLore(Arrays.asList(new String[] {
				ChatColor.RESET + "" + ChatColor.BLUE + "3 dégâts",
				ChatColor.RESET + "" + ChatColor.BLUE + "Récupération: 2s" }));

		result.setItemMeta(meta);
		return result;
	}

	public static ItemStack getSpell2() {
		ItemStack result = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = result.getItemMeta();

		meta.setDisplayName(ChatColor.RESET + "Sort 2 - Eclair");
		meta.setLore(Arrays.asList(new String[] {
				ChatColor.RESET + "" + ChatColor.BLUE + "5 dégâts",
				ChatColor.RESET + "" + ChatColor.BLUE + "Récupération: 8s" }));

		result.setItemMeta(meta);
		return result;
	}
}
