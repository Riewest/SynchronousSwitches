package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SwitchBlockTile extends BlockEntity{
    private int timer = 0;
    private String channel = "DEFAULT";
    public SwitchBlockTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.SWITCH_BLOCK.get(), pos, state);
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("channel", this.channel);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.channel = nbt.getString("channel");
    }

    public void setChannel(String channel){
        this.channel = channel;
        setChanged();
    }

    public String getChannel(){
        return this.channel;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be){
        SwitchBlockTile tile = (SwitchBlockTile) be;
        if (!level.isClientSide()){
            tile.timer++;
            if (tile.timer > 10){
                tile.timer = 0;  
                // tile.updateActive(level);
                SwitchData switchData = SwitchData.get(level);
                level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, switchData.isActive(tile.getChannel())));
            }
        }
    }
}
