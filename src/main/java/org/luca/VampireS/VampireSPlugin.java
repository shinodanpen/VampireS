package org.luca.VampireS;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.luca.VampireS.commands.VampireSCommand;
import org.luca.VampireS.listeners.*;
import org.luca.VampireS.listeners.abilities.VampireItemRestorationAbility;
import org.luca.VampireS.listeners.abilities.VampireStopTimeAbility;
import org.luca.VampireS.resourcepack.CustomResourcePack;
import org.valdi.SuperApiX.bukkit.plugin.AbstractBukkitPlugin;
import org.valdi.SuperApiX.common.config.advanced.ConfigLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class VampireSPlugin extends AbstractBukkitPlugin<VampireSBootstrap> {
    public static final String PREFIX = "[prefix]";
    public static final String PREFIX_VALUE = "&8&l<&c&lVampireS&8&l>";

    private static VampireSPlugin instance;

    private File customConfigFile;
    private FileConfiguration customConfig;

    private File customConfigFile2;
    private FileConfiguration customConfig2;

    private ConfigLoader<Settings> settings;
    private CustomResourcePack customResourcePack;
    private ItemManager itemManager;

    private List<UUID> vampires = new ArrayList<>();
    private List<UUID> gods = new ArrayList<>();
    private List<UUID> noCooldown = new ArrayList<>();
    private List<UUID> noExpReq = new ArrayList<>();

    private List<UUID> stoptimevampire = new ArrayList<>();
    private List<UUID> fixitemvampire = new ArrayList<>();

    public VampireSPlugin(VampireSBootstrap bootstrap) {
        super(bootstrap);

        this.customResourcePack = new CustomResourcePack(this);
        this.itemManager = new ItemManager();

        instance = this;
    }

    @Override
    public void load() {
        super.load();

        this.getLocalesManager().init();

        settings = new ConfigLoader<>(this, Settings.class);
        settings.loadAnnotated();
        settings.save();

        if (getConfig().isResourcepackEnabled()) {
            customResourcePack.loadDependency();
        }
    }

    @Override
    public void enable() {
        super.enable();

        this.createVampireDatabase();
        this.createVampireAbilityDatabase();

        this.getCommandsManager().registerCommand(new VampireSCommand(this));
        registerListeners();
        registerRecipes();

        if (getConfig().isResourcepackEnabled()) {
            customResourcePack.onEnable();
        }

        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "VampireS successfully enabled.");
    }

    @Override
    public void disable() {
        super.disable();

        this.saveList();

        if (getConfig().isResourcepackEnabled()) {
            customResourcePack.onDisable();
        }

        this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "VampireS successfully disabled.");
    }

    public static VampireSPlugin getInstance() {
        return instance;
    }

    public Settings getConfig() {
        return settings.getConfig();
    }

    public void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        VampireSilverSword silverSword = new VampireSilverSword(this);
        VampireStopTimeAbility stoptimeClock = new VampireStopTimeAbility(this);
        VampireItemRestorationAbility fixitemRod = new VampireItemRestorationAbility(this);

        pm.registerEvents(new OnStonemaskUse(this), bootstrap);
        pm.registerEvents(new OnStonemaskActivation(this), bootstrap);
        pm.registerEvents(new VampireProperties(this), bootstrap);
        pm.registerEvents(new VampireTransformingPlayer(this), bootstrap);
        pm.registerEvents(silverSword, bootstrap);
        pm.registerEvents(stoptimeClock, bootstrap);
        pm.registerEvents(fixitemRod, bootstrap);
        pm.registerEvents(new VampireCreator(), bootstrap);

        getScheduler().runTaskTimer(new VampireSunDamage(this), 0L, 2L, TimeUnit.SECONDS);
        getScheduler().runTaskTimer(new VampireEffects(this), 0L, 2L, TimeUnit.SECONDS);
        getScheduler().runTaskTimerAsynchronously(silverSword, 0L, 500L, TimeUnit.MILLISECONDS);
        getScheduler().runTaskTimerAsynchronously(stoptimeClock, 0L, 500L, TimeUnit.MILLISECONDS);
        getScheduler().runTaskTimerAsynchronously(fixitemRod, 0L, 500L, TimeUnit.MILLISECONDS);
    }

    private void loadList() {
        List<String> vampiresUUID = this.getVampireDatabase().getStringList("VampireList");
        if (!vampiresUUID.isEmpty()) {
            for (String stringa : vampiresUUID) {
                vampires.add(UUID.fromString(stringa));
            }
        }

        List<String> godsUUID = this.getVampireDatabase().getStringList("NoDamageList");
        if (!godsUUID.isEmpty()) {
            for (String stringa : godsUUID) {
                gods.add(UUID.fromString(stringa));
            }
        }

        List<String> noCooldownUUID = this.getVampireDatabase().getStringList("NoCooldownList");
        if (!noCooldownUUID.isEmpty()) {
            for (String stringa : noCooldownUUID) {
                noCooldown.add(UUID.fromString(stringa));
            }
        }

        List<String> noExpReqUUID = this.getVampireDatabase().getStringList("NoAbilityExpRequirement");
        if (!noExpReqUUID.isEmpty()) {
            for (String stringa : noExpReqUUID) {
                noExpReq.add(UUID.fromString(stringa));
            }
        }

        List<String> stoptimevampireUUID = this.getVampireDatabase().getStringList("StopTimeVampireUsers");
        if (!stoptimevampireUUID.isEmpty()) {
            for (String stringa : stoptimevampireUUID) {
                noExpReq.add(UUID.fromString(stringa));
            }
        }

        List<String> fixitemvampireUUID = this.getVampireDatabase().getStringList("FixItemVampireUsers");
        if (!fixitemvampireUUID.isEmpty()) {
            for (String stringa : fixitemvampireUUID) {
                noExpReq.add(UUID.fromString(stringa));
            }
        }





        this.getVampireDatabase().getStringList("VampireList").forEach(s -> vampires.add(UUID.fromString(s)));
        this.getVampireDatabase().getStringList("NoDamageList").forEach(s -> gods.add(UUID.fromString(s)));
        this.getVampireDatabase().getStringList("NoCooldownList").forEach(s -> noCooldown.add(UUID.fromString(s)));
        this.getVampireDatabase().getStringList("NoAbilityExpRequirement").forEach(s -> noExpReq.add(UUID.fromString(s)));

        this.getVampireAbilityDatabase().getStringList("StopTimeVampireUsers").forEach(s -> stoptimevampire.add(UUID.fromString(s)));
        this.getVampireAbilityDatabase().getStringList("FixItemVampireUsers").forEach(s -> fixitemvampire.add(UUID.fromString(s)));
    }

    private void saveList() {
        this.getVampireDatabase().set("VampireList", vampires.stream().map(UUID::toString).collect(Collectors.toList()));
        this.getVampireDatabase().set("NoDamageList", gods.stream().map(UUID::toString).collect(Collectors.toList()));
        this.getVampireDatabase().set("NoCooldownList", noCooldown.stream().map(UUID::toString).collect(Collectors.toList()));
        this.getVampireDatabase().set("NoAbilityExpRequirement", noExpReq.stream().map(UUID::toString).collect(Collectors.toList()));

        this.getVampireAbilityDatabase().set("StopTimeVampireUsers", stoptimevampire.stream().map(UUID::toString).collect(Collectors.toList()));
        this.getVampireAbilityDatabase().set("FixItemVampireUsers", fixitemvampire.stream().map(UUID::toString).collect(Collectors.toList()));

        // Save file
        this.saveVampireDatabase();
        this.saveVampireAbilityDatabase();
    }

    public FileConfiguration getVampireDatabase() {
        return this.customConfig;
    }

    public FileConfiguration getVampireAbilityDatabase() {
        return this.customConfig2;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public CustomResourcePack getResourcePack() {
        return customResourcePack;
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

        customConfig = new YamlConfiguration(); // Creates a new bukkit config
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

    public List<UUID> getVampires() {
        return vampires;
    }

    public List<UUID> getNoDamage() {
        return gods;
    }

    public List<UUID> noCooldown() {
        return noCooldown;
    }

    public List<UUID> noExpReq() {
        return noExpReq;
    }


    public List<UUID> getStoptimevampire() {
        return stoptimevampire;
    }

    public List<UUID> getFixitemvampire() {
        return fixitemvampire;
    }


    private void createVampireAbilityDatabase() {
        customConfigFile2 = new File(getDataFolder(), "vampireabilitydatabase.yml"); // Define a file: vampireabilitydatabase.yml
        if (!customConfigFile2.exists()) { //If it doesn't exist...
            customConfigFile2.getParentFile().mkdirs(); // ...the plugin creates it with all the folders it needs
            try {
                customConfigFile2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        customConfig2 = new YamlConfiguration(); // Creates a new bukkit config
        try {
            customConfig2.load(customConfigFile2); // I load the old config in the new one
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void saveVampireAbilityDatabase() {
        try {
            customConfig2.save(customConfigFile2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Let's save the config actually loaded in the server's ram into a rea file
    }
    private void registerRecipes() {
        if (getConfig().isStonemaskEnableCrafting()) {
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(bootstrap, "stonemask"), getItemManager().getMask());
            recipe.shape("N#N", "#H#", "###");
            recipe.setIngredient('#', Material.STONE);
            recipe.setIngredient('N', Material.NETHER_STAR);
            recipe.setIngredient('H', Material.HEART_OF_THE_SEA);
            Bukkit.addRecipe(recipe);
        }

        if (getConfig().isSilverswordEnableCrafting()) {
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(bootstrap, "silversword"), getItemManager().getSilverSword());
            recipe.shape(" B ", "NIN", "GBG");
            recipe.setIngredient('B', Material.BLAZE_ROD);
            recipe.setIngredient('N', Material.NETHER_STAR);
            recipe.setIngredient('G', Material.GOLD_INGOT);
            recipe.setIngredient('I', Material.IRON_SWORD);
            Bukkit.addRecipe(recipe);
        }

        if (getConfig().isStoptimeClockEnableCrafting()) {
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(bootstrap, "stoptimeclock"), getItemManager().getStopTimeClock());
            recipe.shape("CNC", "NCN", "CNC");
            recipe.setIngredient('C', Material.CLOCK);
            recipe.setIngredient('N', Material.NETHER_STAR);
            Bukkit.addRecipe(recipe);
        }
    }

}
