package com.suicidesquid.syncswitch.tiles.Buttons;

import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Base.BaseButtonTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class StoneButtonTile extends BaseButtonTile{
    public StoneButtonTile(BlockPos pos, BlockState state) {
        super(Registration.STONE_BUTTON_BLOCK_BE.get(), pos, state);
    }
}
