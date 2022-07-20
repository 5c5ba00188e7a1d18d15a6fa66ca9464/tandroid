package org.webrtc;

import android.content.Context;
import android.view.OrientationEventListener;
import org.telegram.messenger.ApplicationLoader;
/* loaded from: classes3.dex */
public class OrientationHelper {
    private static final int ORIENTATION_HYSTERESIS = 5;
    public static volatile int cameraOrientation;
    public static volatile int cameraRotation;
    private OrientationEventListener orientationEventListener = new AnonymousClass1(ApplicationLoader.applicationContext);
    private int rotation;

    protected void onOrientationUpdate(int i) {
    }

    public int roundOrientation(int i, int i2) {
        boolean z = true;
        if (i2 != -1) {
            int abs = Math.abs(i - i2);
            if (Math.min(abs, 360 - abs) < 50) {
                z = false;
            }
        }
        return z ? (((i + 45) / 90) * 90) % 360 : i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.webrtc.OrientationHelper$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends OrientationEventListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            OrientationHelper.this = r1;
        }

        @Override // android.view.OrientationEventListener
        public void onOrientationChanged(int i) {
            if (OrientationHelper.this.orientationEventListener == null || i == -1) {
                return;
            }
            OrientationHelper orientationHelper = OrientationHelper.this;
            int roundOrientation = orientationHelper.roundOrientation(i, orientationHelper.rotation);
            if (roundOrientation == OrientationHelper.this.rotation) {
                return;
            }
            OrientationHelper orientationHelper2 = OrientationHelper.this;
            orientationHelper2.onOrientationUpdate(orientationHelper2.rotation = roundOrientation);
        }
    }

    public void start() {
        if (this.orientationEventListener.canDetectOrientation()) {
            this.orientationEventListener.enable();
            return;
        }
        this.orientationEventListener.disable();
        this.orientationEventListener = null;
    }

    public void stop() {
        OrientationEventListener orientationEventListener = this.orientationEventListener;
        if (orientationEventListener != null) {
            orientationEventListener.disable();
            this.orientationEventListener = null;
        }
    }

    public int getOrientation() {
        return this.rotation;
    }
}
