/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package net.ivorius.pandorasbox.effects;

import net.ivorius.pandorasbox.entitites.EntityPandorasBox;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by lukas on 03.04.14.
 */
public class PBEffectSetTime extends PBEffectNormal
{
    public int totalPlus;

    public PBEffectSetTime()
    {
    }

    public PBEffectSetTime(int maxTicksAlive, int totalPlus)
    {
        super(maxTicksAlive);
        this.totalPlus = totalPlus;
    }

    @Override
    public void doEffect(World world, EntityPandorasBox entity, Random random, float newRatio, float prevRatio)
    {
        int newPlus = MathHelper.floor_float(totalPlus * newRatio);
        int prevPlus = MathHelper.floor_float(totalPlus * prevRatio);
        int plus = newPlus - prevPlus;

        world.setWorldTime(world.getWorldTime() + plus);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setInteger("totalPlus", totalPlus);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        totalPlus = compound.getInteger("totalPlus");
    }
}
