package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.blocks.VanillaSwitchBlock;
import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.BlockInit;
import com.suicidesquid.syncswitch.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class VanillaSwitchBlockTile extends BlockEntity{
    private int timer = 0;
    private String channel = "DEFAULT";
    public VanillaSwitchBlockTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.VANILLA_SWITCH_BLOCK.get(), pos, state);
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

    protected static Direction getConnectedDirection(BlockState p_53201_) {
        switch ((AttachFace)p_53201_.getValue(LeverBlock.FACE)) {
           case CEILING:
              return Direction.DOWN;
           case FLOOR:
              return Direction.UP;
           default:
              return p_53201_.getValue(LeverBlock.FACING);
        }
     }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be){
        VanillaSwitchBlockTile tile = (VanillaSwitchBlockTile) be;
        if (!level.isClientSide()){
            tile.timer++;
            if (tile.timer > 10){
                tile.timer = 0;  
                SwitchData switchData = SwitchData.get(level);
                boolean channelActive = switchData.isActive(tile.getChannel());
                if (channelActive != state.getValue(LeverBlock.POWERED)){
                    level.setBlockAndUpdate(pos, state.setValue(LeverBlock.POWERED, channelActive));
                    level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), BlockInit.VANILLA_SWITCH_BLOCK.get());
                    float f = channelActive ? 0.6F : 0.5F;
                    level.playSound((Player)null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
                }
                
            }
        }
    }
}
