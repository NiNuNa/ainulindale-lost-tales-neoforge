package ninuna.losttales.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import ninuna.losttales.client.event.LostTalesRegisterKeyMappingsEvent;

@OnlyIn(Dist.CLIENT)
public class LostTalesCharacterMenuScreen extends Screen {
    public static final Component CHARACTER_MENU_TITLE = Component.translatable("characterMenu.title");

    private final Screen parentScreen;

    public LostTalesCharacterMenuScreen(Screen parentScreen) {
        super(CHARACTER_MENU_TITLE);
        this.parentScreen = parentScreen;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 100, 16777215);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (LostTalesRegisterKeyMappingsEvent.CHARACTER_MENU_MAPPING.get().matches(keyCode, scanCode)) {
            this.minecraft.setScreen(null);
            return true;
        } else if (LostTalesRegisterKeyMappingsEvent.QUEST_JOURNAL_MAPPING.get().matches(keyCode, scanCode)) {
            this.minecraft.setScreen(new LostTalesQuestJournalScreen(minecraft.screen));
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
