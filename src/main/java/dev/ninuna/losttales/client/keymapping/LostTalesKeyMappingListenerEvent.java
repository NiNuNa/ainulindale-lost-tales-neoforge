package dev.ninuna.losttales.client.keymapping;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.client.gui.hud.LostTalesQuickLootHud;
import dev.ninuna.losttales.client.gui.screen.LostTalesCharacterMenuScreen;
import dev.ninuna.losttales.client.gui.screen.LostTalesQuestJournalScreen;
import dev.ninuna.losttales.client.gui.LostTalesGuiHelper;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesKeyMappingListenerEvent {

    @SubscribeEvent
    public static void keyMappingListener(ClientTickEvent.Post event) {
        if (LostTalesRegisterKeyMappingsEvent.QUEST_JOURNAL_MAPPING.get().consumeClick()) {
            Minecraft.getInstance().setScreen(new LostTalesQuestJournalScreen(Minecraft.getInstance().screen));
        }
        else if (LostTalesRegisterKeyMappingsEvent.CHARACTER_MENU_MAPPING.get().consumeClick()) {
            Minecraft.getInstance().setScreen(new LostTalesCharacterMenuScreen(Minecraft.getInstance().screen));
        }
        else if (LostTalesRegisterKeyMappingsEvent.TOGGLE_HUD_MAPPING.get().consumeClick()) {
            LostTalesGuiHelper.toggleLostTalesHud();
        }
        else if (LostTalesRegisterKeyMappingsEvent.USE_MAPPING.get().consumeClick()) {
            LostTalesQuickLootHud.dropSelectedItem();
        }
        else {
            LostTalesKeyMappingHelper.setIsModifierKeyDown(LostTalesRegisterKeyMappingsEvent.MODIFIER_MAPPING.get().isDown());
        }
    }
}
