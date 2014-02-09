package nuclearcoder.sdmtnt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	private Game game;

	@Override
	public void onLoad() {
		game = new Game(this);
	}

	@Override
	public void onEnable() {
		getCommand("jointntgames").setExecutor(game);

		Bukkit.getPluginManager().registerEvents(game, this);
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}
}
