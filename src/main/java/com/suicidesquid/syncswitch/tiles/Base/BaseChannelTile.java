package com.suicidesquid.syncswitch.tiles.Base;

import java.util.Random;

import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.setup.LangInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class BaseChannelTile extends BlockEntity{
    public static final String NONE_CHANNEL = "none";
    protected int timerMax;
    private int timer = 0;
    private String channel = NONE_CHANNEL;
    private boolean redacted = false;
    private boolean silent = false;
    private String player = "";

    public BaseChannelTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        timerMax = 10;
        timer = new Random().nextInt(10) + 1;
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putString("channel", this.channel);
        nbt.putBoolean("redacted", this.redacted);
        nbt.putBoolean("silent", this.silent);
        nbt.putString("player", this.player);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.channel = nbt.getString("channel");
        this.redacted = nbt.getBoolean("redacted");
        this.silent = nbt.getBoolean("silent");
        this.player = nbt.getString("player");
    }

    public void setPlayer(String uuid){
        this.player = uuid;
    }

    public String getPlayer(){
        return this.player;
    }

    public boolean isPlayer(String uuid){
        return uuid.equals(this.player);
    }

    public void setChannel(String channel){
        this.channel = channel;
        setChanged();
    }

    public Component getChannelDisplay(String uuid){
        if (uuid.equals(this.player)){
            return this.redacted ? Component.literal(this.channel).append(Component.literal(" (").append(Component.translatable(LangInit.REDACTED)).append(")")) : Component.literal(this.channel);
        }
        return this.redacted ? Component.translatable(LangInit.REDACTED) : Component.literal(this.channel);
    }

    public String getChannel(){
        return this.channel;
    }

    public boolean hasChannel(){
        return !this.channel.toLowerCase().equals(NONE_CHANNEL);
    }

    public void setSilent(boolean silent){
        this.silent = silent;
        setChanged();
    }

    public void toggleSilent(){
        this.silent = !this.silent;
        setChanged();
    }

    public boolean isSilent(){
        return this.silent;
    }

    public void setRedacted(boolean redacted){
        this.redacted = redacted;
        setChanged();
    }

    public boolean isRedacted(){
        return this.redacted;
    }

    public void playSound(BlockState state, Level level, BlockPos pos){
        if(!this.silent){
            float f = state.getValue(BaseSwitchBlock.POWERED) ? 0.6F : 0.5F;
            level.playSound((Player)null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
        }
    }

    public boolean processInteraction(ItemStack held, Player player, SwitchData switchData){
        if (held.isEmpty() && !player.isShiftKeyDown())
            return false;
        else if(player.isShiftKeyDown()) {
            if(this.hasChannel()){
                player.displayClientMessage(Component.translatable(LangInit.CHANNEL).append(this.getChannelDisplay(player.getStringUUID())).append(" - ").append(Component.translatable(LangInit.ACTIVE)).append(String.valueOf(switchData.isActive(this.getChannel()))), true);
            } else {
                player.displayClientMessage(Component.translatable(LangInit.NO_CHANNEL), true);
            }
            return true;
        }
        
        boolean interactionProcessed = false;

        if (held.getItem() == Items.PAPER){
            String channel = held.getDisplayName().getString().replace("[", "").replace("]", "");
            this.setChannel(channel);
            player.displayClientMessage(Component.translatable(LangInit.SET_CHANNEL).append(channel), true);
            interactionProcessed = true;
        } else if (held.getItem() == Items.INK_SAC){
            if (this.isRedacted()){
                this.setRedacted(false);
            player.displayClientMessage(Component.translatable(LangInit.UNREDACTED), true);
            } else {
                this.setRedacted(true);
                player.displayClientMessage(Component.translatable(LangInit.SET_REDACTED), true);
            }
            interactionProcessed = true;
        } else if (held.getItem() == Items.WHITE_WOOL){
            this.toggleSilent();
            if (this.isSilent())
            {
                player.displayClientMessage(Component.translatable(LangInit.SILENCING), true);
            }
            else {
                player.displayClientMessage(Component.translatable(LangInit.UNSILENCING), true);
            }
            interactionProcessed = true;
        }

        return interactionProcessed;
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

    protected void tick(Level level, BlockPos pos, BlockState state){
        SwitchData switchData = SwitchData.get(level);
        boolean channelActive = switchData.isActive(this.getChannel());
        if (channelActive != state.getValue(LeverBlock.POWERED)){
            level.setBlockAndUpdate(pos, state.setValue(LeverBlock.POWERED, channelActive));
            this.playSound(state, level, pos);
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be){
        BaseChannelTile tile = (BaseChannelTile) be;
        if (!level.isClientSide() && tile.hasChannel()){
            tile.timer++;
            if (tile.timer > tile.timerMax){
                tile.timer = 0;
                tile.tick(level, pos, state);
            }
        }
    }
}
