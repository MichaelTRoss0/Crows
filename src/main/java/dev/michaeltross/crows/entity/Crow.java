package dev.michaeltross.crows.entity;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

public class Crow extends Animal implements NeutralMob, FlyingAnimal {

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Tags.Items.SEEDS);
    private static final Item POISONOUS_FOOD = Items.COOKIE;
    private static final EntityDataAccessor<Boolean> DATA_TRUSTING = SynchedEntityData.defineId(Crow.class,
            EntityDataSerializers.BOOLEAN);
    @Nullable
    BlockPos homePos;
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;
    @Nullable
    private Crow.CrowAvoidEntityGoal<Player> crowAvoidPlayersGoal;
    @Nullable
    private Crow.CrowTemptGoal temptGoal;
    @Nullable
    private Crow.CrowCircleMobGoal circleHostileMob;
    @Nullable
    private Crow.CrowCircleMobGoal circlePriorityMob;

    public Crow(EntityType<? extends Crow> crow, Level level) {
        super(crow, level);
    }

    boolean isTrusting() {
        return this.entityData.get(DATA_TRUSTING);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TRUSTING, false);
    }

    @Override
    protected void registerGoals() {
        this.temptGoal = new Crow.CrowTemptGoal(this, 1.25D, FOOD_ITEMS, false);

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, this.temptGoal);
        this.goalSelector.addGoal(0, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 10.F));
        this.goalSelector.addGoal(0, this.circleHostileMob);
        this.goalSelector.addGoal(0, this.circlePriorityMob);
        this.goalSelector.addGoal(0, new CrowRoostGoal());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FLYING_SPEED, (double) 0.7F)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.2F).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static boolean canCrowSpawn(EntityType<? extends Crow> animal, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, Random random) {
        return checkAnimalSpawnRules(animal, levelAccessor, spawnType, pos, random);
    }

    @Override
    public boolean causeFallDamage(float x, float y, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double x, boolean bool, BlockState state, BlockPos pos) {
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float) ((double) this.flapSpeed
                + (double) (!this.onGround && !this.isPassenger() ? 4 : -1) * 0.3D);
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping = (float) ((double) this.flapping * 0.9D);
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
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

    static class CrowAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Crow crow;

        public CrowAvoidEntityGoal(Crow crow, Class<T> avoidClass, float maxDist, double walkSpeedModifier,
                double sprintSpeedModifier) {
            super(crow, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier,
                    EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.crow = crow;
        }

        @Override
        public boolean canUse() {
            return !this.crow.isTrusting() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.crow.isTrusting() && super.canContinueToUse();
        }

    }

    static class CrowTemptGoal extends TemptGoal {
        private final Crow crow;

        public CrowTemptGoal(Crow crow, double speedModifier, Ingredient items, boolean canScare) {
            super(crow, speedModifier, items, canScare);
            this.crow = crow;
        }

        @Override
        public void stop() {
            this.crow.homePos = this.player.getOnPos();
            super.stop();
        }
    }

    public class CrowCircleMobGoal extends Goal {

        @Override
        public boolean canUse() {
            // TODO Auto-generated method stub
            return false;
        }
    }

    class CrowRoostGoal extends Goal {

        @Override
        public boolean canUse() {
            // TODO Auto-generated method stub
            return false;
        }

    }

    class CrowWanderGoal extends Goal {

        @Override
        public boolean canUse() {
            // TODO Auto-generated method stub
            return false;
        }

    }

}
