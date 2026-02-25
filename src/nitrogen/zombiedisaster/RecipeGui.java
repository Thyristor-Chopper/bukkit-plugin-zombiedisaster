package nitrogen.zombiedisaster;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeGui implements InventoryHolder {
	private Inventory inv = Bukkit.createInventory(this, 27, "바다의 심장 조합법");

	public RecipeGui() {
		this.init();
	}

	private void init() {
		ItemStack bg = this.createItem(" ", Material.BLACK_STAINED_GLASS_PANE, (List)null);
		ItemStack redstone = this.createItem((String)null, Material.REDSTONE, (List)null);
		ItemStack emerald = this.createItem((String)null, Material.EMERALD, (List)null);
		ItemStack diamond = this.createItem((String)null, Material.DIAMOND, (List)null);
		ItemStack water_bottle = this.createItem((String)null, Material.POTION, (List)null);
		ItemStack heart = this.createItem((String)null, Material.HEART_OF_THE_SEA, (List)null);
		this.inv.setItem(0, bg);
		this.inv.setItem(1, bg);
		this.inv.setItem(9, bg);
		this.inv.setItem(10, bg);
		this.inv.setItem(18, bg);
		this.inv.setItem(19, bg);

		int i;
		for(i = 5; i < 9; ++i) {
			this.inv.setItem(i, bg);
		}

		this.inv.setItem(14, bg);
		this.inv.setItem(16, bg);
		this.inv.setItem(17, bg);

		for(i = 23; i < 27; ++i) {
			this.inv.setItem(i, bg);
		}

		this.inv.setItem(2, redstone);
		this.inv.setItem(4, redstone);
		this.inv.setItem(11, redstone);
		this.inv.setItem(13, redstone);
		this.inv.setItem(20, redstone);
		this.inv.setItem(22, redstone);
		this.inv.setItem(3, water_bottle);
		this.inv.setItem(12, diamond);
		this.inv.setItem(21, emerald);
		this.inv.setItem(15, heart);
	}

	private ItemStack createItem(String name, Material mat, List<String> lore) {
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public Inventory getInventory() {
		return this.inv;
	}
}
