package nitrogen.zombiedisaster;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	private ZombieDisaster plugin;
	
	public Commands(ZombieDisaster plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final String commandName = command.getName().toLowerCase();
		if(commandName.equalsIgnoreCase("recipe")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("§c플레이어만 실행 가능");
				return true;
			}
			if(args.length != 0)
				return false;
			Player player = (Player) sender;
			if(!plugin.isInZombieWorld(player)) {
				sender.sendMessage("§c이 차원은 좀비 아포칼립스 세계가 아닙니다");
				return true;
			}
			RecipeGui gui = new RecipeGui();
			player.openInventory(gui.getInventory());
			return true;
		} else if(commandName.equalsIgnoreCase("zombie")) {
			int opLevel;
			try {
				opLevel = sender.getOpLevel();
			} catch(Exception e) {
				opLevel = 4;
			}
			if(!(sender instanceof ConsoleCommandSender || (sender.isOp() && opLevel >= 3))) {
				sender.sendMessage("§c권한이 부족합니다");
				return true;
			}
			if(args.length != 1)
				return false;
			World world;
			if(sender instanceof Player) {
				Player player = (Player) sender;
				world = player.getLocation().getWorld();
			} else {
				world = plugin.getServer().getWorlds().get(0);
			}
			String worldID = world.getUID().toString();
			String worldName = world.getName();
			if(args[0].equalsIgnoreCase("enable")) {
				List<String> worlds = plugin.config.getStringList("enabled-worlds");
				if(worlds.contains(worldID)) {
					sender.sendMessage("§c" + worldName + "은(는) 이미 좀비 아포칼립스 차원으로 등록되어 있습니다");
					return true;
				}
				worlds.add(worldID);
				plugin.config.set("enabled-worlds", worlds);
				plugin.saveConfig();
				plugin.enabledWorlds = worlds;
				Bukkit.broadcastMessage("§e" + worldName + "이(가) 좀비 아포칼립스 차원이 되었습니다");
				return true;
			} else if(args[0].equalsIgnoreCase("disable")) {
				List<String> worlds = plugin.config.getStringList("enabled-worlds");
				if(!worlds.contains(worldID)) {
					sender.sendMessage("§c해당 차원은 등록되어 있지 않습니다");
					return true;
				}
				worlds.remove(worldID);
				plugin.config.set("enabled-worlds", worlds);
				plugin.saveConfig();
				plugin.enabledWorlds = worlds;
				Bukkit.broadcastMessage("§e" + worldName + "이(가) 더 이상 좀비 아포칼립스 차원이 아닙니다");
				return true;
			} else {
				return false;
			}
		}
		
		return true;
	}
}
