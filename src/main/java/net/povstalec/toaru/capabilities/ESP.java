package net.povstalec.toaru.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.povstalec.toaru.abilities.Ability;
import net.povstalec.toaru.abilities.esper.VectorControl;

public class ESP
{
	private static final String FIRST_JOIN = "FirstJoin";
	private static final String ESPER_ABILITY = "EsperAbility";
	
	public enum AbilityType
	{
		NONE(null),
		VECTOR_CONTROL(new VectorControl());
		
		private final Ability ability;
		
		private AbilityType(Ability ability)
		{
			this.ability = ability;
		}
		
		private Ability getAbility()
		{
			return this.ability;
		}
	}
	
	private boolean firstJoin = true;
	private AbilityType esperAbility = AbilityType.NONE;
	
	public void onAbilityTick(Entity esper)
	{
		Ability ability = this.esperAbility.getAbility();
		
		if(ability != null)
			ability.onTick(esper);
	}
	
	public boolean onAbilityHurt(Entity esper, DamageSource source, float amount)
	{
		Ability ability = this.esperAbility.getAbility();
		
		if(ability != null)
			return ability.onHurt(esper, source, amount);
		return false;
	}
	
	public boolean onAbilityKnockback(Entity esper, float strength, double ratioX, double ratioZ)
	{
		Ability ability = this.esperAbility.getAbility();
		
		if(ability != null)
			return this.esperAbility.getAbility().onKnockback(esper, strength, ratioX, ratioZ);
		return false;
	}
	
	public void giveAbility(AbilityType esperAbility)
	{
		this.esperAbility = esperAbility;
	}
	
	public void removeAbility()
	{
		this.esperAbility = AbilityType.NONE;
	}
	
	public static void grantAbility(Entity entity)
	{
		entity.getCapability(ESPProvider.ESPER_ABILITY).ifPresent(cap -> cap.giveAbility(AbilityType.VECTOR_CONTROL));
	}
	
	public boolean firstJoin()
	{
		return this.firstJoin;
	}
	
	public void markJoined()
	{
		this.firstJoin = false;
	}
	
	public void copyFrom(ESP source)
	{
		this.esperAbility = source.esperAbility;
	}
	
	public void saveData(CompoundTag tag)
	{
		tag.putBoolean(FIRST_JOIN, firstJoin);
		tag.putString("EsperAbility", this.esperAbility.toString().toUpperCase());
	}
	
	public void loadData(CompoundTag tag)
	{
		if(tag.contains(FIRST_JOIN))
			this.firstJoin = tag.getBoolean(FIRST_JOIN);
		else
			this.firstJoin = false;
		
		if(tag.contains(ESPER_ABILITY))
			this.esperAbility = AbilityType.valueOf(tag.getString(ESPER_ABILITY));
		else
			this.esperAbility = AbilityType.NONE;
	}
}
