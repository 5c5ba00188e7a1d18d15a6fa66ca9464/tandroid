package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC$TL_langPackDifference;
/* loaded from: classes.dex */
public final /* synthetic */ class LocaleController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ LocaleController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ LocaleController.LocaleInfo f$2;
    public final /* synthetic */ TLRPC$TL_langPackDifference f$3;
    public final /* synthetic */ HashMap f$4;

    public /* synthetic */ LocaleController$$ExternalSyntheticLambda2(LocaleController localeController, int i, LocaleController.LocaleInfo localeInfo, TLRPC$TL_langPackDifference tLRPC$TL_langPackDifference, HashMap hashMap) {
        this.f$0 = localeController;
        this.f$1 = i;
        this.f$2 = localeInfo;
        this.f$3 = tLRPC$TL_langPackDifference;
        this.f$4 = hashMap;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$saveRemoteLocaleStrings$5(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
