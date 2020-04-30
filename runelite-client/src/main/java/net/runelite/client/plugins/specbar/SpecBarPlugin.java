package net.runelite.client.plugins.specbar;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.api.Client;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
/*
@PluginDescriptor(
        name = "Spec Bar",
        description = "Adds a spec bar to every weapon",
        tags = {"spec bar", "special attack", "spec", "bar", "pklite"},
        enabledByDefault = false
)*/
@Singleton
public class SpecBarPlugin extends Plugin {

    @Inject
    private Client client;

    @Subscribe
    private void onScriptCallbackEvent(ScriptCallbackEvent event) {
        if (!"drawSpecbarAnyway".equals(event.getEventName())) {
            return;
        }

        int[] iStack = client.getIntStack();
        int iStackSize = client.getIntStackSize();
        iStack[iStackSize - 1] = 1;
    }
}