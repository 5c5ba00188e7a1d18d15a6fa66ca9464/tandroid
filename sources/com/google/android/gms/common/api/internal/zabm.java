package com.google.android.gms.common.api.internal;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
final class zabm implements Runnable {
    final /* synthetic */ zabq zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zabm(zabq zabqVar) {
        this.zaa = zabqVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zaa.zaG();
    }
}
