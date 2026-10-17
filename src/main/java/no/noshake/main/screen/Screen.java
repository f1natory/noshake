package no.noshake.main.screen;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import no.noshake.api.config.ConfigManager;

public class Screen extends OptionsSubScreen {
    private static final int width = 310;
    private static final int height = 20;

    private StrengthSlider strengthSlider;

    public Screen(net.minecraft.client.gui.screens.Screen lastScreen, Options options) {
        super(lastScreen, options, Component.translatable("noshake.config.title"));
    }

    @Override
    protected void addOptions() {
        ConfigManager config = ConfigManager.get();

        Checkbox customBob = Checkbox.builder(Component.translatable("noshake.config.custom_bob"), this.font)
                .selected(config.isCustomBob())
                .onValueChange((checkbox, value) -> {
                    config.setCustomBob(value);
                    if (this.strengthSlider != null) {
                        this.strengthSlider.active = value;
                    }
                })
                .build();
        this.list.addBig(customBob);

        this.strengthSlider = new StrengthSlider(0, 0, width, height, config.getDownStrength());
        this.strengthSlider.active = config.isCustomBob();
        this.list.addBig(this.strengthSlider);
    }

    @Override
    public void onClose() {
        ConfigManager.get().save();
        super.onClose();
    }

    private static final class StrengthSlider extends AbstractSliderButton {
        private StrengthSlider(int x, int y, int width, int height, float initialStrength) {
            super(x, y, width, height, Component.empty(), initialStrength / ConfigManager.maxdownstrength);
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int percent = (int) Math.round(this.value * ConfigManager.maxdownstrength * 100.0);
            this.setMessage(Component.translatable("noshake.config.hand_drop", percent));
        }

        @Override
        protected void applyValue() {
            ConfigManager.get().setDownStrength((float) this.value * ConfigManager.maxdownstrength);
        }
    }
}
