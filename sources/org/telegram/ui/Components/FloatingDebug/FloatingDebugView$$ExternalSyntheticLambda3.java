package org.telegram.ui.Components.FloatingDebug;

import android.view.View;
import com.google.android.exoplayer2.util.Consumer;
/* loaded from: classes4.dex */
public final /* synthetic */ class FloatingDebugView$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ FloatingDebugView$$ExternalSyntheticLambda3 INSTANCE = new FloatingDebugView$$ExternalSyntheticLambda3();

    private /* synthetic */ FloatingDebugView$$ExternalSyntheticLambda3() {
    }

    @Override // com.google.android.exoplayer2.util.Consumer
    public final void accept(Object obj) {
        ((View) obj).invalidate();
    }
}
