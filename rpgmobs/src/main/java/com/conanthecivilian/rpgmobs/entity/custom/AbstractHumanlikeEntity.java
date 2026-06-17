package com.conanthecivilian.rpgmobs.entity.custom;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.service.FactionService;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractHumanlikeEntity<T extends AbstractHumanlikeEntity<T>> extends PathfinderMob implements
    IHumanLike,
    SmartBrainOwner<T> {
    protected AbstractHumanlikeEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public enum BipedArmPose {
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        NEUTRAL;
    }

    public FactionService factionService = new FactionService();

    public HashMap<EquipmentSlot, ItemStack> equipment = new HashMap<>();

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
        EntityType<? extends AbstractHumanlikeEntity<?>> humanlike,
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

    @Override
    protected Brain.@NotNull Provider<T> brainProvider() {
        return new SmartBrainProvider<>((T) this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain((T) this);
    }

    @Override
    public List<ExtendedSensor<T>> getSensors() {
        return ObjectArrayList.of(
            new NearbyLivingEntitySensor<>(), // This tracks nearby entities
            new HurtBySensor<>()              // This tracks the last damage source and attacker
        );
    }

    @Override
    public BrainActivityGroup<T> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            new LookAtTarget<>(),
            new MoveToWalkTarget<>(),
            new FloatToSurfaceOfFluid<>()
        );
    }

    @Override
    public BrainActivityGroup<T> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            new FirstApplicableBehaviour<T>(
                new SetPlayerLookTarget<>(),
                new SetRandomLookTarget<>()
            ),
            new OneRandomBehaviour<T>(
                new SetRandomWalkTarget<>(),
                new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
            )
        );
    }

    public void setEquipment(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        this.equipment.put(equipmentSlot, itemStack);
        this.setItemSlot(equipmentSlot, itemStack);

        this.onEquipmentChanged();
    }

    public void onEquipmentChanged() {
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        this.factionService.saveFactions(nbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.factionService.loadFactions(nbt);
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
    public void swing(@NotNull InteractionHand hand, boolean updateVisuals) {
        super.swing(hand, updateVisuals);

        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().broadcastAndSend(this,
                new ClientboundAnimatePacket(this, hand == InteractionHand.MAIN_HAND ? 0 : 3)
            );
        }
    }
}
