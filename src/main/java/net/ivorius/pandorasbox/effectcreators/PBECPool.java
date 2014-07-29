/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package net.ivorius.pandorasbox.effectcreators;

import net.ivorius.pandorasbox.PandorasBoxHelper;
import net.ivorius.pandorasbox.effects.PBEffect;
import net.ivorius.pandorasbox.effects.PBEffectGenPool;
import net.ivorius.pandorasbox.random.DValue;
import net.ivorius.pandorasbox.weighted.WeightedBlock;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

/**
 * Created by lukas on 30.03.14.
 */
public class PBECPool implements PBEffectCreator
{
    public DValue range;

    public Block block;
    public Collection<WeightedBlock> platformBlocks;

    public PBECPool(DValue range, Block block, Collection<WeightedBlock> platformBlocks)
    {
        this.range = range;
        this.block = block;
        this.platformBlocks = platformBlocks;
    }

    @Override
    public PBEffect constructEffect(World world, double x, double y, double z, Random random)
    {
        double range = this.range.getValue(random);
        int time = MathHelper.floor_double((random.nextDouble() * 7.0 + 3.0) * range);

        Block platformBlock = PandorasBoxHelper.getRandomBlock(random, platformBlocks);

        PBEffectGenPool genPool = new PBEffectGenPool(time, range, PandorasBoxHelper.getRandomUnifiedSeed(random), block, platformBlock);
        return genPool;
    }

    @Override
    public float chanceForMoreEffects(World world, double x, double y, double z, Random random)
    {
        return 0.1f;
    }
}
