package com.google.android.play.integrity.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.play:integrity@@1.3.0 */
/* loaded from: classes.dex */
public final class ad implements ServiceConnection {
    final /* synthetic */ ae a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ ad(ae aeVar, ac acVar) {
        this.a = aeVar;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ae.f(this.a).d("ServiceConnectionImpl.onServiceConnected(%s)", componentName);
        this.a.c().post(new aa(this, iBinder));
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        ae.f(this.a).d("ServiceConnectionImpl.onServiceDisconnected(%s)", componentName);
        this.a.c().post(new ab(this));
    }
}
