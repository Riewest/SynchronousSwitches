package com.suicidesquid.syncswitch.tiles.Base;

import java.util.Random;

import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.data.SwitchData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class BaseChannelTile extends BlockEntity{
    public static final String NONE_CHANNEL = "none";
    private int timer = new Random().nextInt(10) + 1;
    private String channel = NONE_CHANNEL;
    private boolean redacted = false;
    private String player;

    public BaseChannelTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("channel", this.channel);
        nbt.putBoolean("redacted", this.redacted);
        nbt.putString("player", this.player);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.channel = nbt.getString("channel");
        this.redacted = nbt.getBoolean("redacted");
        this.player = nbt.getString("player");
    }

    public void setPlayer(String uuid){
        this.player = uuid;
    }

    public String getPlayer(){
        return this.player;
    }

    public void setChannel(String channel){
        this.channel = channel;
        setChanged();
    }

    public String getChannelDisplay(String uuid){
        if (uuid == this.player){
            return this.channel;
        }
        return this.redacted ? "REDACTED" : this.channel;
    }

    public String getChannel(){
        return this.channel;
    }

    public boolean hasChannel(){
        return !this.channel.toLowerCase().equals(NONE_CHANNEL);
    }

    public void setRedacted(boolean redacted){
        this.redacted = redacted;
        setChanged();
    }

    public boolean isRedacted(){
        return this.redacted;
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

    private static void tickUpdate(Level level, BlockPos pos, BlockState state, BaseChannelTile tile){
        if (!level.isClientSide() && tile.hasChannel()){
            tile.timer++;
            if (tile.timer > 10){
                tile.timer = 0;  
                SwitchData switchData = SwitchData.get(level);
                boolean channelActive = switchData.isActive(tile.getChannel());
                if (channelActive != state.getValue(LeverBlock.POWERED)){
                    level.setBlockAndUpdate(pos, state.setValue(LeverBlock.POWERED, channelActive));
                    BaseSwitchBlock.playSound(state, level, pos);
                }
                
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be){
        BaseChannelTile tile = (BaseChannelTile) be;
        tickUpdate(level, pos, state, tile);
    }
}
