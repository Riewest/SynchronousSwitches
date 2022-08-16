package com.suicidesquid.syncswitch.blocks;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.SwitchBlockTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SwitchBlock extends BaseSwitchBlock implements EntityBlock{

    public SwitchBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

		return switch (state.getValue(FACING)) {
			default -> switch (state.getValue(FACE)) {
				case FLOOR -> box(5, 0, 3, 11, 6, 13);
				case WALL -> box(5, 3, 0, 11, 13, 6);
				case CEILING -> box(5, 10, 3, 11, 16, 13);
			};
			case NORTH -> switch (state.getValue(FACE)) {
				case FLOOR -> box(5, 0, 3, 11, 6, 13);
				case WALL -> box(5, 3, 10, 11, 13, 16);
				case CEILING -> box(5, 10, 3, 11, 16, 13);
			};
			case EAST -> switch (state.getValue(FACE)) {
				case FLOOR -> box(3, 0, 5, 13, 6, 11);
				case WALL -> box(0, 3, 5, 6, 13, 11);
				case CEILING -> box(3, 10, 5, 13, 16, 11);
			};
			case WEST -> switch (state.getValue(FACE)) {
				case FLOOR -> box(3, 0, 5, 13, 6, 11);
				case WALL -> box(10, 3, 5, 16, 13, 11);
				case CEILING -> box(3, 10, 5, 13, 16, 11);
			};
		};
	}

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.SWITCH_BLOCK.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.SWITCH_BLOCK.get() ? SwitchBlockTile::tick : null;
    }

}
