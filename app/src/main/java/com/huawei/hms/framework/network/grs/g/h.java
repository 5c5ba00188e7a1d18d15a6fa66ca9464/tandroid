package com.huawei.hms.framework.network.grs.g;

import com.huawei.hms.framework.common.ExecutorsUtils;
import com.huawei.hms.framework.common.Logger;
import com.huawei.hms.framework.common.NetworkUtil;
import com.huawei.hms.framework.network.grs.h.d;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class h {
    private final ExecutorService a = ExecutorsUtils.newCachedThreadPool("GRS_RequestController-Task");
    private final Map<String, com.huawei.hms.framework.network.grs.g.k.b> b = new ConcurrentHashMap(16);
    private final Object c = new Object();
    private com.huawei.hms.framework.network.grs.e.a d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Callable<d> {
        final /* synthetic */ com.huawei.hms.framework.network.grs.g.k.c a;
        final /* synthetic */ String b;
        final /* synthetic */ com.huawei.hms.framework.network.grs.e.c c;

        a(com.huawei.hms.framework.network.grs.g.k.c cVar, String str, com.huawei.hms.framework.network.grs.e.c cVar2) {
            this.a = cVar;
            this.b = str;
            this.c = cVar2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        /* renamed from: call */
        public d mo229call() {
            return new c(this.a, h.this.d).a(h.this.a, this.b, this.c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        final /* synthetic */ com.huawei.hms.framework.network.grs.g.k.c a;
        final /* synthetic */ String b;
        final /* synthetic */ com.huawei.hms.framework.network.grs.e.c c;
        final /* synthetic */ com.huawei.hms.framework.network.grs.b d;

        b(com.huawei.hms.framework.network.grs.g.k.c cVar, String str, com.huawei.hms.framework.network.grs.e.c cVar2, com.huawei.hms.framework.network.grs.b bVar) {
            this.a = cVar;
            this.b = str;
            this.c = cVar2;
            this.d = bVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            h hVar = h.this;
            hVar.a(hVar.a(this.a, this.b, this.c), this.d);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(d dVar, com.huawei.hms.framework.network.grs.b bVar) {
        if (bVar != null) {
            if (dVar == null) {
                Logger.v("RequestController", "GrsResponse is null");
                bVar.a();
                return;
            }
            Logger.v("RequestController", "GrsResponse is not null");
            bVar.a(dVar);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0069, code lost:
        if (r2.a() != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x006d, code lost:
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public d a(com.huawei.hms.framework.network.grs.g.k.c cVar, String str, com.huawei.hms.framework.network.grs.e.c cVar2) {
        Future<d> submit;
        String str2;
        String str3;
        Logger.d("RequestController", "request to server with service name is: " + str);
        String grsParasKey = cVar.b().getGrsParasKey(true, true, cVar.a());
        Logger.v("RequestController", "request spUrlKey: " + grsParasKey);
        synchronized (this.c) {
            if (!NetworkUtil.isNetworkAvailable(cVar.a())) {
                return null;
            }
            d.a a2 = com.huawei.hms.framework.network.grs.h.d.a(grsParasKey);
            com.huawei.hms.framework.network.grs.g.k.b bVar = this.b.get(grsParasKey);
            try {
                if (bVar != null && bVar.b()) {
                    submit = bVar.a();
                    return submit.get();
                }
                return submit.get();
            } catch (InterruptedException e) {
                e = e;
                str2 = "RequestController";
                str3 = "when check result, find InterruptedException, check others";
                Logger.w(str2, str3, e);
                return null;
            } catch (CancellationException e2) {
                e = e2;
                str2 = "RequestController";
                str3 = "when check result, find CancellationException, check others";
                Logger.w(str2, str3, e);
                return null;
            } catch (ExecutionException e3) {
                e = e3;
                str2 = "RequestController";
                str3 = "when check result, find ExecutionException, check others";
                Logger.w(str2, str3, e);
                return null;
            }
            Logger.d("RequestController", "hitGrsRequestBean == null or request block is released.");
            submit = this.a.submit(new a(cVar, str, cVar2));
            this.b.put(grsParasKey, new com.huawei.hms.framework.network.grs.g.k.b(submit));
        }
    }

    public void a(com.huawei.hms.framework.network.grs.e.a aVar) {
        this.d = aVar;
    }

    public void a(com.huawei.hms.framework.network.grs.g.k.c cVar, com.huawei.hms.framework.network.grs.b bVar, String str, com.huawei.hms.framework.network.grs.e.c cVar2) {
        this.a.execute(new b(cVar, str, cVar2, bVar));
    }

    public void a(String str) {
        synchronized (this.c) {
            this.b.remove(str);
        }
    }
}
