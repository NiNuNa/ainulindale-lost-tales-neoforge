package dev.ninuna.losttales.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import dev.ninuna.losttales.client.keymapping.LostTalesRegisterKeyMappingsEvent;

@OnlyIn(Dist.CLIENT)
public class LostTalesQuestJournalScreen extends Screen {
    private static final Component QUEST_JOURNAL_TITLE = Component.translatable("questJournal.title");

    private final Screen parentScreen;

    public LostTalesQuestJournalScreen(Screen parentScreen) {
        super(QUEST_JOURNAL_TITLE);
        this.parentScreen = parentScreen;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 100, 16777215);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (LostTalesRegisterKeyMappingsEvent.QUEST_JOURNAL_MAPPING.get().matches(keyCode, scanCode)) {
            this.minecraft.setScreen(null);
            return true;
        } else if (LostTalesRegisterKeyMappingsEvent.CHARACTER_MENU_MAPPING.get().matches(keyCode, scanCode)) {
            this.minecraft.setScreen(new LostTalesCharacterMenuScreen(minecraft.screen));
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parentScreen);
    }
}
