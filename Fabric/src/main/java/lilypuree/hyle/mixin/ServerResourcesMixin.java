package lilypuree.hyle.mixin;

import lilypuree.hyle.misc.HyleDataLoaders;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ReloadableServerResources.class)
public class ServerResourcesMixin {

    @Inject(method = "listeners", at = @At("RETURN"), cancellable = true)
    public void onListeners(CallbackInfoReturnable<List<PreparableReloadListener>> cir){
        List<PreparableReloadListener> mutable = new ArrayList<>(cir.getReturnValue());
        mutable.add(HyleDataLoaders.STONE_TYPE_LOADER);
        mutable.add(HyleDataLoaders.REGION_LOADER);
        cir.setReturnValue(mutable);
    }
}
