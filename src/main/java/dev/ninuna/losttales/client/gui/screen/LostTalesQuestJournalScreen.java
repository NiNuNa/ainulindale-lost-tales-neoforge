package dev.ninuna.losttales.client.gui.screen;

import dev.ninuna.losttales.client.cache.LostTalesClientQuestCache;
import dev.ninuna.losttales.client.gui.LostTalesColor;
import dev.ninuna.losttales.client.gui.screen.widget.LostTalesQuestListWidget;
import dev.ninuna.losttales.client.gui.screen.widget.entry.LostTalesQuestListWidgetEntry;
import dev.ninuna.losttales.client.keymapping.LostTalesKeyMappingRegistry;
import dev.ninuna.losttales.common.datapack.loader.LostTalesQuestDatapackLoader;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class LostTalesQuestJournalScreen extends Screen {
    public static final Component QUEST_JOURNAL_TITLE = Component.translatable("questJournal.title");
    private static final int QUEST_LIST_WIDGET_WIDTH = 248;

    private final Screen parentScreen;
    private LostTalesQuestListWidget questListWidget;

    public LostTalesQuestJournalScreen(Screen parentScreen) {
        super(QUEST_JOURNAL_TITLE);
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.questListWidget = new LostTalesQuestListWidget(this.minecraft, QUEST_LIST_WIDGET_WIDTH, this.height, 0, 32, 0);
        this.addRenderableWidget(this.questListWidget);

        reloadQuestListEntries();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 12, LostTalesColor.WHITE.getColorWithAlpha(1.0f));
    }


    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_, double p_294830_) {
        if (this.questListWidget != null && this.questListWidget.mouseScrolled(p_94686_, p_94687_, p_94688_, p_294830_)) return true;
        return super.mouseScrolled(p_94686_, p_94687_, p_94688_, p_294830_);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (LostTalesKeyMappingRegistry.CHARACTER_MENU_MAPPING.get().matches(keyCode, scanCode)) {
            this.minecraft.setScreen(new LostTalesCharacterMenuScreen(this.parentScreen));
            return true;
        }
        if (LostTalesKeyMappingRegistry.QUEST_JOURNAL_MAPPING.get().matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parentScreen);
    }


    private void reloadQuestListEntries() {
        var entries = new ArrayList<LostTalesQuestListWidgetEntry>();
        var active = LostTalesClientQuestCache.get().getActive();

        if (active == null || active.isEmpty()) {
            entries.add(new LostTalesQuestListWidgetEntry(Component.literal("No active quests").withStyle(ChatFormatting.GRAY)));
            entries.add(new LostTalesQuestListWidgetEntry(Component.translatable("test")));
        } else {
            active.forEach((questId, progress) -> {
                LostTalesQuest quest = LostTalesQuestDatapackLoader.getQuest(questId).get();
                Component title = quest != null
                        ? Component.literal(quest.title()).withStyle(ChatFormatting.GOLD)
                        : Component.literal(questId.toString()).withStyle(ChatFormatting.GOLD);

                // Show a small progress/stage line if you have it
                Component sub = (progress != null && progress.stageId != null)
                        ? Component.literal("Stage: " + progress.stageId).withStyle(ChatFormatting.DARK_GRAY)
                        : Component.empty();

                entries.add(new LostTalesQuestListWidgetEntry(questId, title, sub));
            });
        }

        this.questListWidget.replaceEntries(entries);
    }
}
