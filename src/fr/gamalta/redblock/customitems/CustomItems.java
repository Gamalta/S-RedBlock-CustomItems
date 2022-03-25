package fr.gamalta.redblock.customitems;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.lib.database.SqlConnection;
import fr.gamalta.lib.logger.LogFormatter;
import fr.gamalta.redblock.customitems.blocks.BlockManager;
import fr.gamalta.redblock.customitems.commands.CustomItemsCmd;
import fr.gamalta.redblock.customitems.items.ItemManager;
import fr.gamalta.redblock.customitems.listeners.onBlockBreakEvent;
import fr.gamalta.redblock.customitems.listeners.onBlockPhysicsEvent;
import fr.gamalta.redblock.customitems.listeners.onBlockPistonExtendEvent;
import fr.gamalta.redblock.customitems.listeners.onBlockPistonRetractEvent;
import fr.gamalta.redblock.customitems.listeners.onBlockPlaceEvent;
import fr.gamalta.redblock.customitems.listeners.onEntityExplodeEvent;
import fr.gamalta.redblock.customitems.listeners.onPlayerInteractEvent;
import fr.gamalta.redblock.customitems.listeners.onPlayerItemHeldEvent;
import fr.gamalta.redblock.customitems.listeners.onPlayerJoinEvent;
import fr.gamalta.redblock.customitems.listeners.onPlayerResourcePackStatusEvent;
import fr.gamalta.redblock.customitems.listeners.onPrepareItemCraftEvent;
import fr.gamalta.redblock.customitems.listeners.onStructureGrowEvent;
import fr.gamalta.redblock.customitems.utils.Utils;

public class CustomItems extends JavaPlugin {

	// ajouter sauvegarde regulière drawer
	// drawers explosion/piston etc

	public static CustomItems customItems;
	public static SqlConnection sqlConnection;

	public static Connection getConnection() {
		return sqlConnection.getConnection();
	}

	public static CustomItems getInstance() {
		return customItems;
	}

	public String parentFileName = "Custom Items";
	public BlockManager blockManager = new BlockManager(this);
	public FileHandler fileHandler;
	public ItemManager itemManager = new ItemManager(this);
	public Logger logger = getLogger();
	public Boolean resourcePackEnable = true;
	public Configuration messagesCFG = new Configuration(this, parentFileName, "Messages");
	public Configuration settingsCFG = new Configuration(this, parentFileName, "Settings");

	public String getParentFileName() {
		return parentFileName;
	}

	private void initCommands() {

		getCommand("CustomItems").setExecutor(new CustomItemsCmd(this));

	}

	private void initListeners() {

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new onPlayerJoinEvent(this), this);
		pm.registerEvents(new onPlayerResourcePackStatusEvent(this), this);
		pm.registerEvents(new onStructureGrowEvent(this), this);
		pm.registerEvents(new onBlockPlaceEvent(this), this);
		pm.registerEvents(new onBlockPhysicsEvent(this), this);
		pm.registerEvents(new onPlayerItemHeldEvent(this), this);
		pm.registerEvents(new onPrepareItemCraftEvent(this), this);
		pm.registerEvents(new onBlockBreakEvent(this), this);
		pm.registerEvents(new onPlayerInteractEvent(this), this);
		pm.registerEvents(new onBlockPistonExtendEvent(this), this);
		pm.registerEvents(new onBlockPistonRetractEvent(this), this);
		pm.registerEvents(new onEntityExplodeEvent(this), this);

	}

	private void initLogger() {

		if (settingsCFG.getBoolean("Log.File")) {

			File log = new File("plugins/RedBlock/Log/CustomItems.log");

			if (!log.getParentFile().exists()) {
				log.getParentFile().mkdir();
			}

			if (!log.exists()) {

				try {
					log.createNewFile();

				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			try {

				fileHandler = new FileHandler(log.getAbsolutePath(), true);

			} catch (IOException iOException) {

				iOException.printStackTrace();
				return;
			}

			fileHandler.setFormatter(new LogFormatter());
			logger.addHandler(fileHandler);
			logger.setUseParentHandlers(false);
			logger.info("Enabling CustomItems v1.0");

			if (settingsCFG.getBoolean("Log.Console")) {

				logger.setUseParentHandlers(true);
			}
		}
	}

	private void initSqlConnection() {

		sqlConnection = new SqlConnection(settingsCFG, "SqlConnection", this, "CustomItems");
		Statement statement = null;

		try {

			statement = sqlConnection.getConnection().createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS `s5_developpement`.`Drawers` (`id` INT NOT NULL AUTO_INCREMENT, `World` VARCHAR(10000) NOT NULL , `X` INT NOT NULL, `Y` INT NOT NULL, `Z` INT NOT NULL, `ItemStack` VARCHAR(10000) , `Amount` INT ,  PRIMARY KEY (id)) COLLATE utf8_general_ci");

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {

			if (statement != null) {

				try {

					statement.close();

				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}
	}

	private void initTabCompletes() {

		getCommand("CustomItems").setTabCompleter(new CustomItemsCmd(this));

	}

	@Override
	public void onDisable() {

		if (fileHandler != null) {

			fileHandler.close();
			getLogger().removeHandler(fileHandler);
		}

		blockManager.disable();
	}

	@Override
	public void onEnable() {

		customItems = this;
		new Utils(this);

		initSqlConnection();

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		itemManager.init();
		blockManager.init();

		initListeners();
		initCommands();
		initTabCompletes();
		initLogger();
	}
}