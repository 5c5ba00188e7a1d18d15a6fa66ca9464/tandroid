package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda46 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ ArrayList f$4;
    public final /* synthetic */ int f$5;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda46(MessagesStorage messagesStorage, int i, long j, int i2, ArrayList arrayList, int i3) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
        this.f$3 = i2;
        this.f$4 = arrayList;
        this.f$5 = i3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateRepliesCount$156(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
