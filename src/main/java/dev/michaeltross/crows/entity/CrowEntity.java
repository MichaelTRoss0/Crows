package dev.michaeltross.crows.entity;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

public class CrowEntity extends Animal implements NeutralMob, FlyingAnimal {
    private static final Ingredient TEMPT_ITEMS = Ingredient.of(Tags.Items.SEEDS);
    private static final Item POISONOUS_FOOD = Items.COOKIE;
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public CrowEntity(EntityType<? extends CrowEntity> crow, Level level) {
        super(crow, level);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }
    
    @Override
    public boolean causeFallDamage(float x, float y, DamageSource source) {
        return false;
     }

    @Override
     protected void checkFallDamage(double x, boolean bool, BlockState state, BlockPos pos) {
     }

    @Override
    public boolean isFlying() {
        return !this.onGround;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int p_21673_) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public UUID getPersistentAngerTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPersistentAngerTarget(UUID p_21672_) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void startPersistentAngerTimer() {
        // TODO Auto-generated method stub
        
    }

}
