package ender.dwmod.tardis.networking;

import ender.dwmod.DwMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TardisScreenOpeningS2C(int screenID) implements CustomPacketPayload {
    public static final ResourceLocation TARDIS_SCREEN_OPENING_S2C = ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "tardis_screen_opening_s2c");
    public static final CustomPacketPayload.Type<TardisScreenOpeningS2C> ID = new CustomPacketPayload.Type<>(TARDIS_SCREEN_OPENING_S2C);

    public static final StreamCodec<RegistryFriendlyByteBuf, TardisScreenOpeningS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, TardisScreenOpeningS2C::screenID,
            TardisScreenOpeningS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
