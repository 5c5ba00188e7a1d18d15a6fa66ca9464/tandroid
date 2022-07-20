package com.huawei.hms.framework.network.grs.g;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.huawei.hms.framework.common.Logger;
import com.huawei.hms.framework.network.grs.GrsBaseInfo;
import com.huawei.hms.framework.network.grs.h.d;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONArray;
/* loaded from: classes.dex */
public class c {
    private static final String n = "c";
    private final GrsBaseInfo a;
    private final Context b;
    private final com.huawei.hms.framework.network.grs.e.a c;
    private d d;
    private final com.huawei.hms.framework.network.grs.g.k.c j;
    private com.huawei.hms.framework.network.grs.g.k.d k;
    private final Map<String, Future<d>> e = new ConcurrentHashMap(16);
    private final List<d> f = new CopyOnWriteArrayList();
    private final JSONArray g = new JSONArray();
    private final List<String> h = new CopyOnWriteArrayList();
    private final List<String> i = new CopyOnWriteArrayList();
    private String l = "";
    private long m = 1;

    /* loaded from: classes.dex */
    public class a implements Callable<d> {
        final /* synthetic */ ExecutorService a;
        final /* synthetic */ String b;
        final /* synthetic */ com.huawei.hms.framework.network.grs.e.c c;

        a(ExecutorService executorService, String str, com.huawei.hms.framework.network.grs.e.c cVar) {
            c.this = r1;
            this.a = executorService;
            this.b = str;
            this.c = cVar;
        }

        @Override // java.util.concurrent.Callable
        public d call() {
            return c.this.b(this.a, this.b, this.c);
        }
    }

    public c(com.huawei.hms.framework.network.grs.g.k.c cVar, com.huawei.hms.framework.network.grs.e.a aVar) {
        this.j = cVar;
        this.a = cVar.b();
        this.b = cVar.a();
        this.c = aVar;
        c();
        d();
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x009a A[LOOP:0: B:3:0x0006->B:33:0x009a, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0092 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private d a(ExecutorService executorService, List<String> list, String str, com.huawei.hms.framework.network.grs.e.c cVar) {
        ExecutionException e;
        InterruptedException e2;
        d dVar;
        d dVar2 = null;
        int i = 0;
        while (true) {
            if (i >= list.size()) {
                break;
            }
            String str2 = list.get(i);
            boolean z = true;
            if (!TextUtils.isEmpty(str2)) {
                Future<d> submit = executorService.submit(new com.huawei.hms.framework.network.grs.g.a(str2, i, this, this.b, str, this.a, cVar).g());
                this.e.put(str2, submit);
                try {
                    dVar = submit.get(this.m, TimeUnit.SECONDS);
                } catch (InterruptedException e3) {
                    e2 = e3;
                } catch (CancellationException unused) {
                } catch (ExecutionException e4) {
                    e = e4;
                } catch (TimeoutException unused2) {
                }
                if (dVar != null) {
                    try {
                    } catch (InterruptedException e5) {
                        e2 = e5;
                        dVar2 = dVar;
                        Logger.w(n, "the current thread was interrupted while waiting", e2);
                        if (!z) {
                        }
                    } catch (CancellationException unused3) {
                        dVar2 = dVar;
                        Logger.i(n, "{requestServer} the computation was cancelled");
                        if (!z) {
                        }
                    } catch (ExecutionException e6) {
                        e = e6;
                        dVar2 = dVar;
                        Logger.w(n, "the computation threw an ExecutionException", e);
                        z = false;
                        if (!z) {
                        }
                    } catch (TimeoutException unused4) {
                        dVar2 = dVar;
                        Logger.w(n, "the wait timed out");
                        z = false;
                        if (!z) {
                        }
                    }
                    if (dVar.o() || dVar.m()) {
                        Logger.i(n, "grs request return body is not null and is OK.");
                        dVar2 = dVar;
                        if (!z) {
                            Logger.v(n, "needBreak is true so need break current circulation");
                            break;
                        }
                        i++;
                    }
                }
                dVar2 = dVar;
            }
            z = false;
            if (!z) {
            }
        }
        return b(dVar2);
    }

    private void a(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(str);
        String grsReqParamJoint = this.a.getGrsReqParamJoint(false, false, e(), this.b);
        if (!TextUtils.isEmpty(grsReqParamJoint)) {
            sb.append("?");
            sb.append(grsReqParamJoint);
        }
        this.i.add(sb.toString());
    }

    private d b(d dVar) {
        String str;
        String str2;
        Throwable e;
        for (Map.Entry<String, Future<d>> entry : this.e.entrySet()) {
            if (dVar != null && (dVar.o() || dVar.m())) {
                break;
            }
            try {
                dVar = entry.getValue().get(40000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e2) {
                e = e2;
                str2 = n;
                str = "{checkResponse} when check result, find InterruptedException, check others";
                Logger.w(str2, str, e);
            } catch (CancellationException unused) {
                Logger.i(n, "{checkResponse} when check result, find CancellationException, check others");
            } catch (ExecutionException e3) {
                e = e3;
                str2 = n;
                str = "{checkResponse} when check result, find ExecutionException, check others";
                Logger.w(str2, str, e);
            } catch (TimeoutException unused2) {
                Logger.w(n, "{checkResponse} when check result, find TimeoutException, cancel current request task");
                if (!entry.getValue().isCancelled()) {
                    entry.getValue().cancel(true);
                }
            }
        }
        return dVar;
    }

    public d b(ExecutorService executorService, String str, com.huawei.hms.framework.network.grs.e.c cVar) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        d a2 = a(executorService, this.i, str, cVar);
        int b = a2 == null ? 0 : a2.b();
        String str2 = n;
        Logger.v(str2, "use 2.0 interface return http's code is：{%s}", Integer.valueOf(b));
        if (b == 404 || b == 401) {
            if (TextUtils.isEmpty(e()) && TextUtils.isEmpty(this.a.getAppName())) {
                Logger.i(str2, "request grs server use 1.0 API must set appName,please check.");
                return null;
            }
            this.e.clear();
            Logger.i(str2, "this env has not deploy new interface,so use old interface.");
            a2 = a(executorService, this.h, str, cVar);
        }
        e.a(new ArrayList(this.f), SystemClock.elapsedRealtime() - elapsedRealtime, this.g, this.b);
        this.f.clear();
        return a2;
    }

    private void b(String str, String str2) {
        if (!TextUtils.isEmpty(this.a.getAppName()) || !TextUtils.isEmpty(e())) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            Locale locale = Locale.ROOT;
            Object[] objArr = new Object[1];
            objArr[0] = TextUtils.isEmpty(e()) ? this.a.getAppName() : e();
            sb.append(String.format(locale, str, objArr));
            String grsReqParamJoint = this.a.getGrsReqParamJoint(false, false, "1.0", this.b);
            if (!TextUtils.isEmpty(grsReqParamJoint)) {
                sb.append("?");
                sb.append(grsReqParamJoint);
            }
            this.h.add(sb.toString());
        }
    }

    private void c() {
        com.huawei.hms.framework.network.grs.g.k.d a2 = com.huawei.hms.framework.network.grs.g.j.a.a(this.b);
        if (a2 == null) {
            Logger.w(n, "g*s***_se****er_conf*** maybe has a big error");
            return;
        }
        a(a2);
        List<String> a3 = a2.a();
        if (a3 == null || a3.size() <= 0) {
            Logger.v(n, "maybe grs_base_url config with [],please check.");
        } else if (a3.size() > 10) {
            throw new IllegalArgumentException("grs_base_url's count is larger than MAX value 10");
        } else {
            String c = a2.c();
            String b = a2.b();
            if (a3.size() > 0) {
                for (String str : a3) {
                    if (!str.startsWith("https://")) {
                        Logger.w(n, "grs server just support https scheme url,please check.");
                    } else {
                        b(c, str);
                        a(b, str);
                    }
                }
            }
            Logger.v(n, "request to GRS server url is{%s} and {%s}", this.h, this.i);
        }
    }

    private void d() {
        String grsParasKey = this.a.getGrsParasKey(true, true, this.b);
        com.huawei.hms.framework.network.grs.e.c a2 = this.c.a();
        this.l = a2.a(grsParasKey + "ETag", "");
    }

    private String e() {
        com.huawei.hms.framework.network.grs.f.b a2 = com.huawei.hms.framework.network.grs.f.b.a(this.b.getPackageName(), this.a);
        com.huawei.hms.framework.network.grs.local.model.a a3 = a2 != null ? a2.a() : null;
        if (a3 != null) {
            String b = a3.b();
            Logger.v(n, "get appName from local assets is{%s}", b);
            return b;
        }
        return "";
    }

    public d a(ExecutorService executorService, String str, com.huawei.hms.framework.network.grs.e.c cVar) {
        String str2;
        String str3;
        Throwable e;
        if (!this.h.isEmpty() || !this.i.isEmpty()) {
            try {
                com.huawei.hms.framework.network.grs.g.k.d b = b();
                return (d) executorService.submit(new a(executorService, str, cVar)).get(b != null ? b.d() : 10, TimeUnit.SECONDS);
            } catch (InterruptedException e2) {
                e = e2;
                str3 = n;
                str2 = "{submitExcutorTaskWithTimeout} the current thread was interrupted while waiting";
                Logger.w(str3, str2, e);
                return null;
            } catch (CancellationException unused) {
                Logger.i(n, "{submitExcutorTaskWithTimeout} the computation was cancelled");
                return null;
            } catch (ExecutionException e3) {
                e = e3;
                str3 = n;
                str2 = "{submitExcutorTaskWithTimeout} the computation threw an ExecutionException";
                Logger.w(str3, str2, e);
                return null;
            } catch (TimeoutException unused2) {
                Logger.w(n, "{submitExcutorTaskWithTimeout} the wait timed out");
                return null;
            } catch (Exception e4) {
                e = e4;
                str3 = n;
                str2 = "{submitExcutorTaskWithTimeout} catch Exception";
                Logger.w(str3, str2, e);
                return null;
            }
        }
        return null;
    }

    public String a() {
        return this.l;
    }

    public synchronized void a(d dVar) {
        this.f.add(dVar);
        d dVar2 = this.d;
        if (dVar2 != null && (dVar2.o() || this.d.m())) {
            Logger.v(n, "grsResponseResult is ok");
        } else if (dVar.n()) {
            Logger.i(n, "GRS server open 503 limiting strategy.");
            com.huawei.hms.framework.network.grs.h.d.a(this.a.getGrsParasKey(true, true, this.b), new d.a(dVar.k(), SystemClock.elapsedRealtime()));
        } else {
            if (dVar.m()) {
                Logger.i(n, "GRS server open 304 Not Modified.");
            }
            if (!dVar.o() && !dVar.m()) {
                Logger.v(n, "grsResponseResult has exception so need return");
                return;
            }
            this.d = dVar;
            this.c.a(this.a, dVar, this.b, this.j);
            for (Map.Entry<String, Future<d>> entry : this.e.entrySet()) {
                if (!entry.getKey().equals(dVar.l()) && !entry.getValue().isCancelled()) {
                    Logger.i(n, "future cancel");
                    entry.getValue().cancel(true);
                }
            }
        }
    }

    public void a(com.huawei.hms.framework.network.grs.g.k.d dVar) {
        this.k = dVar;
    }

    public com.huawei.hms.framework.network.grs.g.k.d b() {
        return this.k;
    }
}
