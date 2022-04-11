package lilypuree.hyle;


import lilypuree.hyle.compat.StoneTypeEvent;
import lilypuree.hyle.core.Registration;
import lilypuree.hyle.core.RegistryHelper;
import lilypuree.hyle.misc.HyleDataLoaders;
import lilypuree.hyle.misc.StoneTypeCodecJsonDataManager;
import lilypuree.hyle.world.feature.gen.StoneType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(Constants.MOD_ID)
public class HyleForge implements CommonHelper {
    public HyleForge() {
        CommonMod.init(this);
        Constants.CONFIG = new HyleForgeConfigs();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HyleForgeConfigs.COMMON_CONFIG);

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addGenericListener(Feature.class, (RegistryEvent.Register<Feature<?>> e) -> Registration.registerFeatures(new RegistryHelperForge<>(e.getRegistry())));

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent e) -> {
            e.addListener(HyleDataLoaders.STONE_TYPE_LOADER);
            e.addListener(HyleDataLoaders.REGION_LOADER);
        });
    }


    public static class RegistryHelperForge<T extends IForgeRegistryEntry<T>> implements RegistryHelper<T> {
        IForgeRegistry<T> registry;

        public RegistryHelperForge(IForgeRegistry<T> registry) {
            this.registry = registry;
        }

        @Override
        public void register(T entry, ResourceLocation name) {
            registry.register(entry.setRegistryName(name));
        }
    }


    @Override
    public StoneTypeCodecJsonDataManager getStoneTypeManager() {
        return new StoneTypeCodecJsonDataManager() {
            @Override
            public boolean fireEvent(StoneType stoneType) {
                StoneTypeEvent event = new StoneTypeEvent(stoneType);
                MinecraftForge.EVENT_BUS.post(event);
                return !event.isCanceled();
            }
        };
    }
}
