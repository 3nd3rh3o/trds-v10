package client.ender.dwmod.blockEntity.console;

import ender.dwmod.block.tardis.exoshell.DefaultConsoleBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DefaultConsoleRenderer extends GeoBlockRenderer<DefaultConsoleBE> {
    public DefaultConsoleRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new DefaultConsoleModel());
    }
}
