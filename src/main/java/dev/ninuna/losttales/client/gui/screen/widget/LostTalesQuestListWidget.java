package dev.ninuna.losttales.client.gui.screen.widget;

import dev.ninuna.losttales.client.gui.screen.widget.entry.LostTalesQuestListWidgetEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;

public class LostTalesQuestListWidget extends ObjectSelectionList<LostTalesQuestListWidgetEntry> {

    public LostTalesQuestListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight, int headerHeight) {
        super(minecraft, width, height, y, itemHeight, headerHeight);
    }
}
