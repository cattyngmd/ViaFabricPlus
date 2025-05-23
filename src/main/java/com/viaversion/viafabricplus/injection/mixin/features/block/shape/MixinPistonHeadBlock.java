/*
 * This file is part of ViaFabricPlus - https://github.com/ViaVersion/ViaFabricPlus
 * Copyright (C) 2021-2025 the original authors
 *                         - FlorianMichael/EnZaXD <florian.michael07@gmail.com>
 *                         - RK_01/RaphiMC
 * Copyright (C) 2023-2025 ViaVersion and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.viaversion.viafabricplus.injection.mixin.features.block.shape;

import com.viaversion.viafabricplus.protocoltranslator.ProtocolTranslator;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonHeadBlock.class)
public abstract class MixinPistonHeadBlock extends FacingBlock {

    @Unique
    private static final VoxelShape viaFabricPlus$east_head_shape = Block.createCuboidShape(12.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    @Unique
    private static final VoxelShape viaFabricPlus$west_head_shape = Block.createCuboidShape(0.0, 0.0, 0.0, 4.0, 16.0, 16.0);

    @Unique
    private static final VoxelShape viaFabricPlus$south_head_shape = Block.createCuboidShape(0.0, 0.0, 12.0, 16.0, 16.0, 16.0);

    @Unique
    private static final VoxelShape viaFabricPlus$north_head_shape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 4.0);

    @Unique
    private static final VoxelShape viaFabricPlus$up_head_shape = Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);

    @Unique
    private static final VoxelShape viaFabricPlus$down_head_shape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    @Unique
    private static final VoxelShape viaFabricPlus$up_arm_shape_r1_8_x = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 12.0, 10.0);

    @Unique
    private static final VoxelShape viaFabricPlus$down_arm_shape_r1_8_x = Block.createCuboidShape(6.0, 4.0, 6.0, 10.0, 16.0, 10.0);

    @Unique
    private static final VoxelShape viaFabricPlus$south_arm_shape_r1_8_x = Block.createCuboidShape(4.0, 6.0, 0.0, 12.0, 10.0, 12.0);

    @Unique
    private static final VoxelShape viaFabricPlus$north_arm_shape_r1_8_x = Block.createCuboidShape(4.0, 6.0, 4.0, 12.0, 10.0, 16.0);

    @Unique
    private static final VoxelShape viaFabricPlus$east_arm_shape_r1_8_x = Block.createCuboidShape(0.0, 6.0, 4.0, 12.0, 10.0, 12.0);

    @Unique
    private static final VoxelShape viaFabricPlus$west_arm_shape_r1_8_x = Block.createCuboidShape(6.0, 4.0, 4.0, 10.0, 12.0, 16.0);

    @Unique
    private boolean viaFabricPlus$selfInflicted = false;

    protected MixinPistonHeadBlock(Settings settings) {
        super(settings);
    }

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    private void changeOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (viaFabricPlus$selfInflicted) {
            viaFabricPlus$selfInflicted = false;
            return;
        }
        // Outline shape for piston head doesn't exist in <= 1.12.2
        if (ProtocolTranslator.getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_12_2)) {
            cir.setReturnValue(switch (state.get(PistonHeadBlock.FACING)) {
                case DOWN -> viaFabricPlus$down_head_shape;
                case UP -> viaFabricPlus$up_head_shape;
                case NORTH -> viaFabricPlus$north_head_shape;
                case SOUTH -> viaFabricPlus$south_head_shape;
                case WEST -> viaFabricPlus$west_head_shape;
                case EAST -> viaFabricPlus$east_head_shape;
            });
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (ProtocolTranslator.getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8)) {
            return switch (state.get(PistonHeadBlock.FACING)) {
                case DOWN -> VoxelShapes.union(viaFabricPlus$down_head_shape, viaFabricPlus$down_arm_shape_r1_8_x);
                case UP -> VoxelShapes.union(viaFabricPlus$up_head_shape, viaFabricPlus$up_arm_shape_r1_8_x);
                case NORTH -> VoxelShapes.union(viaFabricPlus$north_head_shape, viaFabricPlus$north_arm_shape_r1_8_x);
                case SOUTH -> VoxelShapes.union(viaFabricPlus$south_head_shape, viaFabricPlus$south_arm_shape_r1_8_x);
                case WEST -> VoxelShapes.union(viaFabricPlus$west_head_shape, viaFabricPlus$west_arm_shape_r1_8_x);
                case EAST -> VoxelShapes.union(viaFabricPlus$east_head_shape, viaFabricPlus$east_arm_shape_r1_8_x);
            };
        } else if (ProtocolTranslator.getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_12_2)) {
            // Collision shape for piston head in <= 1.12.2 needs to be the 1.13+ outline shape
            viaFabricPlus$selfInflicted = true;
            return getOutlineShape(state, world, pos, context);
        } else {
            return super.getCollisionShape(state, world, pos, context);
        }
    }

}
