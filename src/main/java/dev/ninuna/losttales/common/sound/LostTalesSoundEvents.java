package dev.ninuna.losttales.common.sound;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import dev.ninuna.losttales.common.LostTales;

public class LostTalesSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, LostTales.MOD_ID);

    public static final Holder<SoundEvent> PLUSHIE_SQUEAK = SOUND_EVENTS.register(
            "plushie_squeak", SoundEvent::createVariableRangeEvent
    );

    public static void register (IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
