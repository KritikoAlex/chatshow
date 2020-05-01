package com.kritiko.controller;

import com.kritiko.ChatShow;
import ninja.leaping.configurate.ConfigurationNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This config controller manages the config by creating and loading it into the system
 */
public class ConfigController {

    /**
     * The hoverMessage list that serves as a presentation of possibilities when creating the config
     */
    private static final List<String> hoverMessages = new ArrayList<>(Arrays.asList("&eName: <prefix>_nickname%", "&4First Powermove: <prefix>_moveset_1%"));

    /**
     * This method creates the config in the ROOT/config/chatshow.conf
     */
    public void createConfig() {
        try {
            if (!ChatShow.getInstance().getConfigFile().exists()) {
                ChatShow.getInstance().getConfigFile().createNewFile();
                ChatShow.getInstance().setConfigurationNode(ChatShow.getInstance().getConfig().load());
                ChatShow.getInstance().getConfigurationNode().getNode("hover_messages").setValue(hoverMessages);
                ChatShow.getInstance().getConfig().save(ChatShow.getInstance().getConfigurationNode());
            }
            ChatShow.getInstance().setConfigurationNode(ChatShow.getInstance().getConfig().load());
            readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads in the list of all hover lines
     */
    public static void readConfig() {
        ConfigurationNode config = ChatShow.getInstance().getConfigurationNode();
        ChatShow.hoverMessages = config.getNode("hover_messages").getChildrenList().stream()
                .map(ConfigurationNode::getString)
                .collect(Collectors.toList());
    }
}
