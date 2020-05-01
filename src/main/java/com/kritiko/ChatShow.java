package com.kritiko;

import com.google.inject.Inject;
import com.kritiko.controller.ConfigController;
import com.kritiko.controller.PlaceholderController;
import com.kritiko.listener.ChatListener;
import me.rojo8399.placeholderapi.PlaceholderService;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is the main class of the plugin. It registers the listener and holds the controllers.
 * With this plugin players can show their pokemon's specs in chat to other players.
 * I am using the placeholder API which is not my work and can be found here: https://www.spigotmc.org/resources/placeholderapi.6245/
 * All 'Trainer's party info' tags from pixelmons placeholder plugin can be used in this plugin (A list can be found here: https://github.com/happyzleaf/PixelmonPlaceholders/wiki/Placeholders)
 */
@Plugin(id = "chatshow",
        name = "ChatShow",
        version = "1.0",
        description = "With this plugin players can show off their pokemon in chat",
        dependencies = {@Dependency(id = "placeholderapi")})
public class ChatShow {

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File configFile;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> config;

    @Inject
    @DefaultConfig(sharedRoot = true)
    ConfigurationLoader<CommentedConfigurationNode> loader;

    private CommentedConfigurationNode configurationNode;

    private static ChatShow instance;

    private PlaceholderService placeholderService;

    public static List<String> hoverMessages;

    private PlaceholderController placeholderController = new PlaceholderController();

    private ConfigController configController = new ConfigController();

    @Listener
    public void onEnable(GameInitializationEvent e) {
        placeholderService = Sponge.getServiceManager().provideUnchecked(PlaceholderService.class);
        instance = this;
        Sponge.getEventManager().registerListeners(this, new ChatListener());
        this.configController = new ConfigController();
        this.configController.createConfig();
        System.out.println("ChatShow  ~ by Kritiko");
        System.out.println("Loaded!");
    }

    @Listener
    public void onReload(GameReloadEvent event) throws IOException {
        this.configurationNode = this.loader.load();
        this.configController.createConfig();
    }

    public static ChatShow getInstance() {
        return instance;
    }

    public File getConfigFile() {
        return configFile;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getConfig() {
        return this.config;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return this.loader;
    }

    public void setConfigurationNode(CommentedConfigurationNode configurationNode) {
        this.configurationNode = configurationNode;
    }

    public PlaceholderService getPlaceholderService() {
        return placeholderService;
    }

    public CommentedConfigurationNode getConfigurationNode() {
        return this.configurationNode;
    }

    public PlaceholderController getPlaceholderController() {
        return placeholderController;
    }
}
