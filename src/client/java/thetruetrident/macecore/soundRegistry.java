package thetruetrident.macecore;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static thetruetrident.macecore.MacecoreClient.MOD_ID;


public class soundRegistry {
    public static final SoundEvent BREACH_SWAP = registerSoundEvent("breach_swap");




    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(MOD_ID);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));

    }
    public static void registerModSounds() {

    }
}
