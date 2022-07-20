package org.telegram.ui;

import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.CountrySelectActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class CountrySelectActivity$4$$ExternalSyntheticLambda0 implements NotificationCenter.NotificationCenterDelegate {
    public final /* synthetic */ TextSettingsCell f$0;

    public /* synthetic */ CountrySelectActivity$4$$ExternalSyntheticLambda0(TextSettingsCell textSettingsCell) {
        this.f$0 = textSettingsCell;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public final void didReceivedNotification(int i, int i2, Object[] objArr) {
        CountrySelectActivity.AnonymousClass4.lambda$$0(this.f$0, i, i2, objArr);
    }
}
