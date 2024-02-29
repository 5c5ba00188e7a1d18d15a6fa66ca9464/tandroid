package org.telegram.messenger;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedConfig$$ExternalSyntheticLambda5 implements Runnable {
    public static final /* synthetic */ SharedConfig$$ExternalSyntheticLambda5 INSTANCE = new SharedConfig$$ExternalSyntheticLambda5();

    private /* synthetic */ SharedConfig$$ExternalSyntheticLambda5() {
    }

    @Override // java.lang.Runnable
    public final void run() {
        SharedConfig.saveConfig();
    }
}
