/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.effects;

import ivorius.pandorasbox.entitites.PandorasBoxEntity;
import ivorius.pandorasbox.utils.PBNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * Created by lukas on 03.04.14.
 */
public class PBEffectEntitiesThrowItems extends PBEffectEntityBased
{
    public double chancePerItem;
    public double itemDeletionChance;
    public PBEffectEntitiesThrowItems() {}

    public ItemStack[] smuggledInItems;

    public PBEffectEntitiesThrowItems(int maxTicksAlive, double range, double chancePerItem, double itemDeletionChance, ItemStack[] smuggledInItems)
    {
        super(maxTicksAlive, range);
        this.chancePerItem = chancePerItem;
        this.itemDeletionChance = itemDeletionChance;
        this.smuggledInItems = smuggledInItems;
    }

    @Override
    public void affectEntity(World world, PandorasBoxEntity box, Random random, LivingEntity entity, double newRatio, double prevRatio, double strength)
    {
        if (world instanceof ServerWorld && entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;

            Random itemRandom = new Random(entity.getId());
            for (int i = 0; i < player.inventory.getContainerSize(); i++)
            {
                double expectedThrow = itemRandom.nextDouble();
                if (newRatio >= expectedThrow && prevRatio < expectedThrow)
                {
                    ItemStack stack = player.inventory.getItem(i);
                    if (stack != null)
                    {
                        if (random.nextDouble() < chancePerItem)
                        {
                            if (random.nextDouble() >= itemDeletionChance)
                            {
                                player.drop(stack, false);
                            }

                            player.inventory.setItem(i, ItemStack.EMPTY);
                        }
                    }
                }
            }

            for (ItemStack smuggledInItem : smuggledInItems) {
                double expectedThrow = itemRandom.nextDouble();
                if (newRatio >= expectedThrow && prevRatio < expectedThrow) {
                    player.drop(smuggledInItem, false);
                }
            }
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound)
    {
        super.writeToNBT(compound);

        compound.putDouble("chancePerItem", chancePerItem);
        compound.putDouble("itemDeletionChance", itemDeletionChance);
        PBNBTHelper.writeNBTStacks("smuggledInItems", smuggledInItems, compound);
    }

    @Override
    public void readFromNBT(CompoundNBT compound)
    {
        super.readFromNBT(compound);

        chancePerItem = compound.getDouble("chancePerItem");
        itemDeletionChance = compound.getDouble("itemDeletionChance");
        smuggledInItems = PBNBTHelper.readNBTStacks("smuggledInItems", compound);
    }
}
