package lilypuree.hyle.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NoiseBasedChunkGenerator.class)
public interface NoiseBasedChunkGeneratorAccessor {
    @Accessor(value = "defaultBlock")
    BlockState getDefaultBlock();
}
