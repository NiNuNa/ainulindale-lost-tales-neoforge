package dev.ninuna.losttales.common.entity;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import dev.ninuna.losttales.common.LostTales;

public class LostTalesEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(LostTales.MOD_ID);

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
