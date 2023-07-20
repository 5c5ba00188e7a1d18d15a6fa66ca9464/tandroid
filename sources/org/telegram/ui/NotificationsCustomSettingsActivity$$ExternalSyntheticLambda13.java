package org.telegram.ui;

import j$.util.function.ToDoubleFunction;
import org.telegram.tgnet.TLRPC$TL_topPeer;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsCustomSettingsActivity$$ExternalSyntheticLambda13 implements ToDoubleFunction {
    public static final /* synthetic */ NotificationsCustomSettingsActivity$$ExternalSyntheticLambda13 INSTANCE = new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda13();

    private /* synthetic */ NotificationsCustomSettingsActivity$$ExternalSyntheticLambda13() {
    }

    @Override // j$.util.function.ToDoubleFunction
    public final double applyAsDouble(Object obj) {
        double d;
        d = ((TLRPC$TL_topPeer) obj).rating;
        return d;
    }
}
