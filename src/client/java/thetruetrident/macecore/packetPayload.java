package thetruetrident.macecore;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
public class packetPayload {


    public record MacecoreRepPayload() implements CustomPayload {
        public static final Id<MacecoreRepPayload> ID = new Id<>(Identifier.of("macecore", "macecore-rep"));
        public static final PacketCodec<RegistryByteBuf, MacecoreRepPayload> CODEC =
                PacketCodec.unit(new MacecoreRepPayload()); // no fields, just unit

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
