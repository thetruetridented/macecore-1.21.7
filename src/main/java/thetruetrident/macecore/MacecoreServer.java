package thetruetrident.macecore;

import com.macecore.networking.MacecoreRepPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;

public class MacecoreServer implements ModInitializer {
	public void onInitialize() {

		PayloadTypeRegistry.playC2S().register(
				MacecoreRepPayload.ID,
				MacecoreRepPayload.CODEC
		);


		ServerPlayNetworking.registerGlobalReceiver(MacecoreRepPayload.ID, (payload, context) -> {
			var player = context.player();
			System.out.println("Heartbeat from " + player.getName().getString());

			context.server().execute(() -> {
				player.networkHandler.disconnect(Text.literal("Mace performance enhancements are not allowed on this server. (detected due to Macecore-LITE packets)"));
			});
		});
	}
}