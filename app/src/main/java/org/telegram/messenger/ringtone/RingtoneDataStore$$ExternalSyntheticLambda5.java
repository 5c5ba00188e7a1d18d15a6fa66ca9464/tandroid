package org.telegram.messenger.ringtone;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class RingtoneDataStore$$ExternalSyntheticLambda5 implements RequestDelegate {
    public final /* synthetic */ RingtoneDataStore f$0;

    public /* synthetic */ RingtoneDataStore$$ExternalSyntheticLambda5(RingtoneDataStore ringtoneDataStore) {
        this.f$0 = ringtoneDataStore;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadUserRingtones$2(tLObject, tLRPC$TL_error);
    }
}
