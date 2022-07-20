package org.telegram.ui;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$$ExternalSyntheticLambda14 implements RequestDelegate {
    public final /* synthetic */ ArrayList f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ AtomicInteger f$2;
    public final /* synthetic */ ArrayList f$3;
    public final /* synthetic */ Runnable f$4;

    public /* synthetic */ ChatUsersActivity$$ExternalSyntheticLambda14(ArrayList arrayList, int i, AtomicInteger atomicInteger, ArrayList arrayList2, Runnable runnable) {
        this.f$0 = arrayList;
        this.f$1 = i;
        this.f$2 = atomicInteger;
        this.f$3 = arrayList2;
        this.f$4 = runnable;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatUsersActivity.lambda$loadChatParticipants$16(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tLRPC$TL_error);
    }
}
