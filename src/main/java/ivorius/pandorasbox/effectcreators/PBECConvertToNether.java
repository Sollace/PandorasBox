/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.effectcreators;

import ivorius.pandorasbox.PandorasBoxHelper;
import ivorius.pandorasbox.effects.PBEffect;
import ivorius.pandorasbox.effects.PBEffectGenConvertToNether;
import ivorius.pandorasbox.random.DValue;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by lukas on 30.03.14.
 */
public class PBECConvertToNether implements PBEffectCreator
{
    public DValue range;
    public String biome;

    public PBECConvertToNether(DValue range, String biome)
    {
        this.range = range;
        this.biome = biome;
    }

    @Override
    public PBEffect constructEffect(World world, double x, double y, double z, Random random)
    {
        double range = this.range.getValue(random);
        int time = MathHelper.floor((random.nextDouble() * 7.0 + 3.0) * range);

        return new PBEffectGenConvertToNether(time, range, PandorasBoxHelper.getRandomUnifiedSeed(random), biome);
    }

    @Override
    public float chanceForMoreEffects(World world, double x, double y, double z, Random random)
    {
        return 0.1f;
    }
}
