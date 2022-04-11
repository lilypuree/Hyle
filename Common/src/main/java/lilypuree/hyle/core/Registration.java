package lilypuree.hyle.core;

import net.minecraft.world.level.levelgen.feature.Feature;

public class Registration {
    public static void registerFeatures(RegistryHelper<Feature<?>> helper) {
        HyleFeatures.init();
        helper.register(HyleFeatures.STONE_REPLACER, HyleNames.STONE_REPLACER);
    }
}
