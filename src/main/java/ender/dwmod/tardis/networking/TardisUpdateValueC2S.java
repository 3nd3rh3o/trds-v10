package ender.dwmod.tardis.networking;

import ender.dwmod.DwMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
   
public record TardisUpdateValueC2S(int tardisID, String category, String key, String value) implements CustomPacketPayload {
    public static final ResourceLocation TARDIS_UPDATE_VALUE_C2S = ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "tardis_update_value_c2s");
    public static final CustomPacketPayload.Type<TardisUpdateValueC2S> ID = new CustomPacketPayload.Type<>(TARDIS_UPDATE_VALUE_C2S);

    public static final StreamCodec<RegistryFriendlyByteBuf, TardisUpdateValueC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, TardisUpdateValueC2S::tardisID,
            ByteBufCodecs.STRING_UTF8, TardisUpdateValueC2S::category,
            ByteBufCodecs.STRING_UTF8, TardisUpdateValueC2S::key,
            ByteBufCodecs.STRING_UTF8, TardisUpdateValueC2S::value,
            TardisUpdateValueC2S::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
