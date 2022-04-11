package lilypuree.hyle;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Constants.MOD_ID)
public class HyleFabricConfigs implements ConfigData, CommonConfig {

    private boolean enableDebug = false;
    private boolean disableReplacement = false;
    private boolean disableCobbleReplacement = false;
    private boolean disableDirtReplacement = false;
    private boolean disableOreReplacement = false;

    @Override
    public boolean enableDebug() {
        return enableDebug;
    }

    @Override
    public boolean disableReplacement() {
        return disableReplacement;
    }

    @Override
    public boolean disableCobbleReplacement() {
        return disableCobbleReplacement;
    }

    @Override
    public boolean disableDirtReplacement() {
        return disableDirtReplacement;
    }

    @Override
    public boolean disableOreReplacement() {
        return disableOreReplacement;
    }
}
