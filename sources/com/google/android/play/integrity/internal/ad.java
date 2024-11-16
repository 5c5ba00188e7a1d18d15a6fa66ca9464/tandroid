package com.google.android.play.integrity.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/* loaded from: classes.dex */
final class ad implements ServiceConnection {
    final /* synthetic */ ae a;

    /* synthetic */ ad(ae aeVar, ac acVar) {
        this.a = aeVar;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        s sVar;
        sVar = this.a.c;
        sVar.d("ServiceConnectionImpl.onServiceConnected(%s)", componentName);
        this.a.c().post(new aa(this, iBinder));
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        s sVar;
        sVar = this.a.c;
        sVar.d("ServiceConnectionImpl.onServiceDisconnected(%s)", componentName);
        this.a.c().post(new ab(this));
    }
}
