package com.kritiko.controller;

import com.kritiko.ChatShow;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.PixelmonPlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.regex.Pattern;

/**
 * This placeholder controller manages the replacement of the chat messages so pokemons' specs will successfully be displayed
 */
public class PlaceholderController {

    /**
     * This method handles the chat event of the server and replaces the message of this event if a player used the right placeholder.
     * This method gets called from the listener that delegates it to this controller
     *
     * @param event The chat event that gets called whenever a client-message is sent
     * @param p     The player that sent this message
     */
    public void manageChatEvent(MessageChannelEvent.Chat event, Player p) {
        if (!(p instanceof EntityPlayerMP)) return;
        EntityPlayerMP mp = (EntityPlayerMP) p;

        // This section is to display the currently held item in chat
        if (event.getRawMessage().toPlain().matches("(.*)\\{item}(.*)")) {
            if (p.getItemInHand(HandTypes.MAIN_HAND).isPresent() && !p.getItemInHand(HandTypes.MAIN_HAND).get().isEmpty()) {
                Text text = Text.builder()
                        .color(TextColors.GOLD)
                        .style(TextStyles.BOLD)
                        .append(Text.of(p.getItemInHand(HandTypes.MAIN_HAND).get().getTranslation().get()))
                        .onHover(TextActions.showItem(p.getItemInHand(HandTypes.MAIN_HAND).get().createSnapshot()))
                        .build();
                event.setMessage(Text.of(event.getMessage().replace(Pattern.compile("\\{item}"), text)));
            } else {
                p.sendMessage(Text.of(TextColors.RED, "You do not have an item in your hand!"));
                event.setCancelled(true);
                return;
            }
        }

        //Check if this message contains a valid pokemon placeholder
        //Only messages that contain a '{pokemon*NUM*}' where NUM equals 1-6
        if (!event.getRawMessage().toPlain().matches("(.*)\\{pokemon[1-6]}(.*)")) return;

        PlayerPartyStorage pps = Pixelmon.storageManager.getParty(mp);
        Text message = event.getMessage();

        while (Pattern.matches("(.*)\\{pokemon[1-6]}(.*)", message.toPlain())) {
            String num = message.toPlain().split("\\{pokemon", 2)[1].split("}", 2)[0];
            //Gets converted to the number that is used internally because internally the list begins at index 0 instead of 1
            int number = Integer.parseInt(num) - 1;

            //Check if this slot even contains a pokemon
            if (pps.get(number) == null) {
                p.sendMessage(Text.of(TextColors.RED, "There is no pokemon in slot " + (number + 1) + "!"));
                event.setCancelled(true);
                return;
            }

            //The hover message
            Text.Builder builder = Text.builder();
            ChatShow.hoverMessages.forEach(x -> {
                String replaced = x.replaceAll("<prefix>", "%party_" + (number + 1));
                builder.append(ChatShow.getInstance().getPlaceholderService().replacePlaceholders(replaced, p, p)).append(Text.NEW_LINE);
            });

            //The pokemon text with the hover effect
            Text text = Text.builder()
                    .color(TextColors.GOLD)
                    .style(TextStyles.BOLD)
                    .append(ChatShow.getInstance().getPlaceholderService().replacePlaceholders("%party_" + (number + 1) + "_nickname%", p, p))
                    .onHover(TextActions.showText(builder.build()))
                    .build();

            //Replace the pokemon placeholder with the actual text with the hover message
            message = message.replace(Pattern.compile("\\{pokemon[" + (number + 1) + "]}"), text);
        }
        event.setMessage(Text.of(message));
    }
}
