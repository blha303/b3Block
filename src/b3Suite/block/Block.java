package b3Suite.block;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Block extends JavaPlugin {

	public void onEnable() {
		getConfig().addDefault("ShowErrorWhenNoIDIsEntered", true);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		int id = 1;
		Location loc;
		Player player;
		BlockCommandSender bcs;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			player = null;
		}
		if (sender instanceof BlockCommandSender) {
			bcs = (BlockCommandSender) sender;
		} else {
			bcs = null;
		}
		id = getID(args);
		if (id == -1) {
			if (bcs != null) {
				this.getLogger().info(
						"Command block at " + bcs.getBlock().getX() + ","
								+ bcs.getBlock().getY() + ","
								+ bcs.getBlock().getZ()
								+ " doesn't have a valid block id for /block.");
				return true;
			}
			if (sender.hasPermission("command.block.1")) {
				if (getConfig().getBoolean("ShowErrorWhenNoIDIsEntered")) {
					sender.sendMessage(args[0]
							+ " is not a number. Using stone instead. "
							+ "(Ask the server owner to turn this off in the config!)");
				}
				id = 1;
			} else {
				sender.sendMessage("That isn't a valid block ID, and you don't have permission to place stone (the default)");
				return true;
			}
		}

		if (player != null) {
			if (player.hasPermission("command.block." + id)
					|| player.hasPermission("command.block.*")) {
				loc = getLoc(args, player);
				if (loc == null) {
					loc = player.getLocation();
				}
				loc.getBlock().setTypeId(id);
				return true;
			} else {
				player.sendMessage("You don't have permission to use this command.");
				return true;
			}
		} else if (bcs != null) {
			loc = getLoc(args);
			if (loc == null) {
				this.getLogger().info(
						"Command block at " + bcs.getBlock().getX() + ","
								+ bcs.getBlock().getY() + ","
								+ bcs.getBlock().getZ()
								+ " wasn't able to get a location.");
				return false;
			}
			loc.getBlock().setTypeId(id);
			return true;
		} else {
			loc = getLoc(args);
			if (loc == null) {
				this.getLogger().info("Unable to get location");
				return false;
			}
			loc.getBlock().setTypeId(id);
			return true;
		}
	}

	public Location getLoc(String[] args, CommandSender sender) {
		Location loc;
		Player player;
		BlockCommandSender bcs;
		if (sender != null) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				player = null;
			}
			if (sender instanceof BlockCommandSender) {
				bcs = (BlockCommandSender) sender;
			} else {
				bcs = null;
			}
		} else {
			player = null;
			bcs = null;
		}
		if (player != null) {
			try { // location getting
				String[] locnum = args[1].split(",");
				String world = "";
				try {
					world = args[2];
				} catch (Exception e1) {
					world = player.getWorld().getName();
				}
				if (getServer().getWorld(world) != null) {
					loc = new Location(getServer().getWorld(world),
							Integer.parseInt(locnum[0]),
							Integer.parseInt(locnum[1]),
							Integer.parseInt(locnum[2]));
				} else {
					return null;
				}
				return loc;
			} catch (Exception e) {
				return null;
			}
		} else if (bcs != null) {
			try { // location getting
				String[] locnum = args[1].split(",");
				String world = "";
				try {
					world = args[2];
				} catch (Exception e1) {
					world = bcs.getBlock().getLocation().getWorld().getName();
				}
				if (getServer().getWorld(world) != null) {
					loc = new Location(getServer().getWorld(world),
							Integer.parseInt(locnum[0]),
							Integer.parseInt(locnum[1]),
							Integer.parseInt(locnum[2]));
				} else {
					return null;
				}
				return loc;
			} catch (Exception e) {
				return null;
			}
		} else {
			try { // location getting
				String[] locnum = args[1].split(",");
				String world = "";
				try {
					world = args[2];
				} catch (Exception e1) {
					world = "world";
				}
				if (getServer().getWorld(world) != null) {
					loc = new Location(getServer().getWorld(world),
							Integer.parseInt(locnum[0]),
							Integer.parseInt(locnum[1]),
							Integer.parseInt(locnum[2]));
				} else {
					return null;
				}
				return loc;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public Location getLoc(String[] args) {
		return getLoc(args, null);
	}

	public int getID(String[] args) {
		int id;
		try { // ID getting
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			id = -1;
		} catch (ArrayIndexOutOfBoundsException e1) {
			id = 1;
		}
		return id;
	}
}
