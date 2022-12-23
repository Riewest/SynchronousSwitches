package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.blocks.base.BaseLightBlock;
import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Base.BaseLightTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LightBlockTile extends BaseLightTile{
    public LightBlockTile(BlockPos pos, BlockState state) {
        super(Registration.LIGHT_BLOCK_BE.get(), pos, state);
    }

    @Override
    protected void tick(Level level, BlockPos pos, BlockState state) {
        SwitchData switchData = SwitchData.get(level);
        boolean channelActive = switchData.isActive(this.getChannel());
        if (channelActive != state.getValue((BaseLightBlock.LIT))){
            level.setBlockAndUpdate(pos, state.setValue(BaseLightBlock.LIT, channelActive));
        }
    }
}
