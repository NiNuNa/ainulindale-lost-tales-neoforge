package dev.ninuna.losttales.client.gui.hud.loot;

import com.mojang.blaze3d.platform.Window;
import dev.ninuna.losttales.client.gui.LostTalesColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
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
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.block.entity.custom.LostTalesUrnBlockEntity;
import dev.ninuna.losttales.common.config.LostTalesConfigs;
import dev.ninuna.losttales.common.network.packet.LostTalesQuickLootHudDropItemPacket;

import java.util.ArrayList;
import java.util.List;

public class LostTalesQuickLootHudRenderer {
    private static int INDEX_SCROLL_OVERFLOW = 0;
    private static int INDEX_SELECTED_ROW = 0;

    private static final ResourceLocation QUICK_LOOT_HUD_TEXTURE = ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/gui/quickloothud.png");
    private static final String LANG_PATH = "quickLootHud";

    private static final Component TEXT_KEY_R = getTranslatableComponent("drop");
    private static final Component TEXT_KEY_SCROLL = getTranslatableComponent("scroll");
    private static final Component TEXT_KEY_ALT = getTranslatableComponent("plus");
    private static final Component TEXT_EMPTY = getTranslatableComponent("empty");

    private static final int TEXTURE_HEIGHT= 160;
    private static final int TEXTURE_WIDTH = 280;

    private static final int TEXTURE_SEGMENT_TOP_HEIGHT = 23;
    private static final int TEXTURE_SEGMENT_MID_HEIGHT = 22;
    private static final int TEXTURE_SEGMENT_BOT_HEIGHT = 8;

    private static final int TEXTURE_ORNAMENT_HORIZONTAL_HEIGHT = 10;
    private static final int TEXTURE_ORNAMENT_VERTICAL_HEIGHT = 22;

    private static final int TEXTURE_SELECTION_BOX_HEIGHT = 20;
    private static final int TEXTURE_SELECTION_BOX_WIDTH = 211;
    private static final int TEXTURE_SELECTION_BOX_OFFSET_X = 13;

    private static final int TEXTURE_KEYS_HEIGHT = 13;
    private static final int TEXTURE_KEY_R_WIDTH = 14;
    private static final int TEXTURE_KEY_ALT_WIDTH = 20;
    private static final int TEXTURE_KEY_SCROLL_WIDTH = 16;

    private static final int TEXTURE_ARROW_WIDTH = 5;
    private static final int TEXTURE_ARROW_HEIGHT = 3;

    public static void renderHud(Minecraft minecraft, GuiGraphics guiGraphics, Container container) {
        Font font = minecraft.font;
        Window window = minecraft.getWindow();
        Component name = getContainerName(container);

        int windowHeight = window.getGuiScaledHeight();
        int windowWidth = window.getGuiScaledWidth();

        int maxRowsPerScreen = LostTalesConfigs.CLIENT.quickLootHudMaxRows.get();
        int offsetX = LostTalesConfigs.CLIENT.quickLootHudOffsetX.get();
        int offsetY = LostTalesConfigs.CLIENT.quickLootHudOffsetY.get();;

        int j = 0;
        int itemStackXY = 16;
        int itemStackRowOffsetY = 0;

        // Variables for HUD screen layout.
        int textureSegmentTopY = windowHeight * offsetY / 100;
        int textureSegmentTopX = windowWidth / 2 + windowWidth / 2 * offsetX / 100;
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
        int textureArrowX = textureSegmentTopX + TEXTURE_SELECTION_BOX_OFFSET_X + TEXTURE_SELECTION_BOX_WIDTH + 5;

        // Draw top segment of the texture.
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureSegmentTopX, textureSegmentTopY, 0, 0, TEXTURE_WIDTH, TEXTURE_SEGMENT_TOP_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureOrnamentHorX, textureOrnamentHorY, 0, 120, TEXTURE_WIDTH, TEXTURE_ORNAMENT_HORIZONTAL_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.drawString(font, name, containerNameX, containerNameY, LostTalesColor.WHITE.getColorWithAlpha(1.0f), true);

        List<Integer> visibleSlots = getNonEmptyContainerSlots(container);
        int totalRows = visibleSlots.size();

        if (!container.isEmpty()) {
            if (INDEX_SELECTED_ROW >= totalRows) {
                INDEX_SELECTED_ROW = Math.max(0, totalRows - 1);
            }

            for (int i = INDEX_SCROLL_OVERFLOW; i < totalRows && j < maxRowsPerScreen; i++) {
                int containerIndex = visibleSlots.get(i);
                ItemStack itemStack = container.getItem(containerIndex);
                // Draw middle segment of the texture.
                renderItemStacks(guiGraphics, itemStack, font, j, itemStackX, itemStackY, itemStackRowOffsetY, itemStackNameX, itemStackNameY, textureSegmentTopX, textureSegmentTopY);
                itemStackRowOffsetY += TEXTURE_SEGMENT_MID_HEIGHT;
                j++;
            }
        } else {
            j = 1;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureSegmentTopX, textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT, 0, 25, TEXTURE_WIDTH, TEXTURE_SEGMENT_MID_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
            guiGraphics.drawString(font, TEXT_EMPTY, itemStackNameX, itemStackNameY, LostTalesColor.WHITE.getColorWithAlpha(1.0f), true);
        }

        // Draw bottom segment of the texture.
        int textureSegmentBotY = textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT * j;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureSegmentTopX, textureSegmentBotY, 0, 49, TEXTURE_WIDTH, TEXTURE_SEGMENT_BOT_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        //Draw scroll arrow texture.
        if (INDEX_SCROLL_OVERFLOW > 0) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureArrowX,textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT - TEXTURE_ARROW_HEIGHT, 6, 132, TEXTURE_ARROW_WIDTH, TEXTURE_ARROW_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else if (totalRows > maxRowsPerScreen) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureArrowX, textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT * j, 0, 132, TEXTURE_ARROW_WIDTH, TEXTURE_ARROW_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }

        // Draw keys, don't draw the R/Drop key if the container is a sealed urn.
        int textureKeysY = textureSegmentBotY + TEXTURE_SEGMENT_BOT_HEIGHT + 3;
        renderKeys(guiGraphics, font, containerNameX, textureKeysY, !(container instanceof LostTalesUrnBlockEntity urnBlockEntity && urnBlockEntity.isSealed()));

        //Draw vertical ornament texture.
        int textureOrnamentVertY = textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT * j / 2 - TEXTURE_ORNAMENT_VERTICAL_HEIGHT / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureOrnamentVertX, textureOrnamentVertY, 0, 96, TEXTURE_WIDTH, TEXTURE_ORNAMENT_VERTICAL_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.fillGradient(textureOrnamentVertLineX + 1, textureOrnamentVertY, textureOrnamentVertLineX, textureOrnamentVertY - (j - 1) * 11 - 3, LostTalesColor.WHITE.getColorWithAlpha(1.0f), LostTalesColor.WHITE.getColorWithAlpha(0.0f));
        guiGraphics.fillGradient(textureOrnamentVertLineX + 1, textureOrnamentVertY + TEXTURE_ORNAMENT_VERTICAL_HEIGHT + (j - 1) * 11 + 3, textureOrnamentVertLineX, textureOrnamentVertY + TEXTURE_ORNAMENT_VERTICAL_HEIGHT, LostTalesColor.WHITE.getColorWithAlpha(0.0f), LostTalesColor.WHITE.getColorWithAlpha(1.0f));
    }

    public static void renderItemStacks(GuiGraphics guiGraphics, ItemStack itemStack, Font font, int j, int itemStackX, int itemStackY, int itemStackRowOffsetY, int itemStackNameX, int itemStackNameY, int textureSegmentTopX, int textureSegmentTopY) {
        int textureSegmentMidY = textureSegmentTopY + TEXTURE_SEGMENT_TOP_HEIGHT + TEXTURE_SEGMENT_MID_HEIGHT * j;
        int textureSelectionBoxY = textureSegmentMidY + (TEXTURE_SEGMENT_MID_HEIGHT - TEXTURE_SELECTION_BOX_HEIGHT) / 2;
        int textureSelectionBoxX = textureSegmentTopX + TEXTURE_SELECTION_BOX_OFFSET_X;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureSegmentTopX, textureSegmentMidY, 0, 25,  TEXTURE_WIDTH, TEXTURE_SEGMENT_MID_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        int actualIndex = INDEX_SCROLL_OVERFLOW + j;
        if (actualIndex == INDEX_SELECTED_ROW) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureSelectionBoxX, textureSelectionBoxY, 0, 74,  TEXTURE_SELECTION_BOX_WIDTH, TEXTURE_SELECTION_BOX_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }

        guiGraphics.renderItem(itemStack, itemStackX, itemStackY + itemStackRowOffsetY);
        guiGraphics.renderItemDecorations(font, itemStack, itemStackX , itemStackY + itemStackRowOffsetY);
        guiGraphics.drawString(font, itemStack.getStyledHoverName(), itemStackNameX, itemStackNameY + itemStackRowOffsetY, LostTalesColor.WHITE.getColorWithAlpha(1.0f), true);
    }

    private static void renderKeys(GuiGraphics guiGraphics, Font font, int x, int y, boolean drawRKey) {
        int textKeysY = y + TEXTURE_KEYS_HEIGHT / 2 - font.lineHeight / 2;
        int textKeyRX = x + TEXTURE_KEY_R_WIDTH + 3;
        int textureKeyAltX = textKeyRX + font.width(TEXT_KEY_R) + 7;

        if (drawRKey) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, x, y, 0, 59, TEXTURE_KEY_R_WIDTH, TEXTURE_KEYS_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
            guiGraphics.drawString(font, TEXT_KEY_R, textKeyRX, textKeysY, LostTalesColor.WHITE.getColorWithAlpha(1.0f), true);
        } else {
            textureKeyAltX = x;
        }

        int textKeyAltX = textureKeyAltX + TEXTURE_KEY_ALT_WIDTH + 3;
        int textureKeyScrollX = textKeyAltX + font.width(TEXT_KEY_ALT) + 3;
        int textKeyScrollX = textureKeyScrollX + TEXTURE_KEY_SCROLL_WIDTH + 3;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureKeyAltX, y, 15, 59, TEXTURE_KEY_ALT_WIDTH, TEXTURE_KEYS_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.drawString(font, TEXT_KEY_ALT, textKeyAltX, textKeysY, LostTalesColor.WHITE.getColorWithAlpha(1.0f), true);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, QUICK_LOOT_HUD_TEXTURE, textureKeyScrollX, y, 36, 59, TEXTURE_KEY_SCROLL_WIDTH, TEXTURE_KEYS_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.drawString(font, TEXT_KEY_SCROLL, textKeyScrollX, textKeysY, LostTalesColor.WHITE.getColorWithAlpha(1.0f), true);
    }

    public static void moveSelectionIndex(Container container, int scrollDelta) {
        int maxIndex = getNonEmptyContainerSlots(container).size() - 1;
        int maxRowsPerScreen = LostTalesConfigs.CLIENT.quickLootHudMaxRows.get();

        INDEX_SELECTED_ROW = Math.max(0, Math.min(INDEX_SELECTED_ROW + scrollDelta, maxIndex));

        if (INDEX_SELECTED_ROW < INDEX_SCROLL_OVERFLOW) {
            INDEX_SCROLL_OVERFLOW = INDEX_SELECTED_ROW;
        } else if (INDEX_SELECTED_ROW >= INDEX_SCROLL_OVERFLOW + maxRowsPerScreen) {
            INDEX_SCROLL_OVERFLOW = INDEX_SELECTED_ROW - maxRowsPerScreen + 1;
        }

        INDEX_SCROLL_OVERFLOW = Math.max(0, Math.min(INDEX_SCROLL_OVERFLOW, Math.max(0, maxIndex - maxRowsPerScreen + 1)));
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

        return TEXT_EMPTY;
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
                    int selectedIndex = LostTalesQuickLootHudRenderer.getIndexSelectedRow();
                    List<Integer> visibleSlots = LostTalesQuickLootHudRenderer.getNonEmptyContainerSlots(container);

                    if (selectedIndex >= 0 && selectedIndex < visibleSlots.size()) {
                        int containerSlot = visibleSlots.get(selectedIndex);
                        ClientPacketDistributor.sendToServer(new LostTalesQuickLootHudDropItemPacket(blockPos.getX(), blockPos.getY(), blockPos.getZ(), containerSlot));
                    }
                }
            }
        }
    }

    public static void resetHud() {
        INDEX_SCROLL_OVERFLOW = 0;
        INDEX_SELECTED_ROW = 0;
        LostTalesQuickLootHudScrollEvent.LAST_SCROLL_TIME = 0;
    }

    private static Component getTranslatableComponent(String key) {
        return Component.translatable(LANG_PATH + "." + LostTales.MOD_ID + "." + key);
    }

    public static int getIndexSelectedRow() {
        return INDEX_SELECTED_ROW;
    }

    public static int getIndexScrollOverflow() {
        return INDEX_SCROLL_OVERFLOW;
    }

    public static void setIndexScrollOverflow(int indexScrollOverflow) {
        INDEX_SCROLL_OVERFLOW = indexScrollOverflow;
    }
}
