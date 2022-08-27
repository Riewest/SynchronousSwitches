package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.blocks.base.BaseLightBlock;
import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Base.BaseLightTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LightPanelTile extends BaseLightTile{
    public LightPanelTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.LIGHT_PANEL_BLOCK.get(), pos, state);
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
