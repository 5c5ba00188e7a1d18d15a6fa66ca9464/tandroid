package com.airbnb.lottie;

import androidx.collection.ArraySet;
import androidx.core.util.Pair;
import com.airbnb.lottie.utils.MeanCalculator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class PerformanceTracker {
    private boolean enabled = false;
    private final Set<FrameListener> frameListeners = new ArraySet();
    private final Map<String, MeanCalculator> layerRenderTimes = new HashMap();

    /* loaded from: classes.dex */
    public interface FrameListener {
        void onFrameRendered(float f);
    }

    public PerformanceTracker() {
        new Comparator<Pair<String, Float>>(this) { // from class: com.airbnb.lottie.PerformanceTracker.1
            @Override // java.util.Comparator
            public int compare(Pair<String, Float> pair, Pair<String, Float> pair2) {
                float floatValue = pair.second.floatValue();
                float floatValue2 = pair2.second.floatValue();
                if (floatValue2 > floatValue) {
                    return 1;
                }
                return floatValue > floatValue2 ? -1 : 0;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public void recordRenderTime(String str, float f) {
        if (this.enabled) {
            MeanCalculator meanCalculator = this.layerRenderTimes.get(str);
            if (meanCalculator == null) {
                meanCalculator = new MeanCalculator();
                this.layerRenderTimes.put(str, meanCalculator);
            }
            meanCalculator.add(f);
            if (str.equals("__container")) {
                for (FrameListener frameListener : this.frameListeners) {
                    frameListener.onFrameRendered(f);
                }
            }
        }
    }
}
