package org.telegram.ui;

import java.util.Comparator;
import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$$ExternalSyntheticLambda10 implements Comparator {
    public final /* synthetic */ ChatUsersActivity f$0;

    public /* synthetic */ ChatUsersActivity$$ExternalSyntheticLambda10(ChatUsersActivity chatUsersActivity) {
        this.f$0 = chatUsersActivity;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$onOwnerChaged$4;
        lambda$onOwnerChaged$4 = this.f$0.lambda$onOwnerChaged$4((TLObject) obj, (TLObject) obj2);
        return lambda$onOwnerChaged$4;
    }
}
