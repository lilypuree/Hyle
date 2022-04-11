package lilypuree.hyle;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import lilypuree.hyle.core.HyleNames;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeInjector {

    public static void apply(BiomeSource source) {
        source.featuresPerStep = Suppliers.memoize(() -> source.buildFeaturesPerStep(source.possibleBiomes().stream().toList(), true));
    }

    public static void apply(RegistryAccess registryAccess) {
        Constants.LOG.log(Level.INFO, "Hyle stone replacer injection started.");
        long start = System.currentTimeMillis();
        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
        PlacedFeature STONE_REPLACER = registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY).get(HyleNames.STONE_REPLACER);
        for (Biome biome : biomeRegistry) {
            addFeatureToBiome(biome, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, STONE_REPLACER);
        }
        long timeTook = System.currentTimeMillis() - start;
        Constants.LOG.log(Level.INFO, "Hyle stone replacer injection took {} ms to complete.", timeTook);
    }

    private static void addFeatureToBiome(Biome biome, GenerationStep.Decoration step, PlacedFeature placedFeature) {
        List<HolderSet<PlacedFeature>> features = new ArrayList<>(biome.getGenerationSettings().features());
        while (features.size() <= step.ordinal()) {
            features.add(HolderSet.direct());
        }
        List<Holder<PlacedFeature>> list = features.get(step.ordinal()).stream().collect(Collectors.toList());
        list.add(Holder.direct(placedFeature));
        features.set(step.ordinal(), HolderSet.direct(ImmutableList.copyOf(list)));
        biome.getGenerationSettings().features = ImmutableList.copyOf(features);
    }
}
