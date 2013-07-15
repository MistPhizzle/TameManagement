package com.etriacraft.tamemanagement;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TameManagement extends JavaPlugin {

	protected static Logger log;
	public static TameManagement instance;

	File configFile;
	FileConfiguration config;

	Commands cmd;

	private final MobListener moblistener = new MobListener (this);

	@Override
	public void onEnable() {
		instance = this;
		TameManagement.log = this.getLogger();

		configCheck();

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(moblistener, this);

		cmd = new Commands(this);

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
			// Failed to Submit Stats
		}

		if (getConfig().getBoolean("RespectGriefPrevention")) {
			if (getServer().getPluginManager().getPlugin("GriefPrevention") != null) {
				TameManagement.log.info("GriefPrevention Support enabled.");
				MobListener.griefpreventionsupport = true;
			} else {
				TameManagement.log.info("GriefPrevention not found.");
				MobListener.griefpreventionsupport = false;
			}
		}
		MobListener.Prefix = colorize(getConfig().getString("messages.Prefix"));
		MobListener.horseClaimed = colorize(getConfig().getString("messages.listener.horseClaimed"));
		MobListener.animalDoesNotBelongToYou = colorize(getConfig().getString("messages.listener.animalDoesNotBelongToYou"));
		MobListener.horseAlreadyOwned = colorize (getConfig().getString("messages.listener.horseAlreadyOwned"));
		MobListener.cantInteractWithHorse = colorize(getConfig().getString("messages.listener.cantInteractWithHorse"));
		MobListener.cantChangeStyle = colorize(getConfig().getString("messages.listener.cantChangeStyle"));
		MobListener.styleChanged = colorize(getConfig().getString("messages.listener.styleChanged"));
		MobListener.cantChangeColor = colorize(getConfig().getString("messages.listener.cantChangeColor"));
		MobListener.colorChanged = colorize(getConfig().getString("messages.listener.colorChanged"));
		MobListener.cantChangeVariant = colorize(getConfig().getString("messages.listener.cantChangeVariant"));
		MobListener.changedVariant = colorize(getConfig().getString("messages.listener.changedVariant"));
		MobListener.doesNotOwn = colorize(getConfig().getString("messages.listener.doesNotOwn"));
		MobListener.animalReleased = colorize(getConfig().getString("messages.listener.animalReleased"));
		MobListener.animalTransferred = colorize(getConfig().getString("messages.listener.animalTransferred"));
	}

	public static TameManagement getInstance() {
		return instance;
	}

	public void configReload() {
		reloadConfig();
	}

	public void configCheck() {
		getConfig().addDefault("RespectGriefPrevention", false);
		getConfig().addDefault("ProtectTames", true);
		getConfig().addDefault("AllowTransfers", true);
		getConfig().addDefault("AllowReleases", true);
		getConfig().addDefault("ProtectHorses", true);
		getConfig().addDefault("Breeding.Horse", true);
		getConfig().addDefault("Breeding.Wolf", true);
		getConfig().addDefault("Breeding.Ocelot", true);

		getConfig().addDefault("messages.Prefix", "&7[&6TameManagement&7] ");
		getConfig().addDefault("messages.listener.animalDoesNotBelongToYou", "&cYou cant damage an animal that doesnt belong to you.");
		getConfig().addDefault("messages.listener.horseAlreadyOwned", "&cThis horse is already owned by &3%owner&c.");
		getConfig().addDefault("messages.listener.cantInteractWithHorse", "&cYou cant interact with a horse belonging to &3&owner&c.");
		getConfig().addDefault("messages.listener.cantChangeStyle", "You cant change the style on &3%owners &chorse.");
		getConfig().addDefault("messages.listener.styleChanged", "&aHorse style changed.");
		getConfig().addDefault("messages.listener.cantChangeColor", "&cYou cant change the color of &3%owners &chorse.");
		getConfig().addDefault("messages.listener.colorChanged", "&aHorse color changed.");
		getConfig().addDefault("messages.listener.cantChangeVariant", "&cYou cant change the variant of &3%owners &chorse.");
		getConfig().addDefault("messages.listener.changedVariant", "&aHorse variation changed.");
		getConfig().addDefault("messages.listener.doesNotOwn", "&cThis animal belongs to &3%owner&c.");
		getConfig().addDefault("messages.listener.animalReleased", "&aYou have released this animal to the wild.");
		getConfig().addDefault("messages.listener.animalTransferred", "&aYou have transferred this animal to &3%newowner");
		getConfig().addDefault("messages.listener.horseClaimed", "&aYou now own this horse.");
		// getConfig().set("ConfigVersion", 120); no longer needed
		getConfig().options().copyDefaults(true);

		saveConfig();
	}

	public static String colorize(String message) {
		return message.replaceAll("(?i)&([a-fk-or0-9])", "\u00A7$1");
	}
	
}
