package com.macecore.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MacecoreRepPayload() implements CustomPayload {

    public static final Id<MacecoreRepPayload> ID =
            new Id<>(Identifier.of("macecore", "macecore-rep"));

    public static final PacketCodec<RegistryByteBuf, MacecoreRepPayload> CODEC =
            PacketCodec.unit(new MacecoreRepPayload()); // no fields

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}