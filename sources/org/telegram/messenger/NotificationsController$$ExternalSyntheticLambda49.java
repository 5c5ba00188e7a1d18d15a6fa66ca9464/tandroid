package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsController$$ExternalSyntheticLambda49 implements RequestDelegate {
    public static final /* synthetic */ NotificationsController$$ExternalSyntheticLambda49 INSTANCE = new NotificationsController$$ExternalSyntheticLambda49();

    private /* synthetic */ NotificationsController$$ExternalSyntheticLambda49() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        NotificationsController.lambda$updateServerNotificationsSettings$45(tLObject, tLRPC$TL_error);
    }
}
