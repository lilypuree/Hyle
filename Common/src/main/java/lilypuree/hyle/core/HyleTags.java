package lilypuree.hyle.core;

import lilypuree.hyle.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class HyleTags {

    public static void init(){
        Blocks.init();
    }

    public static class Blocks {
        public static TagKey<Block> REPLACABLE;
        public static TagKey<Block> REPLACE_GRASS;
        public static TagKey<Block> REPLACE_DIRT;
        
        public static void init(){
            HyleTags.Blocks.REPLACABLE = create("replaceable");
            HyleTags.Blocks.REPLACE_DIRT = create("replace_dirt");
            HyleTags.Blocks.REPLACE_GRASS = create("replace_grass");
        }
        private static TagKey<Block> create(String name){
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Constants.MOD_ID, name));
        }
    }
}
