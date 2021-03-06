package nuclearcoder.sdmtnt;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemFactory {
	public static void addSpells(Inventory inv) {
		inv.clear();

		ItemStack[] spells = getSpells();

		inv.setItem(0, spells[0]);
		inv.setItem(1, spells[1]);
		inv.setItem(9, new ItemStack(Material.ARROW));
	}

	public static ItemStack[] getSpells() {
		return new ItemStack[] { getSpell1(), getSpell2() };
	}

	public static ItemStack getSpell1() {
		ItemStack result = new ItemStack(Material.BOW);
		ItemMeta meta = result.getItemMeta();

		meta.setDisplayName(ChatColor.RESET + "Sort 1 - Boule de Feu");
		meta.setLore(Arrays.asList(new String[] {
				ChatColor.RESET + "" + ChatColor.BLUE + "3 d�g�ts",
				ChatColor.RESET + "" + ChatColor.BLUE + "R�cup�ration: 2s" }));
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);

		result.setItemMeta(meta);
		return result;
	}

	public static ItemStack getSpell2() {
		ItemStack result = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = result.getItemMeta();

		meta.setDisplayName(ChatColor.RESET + "Sort 2 - Eclair");
		meta.setLore(Arrays.asList(new String[] {
				ChatColor.RESET + "" + ChatColor.BLUE + "5 d�g�ts",
				ChatColor.RESET + "" + ChatColor.BLUE + "R�cup�ration: 8s" }));

		result.setItemMeta(meta);
		return result;
	}
}
