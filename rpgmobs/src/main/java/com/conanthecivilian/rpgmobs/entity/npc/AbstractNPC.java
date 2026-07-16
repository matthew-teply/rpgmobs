package com.conanthecivilian.rpgmobs.entity.npc;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.ModAttachments;
import com.conanthecivilian.rpgmobs.entity.conversation.AttachedConversationTopics;
import com.conanthecivilian.rpgmobs.entity.conversation.ConversationTopic;
import com.conanthecivilian.rpgmobs.entity.conversation.IConversationTopicsAccessor;
import com.conanthecivilian.rpgmobs.entity.npc.data.NPCData;
import com.conanthecivilian.rpgmobs.entity.npc.data.NPCRace;
import com.conanthecivilian.rpgmobs.entity.npc.template.NPCTemplate;
import com.conanthecivilian.rpgmobs.entity.trait.AttachedTraits;
import com.conanthecivilian.rpgmobs.entity.trait.ITraitHolder;
import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.ConversationDenialReason;
import com.conanthecivilian.rpgmobs.manager.FactionManager.FactionManager;
import com.conanthecivilian.rpgmobs.manager.NPCSpawnManager.NPCSpawnManager;
import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitManager;
import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitType;
import com.conanthecivilian.rpgmobs.repository.ConversationRepository;
import com.conanthecivilian.rpgmobs.repository.RaceRepository;
import com.conanthecivilian.rpgmobs.repository.TraitDatabaseRepository;
import com.conanthecivilian.rpgmobs.repository.TraitRepository;
import com.conanthecivilian.rpgmobs.screen.custom.conversation.ConversationMenu;
import com.conanthecivilian.rpgmobs.screen.custom.conversation.ConversationUI;
import com.lowdragmc.lowdraglib2.gui.factory.IContainerUIHolder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractNPC<T extends AbstractNPC<T>> extends PathfinderMob implements
    INPC,
    RangedAttackMob,
    CrossbowAttackMob,
    SmartBrainOwner<T>,
    IContainerUIHolder,
    IConversationTopicsAccessor,
    ITraitHolder {

    private static final EntityDataAccessor<Integer> RELATIONSHIP = SynchedEntityData.defineId(AbstractNPC.class, EntityDataSerializers.INT);

    public FactionManager factionManager = new FactionManager();
    public HashMap<EquipmentSlot, ItemStack> equipment = new HashMap<>();

    public enum HumanlikeArmPose {
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        NEUTRAL;
    }

    protected AbstractNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.setCustomName(Component.literal(String.valueOf(this.getId())));

        this.setData(
            ModAttachments.NPC_TOPICS.get(),
            new AttachedConversationTopics(this)
        );
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(RELATIONSHIP, 0);
    }

    @Override
    public List<ResourceLocation> getDefaultConversationTopics() {
        return List.of(
            ResourceLocation.parse("rpgmobs:topic_background"),
            ResourceLocation.parse("rpgmobs:topic_chat"),
            ResourceLocation.parse("rpgmobs:topic_nearby_enemies"),
            ResourceLocation.parse("rpgmobs:topic_creeper")
        );
    }

    @Override
    public List<ConversationTopic> getConversationTopics() {
        return this
            .getData(ModAttachments.NPC_TOPICS.get())
            .topics()
            .stream()
            .map(ConversationRepository::getTopic)
            .toList();
    }

    @Override
    public void unlockConversationTopics(ResourceLocation topicId) {
    }

    public void setRelationship(int value) {
        if (value < 0 || value > 100) {
            return;
        }

        this.entityData.set(RELATIONSHIP, value);
    }

    public int getRelationship() {
        return this.entityData.get(RELATIONSHIP);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            RPGMobs.LOGGER.info(this.getData(ModAttachments.NPC_TRAITS).toString());

            if (!this.canHoldConversation(player)) {
                this.tellPlayerConversationDenialReason(player);
                return InteractionResult.FAIL;
            }

            ConversationMenu.open(serverPlayer, this);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull ModularUI createUI(@NotNull Player player) {
        ConversationUI conversationUI = new ConversationUI(player, this);

        return conversationUI.createModularUI();
    }

    public void tellPlayerConversationDenialReason(Player player) {
        MutableComponent denialMessage = Component.literal(this.getName().getString() + ": ")
            .withStyle(ChatFormatting.WHITE);

        switch (this.getConversationDenialReason(player)) {
            case ATTACKING -> {
                assert this.getTarget() != null;

                denialMessage.append(
                    Component.literal("I am busy fighting this " + this.getTarget().getName().getString() + "!")
                        .withStyle(ChatFormatting.YELLOW)
                );
            }
            case PLAYER_ENEMY -> denialMessage.append(
                Component.literal("You are my enemy!")
                    .withStyle(ChatFormatting.YELLOW)
            );
            default -> {
                return;
            }
        }

        player.sendSystemMessage(denialMessage);
    }

    public ConversationDenialReason getConversationDenialReason(Player player) {
        if (this.dead) {
            return ConversationDenialReason.DEAD;
        }

        if (this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            return ConversationDenialReason.ATTACKING;
        }

        if (this.factionManager.isEnemyFaction(player)) {
            return ConversationDenialReason.PLAYER_ENEMY;
        }

        return ConversationDenialReason.NONE;
    }

    public boolean canHoldConversation(@NotNull Player player) {
        return this.getConversationDenialReason(player) == ConversationDenialReason.NONE;
    }

    @Override
    public boolean isStillValid(@NotNull Player player) {
        this.tellPlayerConversationDenialReason(player);

        return this.canHoldConversation(player);
    }

    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter level, BlockPos pos) {
        return true;
    }

    public static boolean checkNPCSpawnRules(
        EntityType<? extends AbstractNPC<?>> npc,
        LevelAccessor level,
        MobSpawnType spawnType,
        BlockPos pos,
        RandomSource random
    ) {
        // RPGMobs.LOGGER.info("Evaluating spawn for {} at [{} {} {}]", npc.getDescriptionId(), pos.getX(), pos.getY(), pos.getZ());

        List<NPCTemplate> viableTemplates = NPCSpawnManager.getViableTemplates(level, pos);

        if (viableTemplates.isEmpty()) {
            // RPGMobs.LOGGER.info("No viable template found for {} at [{} {} {}]", npc.getDescriptionId(), pos.getX(), pos.getY(), pos.getZ());
            return false;
        }

        float spawnChance = NPCSpawnManager.getHighestSpawnChance(viableTemplates);
        boolean shouldSpawn = random.nextFloat() < spawnChance;

        if (!shouldSpawn) {
            // RPGMobs.LOGGER.info("Random chance determined not to spawn {} at [{} {} {}]", npc.getDescriptionId(), pos.getX(), pos.getY(), pos.getZ());
            return false;
        }

        RPGMobs.LOGGER.info("{} spawned at [{} {} {}]", npc.getDescriptionId(), pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    protected Brain.@NotNull Provider<T> brainProvider() {
        return new SmartBrainProvider<>((T) this);
    }

    @Override
    protected void customServerAiStep() {
        this.tickBrain((T) this);
    }

    @Override
    public List<ExtendedSensor<T>> getSensors() {
        return ObjectArrayList.of(
            new NearbyLivingEntitySensor<>(),
            new HurtBySensor<>()
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
                new OneRandomBehaviour<T>(
                    new SetRandomWalkTarget<>(),
                    new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                ),
                new SetRandomLookTarget<>()
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

        nbt.putInt("Relationship", this.getRelationship());

        this.factionManager.saveFactions(nbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.setRelationship(nbt.getInt("Relationship"));

        this.factionManager.loadFactions(nbt);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);

        if (this.equipment.containsKey(EquipmentSlot.MAINHAND)) {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.equipment.get(EquipmentSlot.MAINHAND));
        }
    }

    public ResourceLocation getTextureLocation() {
        return this.getNPCData()
            .getTexture()
            .orElse(ResourceLocation.parse("rpgmobs:textures/npc/missing.png"));
    }

    @Override
    public @NotNull Component getName() {
        return this.getCustomName() != null ? this.getCustomName() : Component.literal("Undefined");
    }

    public void setNPCTemplate(NPCTemplate npcTemplate) {
        this.setData(ModAttachments.NPC_TEMPLATE, npcTemplate);
    }

    public NPCTemplate getNPCTemplate() {
        return this.getData(ModAttachments.NPC_TEMPLATE);
    }

    public void setNPCData(NPCData npcData) {
        this.setData(
            ModAttachments.NPC_DATA,
            npcData
        );
    }

    public void setNPCData(NPCTemplate npcTemplate) {
        NPCData npcData = new NPCData(npcTemplate.data());
        NPCRace race = RaceRepository.get(npcData.getRace());

        if (npcData.getTexture().isEmpty()) {
            npcData.setTexture(Optional.of(race.getRandomTexture()));
        }

        if (npcData.getName().isEmpty()) {
            npcData.setName(Optional.of(race.getRandomName()));
        }

        this.setData(
            ModAttachments.NPC_DATA.get(),
            npcData
        );

        var npcEquipment = npcData.getEquipment();

        if (npcEquipment.isPresent()) {
            for (EquipmentSlot equipmentSlot : npcEquipment.get().keySet()) {
                ResourceLocation itemId = npcEquipment.get().get(equipmentSlot);
                Item item = BuiltInRegistries.ITEM.get(itemId);

                this.setEquipment(equipmentSlot, new ItemStack(item));
            }
        }
    }

    public NPCData getNPCData() {
        return this.getData(ModAttachments.NPC_DATA);
    }

    @Override
    public void registerTraits() {
        TraitDatabaseRepository.setAll(
            this.getData(ModAttachments.NPC_TRAITS).traitIds(),
            TraitType.NPC,
            this.getUUID()
        );
    }

    @Override
    public List<ResourceLocation> getTraits() {
        return this.getData(ModAttachments.NPC_TRAITS).traitIds();
    }

    public void setNPCTraits() {
        RandomSource random = RandomSource.create();

        List<ResourceLocation> traitIds = new ArrayList<>();
        String[] categories = {"attitude"};

        for (String category : categories) {
            Trait trait = TraitManager.determineTraitByWeight(
                random,
                TraitRepository.getTraits(TraitType.NPC, category)
            );

            if (trait != null) {
                traitIds.add(trait.id());
            }

            random.nextInt();
        }

        this.setData(
            ModAttachments.NPC_TRAITS.get(),
            new AttachedTraits(traitIds)
        );
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor levelAccessor,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficulty, spawnType, spawnGroupData);

        BlockPos pos = this.blockPosition();

        List<NPCTemplate> viableTemplates = NPCSpawnManager.getViableTemplates(
            levelAccessor,
            pos
        );

        if (viableTemplates.isEmpty()) {
            RPGMobs.LOGGER.info("No viable entity templates");

            this.discard();
            return null;
        }

        RandomSource random = levelAccessor.getRandom();
        NPCTemplate npcTemplate = NPCSpawnManager.determineTemplateByWeight(
            levelAccessor,
            pos,
            random,
            viableTemplates
        );

        if (npcTemplate == null) {
            RPGMobs.LOGGER.info("No template was determined by spawn weight");

            this.discard();
            return null;
        }

        //RPGMobs.LOGGER.info("NPC was determined to be {}", npcTemplate.id());

        this.setNPCTemplate(npcTemplate);
        this.setNPCData(npcTemplate);
        this.setNPCTraits();

        this.setCustomName(
            Component.literal(this.getData(ModAttachments.NPC_DATA.get()).getName().orElse("Undefined"))
        );

        this.populateDefaultEquipmentSlots(random, difficulty);
        return spawnGroupData;
    }

    @Override
    public boolean isHolding(Predicate<ItemStack> predicate) {
        return predicate.test(this.getItemBySlot(EquipmentSlot.MAINHAND)) || super.isHolding(predicate);
    }

    public HumanlikeArmPose getArmPose() {
        return HumanlikeArmPose.NEUTRAL;
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
