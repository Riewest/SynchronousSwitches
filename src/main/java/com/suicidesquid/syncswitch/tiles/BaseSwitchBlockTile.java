package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class BaseSwitchBlockTile extends BlockEntity{
    private int timer = 0;
    private String channel = "DEFAULT";
    public BaseSwitchBlockTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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

    public static Direction getConnectedDirection(BlockState state) {
        switch ((AttachFace)state.getValue(LeverBlock.FACE)) {
           case CEILING:
              return Direction.DOWN;
           case FLOOR:
              return Direction.UP;
           default:
              return state.getValue(LeverBlock.FACING);
        }
     }

    private static void tickUpdate(Level level, BlockPos pos, BlockState state, BaseSwitchBlockTile tile){
        if (!level.isClientSide()){
            tile.timer++;
            if (tile.timer > 10){
                tile.timer = 0;  
                SwitchData switchData = SwitchData.get(level);
                boolean channelActive = switchData.isActive(tile.getChannel());
                if (channelActive != state.getValue(LeverBlock.POWERED)){
                    level.setBlockAndUpdate(pos, state.setValue(LeverBlock.POWERED, channelActive));
                }
                
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be){
        BaseSwitchBlockTile tile = (BaseSwitchBlockTile) be;
        tickUpdate(level, pos, state, tile);
    }
}
