package org.telegram.tgnet;
/* loaded from: classes.dex */
public final /* synthetic */ class ConnectionsManager$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ TLRPC$TL_config f$1;

    public /* synthetic */ ConnectionsManager$$ExternalSyntheticLambda6(int i, TLRPC$TL_config tLRPC$TL_config) {
        this.f$0 = i;
        this.f$1 = tLRPC$TL_config;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ConnectionsManager.lambda$onUpdateConfig$12(this.f$0, this.f$1);
    }
}
