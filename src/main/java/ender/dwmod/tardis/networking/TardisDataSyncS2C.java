package ender.dwmod.tardis.networking;

import ender.dwmod.DwMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TardisDataSyncS2C(CompoundTag tag) implements CustomPacketPayload {
    public static final ResourceLocation TARDIS_DATA_SYNC_S2C = ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "tardis_data_sync_s2c");
    public static final CustomPacketPayload.Type<TardisDataSyncS2C> ID = new CustomPacketPayload.Type<>(TARDIS_DATA_SYNC_S2C);

    public static final StreamCodec<RegistryFriendlyByteBuf, TardisDataSyncS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, TardisDataSyncS2C::tag,
            TardisDataSyncS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }    
}
