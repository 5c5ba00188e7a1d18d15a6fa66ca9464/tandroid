package org.telegram.ui;

import java.util.Comparator;
import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$$ExternalSyntheticLambda12 implements Comparator {
    public final /* synthetic */ ChatUsersActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ChatUsersActivity$$ExternalSyntheticLambda12(ChatUsersActivity chatUsersActivity, int i) {
        this.f$0 = chatUsersActivity;
        this.f$1 = i;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$sortUsers$17;
        lambda$sortUsers$17 = this.f$0.lambda$sortUsers$17(this.f$1, (TLObject) obj, (TLObject) obj2);
        return lambda$sortUsers$17;
    }
}
