package org.telegram.tgnet;
/* loaded from: classes.dex */
public final /* synthetic */ class ConnectionsManager$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int f$0;

    public /* synthetic */ ConnectionsManager$$ExternalSyntheticLambda2(int i) {
        this.f$0 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ConnectionsManager.lambda$onSessionCreated$5(this.f$0);
    }
}
