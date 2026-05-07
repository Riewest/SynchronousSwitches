package com.suicidesquid.syncswitch.tiles.Buttons;

import com.suicidesquid.syncswitch.setup.ModRegistration;
import com.suicidesquid.syncswitch.tiles.Base.BaseButtonTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class StoneButtonTile extends BaseButtonTile{
    public StoneButtonTile(BlockPos pos, BlockState state) {
        super(ModRegistration.STONE_BUTTON_BLOCK.blockEntity().get(), pos, state);
    }
}
