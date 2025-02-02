/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.effects;

import ivorius.pandorasbox.PandorasBox;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * Created by lukas on 30.03.14.
 */
public class PBEffectRegistry
{
    private static Hashtable<String, Class<? extends PBEffect>> registeredEffects = new Hashtable<>();

    public static Class<? extends PBEffect> getEffect(String id)
    {
        return registeredEffects.get(id);
    }
    public static final List<ResourceLocation> resourceLocationList = new ArrayList<>();

    public static void register(Class<? extends PBEffect> effect, String id)
    {
        id = id.toLowerCase();
        resourceLocationList.add(new ResourceLocation(PandorasBox.MOD_ID, id));
        registeredEffects.put(id, effect);
    }

    public static Set<String> getAllEffectIDs()
    {
        return registeredEffects.keySet();
    }

    public static Collection<Class<? extends PBEffect>> getAllEffects()
    {
        return registeredEffects.values();
    }

    public static void writeEffect(PBEffect effect, CompoundNBT compound)
    {
        if (effect != null)
        {
            compound.putString("pbEffectID", effect.getEffectID());
            CompoundNBT pbEffectCompound = new CompoundNBT();
            effect.writeToNBT(pbEffectCompound);
            compound.put("pbEffectCompound", pbEffectCompound);
        }
    }

    public static PBEffect loadEffect(CompoundNBT compound)
    {
        return loadEffect(compound.getString("pbEffectID"), compound.getCompound("pbEffectCompound"));
    }

    public static PBEffect loadEffect(String id, CompoundNBT compound)
    {
        Class<? extends PBEffect> clazz = getEffect(id);

        PBEffect effect = null;

        if (clazz != null)
        {
            try
            {
                effect = clazz.newInstance();
            }
            catch (InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        if (effect != null && compound != null)
        {
            effect.readFromNBT(compound);
            return effect;
        }
        else
        {
            System.err.println("Pandoras Box: Could not load effect with id '" + id + "'!");
        }

        return null;
    }

    public static String getEffectID(PBEffect effect)
    {
        Class<? extends PBEffect> clazz = effect.getClass();

        for (String id : registeredEffects.keySet())
        {
            if (registeredEffects.get(id).equals(clazz))
            {
                return id;
            }
        }

        return null;
    }
}
