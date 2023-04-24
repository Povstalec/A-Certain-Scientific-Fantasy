package net.povstalec.toaru.init;

import java.util.Optional;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.povstalec.toaru.Toaru;
import net.povstalec.toaru.capabilities.ESP;
import net.povstalec.toaru.capabilities.ESPProvider;

@Mod.EventBusSubscriber(modid = Toaru.MODID)
public class EventInit
{
	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event)
	{
		if(event.getEntity() instanceof Player player)
		{
			player.getCapability(ESPProvider.ESPER_ABILITY).ifPresent(esperAbility -> esperAbility.onAbilityTick(player));
		}
	}
	
	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event)
	{
		if(event.getEntity() instanceof Player player)
		{
			Optional<Boolean> optional = player.getCapability(ESPProvider.ESPER_ABILITY).map(esperAbility -> esperAbility.onAbilityHurt(player, event.getSource(), event.getAmount()));
			boolean cancelEvent = optional.isPresent() ? optional.get() : false;
			event.setCanceled(cancelEvent);
		}
	}
	
	@SubscribeEvent
	public static void onLivingKnockback(LivingKnockBackEvent event)
	{
		if(event.getEntity() instanceof Player player)
		{
			Optional<Boolean> optional = player.getCapability(ESPProvider.ESPER_ABILITY).map(esperAbility -> esperAbility.onAbilityKnockback(player, event.getOriginalStrength(), event.getOriginalRatioX(), event.getOriginalRatioZ()));
			boolean cancelEvent = optional.isPresent() ? optional.get() : false;
			event.setCanceled(cancelEvent);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event)
	{
		Player player = event.getEntity();
		
		ESP.grantAbility(player);
	}
	
	@SubscribeEvent
	public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof Player player)
		{
			if(!player.getCapability(ESPProvider.ESPER_ABILITY).isPresent())
				event.addCapability(new ResourceLocation(Toaru.MODID, "ability"), new ESPProvider());
		}
	}
	
	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event)
	{
		Player original = event.getOriginal();
		Player clone = event.getEntity();
		original.reviveCaps();
		
		original.getCapability(ESPProvider.ESPER_ABILITY).ifPresent(oldCap ->
			clone.getCapability(ESPProvider.ESPER_ABILITY).ifPresent(newCap -> newCap.copyFrom(oldCap)));
		
		original.invalidateCaps();
	}
	
	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event)
	{
		event.register(ESP.class);
	}
}
