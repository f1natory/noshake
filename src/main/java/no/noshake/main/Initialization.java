package no.noshake.main;

import net.fabricmc.api.ClientModInitializer;
import no.noshake.api.config.ConfigManager;

public class Initialization implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.get().load();
    }
}
