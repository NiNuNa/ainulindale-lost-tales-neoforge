package ninuna.losttales.client.gui;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.entity.custom.LostTalesUrnBlockEntity;
import ninuna.losttales.client.event.LostTalesQuickLootHudScrollEvent;
import ninuna.losttales.network.packet.LostTalesQuickLootHudDropItemPacket;
import ninuna.losttales.util.LostTalesClientUtil;

import java.util.ArrayList;
import java.util.List;

public class LostTalesQuickLootHud {
    private static int SCROLL_INDEX = 0;
    private static int SELECTED_ROW_INDEX = 0;

    private static final int MAX_ITEMS_PER_SCREEN = 5;
    private static final int COLOR_WHITE = 0xFFFFFBDE;
    private static final int COLOR_BLACK = 0x1228242E;
    private static final int COLOR_FADE = 0x0028242E;
    private static final ResourceLocation QUICK_LOOT_HUD_TEXTURE = ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/gui/quickloothud.png");
    private static final int TEXTURE_HEIGHT= 160;
    private static final int TEXTURE_WIDTH = 320;
    private static final int TEXTURE_SEGMENT_TOP_HEIGHT = 23;
    private static final int TEXTURE_SEGMENT_MID_HEIGHT = 22;
    private static final int TEXTURE_SEGMENT_MID_WIDTH = 280;
    private static final int TEXTURE_SEGMENT_BOT_HEIGHT = 8;
    private static final int TEXTURE_ORNAMENT_HORIZONTAL_HEIGHT = 10;
    private static final int TEXTURE_ORNAMENT_VERTICAL_HEIGHT = 22;
    private static final int TEXTURE_SELECTION_BOX_HEIGHT = 20;
    private static final int TEXTURE_SELECTION_BOX_OFFSET_X = 13;
    private static final int TEXTURE_KEYS_HEIGHT = 13;
    private static final int TEXTURE_KEY_R_WIDTH = 14;
    private static final int TEXTURE_KEY_ALT_WIDTH = 20;
    private static final int TEXTURE_KEY_SCROLL_WIDTH = 16;

    public static void renderHud(Minecraft minecraft, GuiGraphics guiGraphics, Container container) {
        Font font = minecraft.font;
        Window window = minecraft.getWindow();
        Component name = getContainerName(container);
        Component textKeyR = Component.translatable("quickLootHud." + LostTales.MOD_ID + ".drop");
        Component textKeyScroll = Component.translatable("quickLootHud." + LostTales.MOD_ID + ".scroll");
        Component textKeyAlt = Component.translatable("quickLootHud." + LostTales.MOD_ID + ".plus");

        int windowHeight = window.getGuiScaledHeight();
        int windowWidth = window.getGuiScaledWidth();

        int j = 0;
        int itemStackXY = 16;
        int itemStackRowOffsetY = 0;

        // Variables for HUD screen layout.
        int offsetX = windowWidth / 8;
        int offsetY = windowHeight / 6;
        int textureSegmentTopY = windowHeight / 2 - offsetY;
        int textureSegmentTopX =  windowWidth / 2 + offsetX;
        int containerNameY = textureSegmentTopY + font.lineHeight / 2;
        int containerNameX = textureSegmentTopX + 3;
        int textureOrnamentHorY = containerNameY - 1;
        int textureOrnamentHorX = textureSegmentTopX + font.width(name) + 5;
        int itemStackX = textureSegmentTopX + 17;
        int itemStackY = textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT / 2 - itemStackXY / 2;
        int itemStackNameX = itemStackX + 21;
        int itemStackNameY = textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT / 2 - font.lineHeight / 2;
        int textureOrnamentVertX = textureSegmentTopX - 5;
        int textureOrnamentVertLineX = textureSegmentTopX + 5;
        int textKeyRX = containerNameX + TEXTURE_KEY_R_WIDTH + 3;
        int textureKeyAltX = textKeyRX + font.width(textKeyR) + 7;
        int textKeyAltX = textureKeyAltX + TEXTURE_KEY_ALT_WIDTH + 3;
        int textureKeyScrollX = textKeyAltX + font.width(textKeyAlt) + 3;
        int textKeyScrollX = textureKeyScrollX + TEXTURE_KEY_SCROLL_WIDTH + 3;

        // Draw top segment of the texture.
        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureSegmentTopX, textureSegmentTopY, 0, 0, TEXTURE_WIDTH, TEXTURE_SEGMENT_TOP_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureOrnamentHorX, textureOrnamentHorY, 0, 122, TEXTURE_WIDTH, TEXTURE_ORNAMENT_HORIZONTAL_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.drawString(font, name, containerNameX, containerNameY, COLOR_WHITE, true);

        if (!container.isEmpty()) {
            List<Integer> visibleSlots = getNonEmptyContainerSlots(container);

            int totalRows = visibleSlots.size();
            if (SELECTED_ROW_INDEX >= totalRows) {
                SELECTED_ROW_INDEX = Math.max(0, totalRows - 1);
            }

            for (int i = SCROLL_INDEX; i < totalRows && j < MAX_ITEMS_PER_SCREEN; i++) {
                int containerIndex = visibleSlots.get(i);
                ItemStack itemStack = container.getItem(containerIndex);
                // Draw middle segment of the texture.
                renderItemStacks(guiGraphics, itemStack, font, j, itemStackX, itemStackY, itemStackRowOffsetY, itemStackNameX, itemStackNameY, textureSegmentTopX, textureSegmentTopY);
                itemStackRowOffsetY += TEXTURE_SEGMENT_MID_HEIGHT;
                j++;
            }
        } else {
            j = 1;
            guiGraphics.drawString(font, Component.translatable("quickLootHud." + LostTales.MOD_ID + ".empty"), itemStackNameX, itemStackNameY, COLOR_WHITE, true);
            guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureSegmentTopX, textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT, 0, 25, TEXTURE_WIDTH, TEXTURE_SEGMENT_MID_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
        // Draw bottom segment of the texture.
        int textureSegmentBotY = textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT * j;
        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureSegmentTopX, textureSegmentBotY, 0, 51, TEXTURE_WIDTH, TEXTURE_SEGMENT_BOT_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Draw keys.
        int textureKeysY = textureSegmentBotY + TEXTURE_SEGMENT_BOT_HEIGHT + 3;
        int textKeysY = textureKeysY + TEXTURE_KEYS_HEIGHT / 2 - font.lineHeight / 2;

        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, containerNameX, textureKeysY, 0, 61, TEXTURE_KEY_R_WIDTH, TEXTURE_KEYS_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.drawString(font, textKeyR, textKeyRX, textKeysY, COLOR_WHITE, true);
        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureKeyAltX, textureKeysY, 15, 61, TEXTURE_KEY_ALT_WIDTH, TEXTURE_KEYS_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.drawString(font, textKeyAlt, textKeyAltX, textKeysY, COLOR_WHITE, true);
        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureKeyScrollX, textureKeysY, 36, 61, TEXTURE_KEY_SCROLL_WIDTH, TEXTURE_KEYS_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.drawString(font, textKeyScroll, textKeyScrollX, textKeysY, COLOR_WHITE, true);

        //Draw vertical ornament texture.
        int textureOrnamentVertY = textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT * j / 2 - TEXTURE_ORNAMENT_VERTICAL_HEIGHT / 2;

        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureOrnamentVertX, textureOrnamentVertY, 0, 98, TEXTURE_WIDTH, TEXTURE_ORNAMENT_VERTICAL_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.fillGradient(textureOrnamentVertLineX + 1, textureOrnamentVertY, textureOrnamentVertLineX, textureOrnamentVertY - (j - 1) * 11 - 3, COLOR_WHITE, COLOR_FADE);
        guiGraphics.fillGradient(textureOrnamentVertLineX + 1, textureOrnamentVertY + TEXTURE_ORNAMENT_VERTICAL_HEIGHT + (j - 1) * 11 + 3, textureOrnamentVertLineX, textureOrnamentVertY + TEXTURE_ORNAMENT_VERTICAL_HEIGHT, COLOR_FADE, COLOR_WHITE);
    }

    public static void renderItemStacks(GuiGraphics guiGraphics, ItemStack itemStack, Font font, int j, int itemStackX, int itemStackY, int itemStackRowOffsetY, int itemStackNameX, int itemStackNameY, int textureTopX, int textureTopY) {
        int textureSegmentMidX2 = textureTopX + TEXTURE_SEGMENT_MID_WIDTH;
        int textureSegmentMidY1 = textureTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT * j;
        int textureSegmentMidY2 = textureSegmentMidY1 + TEXTURE_SEGMENT_MID_HEIGHT;
        int textureSelectionBoxY = textureSegmentMidY1 + (TEXTURE_SEGMENT_MID_HEIGHT - TEXTURE_SELECTION_BOX_HEIGHT) / 2;
        int textureSelectionBoxX = textureTopX + TEXTURE_SELECTION_BOX_OFFSET_X;

        LostTalesClientUtil.renderHorizontalFade(guiGraphics, textureTopX, textureSegmentMidY1, textureSegmentMidX2, textureSegmentMidY2,COLOR_BLACK, COLOR_FADE);
        guiGraphics.renderItem(itemStack, itemStackX, itemStackY + itemStackRowOffsetY);
        guiGraphics.renderItemDecorations(font, itemStack, itemStackX , itemStackY + itemStackRowOffsetY);

        int actualIndex = SCROLL_INDEX + j;
        if (actualIndex == SELECTED_ROW_INDEX) {
            guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureSelectionBoxX, textureSelectionBoxY, 0, 76,  TEXTURE_WIDTH, TEXTURE_SELECTION_BOX_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
        guiGraphics.drawString(font, itemStack.getStyledHoverName(), itemStackNameX, itemStackNameY + itemStackRowOffsetY, COLOR_WHITE, true);
    }

    public static void moveSelectionIndex(Container container, int scrollDelta) {
        int maxIndex = getNonEmptyContainerSlots(container).size() - 1;
        SELECTED_ROW_INDEX = Math.max(0, Math.min(SELECTED_ROW_INDEX + scrollDelta, maxIndex));

        if (SELECTED_ROW_INDEX < SCROLL_INDEX) {
            SCROLL_INDEX = SELECTED_ROW_INDEX;
        } else if (SELECTED_ROW_INDEX >= SCROLL_INDEX + MAX_ITEMS_PER_SCREEN) {
            SCROLL_INDEX = SELECTED_ROW_INDEX - MAX_ITEMS_PER_SCREEN + 1;
        }

        SCROLL_INDEX = Math.max(0, Math.min(SCROLL_INDEX, Math.max(0, maxIndex - MAX_ITEMS_PER_SCREEN + 1)));
    }

    public static Container getContainer(Minecraft minecraft) {
        if (minecraft.crosshairPickEntity == null && minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) minecraft.hitResult;
            BlockPos blockPos = blockHit.getBlockPos();
            Level level = minecraft.level;

            if (level != null && level.getBlockEntity(blockPos) != null && level.getBlockEntity(blockPos) instanceof Container container) {
                return container;
            }
        }
        return null;
    }

    public static List<Integer> getNonEmptyContainerSlots(Container container) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) {
                indices.add(i);
            }
        }
        return indices;
    }

    public static void resetHud() {
        SCROLL_INDEX = 0;
        SELECTED_ROW_INDEX = 0;
        LostTalesQuickLootHudScrollEvent.LAST_SCROLL_TIME = 0;
    }

    public static int getSelectedRowIndex() {
        return SELECTED_ROW_INDEX;
    }

    private static Component getContainerName(Container container) {
        if (container instanceof LostTalesUrnBlockEntity urnBlockEntity) {
            Item urnItem = urnBlockEntity.getBlockState().getBlock().asItem();
            return urnBlockEntity.isSealed()
                    ? Component.translatable(urnItem.getDescriptionId() + "_sealed")
                    : urnItem.getName();
        }

        if (container instanceof BlockEntity blockEntity) {
            Block block = blockEntity.getBlockState().getBlock();
            return block.getName();
        }

        return Component.translatable("quickLootHud." + LostTales.MOD_ID + ".empty");
    }

    public static void dropSelectedItem() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.crosshairPickEntity == null && minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) minecraft.hitResult;
            BlockPos blockPos = blockHit.getBlockPos();
            Level level = minecraft.level;

            if (level != null && level.getBlockEntity(blockPos) != null && level.getBlockEntity(blockPos) instanceof Container container) {
                // Doesn't work when urn is sealed
                if (!(container instanceof LostTalesUrnBlockEntity urnBlockEntity) || !urnBlockEntity.isSealed()) {
                    int selectedIndex = LostTalesQuickLootHud.getSelectedRowIndex();
                    List<Integer> visibleSlots = LostTalesQuickLootHud.getNonEmptyContainerSlots(container);
                    if (selectedIndex >= 0 && selectedIndex < visibleSlots.size()) {
                        int containerSlot = visibleSlots.get(selectedIndex);
                        PacketDistributor.sendToServer(new LostTalesQuickLootHudDropItemPacket(blockPos.getX(), blockPos.getY(), blockPos.getZ(), containerSlot));
                    }
                }
            }
        }
    }
}
