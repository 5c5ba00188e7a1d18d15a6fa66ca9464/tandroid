package org.telegram.ui;

import org.telegram.ui.QrActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class QrActivity$$ExternalSyntheticLambda11 implements QrActivity.QrView.QrCenterChangedListener {
    public final /* synthetic */ QrActivity f$0;

    public /* synthetic */ QrActivity$$ExternalSyntheticLambda11(QrActivity qrActivity) {
        this.f$0 = qrActivity;
    }

    @Override // org.telegram.ui.QrActivity.QrView.QrCenterChangedListener
    public final void onCenterChanged(int i, int i2, int i3, int i4) {
        this.f$0.lambda$createView$0(i, i2, i3, i4);
    }
}
