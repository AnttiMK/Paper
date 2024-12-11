package ca.spottedleaf.moonrise.paper;

import ca.spottedleaf.moonrise.common.PlatformHooks;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import java.util.List;
import java.util.function.Predicate;

public final class PaperHooks implements PlatformHooks {

    @Override
    public String getBrand() {
        return "Paper";
    }

    @Override
    public int getLightEmission(final BlockState blockState, final BlockGetter world, final BlockPos pos) {
        return blockState.getLightEmission();
    }

    @Override
    public Predicate<BlockState> maybeHasLightEmission() {
        return (final BlockState state) -> {
            return state.getLightEmission() != 0;
        };
    }

    @Override
    public boolean hasCurrentlyLoadingChunk() {
        return false;
    }

    @Override
    public LevelChunk getCurrentlyLoadingChunk(final GenerationChunkHolder holder) {
        return null;
    }

    @Override
    public void setCurrentlyLoading(final GenerationChunkHolder holder, final LevelChunk levelChunk) {

    }

    @Override
    public void chunkFullStatusComplete(final LevelChunk newChunk, final ProtoChunk original) {

    }

    @Override
    public boolean allowAsyncTicketUpdates() {
        return true;
    }

    @Override
    public void onChunkHolderTicketChange(final ServerLevel world, final ChunkHolder holder, final int oldLevel, final int newLevel) {

    }

    @Override
    public void chunkUnloadFromWorld(final LevelChunk chunk) {

    }

    @Override
    public void chunkSyncSave(final ServerLevel world, final ChunkAccess chunk, final SerializableChunkData data) {

    }

    @Override
    public void onChunkWatch(final ServerLevel world, final LevelChunk chunk, final ServerPlayer player) {

    }

    @Override
    public void onChunkUnWatch(final ServerLevel world, final ChunkPos chunk, final ServerPlayer player) {

    }

    @Override
    public void addToGetEntities(final Level world, final Entity entity, final AABB boundingBox, final Predicate<? super Entity> predicate, final List<Entity> into) {
        final Collection<EnderDragonPart> parts = world.dragonParts();
        if (parts.isEmpty()) {
            return;
        }

        for (final EnderDragonPart part : parts) {
            if (part != entity && part.getBoundingBox().intersects(boundingBox) && (predicate == null || predicate.test(part))) {
                into.add(part);
            }
        }
    }

    @Override
    public <T extends Entity> void addToGetEntities(final Level world, final EntityTypeTest<Entity, T> entityTypeTest, final AABB boundingBox, final Predicate<? super T> predicate, final List<? super T> into, final int maxCount) {
        if (into.size() >= maxCount) {
            // fix neoforge issue: do not add if list is already full
            return;
        }

        final Collection<EnderDragonPart> parts = world.dragonParts();
        if (parts.isEmpty()) {
            return;
        }
        for (final EnderDragonPart part : parts) {
            if (!part.getBoundingBox().intersects(boundingBox)) {
                continue;
            }
            final T casted = (T)entityTypeTest.tryCast(part);
            if (casted != null && (predicate == null || predicate.test(casted))) {
                into.add(casted);
                if (into.size() >= maxCount) {
                    break;
                }
            }
        }
    }

    @Override
    public void entityMove(final Entity entity, final long oldSection, final long newSection) {

    }

    @Override
    public boolean screenEntity(final ServerLevel world, final Entity entity, final boolean fromDisk, final boolean event) {
        return true;
    }

    @Override
    public boolean configFixMC224294() {
        return true;
    }

    @Override
    public boolean configAutoConfigSendDistance() {
        return io.papermc.paper.configuration.GlobalConfiguration.get().chunkLoadingAdvanced.autoConfigSendDistance;
    }

    @Override
    public double configPlayerMaxLoadRate() {
        return io.papermc.paper.configuration.GlobalConfiguration.get().chunkLoadingBasic.playerMaxChunkLoadRate;
    }

    @Override
    public double configPlayerMaxGenRate() {
        return io.papermc.paper.configuration.GlobalConfiguration.get().chunkLoadingBasic.playerMaxChunkGenerateRate;
    }

    @Override
    public double configPlayerMaxSendRate() {
        return io.papermc.paper.configuration.GlobalConfiguration.get().chunkLoadingBasic.playerMaxChunkSendRate;
    }

    @Override
    public int configPlayerMaxConcurrentLoads() {
        return io.papermc.paper.configuration.GlobalConfiguration.get().chunkLoadingAdvanced.playerMaxConcurrentChunkLoads;
    }

    @Override
    public int configPlayerMaxConcurrentGens() {
        return io.papermc.paper.configuration.GlobalConfiguration.get().chunkLoadingAdvanced.playerMaxConcurrentChunkGenerates;
    }

    @Override
    public long configAutoSaveInterval(final ServerLevel world) {
        return world.paperConfig().chunks.autoSaveInterval.value();
    }

    @Override
    public int configMaxAutoSavePerTick(final ServerLevel world) {
        return world.paperConfig().chunks.maxAutoSaveChunksPerTick;
    }

    @Override
    public boolean configFixMC159283() {
        return true;
    }

    @Override
    public boolean forceNoSave(final ChunkAccess chunk) {
        return chunk instanceof LevelChunk levelChunk && levelChunk.mustNotSave;
    }

    @Override
    public CompoundTag convertNBT(final DSL.TypeReference type, final DataFixer dataFixer, final CompoundTag nbt,
                                  final int fromVersion, final int toVersion) {
        return (CompoundTag)dataFixer.update(
            type, new Dynamic<>(NbtOps.INSTANCE, nbt), fromVersion, toVersion
        ).getValue();
    }

    @Override
    public boolean hasMainChunkLoadHook() {
        return false;
    }

    @Override
    public void mainChunkLoad(final ChunkAccess chunk, final SerializableChunkData chunkData) {

    }

    @Override
    public List<Entity> modifySavedEntities(final ServerLevel world, final int chunkX, final int chunkZ, final List<Entity> entities) {
        return entities;
    }

    @Override
    public void unloadEntity(final Entity entity) {
        entity.setRemoved(Entity.RemovalReason.UNLOADED_TO_CHUNK, org.bukkit.event.entity.EntityRemoveEvent.Cause.UNLOAD);
    }

    @Override
    public void postLoadProtoChunk(final ServerLevel world, final ProtoChunk chunk) {
        net.minecraft.world.level.chunk.status.ChunkStatusTasks.postLoadProtoChunk(world, chunk.getEntities());
    }

    @Override
    public int modifyEntityTrackingRange(final Entity entity, final int currentRange) {
        return org.spigotmc.TrackingRange.getEntityTrackingRange(entity, currentRange);
    }
}
