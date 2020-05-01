package com.kritiko.listener;

import com.kritiko.ChatShow;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;

/**
 * This listener listens onto the chat event which is fired whenever a client send a message into the chat
 */
public class ChatListener {

    /**
     * This method delegates the handling of the event into the controller
     *
     * @param event The chat event that gets called whenever a client-message is sent
     * @param p     The player that sent this message
     */
    @Listener
    public void onPlayerChat(MessageChannelEvent.Chat event, @First Player p) {
        ChatShow.getInstance().getPlaceholderController().manageChatEvent(event, p);
    }
}