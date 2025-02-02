/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.effects;

import ivorius.pandorasbox.entitites.PandorasBoxEntity;
import ivorius.pandorasbox.utils.PBNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by lukas on 12.04.14.
 */
public class PBEffectGenCover extends PBEffectGenerateByFlag
{
    public boolean overSurface;
    public Block[] blocks;
    public PBEffectGenCover() {}

    public PBEffectGenCover(int time, double range, int unifiedSeed, boolean overSurface, Block[] blocks)
    {
        super(time, range, 1, unifiedSeed);
        this.overSurface = overSurface;
        this.blocks = blocks;
    }

    @Override
    public boolean hasFlag(World world, PandorasBoxEntity entity, Random random, BlockPos pos)
    {
        if (overSurface)
        {
            if (!isReplacable(world, pos))
            {
                return false;
            }

            return world.loadedAndEntityCanStandOn(pos.west(), entity) || world.loadedAndEntityCanStandOn(pos.east(), entity)
                    || world.loadedAndEntityCanStandOn(pos.below(), entity) || world.loadedAndEntityCanStandOn(pos.above(), entity)
                    || world.loadedAndEntityCanStandOn(pos.north(), entity) || world.loadedAndEntityCanStandOn(pos.south(), entity);
        }
        else
        {
            return isReplacable(world, pos.west()) || isReplacable(world, pos.east())
                    || isReplacable(world, pos.below()) || isReplacable(world, pos.above())
                    || isReplacable(world, pos.north()) || isReplacable(world, pos.south());
        }
    }

    private boolean isReplacable(World world, BlockPos pos)
    {
        return world.getBlockState(pos).canSurvive(world, pos);
    }

    @Override
    public void generateOnBlock(World world, PandorasBoxEntity entity, Random random, int pass, BlockPos pos, double range, boolean flag)
    {
        if (flag)
        {
            Block newBlock = blocks[random.nextInt(blocks.length)];
            world.setBlockAndUpdate(pos, newBlock.defaultBlockState());
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound)
    {
        super.writeToNBT(compound);

        compound.putBoolean("overSurface", overSurface);
        PBNBTHelper.writeNBTBlocks("blocks", blocks, compound);
    }

    @Override
    public void readFromNBT(CompoundNBT compound)
    {
        super.readFromNBT(compound);

        overSurface = compound.getBoolean("overSurface");
        blocks = PBNBTHelper.readNBTBlocks("blocks", compound);
    }
}
