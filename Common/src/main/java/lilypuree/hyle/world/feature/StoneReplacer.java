package lilypuree.hyle.world.feature;

import lilypuree.hyle.Constants;
import lilypuree.hyle.mixin.NoiseBasedChunkGeneratorAccessor;
import lilypuree.hyle.world.feature.gen.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class StoneReplacer extends Feature<StoneReplacerConfiguration> {
    public StoneReplacer() {
        super(StoneReplacerConfiguration.CODEC);
    }

    private boolean isSeedSet = false;

    private boolean canReplace(ChunkGenerator generator) {
        if (generator instanceof NoiseBasedChunkGenerator nbcg) {
            return ((NoiseBasedChunkGeneratorAccessor) (Object) nbcg).getDefaultBlock().is(Blocks.STONE);
        }
        return false;
    }

    @Override
    public boolean place(FeaturePlaceContext<StoneReplacerConfiguration> ctx) {
        if (!canReplace(ctx.chunkGenerator())) return false;
        WorldGenLevel level = ctx.level();
        StoneReplacerConfiguration config = ctx.config();
        if (!isSeedSet) {
            int seed = (int) level.getSeed();
            var frequencies = config.getFrequencies();
            NoiseHolder.setSeed(seed - 193864, frequencies.region());
            PrimaryNoiseSampler.setSeed(seed + 777261, frequencies.primary());
            SecondaryNoiseSampler.setSeed(seed + 390271, frequencies.secondary());
            RoughNoiseSampler.setSeed(seed + 1567241, frequencies.tertiary(), frequencies.unconformity());
            isSeedSet = true;
        }

        BlockPos pos = ctx.origin();
        basePos = pos;
        int minY = Math.max(pos.getY(), level.getMinBuildHeight());
        ChunkAccess chunkAccess = level.getChunk(pos);
        NoiseHolder noiseHolder = new NoiseHolder(config, level, pos.getX(), minY, pos.getZ());
        int maxHeight = noiseHolder.getMaxHeight(chunkAccess);
        if (maxHeight <= minY) return false;
        ChunkFiller filler = new ChunkFiller(level.getSeed(), new BlockPos(pos.getX(), minY, pos.getZ()), maxHeight - minY + 1);
        if (Constants.CONFIG.enableDebug()) {
            filler.fillIn(2, noiseHolder::getRoughNoise);
            filler.fillEmpty(2, noiseHolder::getStrataNoise);
            filler.fillInEdgesAndFaces(1);
            filler.fillInCentersRandom(1);
        } else {
            filler.fillIn(16, noiseHolder::getRoughNoise);
            filler.fillInEdgesAndFaces(8);
            filler.fillInCenters(8, noiseHolder::getRoughNoise);
            filler.fillEmpty(8, noiseHolder::getStrataNoise); //fills in the gaps that roughnoise did not fill
            filler.fillInEdgesAndFaces(4);
            filler.fillInCenters(4, noiseHolder::getCombinedNoise);
            filler.fillBiome(4, noiseHolder::getBiomeStrata);
            filler.fillInEdgesAndFaces(2);
            filler.fillInCentersFast(2);
            filler.fillInEdgesAndFaces(1);
            filler.fillInCentersRandom(1);
        }
        replaceAll(chunkAccess, minY, noiseHolder.getHeights(), filler.results());
        return true;
    }

    BlockPos basePos;

    private void replaceAll(ChunkAccess chunkAccess, int minY, int[][] heights, StoneType[][][] results) {
        LevelChunkSection chunkSection = chunkAccess.getSection(chunkAccess.getSectionIndex(minY));
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int height = heights[x][z];
                for (int posY = minY; posY <= height; posY++) {
                    int sectionIndex = chunkAccess.getSectionIndex(posY);
                    if (chunkAccess.getSectionIndex(chunkSection.bottomBlockY()) != sectionIndex) {
                        chunkSection = chunkAccess.getSection(sectionIndex);
                    }
                    if (chunkSection.hasOnlyAir()) continue;

                    BlockState block = chunkSection.getBlockState(x, posY & 15, z);
                    if (block.isAir()) continue;
                    BlockState replaced = results[x][z][posY - minY].replace(block);
                    if (block != replaced) {
                        chunkSection.setBlockState(x, posY & 15, z, replaced, false);
                    }
                }
            }
        }
    }
}
