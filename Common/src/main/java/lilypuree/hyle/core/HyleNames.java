package lilypuree.hyle.core;

import lilypuree.hyle.Constants;
import net.minecraft.resources.ResourceLocation;

public class HyleNames {

    public static final ResourceLocation STONE_REPLACER = getName("stone_replacer");

    private static ResourceLocation getName(String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

}
