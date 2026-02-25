package nitrogen.zombiedisaster;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZombieDisaster extends JavaPlugin {
	protected FileConfiguration config;
	protected List<String> enabledWorlds;
	
	public void onEnable() {
		this.config = this.getConfig();
		this.config.options().copyDefaults(true);
        this.saveConfig();
        enabledWorlds = config.getStringList("enabled-worlds");
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		this.getCommand("recipe").setExecutor(new Commands(this));
		this.getCommand("zombie").setExecutor(new Commands(this));
		ShapedRecipe heart = new ShapedRecipe(new NamespacedKey(this, "heart_of_the_sea"), new ItemStack(Material.HEART_OF_THE_SEA));
		heart.shape(new String[]{"ABA", "ACA", "ADA"});
		heart.setIngredient('A', Material.REDSTONE);
		heart.setIngredient('B', Material.POTION);
		heart.setIngredient('C', Material.DIAMOND);
		heart.setIngredient('D', Material.EMERALD);
		this.getServer().addRecipe(heart);
	}

	public void onDisable() {
	}
	
	public boolean isZombieWorld(World world) {
		return enabledWorlds.contains(world.getUID().toString());
	}
	
	public boolean isInZombieWorld(Entity entity) {
		return enabledWorlds.contains(entity.getWorld().getUID().toString());
	}
}
