package com.conanthecivilian.rpgmobs.entity.custom;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.service.FactionService;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public abstract class AbstractHumanlikeEntity extends PathfinderMob implements IBipedEntity {
    public static enum BipedArmPose {
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        NEUTRAL;
    }

    public FactionService factionService = new FactionService();

    public HashMap<EquipmentSlot, ItemStack> equipment = new HashMap<>();

    public boolean isCoward = false;
    public boolean isFleeing = false;

    public AbstractHumanlikeEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MOVEMENT_SPEED, 0.35F)
            .add(Attributes.MAX_HEALTH, 10.0)
            .add(Attributes.ATTACK_DAMAGE, 2.0)
            .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    public BipedArmPose getArmPose() {
        return BipedArmPose.NEUTRAL;
    }

    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
    }

    public ResourceLocation getTextureLocation() {
        return null;
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter level, BlockPos pos) {
        return true;
    }

    public static boolean checkHumanlikeSpawnRules(
        EntityType<? extends AbstractHumanlikeEntity> humanlike,
        LevelAccessor level,
        MobSpawnType spawnType,
        BlockPos pos,
        RandomSource random
    ) {
        boolean lightValid = MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos);
        boolean blockValid = level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON);

        RPGMobs.LOGGER.info("Evaluating spawn for {} at [{} {} {}] -> Light Valid: {}, Block Valid: {}",
                humanlike.getDescriptionId(), pos.getX(), pos.getY(), pos.getZ(), lightValid, blockValid);

        return blockValid && lightValid;
    }

    public void registerBasicGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
        //this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public void registerCowardGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
    }

    public void setEquipment(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        this.equipment.put(equipmentSlot, itemStack);
        this.setItemSlot(equipmentSlot, itemStack);

        this.onEquipmentChanged();
    }

    public void onEquipmentChanged() {}

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        this.factionService.saveEnemyFactions(nbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.factionService.loadEnemyFactions(nbt);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);

        if (this.equipment.containsKey(EquipmentSlot.MAINHAND)) {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.equipment.get(EquipmentSlot.MAINHAND));
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor levelAccessor,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficulty, spawnType, spawnGroupData);

        RandomSource random = levelAccessor.getRandom();
        this.populateDefaultEquipmentSlots(random, difficulty);

        return spawnGroupData;
    }

    @Override
    protected void registerGoals() {
        this.registerBasicGoals();

        if (this.isCoward) {
            this.registerCowardGoals();
        }
    }

    @Override
    public void swing(@NotNull InteractionHand hand, boolean updateVisuals) {
        super.swing(hand, updateVisuals);

        // If we are on the server side, broadcast the swing animation status to the clients
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().broadcastAndSend(this,
                new ClientboundAnimatePacket(this, hand == InteractionHand.MAIN_HAND ? 0 : 3)
            );
        }
    }
}
