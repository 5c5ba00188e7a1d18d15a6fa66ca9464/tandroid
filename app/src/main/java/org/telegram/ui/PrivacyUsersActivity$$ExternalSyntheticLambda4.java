package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.GroupCreateActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PrivacyUsersActivity$$ExternalSyntheticLambda4 implements GroupCreateActivity.GroupCreateActivityDelegate {
    public final /* synthetic */ PrivacyUsersActivity f$0;

    public /* synthetic */ PrivacyUsersActivity$$ExternalSyntheticLambda4(PrivacyUsersActivity privacyUsersActivity) {
        this.f$0 = privacyUsersActivity;
    }

    @Override // org.telegram.ui.GroupCreateActivity.GroupCreateActivityDelegate
    public final void didSelectUsers(ArrayList arrayList) {
        this.f$0.lambda$createView$0(arrayList);
    }
}
