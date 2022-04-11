package lilypuree.hyle.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lilypuree.hyle.world.feature.gen.PrimaryNoiseSampler;
import lilypuree.hyle.world.feature.gen.SecondaryNoiseSampler;
import lilypuree.hyle.world.feature.gen.StoneType;

import java.util.List;

public class StoneRegion {

    public static final Codec<StoneRegion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PrimaryNoiseSampler.Settings.CODEC.fieldOf("primary").forGetter(s -> s.primarySettings),
            SecondaryNoiseSampler.Settings.CODEC.fieldOf("secondary").forGetter(s -> s.secondarySettings),
            StoneType.CODEC.listOf().fieldOf("tertiary").forGetter(s -> s.tertiaries),
            Codec.floatRange(-1, 1).fieldOf("tertiary_cutoff").forGetter(s -> s.tertiaryCutoff),
            Codec.floatRange(-1, 1).fieldOf("unconformity_cutoff").forGetter(s -> s.unconformityCutoff)
    ).apply(instance, StoneRegion::new));

    private PrimaryNoiseSampler.Settings primarySettings;
    private SecondaryNoiseSampler.Settings secondarySettings;
    private List<StoneType> tertiaries;
    private float tertiaryCutoff;
    private float unconformityCutoff;

    public StoneRegion(PrimaryNoiseSampler.Settings primarySettings, SecondaryNoiseSampler.Settings secondarySettings, List<StoneType> tertiaries, float tertiaryCutoff, float unconformity) {
        this.tertiaryCutoff = tertiaryCutoff;
        this.unconformityCutoff = unconformity;
        this.primarySettings = primarySettings;
        this.secondarySettings = secondarySettings;
        this.tertiaries = tertiaries;
    }

    public PrimaryNoiseSampler.Settings primary() {
        return primarySettings;
    }

    public SecondaryNoiseSampler.Settings secondary() {
        return secondarySettings;
    }

    public List<StoneType> tertiary() {
        return tertiaries;
    }

    public float getTertiaryCutoff() {
        return tertiaryCutoff;
    }

    public float getUnconformityCutoff() {
        return unconformityCutoff;
    }
}
