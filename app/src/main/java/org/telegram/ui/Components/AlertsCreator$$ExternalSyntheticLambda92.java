package org.telegram.ui.Components;

import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda92 implements MessagesStorage.IntCallback {
    public final /* synthetic */ BaseFragment f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ TLRPC$Chat f$3;
    public final /* synthetic */ TLRPC$User f$4;
    public final /* synthetic */ boolean f$5;
    public final /* synthetic */ boolean f$6;
    public final /* synthetic */ MessagesStorage.BooleanCallback f$7;
    public final /* synthetic */ Theme.ResourcesProvider f$8;
    public final /* synthetic */ boolean[] f$9;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda92(BaseFragment baseFragment, boolean z, boolean z2, TLRPC$Chat tLRPC$Chat, TLRPC$User tLRPC$User, boolean z3, boolean z4, MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider, boolean[] zArr) {
        this.f$0 = baseFragment;
        this.f$1 = z;
        this.f$2 = z2;
        this.f$3 = tLRPC$Chat;
        this.f$4 = tLRPC$User;
        this.f$5 = z3;
        this.f$6 = z4;
        this.f$7 = booleanCallback;
        this.f$8 = resourcesProvider;
        this.f$9 = zArr;
    }

    @Override // org.telegram.messenger.MessagesStorage.IntCallback
    public final void run(int i) {
        AlertsCreator.lambda$createClearOrDeleteDialogAlert$24(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, i);
    }
}
