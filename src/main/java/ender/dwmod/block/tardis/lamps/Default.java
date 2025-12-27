package ender.dwmod.block.tardis.lamps;

import net.minecraft.world.level.block.Blocks;

public class Default extends TardisLamp {
    


    public Default() {
        super(Properties.ofFullCopy(Blocks.BEDROCK).lightLevel(state -> state.getValue(LIT) ? 15 : 0));
    }
    
}
