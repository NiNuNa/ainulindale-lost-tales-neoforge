package dev.ninuna.losttales.client.gui.screen.widget.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class LostTalesQuestListWidgetEntry extends ObjectSelectionList.Entry<LostTalesQuestListWidgetEntry> {
    final ResourceLocation questId;
    final Component title;
    final Component sub;

    // Placeholder row, single line
    public LostTalesQuestListWidgetEntry(Component singleLine) {
        this(null, singleLine, Component.empty());
    }

    public LostTalesQuestListWidgetEntry(ResourceLocation id, Component title, Component sub) {
        this.questId = id;
        this.title = title;
        this.sub = sub == null ? Component.empty() : sub;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int index, int top, int left, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTick) {
        int textX  = left + 6;
        int titleY = top + 5;

        // Opaque white for title (ARGB)
        guiGraphics.drawString(Minecraft.getInstance().font, this.title, textX, titleY, 0xFFFFFFFF);

        if (this.sub != null && !this.sub.getString().isEmpty()) {
            guiGraphics.drawString(Minecraft.getInstance().font, this.sub, textX, titleY + 10, 0xFFAAAAAA);
        }
    }

    @Override
    public Component getNarration() {
        return title != null ? title : Component.empty();
    }
}
