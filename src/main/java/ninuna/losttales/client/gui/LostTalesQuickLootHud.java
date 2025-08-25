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

import java.util.ArrayList;
import java.util.List;

public class LostTalesQuickLootHud {
    private static int SCROLL_INDEX = 0;
    private static int SELECTED_ROW_INDEX = 0;

    private static final int TEXTURE_HEIGHT= 160;
    private static final int TEXTURE_TOP_HEIGHT = 23;
    private static final int TEXTURE_MID_HEIGHT = 22;
    private static final int ORNAMENT_HEIGHT = 10;
    private static final int SELECTION_BOX_HEIGHT = 20;
    private static final int TEXTURE_WIDTH = 320;
    private static final int MAX_ITEMS_PER_SCREEN = 5;
    private static final ResourceLocation QUICK_LOOT_HUD_TEXTURE = ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/gui/quickloothud.png");

    public static void renderHud(Minecraft minecraft, GuiGraphics guiGraphics, Container container) {
        Font font = minecraft.font;
        Window window = minecraft.getWindow();
        Component name = getContainerName(container);

        int windowHeight = window.getGuiScaledHeight();
        int windowWidth = window.getGuiScaledWidth();

        int offsetHudX = windowWidth / 8;
        int offsetHudY = windowHeight / 6;
        int itemStackXY = 16;
        int textureTopY = windowHeight / 2 - offsetHudY;
        int textureTopX =  windowWidth / 2 + offsetHudX;
        int containerNameY = textureTopY + font.lineHeight / 2;
        int containerNameX = textureTopX + 3;
        int ornamentY = containerNameY - 1;
        int ornamentX = textureTopX + font.width(name) + 5;
        int itemStackX = textureTopX + 17;
        int itemStackY = textureTopY + TEXTURE_TOP_HEIGHT + TEXTURE_MID_HEIGHT / 2 - itemStackXY / 2;
        int itemStackNameX = itemStackX + 21;
        int itemStackNameY = textureTopY + TEXTURE_TOP_HEIGHT + TEXTURE_MID_HEIGHT / 2 - font.lineHeight / 2;
        int itemStackRowOffsetY = 0;

        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureTopX, textureTopY, 0, 0, TEXTURE_WIDTH, TEXTURE_TOP_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, ornamentX, ornamentY, 0, 126, TEXTURE_WIDTH, ORNAMENT_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.drawString(font, name, containerNameX, containerNameY, 0xFFFFFF, true);

        if (container.isEmpty()) {
            guiGraphics.drawString(font, Component.translatable("quickLootHud." + LostTales.MOD_ID + ".empty"), itemStackNameX, itemStackNameY, 0xFFFFFF, true);
            guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureTopX, textureTopY + TEXTURE_TOP_HEIGHT, 0, 25, TEXTURE_WIDTH, TEXTURE_MID_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else {
            List<Integer> visibleSlots = getNonEmptyContainerSlots(container);

            int totalRows = visibleSlots.size();
            if (SELECTED_ROW_INDEX >= totalRows) {
                SELECTED_ROW_INDEX = Math.max(0, totalRows - 1);
            }

            int j = 0;
            for (int i = SCROLL_INDEX; i < totalRows && j < MAX_ITEMS_PER_SCREEN; i++) {
                int containerIndex = visibleSlots.get(i);
                ItemStack itemStack = container.getItem(containerIndex);
                renderItemStacks(guiGraphics, itemStack, font, j, itemStackX, itemStackY, itemStackRowOffsetY, itemStackNameX, itemStackNameY, textureTopX, textureTopY);
                itemStackRowOffsetY += TEXTURE_MID_HEIGHT;
                j++;
            }
        }
    }

    public static void renderItemStacks(GuiGraphics guiGraphics, ItemStack itemStack, Font font, int j, int itemStackX, int itemStackY, int itemStackRowOffsetY, int itemStackNameX, int itemStackNameY, int textureTopX, int textureTopY) {
        guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureTopX, textureTopY + TEXTURE_TOP_HEIGHT + TEXTURE_MID_HEIGHT * j, 0, 25,  TEXTURE_WIDTH, TEXTURE_MID_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.renderItem(itemStack, itemStackX, itemStackY + itemStackRowOffsetY);
        guiGraphics.renderItemDecorations(font, itemStack, itemStackX , itemStackY + itemStackRowOffsetY);

        int actualIndex = SCROLL_INDEX + j;
        if (actualIndex == SELECTED_ROW_INDEX) {
            guiGraphics.blit(RenderType::guiTextured, QUICK_LOOT_HUD_TEXTURE, textureTopX + 13, textureTopY + TEXTURE_TOP_HEIGHT + TEXTURE_MID_HEIGHT * j + 1, 0, 72,  TEXTURE_WIDTH, SELECTION_BOX_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
        guiGraphics.drawString(font, itemStack.getStyledHoverName(), itemStackNameX, itemStackNameY + itemStackRowOffsetY, 0xFFFFFF, true);
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
