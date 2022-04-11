package lilypuree.hyle.core;

import lilypuree.hyle.Constants;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface RegistryHelper<T> {
    public void register(T entry, ResourceLocation name);

    default void register(T entry, String name) {
        register(entry, new ResourceLocation(Constants.MOD_ID, name));
    }
}
