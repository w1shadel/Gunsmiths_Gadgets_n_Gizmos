package com.maxwell.gunsmiths_gadgetsn_gizmos.network;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.ClientApostleHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public record ClientboundAshStormPacket() implements CustomPacketPayload {
    public static final ClientboundAshStormPacket INSTANCE = new ClientboundAshStormPacket();

    public static final Type<ClientboundAshStormPacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "ash_storm"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAshStormPacket> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientboundAshStormPacket payload, IPayloadContext context) {

        context.enqueueWork(ClientApostleHandler::handleAshStormPacket);
    }
}