package nuclearcoder.sdmtnt;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Game implements Listener, CommandExecutor {
	private int np = 0;
	private HashSet<String> inGame = new HashSet<String>();
	private boolean gameStarted = false, gameStarting = false;

	private int playerMin, playerMax;

	private Plugin plugin;

	public Game(Plugin plugin, int min, int max) {
		this.plugin = plugin;
		this.playerMin = min;
		this.playerMax = max;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void spellCasted(PlayerInteractEvent ev) {
		if (ev.getAction() == Action.RIGHT_CLICK_AIR
				|| ev.getAction() == Action.LEFT_CLICK_AIR
				|| ev.getAction() == Action.RIGHT_CLICK_BLOCK
				|| ev.getAction() == Action.LEFT_CLICK_BLOCK) {
			Player p = ev.getPlayer();
			Location loc = p.getTargetBlock(null, 20).getLocation();
			World world = p.getWorld();

			boolean cancelled = true;

			// if (!inGame.contains(p))
			// return;

			ItemStack used = p.getItemInHand();

			if (used == null)
				return;

			switch (used.getType()) {
			case BLAZE_ROD:
				world.strikeLightning(loc);
				world.strikeLightningEffect(loc);
				break;
			default:
				cancelled = false;
				break;
			}

			ev.setCancelled(cancelled);
		}
	}

	@EventHandler
	public void bowHandle(EntityShootBowEvent ev) {
		Entity ent = ev.getEntity();

		if (ent instanceof Player) {
			Player p = (Player) ent;
			String pn = p.getName().toLowerCase();
			if (inGame.contains(pn)) {
				boolean isFull = ev.getForce() == 1.0F;

				if (isFull) {
					Entity fireball = p.getWorld().spawnEntity(
							p.getLocation().add(0, 3, 0), EntityType.FIREBALL);
					ev.setProjectile(fireball);
					cooldown(p, 0, 5);
				} else {
					tell(p,
							ChatColor.RED
									+ "Vous devez charger l'arc à fond pour pouvoir lancer une boule de feu !");
					ev.setCancelled(true);
				}
			}
		}
	}

	private void cooldown(Player p, final int slot, int seconds) {
		final Inventory inv = p.getInventory();
		final ItemStack stack = inv.getItem(slot);
		inv.clear(slot);
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,
				new Runnable() {
					@Override
					public void run() {
						inv.setItem(slot, stack);
					}
				}, seconds * 20L);
	}

	@EventHandler
	public void joinSign(PlayerInteractEvent ev) {
		if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = ev.getPlayer();
			BlockState state = ev.getClickedBlock().getState();

			if (state instanceof Sign) {
				Sign sign = (Sign) state;
				if (sign.getLine(1).equalsIgnoreCase("TNTGames")) {
					p.performCommand("jointntgames");
				}
			}
		}
	}

	@EventHandler
	public void cancelBreak(BlockBreakEvent ev) {
		String pn = ev.getPlayer().getName().toLowerCase();
		if (inGame.contains(pn)) {
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void cancelDamage(EntityDamageEvent ev) {
		switch (ev.getCause()) {
		case DROWNING:
		case FALL:
		case STARVATION:
			ev.setCancelled(true);
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void playerRespawned(PlayerRespawnEvent ev) {
		Player p = ev.getPlayer();
		String name = p.getName(), nameL = name.toLowerCase();

		if (inGame.contains(nameL)) {
			ItemFactory.addSpells(p.getInventory());
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String c,
			String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED
					+ "Cette commande doit être exécutée par un joueur.");
		} else {
			Player p = (Player) s;
			String name = p.getName(), nameL = name.toLowerCase();

			if (inGame.contains(nameL)) {
				tell(p, ChatColor.RED + "Vous êtes déjà en jeu !");
			} else if (np >= playerMax) {
				tell(p,
						ChatColor.RED
								+ "Tout le monde a déjà rejoint ! Veuillez attendre la fin de la partie ou que quelqu'un quitte.");
			} else if (gameStarted) {
				tell(p,
						ChatColor.RED
								+ "La partie a déjà commencé ! Veuillez attendre la fin.");
			} else {
				inGame.add(nameL);
				np++;
				say(ChatColor.AQUA + name + ChatColor.GOLD
						+ " a rejoint la partie !");
				say(ChatColor.GOLD + "Il y a " + np + "/" + playerMax
						+ " joueurs.");

				if (np >= playerMin) {
					gameStarting = true;
					Bukkit.getScheduler().runTaskAsynchronously(plugin,
							new TimerThread());
				}
			}
		}

		return true;
	}

	public void tell(Player p, String msg) {
		p.sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "TNTGames"
				+ ChatColor.GRAY + "] " + ChatColor.RESET + msg);
	}

	public void say(String msg) {
		for (String pName : inGame) {
			Player p = Bukkit.getPlayer(pName);
			if (p != null)
				tell(p, msg);
		}
	}

	private int timerStarting = 6;// 31;

	private class TimerThread implements Runnable {
		@Override
		public void run() {
			Game.this.timerStarting--;

			boolean notify = false;
			String msg = ChatColor.GOLD + "La partie va commencer dans "
					+ ChatColor.RED + "!~!" + ChatColor.GOLD + " secondes.";
			switch (Game.this.timerStarting) {
			case 30:
			case 15:
			case 5:
			case 4:
			case 3:
			case 2:
			case 1:
			case 0:
				notify = true;
				break;
			}

			if (Game.this.timerStarting == 0) {
				Game.this.gameStarting = false;
				Game.this.gameStarted = true;
				msg = ChatColor.GOLD + "La partie commence !";

				Bukkit.getPluginManager().registerEvents(Game.this, plugin);
				for (String pName : inGame) {
					Player p = Bukkit.getPlayer(pName);
					if (p != null) {
						ItemFactory.addSpells(p.getInventory());
					}
				}
			}

			if (notify)
				say(msg.replace("!~!", String.valueOf(Game.this.timerStarting)));

			if (Game.this.gameStarting)
				Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,
						new TimerThread(), 20L);
		}
	}
}
