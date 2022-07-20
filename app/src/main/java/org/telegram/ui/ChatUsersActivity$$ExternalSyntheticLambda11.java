package org.telegram.ui;

import java.util.Comparator;
import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$$ExternalSyntheticLambda11 implements Comparator {
    public final /* synthetic */ ChatUsersActivity f$0;

    public /* synthetic */ ChatUsersActivity$$ExternalSyntheticLambda11(ChatUsersActivity chatUsersActivity) {
        this.f$0 = chatUsersActivity;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$sortAdmins$3;
        lambda$sortAdmins$3 = this.f$0.lambda$sortAdmins$3((TLObject) obj, (TLObject) obj2);
        return lambda$sortAdmins$3;
    }
}
