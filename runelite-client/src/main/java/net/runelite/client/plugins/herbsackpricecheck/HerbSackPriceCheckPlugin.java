
package net.runelite.client.plugins.herbsackpricecheck;

import net.runelite.client.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.http.api.item.Item;
import net.runelite.http.api.item.ItemPrice;
import net.runelite.client.chat.ChatMessageBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.runelite.api.ItemID.HERB_SACK;

@PluginDescriptor(
        name = "[R] Herb sack price check",
        description = "Price checks the herbs in herb sack",
        tags = {"herbs", "prices", "ritz"}
)

@Slf4j
public class HerbSackPriceCheckPlugin extends Plugin {
    private static boolean gettingHerbs = false;
    private static ArrayList<ChatMessage> herbsInChatMessage = new ArrayList<>();

    @Inject
    private ItemManager itemManager;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private Client client;

    @Subscribe
    private void onMenuOptionClicked(MenuOptionClicked event) {
        if (!event.getMenuOption().equals("Check") || event.getId() != HERB_SACK) {
            return;
        }
        gettingHerbs = true;
    }

    @Subscribe
    private void onChatMessage(ChatMessage message) {
        if (message.getType() != ChatMessageType.GAMEMESSAGE)
            return;

        if (message.getMessage().contains("x Grimy") && gettingHerbs) {
            herbsInChatMessage.add(message);
        }
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (gettingHerbs && !herbsInChatMessage.isEmpty()) {
            HashMap<String, Integer> herbsWithQuantity = reformatHerbChatMessages();
            int totalValue = herbPriceLookup(herbsWithQuantity);

            if (totalValue != -1) {
                buildValueMessage(totalValue);
            }

            gettingHerbs = false;
            herbsInChatMessage = new ArrayList<>();
        }
    }

    private HashMap<String, Integer> reformatHerbChatMessages() {
        HashMap<String, Integer> herbsWithQuantity = new HashMap<>();

        for (ChatMessage message : herbsInChatMessage) {
            String[] fullHerbName = message.getMessage().split("x");

            for (int i = 0; i < fullHerbName.length; i += 2) {
                herbsWithQuantity.put(fullHerbName[i + 1].trim(), Integer.parseInt(fullHerbName[i].trim()));
            }
        }

        return herbsWithQuantity;
    }

    private int herbPriceLookup(HashMap<String, Integer> herbsWithQuantity) {
        int totalValue = 0;
        for (Map.Entry<String, Integer> herbQuant : herbsWithQuantity.entrySet()) {
            List<ItemPrice> results = itemManager.search(herbQuant.getKey());

            if (results != null && !results.isEmpty()) {
                for (ItemPrice result : results) {
                    totalValue += result.getPrice() * herbQuant.getValue();
                }
            }
        }

        return totalValue;
    }

    private Item retrieveFromList(List<Item> items, String herbName) {
        for (Item item : items) {
            if (item.getName().toLowerCase().equals(herbName.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    private void buildValueMessage(int totalValue) {
        final ChatMessageBuilder output = new ChatMessageBuilder()
                .append(ChatColorType.NORMAL)
                .append("Total value of herbs in sack: ")
                .append(ChatColorType.HIGHLIGHT)
                .append(QuantityFormatter.formatNumber(totalValue));

        chatMessageManager.queue(QueuedMessage.builder()
                .type(ChatMessageType.ITEM_EXAMINE)
                .runeLiteFormattedMessage(output.build())
                .build());
    }
}