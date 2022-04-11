package lilypuree.hyle;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getCategory() != Biome.BiomeCategory.NETHER && event.getCategory() != Biome.BiomeCategory.THEEND) {
//            PlacedFeature stoneReplacer = BuiltinRegistries.PLACED_FEATURE.get(UENames.STONE_REPLACER);
//            event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, stoneReplacer);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerAboutToStartEvent event) {
        if (Constants.CONFIG.disableReplacement()) return;

        MinecraftServer server = event.getServer();
        BiomeInjector.apply(server.registryAccess());
    }
}
