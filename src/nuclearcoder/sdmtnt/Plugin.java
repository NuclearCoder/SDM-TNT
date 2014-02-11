package nuclearcoder.sdmtnt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	private Game game;

	@Override
	public void onLoad() {
		game = new Game(this, 1, 1);
	}

	@Override
	public void onEnable() {
		getCommand("jointntgames").setExecutor(game);
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}
}
