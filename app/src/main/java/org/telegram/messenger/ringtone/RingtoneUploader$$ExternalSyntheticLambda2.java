package org.telegram.messenger.ringtone;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class RingtoneUploader$$ExternalSyntheticLambda2 implements RequestDelegate {
    public final /* synthetic */ RingtoneUploader f$0;

    public /* synthetic */ RingtoneUploader$$ExternalSyntheticLambda2(RingtoneUploader ringtoneUploader) {
        this.f$0 = ringtoneUploader;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$didReceivedNotification$1(tLObject, tLRPC$TL_error);
    }
}
