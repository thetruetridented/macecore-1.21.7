package thetruetrident.macecore.mixin.client;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface forceAttack {
    @Invoker("doAttack")
    boolean callDoAttack();

}