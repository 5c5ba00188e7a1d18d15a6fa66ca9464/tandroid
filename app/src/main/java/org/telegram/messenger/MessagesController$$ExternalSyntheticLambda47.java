package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda47 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ int f$5;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda47(MessagesController messagesController, int i, long j, boolean z, int i2, int i3) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = j;
        this.f$3 = z;
        this.f$4 = i2;
        this.f$5 = i3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$markDialogAsRead$206(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
