package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsSettingsActivity$$ExternalSyntheticLambda11 implements RequestDelegate {
    public static final /* synthetic */ NotificationsSettingsActivity$$ExternalSyntheticLambda11 INSTANCE = new NotificationsSettingsActivity$$ExternalSyntheticLambda11();

    private /* synthetic */ NotificationsSettingsActivity$$ExternalSyntheticLambda11() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        NotificationsSettingsActivity.lambda$createView$7(tLObject, tLRPC$TL_error);
    }
}
