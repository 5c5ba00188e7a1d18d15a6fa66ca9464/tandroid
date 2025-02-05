package org.telegram.ui.Stories;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.telegram.messenger.SharedConfig;

/* loaded from: classes5.dex */
abstract class HwFrameLayout extends FrameLayout {
    private final boolean isFastDevice;
    static final Set hwViews = new HashSet();
    static boolean hwEnabled = false;

    public HwFrameLayout(Context context) {
        super(context);
        this.isFastDevice = SharedConfig.getDevicePerformanceClass() == 2;
    }

    private void disableHwAcceleration(boolean z) {
        hwEnabled = false;
        if (z) {
            setLayerType(0, null);
        }
        Iterator it = hwViews.iterator();
        while (it.hasNext()) {
            ((View) it.next()).invalidate();
        }
        hwViews.clear();
    }

    public void checkHwAcceleration(float f) {
        if (f > 0.6f && hwEnabled && this.isFastDevice) {
            disableHwAcceleration(false);
        }
    }

    public void disableHwAcceleration() {
        disableHwAcceleration(true);
    }

    public void enableHwAcceleration() {
        hwEnabled = true;
        setLayerType(2, null);
    }

    @Override // android.view.View
    public void invalidate() {
        if (hwEnabled) {
            hwViews.add(this);
        } else {
            super.invalidate();
        }
    }

    @Override // android.view.View
    public void invalidate(int i, int i2, int i3, int i4) {
        if (hwEnabled) {
            hwViews.add(this);
        } else {
            super.invalidate(i, i2, i3, i4);
        }
    }
}
