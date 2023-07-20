package org.telegram.ui;

import j$.util.function.ToDoubleFunction;
import org.telegram.tgnet.TLRPC$TL_topPeer;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsSettingsActivity$$ExternalSyntheticLambda9 implements ToDoubleFunction {
    public static final /* synthetic */ NotificationsSettingsActivity$$ExternalSyntheticLambda9 INSTANCE = new NotificationsSettingsActivity$$ExternalSyntheticLambda9();

    private /* synthetic */ NotificationsSettingsActivity$$ExternalSyntheticLambda9() {
    }

    @Override // j$.util.function.ToDoubleFunction
    public final double applyAsDouble(Object obj) {
        double d;
        d = ((TLRPC$TL_topPeer) obj).rating;
        return d;
    }
}
