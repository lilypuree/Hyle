package lilypuree.hyle;

import lilypuree.hyle.compat.StoneTypeCallback;
import lilypuree.hyle.core.HyleNames;
import lilypuree.hyle.core.Registration;
import lilypuree.hyle.core.RegistryHelper;
import lilypuree.hyle.misc.StoneTypeCodecJsonDataManager;
import lilypuree.hyle.world.feature.gen.StoneType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;

public class HyleFabric implements ModInitializer, CommonHelper {

    @Override
    public void onInitialize() {
        AutoConfig.register(HyleFabricConfigs.class, JanksonConfigSerializer::new);
        Constants.CONFIG = AutoConfig.getConfigHolder(HyleFabricConfigs.class).getConfig();

        CommonMod.init(this);
        Registration.registerFeatures(new RegistryHelperFabric<>(Registry.FEATURE));
        if (Constants.CONFIG.disableReplacement()) return;
        BiomeModifications.addFeature(ctx -> true, GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, HyleNames.STONE_REPLACER));
    }

    public static class RegistryHelperFabric<T> implements RegistryHelper<T> {
        Registry<T> registry;

        public RegistryHelperFabric(Registry<T> registry) {
            this.registry = registry;
        }

        @Override
        public void register(T entry, ResourceLocation name) {
            Registry.register(registry, name, entry);
        }
    }

    @Override
    public StoneTypeCodecJsonDataManager getStoneTypeManager() {
        return new StoneTypeCodecJsonDataManager() {
            @Override
            public boolean fireEvent(StoneType stoneType) {
                return StoneTypeCallback.EVENT.invoker().apply(stoneType);
            }
        };
    }
}
