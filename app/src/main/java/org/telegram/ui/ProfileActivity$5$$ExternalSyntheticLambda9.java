package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$5$$ExternalSyntheticLambda9 implements MessagesStorage.IntCallback {
    public final /* synthetic */ ProfileActivity.AnonymousClass5 f$0;

    public /* synthetic */ ProfileActivity$5$$ExternalSyntheticLambda9(ProfileActivity.AnonymousClass5 anonymousClass5) {
        this.f$0 = anonymousClass5;
    }

    @Override // org.telegram.messenger.MessagesStorage.IntCallback
    public final void run(int i) {
        this.f$0.lambda$onItemClick$0(i);
    }
}
