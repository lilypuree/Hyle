package lilypuree.hyle.core;

import lilypuree.hyle.world.feature.StoneReplacerConfiguration;
import lilypuree.hyle.world.feature.StoneReplacer;
import net.minecraft.world.level.levelgen.feature.Feature;

public class HyleFeatures {
    public static Feature<StoneReplacerConfiguration> STONE_REPLACER;

    public static void init() {
        STONE_REPLACER = new StoneReplacer();
    }
}
