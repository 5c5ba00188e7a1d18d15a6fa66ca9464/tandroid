package org.telegram.ui;

import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$8$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ PassportActivity.AnonymousClass8 f$0;
    public final /* synthetic */ byte[] f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ PassportActivity$8$$ExternalSyntheticLambda10(PassportActivity.AnonymousClass8 anonymousClass8, byte[] bArr, String str) {
        this.f$0 = anonymousClass8;
        this.f$1 = bArr;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$generateNewSecret$8(this.f$1, this.f$2);
    }
}
