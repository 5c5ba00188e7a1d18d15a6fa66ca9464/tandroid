package org.telegram.tgnet;
/* loaded from: classes.dex */
public final /* synthetic */ class ConnectionsManager$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ ConnectionsManager f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ConnectionsManager$$ExternalSyntheticLambda10(ConnectionsManager connectionsManager, boolean z) {
        this.f$0 = connectionsManager;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setIsUpdating$13(this.f$1);
    }
}
