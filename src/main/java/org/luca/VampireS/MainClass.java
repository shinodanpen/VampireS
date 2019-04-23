package org.luca.VampireS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class MainClass extends JavaPlugin {
	String plPrefix = "&7&l[&c&lVampireS&7&l] &8> ";
	private List<UUID> vampires = new ArrayList<>();
	private List<UUID> gods = new ArrayList<>();
	private List<UUID> noCooldown = new ArrayList<>();
	
    private File customConfigFile;
    private FileConfiguration customConfig;
    
    private static MainClass instance;
    
    public MainClass() {
    	instance = this;
    }
    
    public static MainClass getInstance() {
    	return instance;
    }

	@Override
	public void onEnable() {		
		this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"VampireS successfully enabled.");
		this.saveDefaultConfig(); //Copy the config.yml file that's in the jar inside the VampireS folder in the server's plugins folder
		this.reloadConfig(); //load the updated config
		
		this.createVampireDatabase();
		this.loadList();
		
		this.getCommand("vampires").setExecutor(new VampiresCommand(this));

		VampireSilverSwordDamage silverSword = new VampireSilverSwordDamage(this);
		VampireStopTimeAbility stoptimeClock = new VampireStopTimeAbility(this);
		
		getServer().getPluginManager().registerEvents(new OnStonemaskUse(this), this);
		getServer().getPluginManager().registerEvents(new OnStonemaskActivation(this), this);
		getServer().getPluginManager().registerEvents(new VampireBiteTransformation(this), this);
		getServer().getPluginManager().registerEvents(new CustomResourcePack(this), this);
		getServer().getPluginManager().registerEvents(silverSword, this);
		getServer().getPluginManager().registerEvents(stoptimeClock, this);
		
		getServer().getScheduler().runTaskTimer(this, new VampireSunDamage(this), 0L, 40L);
		getServer().getScheduler().runTaskTimerAsynchronously(this, silverSword, 0L, 10L);
		getServer().getScheduler().runTaskTimerAsynchronously(this, stoptimeClock, 0L, 10L);
		
		registerRecipes();
	}

	@Override
	public void onDisable() {
		this.saveList();
		this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW+"VampireS successfully disabled.");
	}
	
	private void loadList() {
		List<String> vampiresUUID = this.getVampireDatabase().getStringList("VampireList");
		if(vampiresUUID != null && !vampiresUUID.isEmpty()) {
			for(String stringa : vampiresUUID) {
				vampires.add(UUID.fromString(stringa));
			}
		}

		List<String> godsUUID = this.getVampireDatabase().getStringList("NoDamageList");
		if(godsUUID != null && !godsUUID.isEmpty()) {
			for(String stringa : godsUUID) {
				gods.add(UUID.fromString(stringa));
			}
		}
		
		List<String> noCooldownUUID = this.getVampireDatabase().getStringList("NoStoptimeCooldownList");
		if(noCooldownUUID != null && !noCooldownUUID.isEmpty()) {
			for(String stringa : noCooldownUUID) {
				noCooldown.add(UUID.fromString(stringa));
			}
		}

		Optional.ofNullable(this.getVampireDatabase().getStringList("VampireList")).ifPresent(l -> l.forEach(s -> vampires.add(UUID.fromString(s))));
		Optional.ofNullable(this.getVampireDatabase().getStringList("NoDamageList")).ifPresent(l -> l.forEach(s -> gods.add(UUID.fromString(s))));
		Optional.ofNullable(this.getVampireDatabase().getStringList("NoStoptimeCooldownList")).ifPresent(l -> l.forEach(s -> noCooldown.add(UUID.fromString(s))));
	}

	private void saveList () {
		this.getVampireDatabase().set("VampireList", vampires.stream().map(UUID::toString).collect(Collectors.toList()));
		this.getVampireDatabase().set("NoDamageList", gods.stream().map(UUID::toString).collect(Collectors.toList()));
		this.getVampireDatabase().set("NoStoptimeCooldownList", noCooldown.stream().map(UUID::toString).collect(Collectors.toList()));
		
		// Save file
		this.saveVampireDatabase();
	}

    public FileConfiguration getVampireDatabase() {
        return this.customConfig;
    }

    private void createVampireDatabase() {
        customConfigFile = new File(getDataFolder(), "vampiredatabase.yml"); // Define a file: vampiredatabase.yml
        if (!customConfigFile.exists()) { //If it doesn't exist...
            customConfigFile.getParentFile().mkdirs(); // ...the plugin creates it with all the folders it needs
            try {
				customConfigFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
         }

        customConfig= new YamlConfiguration(); // Creates a new bukkit config
        try {
            customConfig.load(customConfigFile); // I load the old config in the new one
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void saveVampireDatabase() {
    	try {
			customConfig.save(customConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	// Let's save the config actually loaded in the server's ram into a rea file
    }
	
	public boolean isMask(ItemStack item) {
		if(item == null) {
			return false; // This can't be the item if the hand is empty
		}
		
		ItemMeta meta = item.getItemMeta();		
		return item.getType()==Material.DIAMOND_HOE 
				&& meta.isUnbreakable() 
				&& meta instanceof Damageable 
				&& ((Damageable) meta).getDamage() == 1462
				&& meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&8&lStone Mask"));
		/* The item is a Stone Mask if it's:
		 * a Diamond hoe
		 * unbreakable
		 * damageable
		 * damage to the durability equals 1462
		 * the displayname is &8&lStone Mask
		 */
	}
	
	public boolean isSilverSword(ItemStack item) {
		if(item == null) {
			return false;
		}
		
		ItemMeta meta = item.getItemMeta();		
		return item.getType()==Material.IRON_SWORD 
				&& meta.isUnbreakable() 
				&& meta instanceof Damageable 
				&& ((Damageable) meta).getDamage() == 151
				&& meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&e&lSilver Sword"));
		/* The item is a Silver Sword if it's:
		 * an Iron Sword
		 * Unbreakable
		 * damageable
		 * damage to durability equals 151
		 * the displayname is &e&lSilver Sword
		 */
	}
	
	public boolean isStopTimeClock(ItemStack item) {
		if(item == null) {
			return false;
		}		
		ItemMeta meta = item.getItemMeta();
		return item.getType()==Material.CLOCK
				&& meta.isUnbreakable()
				&& meta instanceof Damageable
				&& ((Damageable) meta).getDamage() == 151
				&& meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&6&lStop Time Clock"));
		/* The item is a Stop Time Clock if it's
		 * a Clock
		 * Unbreakable
		 * Damageable
		 * damage equals to 151
		 * display name is &6&lStop Time Clock
		 */
	}
	
	public List<UUID> getVampires() {
		return vampires;
	}
	
	public List<UUID> getNoDamage() {
		return gods;
	}
	
	public List<UUID> noStoptimeCooldown() {
		return noCooldown;
	}

	private void registerRecipes() {
		ShapedRecipe stonemask = new ShapedRecipe(new NamespacedKey(this, "stonemask"), VampiresCommand.getMask());
		stonemask.shape("N#N", "#H#", "###");
		stonemask.setIngredient('#', Material.STONE);
		stonemask.setIngredient('N', Material.NETHER_STAR);
		stonemask.setIngredient('H', Material.HEART_OF_THE_SEA);
		if(getConfig().getBoolean("stonemask.enablecrafting")) {
			Bukkit.addRecipe(stonemask);
			}
		
		ShapedRecipe silversword = new ShapedRecipe(new NamespacedKey(this, "silversword"), VampiresCommand.getSilverSword());
		silversword.shape(" B ", "NIN", "GBG");
		silversword.setIngredient('B', Material.BLAZE_ROD);
		silversword.setIngredient('N', Material.NETHER_STAR);
		silversword.setIngredient('G', Material.GOLD_INGOT);
		silversword.setIngredient('I', Material.IRON_SWORD);
		if(getConfig().getBoolean("silversword.enablecrafting")) {
			Bukkit.addRecipe(silversword);
			}
		
		ShapedRecipe stoptimeclock = new ShapedRecipe(new NamespacedKey(this, "stoptimeclock"), VampiresCommand.getStopTimeClock());
		stoptimeclock.shape("CNC", "NCN", "CNC");
		stoptimeclock.setIngredient('C', Material.CLOCK);
		stoptimeclock.setIngredient('N', Material.NETHER_STAR);
		if(getConfig().getBoolean("stoptime.stoptimeclock.enablecrafting")) {
				Bukkit.addRecipe(stoptimeclock);
				}
	}
	
}
