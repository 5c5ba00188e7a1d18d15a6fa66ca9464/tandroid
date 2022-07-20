package org.telegram.tgnet;
/* loaded from: classes.dex */
public final /* synthetic */ class ConnectionsManager$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ ConnectionsManager$$ExternalSyntheticLambda8(String str, long j) {
        this.f$0 = str;
        this.f$1 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ConnectionsManager.lambda$getHostByName$11(this.f$0, this.f$1);
    }
}
