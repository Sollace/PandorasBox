/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox;

import ivorius.pandorasbox.block.PBBlocks;
import ivorius.pandorasbox.block.TileEntityPandorasBox;
import ivorius.pandorasbox.client.ClientProxy;
import ivorius.pandorasbox.client.rendering.RenderPandorasBox;
import ivorius.pandorasbox.effects.PBEffects;
import ivorius.pandorasbox.entitites.EntityPandorasBox;
import ivorius.pandorasbox.events.PBEventHandler;
import ivorius.pandorasbox.init.Registry;
import ivorius.pandorasbox.server.ServerProxy;
import ivorius.pandorasbox.utils.PBEffectArgument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PandorasBox.MOD_ID)
public class PandorasBox
{
    public static final String NAME = "Pandora's Box";
    public static final String MOD_ID = "pandorasbox";
    public static final String VERSION = "2.2.0.0-1.19.2";

    public static PandorasBox instance;

    public static PBProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public static String filePathTexturesFull = "pandorasbox:textures/mod/";
    public static String filePathTextures = "textures/mod/";
    public EntityType<EntityPandorasBox> PANDORAS_BOX;
    public static String basePath = "pandorasbox:";
    public static Logger logger  = LogManager.getLogger();

    public final IEventBus EVENT_BUS;
    public Feature<BaseTreeFeatureConfig> LOLIPOP;
    public Feature<BaseTreeFeatureConfig> COLOURFUL_TREE;
    public Feature<BaseTreeFeatureConfig> RAINBOW;
    public Feature<BaseTreeFeatureConfig> MEGA_JUNGLE;
    public RegistryObject<TileEntityType<TileEntityPandorasBox>> tileEntityPandorasBox;

    public static PBEventHandler fmlEventHandler;
    public PandorasBox() {
        // Register the setup method for modloading
        EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.init(EVENT_BUS);
        EVENT_BUS.addListener(this::preInit);
        EVENT_BUS.addListener(this::clientInit);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }

    public void preInit(final FMLCommonSetupEvent event)
    {
        LOLIPOP = Registry.LOLIPOP.get();
        COLOURFUL_TREE = Registry.COLOURFUL_TREE.get();
        RAINBOW = Registry.RAINBOW.get();
        MEGA_JUNGLE = Registry.MEGA_JUNGLE.get();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PBConfig.commonSpec);
        PBConfig.loadConfig();

        fmlEventHandler = new PBEventHandler();
        fmlEventHandler.register();


        PBBlocks.pandorasBox = Registry.PB.get();
        tileEntityPandorasBox = Registry.TEPB;


        PANDORAS_BOX = Registry.Box.get();
        ArgumentTypes.register("pbeffect", PBEffectArgument.class, new ArgumentSerializer<>(PBEffectArgument::effect));

        proxy.preInit();

        proxy.load();
    }
    public void clientInit(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(Registry.Box.get(), RenderPandorasBox::new);
    }
}