package lilypuree.hyle;

import lilypuree.hyle.core.HyleTags;
import lilypuree.hyle.misc.HyleDataLoaders;

public class CommonMod {
    public static void init(CommonHelper helper) {
        HyleDataLoaders.init(helper.getStoneTypeManager());
        HyleTags.init();
    }
}
