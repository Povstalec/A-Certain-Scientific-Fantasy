package net.povstalec.toaru;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Toaru.MODID)
public class Toaru
{
    public static final String MODID = "toaru";
    
    public static final Logger LOGGER = LogUtils.getLogger();

    public Toaru()
    {
    	IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        eventBus.addListener(this::commonSetup);
        
		MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
    	event.enqueueWork(() -> 
    	{
    		
    	});
    }
    
    @Mod.EventBusSubscriber(modid = Toaru.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        	
        }
    }
    
}
