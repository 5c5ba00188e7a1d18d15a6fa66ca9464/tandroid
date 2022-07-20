package org.telegram.tgnet;
/* loaded from: classes.dex */
public final /* synthetic */ class ConnectionsManager$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ConnectionsManager$$ExternalSyntheticLambda4(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ConnectionsManager.lambda$onRequestNewServerIpAndPort$9(this.f$0, this.f$1);
    }
}
