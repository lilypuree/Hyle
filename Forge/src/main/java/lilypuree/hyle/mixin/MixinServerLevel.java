package lilypuree.hyle.mixin;

import lilypuree.hyle.BiomeInjector;
import lilypuree.hyle.Constants;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(MinecraftServer server, Executor executor, LevelStorageSource.LevelStorageAccess p_203764_, ServerLevelData serverLevelData, ResourceKey<Level> key, Holder<DimensionType> dimensionTypeHolder, ChunkProgressListener p_203768_, ChunkGenerator generator, boolean p_203770_, long p_203771_, List<CustomSpawner> p_203772_, boolean p_203773_, CallbackInfo ci) {
        if (key.equals(Level.OVERWORLD) && !Constants.CONFIG.disableReplacement()) {
            Constants.LOG.log(org.apache.logging.log4j.Level.INFO, "Hyle biomesource cache modification started");
            long start = System.currentTimeMillis();

            if (generator.biomeSource instanceof FixedBiomeSource) {
                BiomeInjector.apply(server.registryAccess());
            }

            BiomeInjector.apply(generator.getBiomeSource());
            if (generator.biomeSource != generator.getBiomeSource()) {
                BiomeInjector.apply(generator.biomeSource);
            }
            long timeTook = System.currentTimeMillis() - start;
            Constants.LOG.log(org.apache.logging.log4j.Level.INFO, "Hyle biomesource cache modification took {} ms to complete.", timeTook);
        }
    }
}
