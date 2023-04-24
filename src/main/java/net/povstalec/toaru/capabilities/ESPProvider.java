package net.povstalec.toaru.capabilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ESPProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	public static Capability<ESP> ESPER_ABILITY = CapabilityManager.get(new CapabilityToken<ESP>() {});
	private ESP esperAbility = null;
	private final LazyOptional<ESP> optional = LazyOptional.of(this::getOrCreateEsperAbility);
	
	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag tag = new CompoundTag();
		getOrCreateEsperAbility().saveData(tag);
		
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag)
	{
		getOrCreateEsperAbility().loadData(tag);
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == ESPER_ABILITY)
			return optional.cast();
		return LazyOptional.empty();
	}
	
	private ESP getOrCreateEsperAbility()
	{
		if(this.esperAbility == null)
			this.esperAbility = new ESP();
		return this.esperAbility;
	}
}
