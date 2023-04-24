package net.povstalec.toaru.abilities;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public abstract class Ability
{
	/**
	 * 
	 * @param esper
	 */
	public abstract void onTick(Entity esper);
	
	/**
	 * 
	 * @param esper
	 * @param attacker
	 * @return Should cancel event
	 */
	public abstract boolean onHurt(Entity esper, DamageSource source, float amount);
	
	/**
	 * 
	 * @param esper
	 * @param strength
	 * @param ratioX
	 * @param ratioZ
	 * @return Should cancel event
	 */
	public abstract boolean onKnockback(Entity esper, float strength, double ratioX, double ratioZ);
}
