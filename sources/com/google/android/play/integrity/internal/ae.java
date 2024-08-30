package com.google.android.play.integrity.internal;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public final class ae {
    private static final Map a = new HashMap();
    private final Context b;
    private final s c;
    private final String d;
    private boolean h;
    private final Intent i;
    private final z j;
    private ServiceConnection n;
    private IInterface o;
    private final List e = new ArrayList();
    private final Set f = new HashSet();
    private final Object g = new Object();
    private final IBinder.DeathRecipient l = new IBinder.DeathRecipient() { // from class: com.google.android.play.integrity.internal.u
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            ae.k(ae.this);
        }
    };
    private final AtomicInteger m = new AtomicInteger(0);
    private final WeakReference k = new WeakReference(null);

    public ae(Context context, s sVar, String str, Intent intent, z zVar, y yVar) {
        this.b = context;
        this.c = sVar;
        this.d = str;
        this.i = intent;
        this.j = zVar;
    }

    public static /* synthetic */ void k(ae aeVar) {
        aeVar.c.d("reportBinderDeath", new Object[0]);
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(aeVar.k.get());
        aeVar.c.d("%s : Binder has died.", aeVar.d);
        for (t tVar : aeVar.e) {
            tVar.a(aeVar.w());
        }
        aeVar.e.clear();
        synchronized (aeVar.g) {
            aeVar.x();
        }
    }

    public static /* bridge */ /* synthetic */ void o(ae aeVar, final TaskCompletionSource taskCompletionSource) {
        aeVar.f.add(taskCompletionSource);
        taskCompletionSource.getTask().addOnCompleteListener(new OnCompleteListener() { // from class: com.google.android.play.integrity.internal.v
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task task) {
                ae.this.u(taskCompletionSource, task);
            }
        });
    }

    public static /* bridge */ /* synthetic */ void q(ae aeVar, t tVar) {
        if (aeVar.o != null || aeVar.h) {
            if (!aeVar.h) {
                tVar.run();
                return;
            }
            aeVar.c.d("Waiting to bind to the service.", new Object[0]);
            aeVar.e.add(tVar);
            return;
        }
        aeVar.c.d("Initiate binding to the service.", new Object[0]);
        aeVar.e.add(tVar);
        ad adVar = new ad(aeVar, null);
        aeVar.n = adVar;
        aeVar.h = true;
        if (aeVar.b.bindService(aeVar.i, adVar, 1)) {
            return;
        }
        aeVar.c.d("Failed to bind to the service.", new Object[0]);
        aeVar.h = false;
        for (t tVar2 : aeVar.e) {
            tVar2.a(new af());
        }
        aeVar.e.clear();
    }

    public static /* bridge */ /* synthetic */ void r(ae aeVar) {
        aeVar.c.d("linkToDeath", new Object[0]);
        try {
            aeVar.o.asBinder().linkToDeath(aeVar.l, 0);
        } catch (RemoteException e) {
            aeVar.c.c(e, "linkToDeath failed", new Object[0]);
        }
    }

    public static /* bridge */ /* synthetic */ void s(ae aeVar) {
        aeVar.c.d("unlinkToDeath", new Object[0]);
        aeVar.o.asBinder().unlinkToDeath(aeVar.l, 0);
    }

    private final RemoteException w() {
        return new RemoteException(String.valueOf(this.d).concat(" : Binder has died."));
    }

    public final void x() {
        for (TaskCompletionSource taskCompletionSource : this.f) {
            taskCompletionSource.trySetException(w());
        }
        this.f.clear();
    }

    public final Handler c() {
        Handler handler;
        Map map = a;
        synchronized (map) {
            try {
                if (!map.containsKey(this.d)) {
                    HandlerThread handlerThread = new HandlerThread(this.d, 10);
                    handlerThread.start();
                    map.put(this.d, new Handler(handlerThread.getLooper()));
                }
                handler = (Handler) map.get(this.d);
            } catch (Throwable th) {
                throw th;
            }
        }
        return handler;
    }

    public final IInterface e() {
        return this.o;
    }

    public final void t(t tVar, TaskCompletionSource taskCompletionSource) {
        c().post(new w(this, tVar.c(), taskCompletionSource, tVar));
    }

    public final /* synthetic */ void u(TaskCompletionSource taskCompletionSource, Task task) {
        synchronized (this.g) {
            this.f.remove(taskCompletionSource);
        }
    }

    public final void v(TaskCompletionSource taskCompletionSource) {
        synchronized (this.g) {
            this.f.remove(taskCompletionSource);
        }
        c().post(new x(this));
    }
}
