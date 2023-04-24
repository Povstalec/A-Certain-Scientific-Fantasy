package net.povstalec.toaru.abilities.esper;

import java.util.List;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.povstalec.toaru.abilities.Ability;

public class VectorControl extends Ability
{

	@Override
	public void onTick(Entity esper)
	{
		Level level = esper.getLevel();
		AABB vectorShield = esper.getBoundingBox().inflate(10.0);
		
		List<Projectile> projectiles = level.getEntitiesOfClass(Projectile.class, vectorShield);
		doVectorShield(esper, projectiles);
		
		List<FallingBlockEntity> fallingBlocks = level.getEntitiesOfClass(FallingBlockEntity.class, vectorShield);
		doVectorShield(esper, fallingBlocks);
	}

	@Override
	public boolean onHurt(Entity esper, DamageSource source, float amount)
	{
		Entity attacker = source.getDirectEntity();
		
		if(esper != null && attacker != null)
		{
			Vec3 vector = attacker.getBoundingBox().getCenter();
			vector = vector.subtract(esper.getBoundingBox().getCenter());
			
			vector.vectorTo(vector);
			
			attacker.push(vector.x(), vector.y(), vector.z());
			attacker.hurt(source, amount);
		}
		
		return true;
	}

	@Override
	public boolean onKnockback(Entity esper, float strength, double ratioX, double ratioZ)
	{
		return true;
	}
	
	private void doVectorShield(Entity esper, List<? extends Entity> entities)
	{
		entities.stream().forEach(entity ->
		{
			if(!entity.equals(esper))
			{
				Vec3 vector = entity.getDeltaMovement();
				if(isVectorHarmful(esper, entity, vector))
				{
					vector = vector.reverse();
					entity.setDeltaMovement(vector);
					if(esper instanceof ServerPlayer player)
						player.connection.send(new ClientboundSetEntityMotionPacket(entity));
				}
			}
		});
	}
	
	private boolean isVectorHarmful(Entity esper, Entity attacker, Vec3 vector)
	{
		if(esper != null && attacker != null)
		{
			Vec3 esperPos = esper.getBoundingBox().getCenter();
			Vec3 attackerPos = attacker.getBoundingBox().getCenter();
			
			return willCollide(esperPos, attackerPos, vector);
		}
		
		return false;
	}
	
	private boolean willCollide(Vec3 esperPos, Vec3 attackerPos, Vec3 vector)
	{
		if(isVectorPointingTowardsEsper(esperPos, attackerPos, vector))
		{
			Vec3 distanceVector = attackerPos.vectorTo(esperPos);
			
			double distance = distanceVector.length();

			Vec3 vectorShieldStart = distanceVector.scale((distance - 1.1) / distance);
			
			return vector.length() >= vectorShieldStart.length();
		}
			
		return false;
	}
	
	private boolean isVectorPointingTowardsEsper(Vec3 esperPos, Vec3 attackerPos, Vec3 vector)
	{
		return attackerPos.vectorTo(esperPos).normalize().dot(vector) > 0;
	}
	
}
