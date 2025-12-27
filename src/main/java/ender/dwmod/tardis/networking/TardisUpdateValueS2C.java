package ender.dwmod.tardis.networking;

import ender.dwmod.DwMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TardisUpdateValueS2C(int tardisID, String category, String key, String value) implements CustomPacketPayload {
    public static final ResourceLocation TARDIS_UPDATE_VALUE_S2C = ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "tardis_update_value_s2c");
    public static final CustomPacketPayload.Type<TardisUpdateValueS2C> ID = new CustomPacketPayload.Type<>(TARDIS_UPDATE_VALUE_S2C);

    public static final StreamCodec<RegistryFriendlyByteBuf, TardisUpdateValueS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, TardisUpdateValueS2C::tardisID,
            ByteBufCodecs.STRING_UTF8, TardisUpdateValueS2C::category,
            ByteBufCodecs.STRING_UTF8, TardisUpdateValueS2C::key,
            ByteBufCodecs.STRING_UTF8, TardisUpdateValueS2C::value,
            TardisUpdateValueS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}