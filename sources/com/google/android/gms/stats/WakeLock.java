package com.google.android.gms.stats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.providers.PooledExecutorsProvider;
import com.google.android.gms.common.stats.StatsUtils;
import com.google.android.gms.common.stats.WakeLockTracker;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.common.util.WorkSourceUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class WakeLock {
    private static ScheduledExecutorService zzn;
    private static volatile zza zzo = new com.google.android.gms.stats.zza();
    private final Object zza;
    private final PowerManager.WakeLock zzb;
    private WorkSource zzc;
    private final int zzd;
    private final String zze;
    private final String zzf;
    private final String zzg;
    private final Context zzh;
    private boolean zzi;
    private final Map<String, Integer[]> zzj;
    private final Set<Future<?>> zzk;
    private int zzl;
    private AtomicInteger zzm;

    /* loaded from: classes.dex */
    public interface zza {
    }

    public WakeLock(Context context, int i, String str) {
        this(context, i, str, null, context == null ? null : context.getPackageName());
    }

    private WakeLock(Context context, int i, String str, String str2, String str3) {
        this(context, i, str, null, str3, null);
    }

    @SuppressLint({"UnwrappedWakeLock"})
    private WakeLock(Context context, int i, String str, String str2, String str3, String str4) {
        this.zza = this;
        this.zzi = true;
        this.zzj = new HashMap();
        this.zzk = Collections.synchronizedSet(new HashSet());
        this.zzm = new AtomicInteger(0);
        Preconditions.checkNotNull(context, "WakeLock: context must not be null");
        Preconditions.checkNotEmpty(str, "WakeLock: wakeLockName must not be empty");
        this.zzd = i;
        this.zzf = null;
        this.zzg = null;
        Context applicationContext = context.getApplicationContext();
        this.zzh = applicationContext;
        if (!"com.google.android.gms".equals(context.getPackageName())) {
            String valueOf = String.valueOf(str);
            this.zze = valueOf.length() != 0 ? "*gcore*:".concat(valueOf) : new String("*gcore*:");
        } else {
            this.zze = str;
        }
        PowerManager.WakeLock newWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(i, str);
        this.zzb = newWakeLock;
        if (WorkSourceUtil.hasWorkSourcePermission(context)) {
            WorkSource fromPackage = WorkSourceUtil.fromPackage(context, Strings.isEmptyOrWhitespace(str3) ? context.getPackageName() : str3);
            this.zzc = fromPackage;
            if (fromPackage != null && WorkSourceUtil.hasWorkSourcePermission(applicationContext)) {
                WorkSource workSource = this.zzc;
                if (workSource != null) {
                    workSource.add(fromPackage);
                } else {
                    this.zzc = fromPackage;
                }
                try {
                    newWakeLock.setWorkSource(this.zzc);
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                    Log.wtf("WakeLock", e.toString());
                }
            }
        }
        if (zzn == null) {
            zzn = PooledExecutorsProvider.getInstance().newSingleThreadScheduledExecutor();
        }
    }

    private final List<String> zza() {
        return WorkSourceUtil.getNames(this.zzc);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0061, code lost:
        if (r16.zzl == 0) goto L14;
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0036 A[Catch: all -> 0x0020, TryCatch #0 {all -> 0x0020, blocks: (B:4:0x0013, B:6:0x001b, B:14:0x0032, B:16:0x0036, B:18:0x0040, B:24:0x0063, B:25:0x0083, B:19:0x004e, B:20:0x005b, B:22:0x005f, B:11:0x0023, B:13:0x002b), top: B:32:0x0013 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005f A[Catch: all -> 0x0020, TryCatch #0 {all -> 0x0020, blocks: (B:4:0x0013, B:6:0x001b, B:14:0x0032, B:16:0x0036, B:18:0x0040, B:24:0x0063, B:25:0x0083, B:19:0x004e, B:20:0x005b, B:22:0x005f, B:11:0x0023, B:13:0x002b), top: B:32:0x0013 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void acquire(long j) {
        this.zzm.incrementAndGet();
        String zza2 = zza((String) null);
        synchronized (this.zza) {
            try {
                if (this.zzj.isEmpty()) {
                    if (this.zzl > 0) {
                    }
                    if (this.zzi) {
                        Integer[] numArr = this.zzj.get(zza2);
                        if (numArr == null) {
                            this.zzj.put(zza2, new Integer[]{1});
                            WakeLockTracker.getInstance().registerEvent(this.zzh, StatsUtils.getEventKey(this.zzb, zza2), 7, this.zze, zza2, null, this.zzd, zza(), j);
                            this.zzl++;
                        } else {
                            numArr[0] = Integer.valueOf(numArr[0].intValue() + 1);
                        }
                    }
                    if (!this.zzi) {
                    }
                }
                if (!this.zzb.isHeld()) {
                    this.zzj.clear();
                    this.zzl = 0;
                }
                if (this.zzi) {
                }
                if (!this.zzi) {
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        this.zzb.acquire();
        if (j > 0) {
            zzn.schedule(new zzb(this), j, TimeUnit.MILLISECONDS);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0055, code lost:
        if (r12.zzl == 1) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void release() {
        Integer[] numArr;
        if (this.zzm.decrementAndGet() < 0) {
            Log.e("WakeLock", String.valueOf(this.zze).concat(" release without a matched acquire!"));
        }
        String zza2 = zza((String) null);
        synchronized (this.zza) {
            try {
                if (this.zzi && (numArr = this.zzj.get(zza2)) != null) {
                    if (numArr[0].intValue() == 1) {
                        this.zzj.remove(zza2);
                        WakeLockTracker.getInstance().registerEvent(this.zzh, StatsUtils.getEventKey(this.zzb, zza2), 8, this.zze, zza2, null, this.zzd, zza());
                        this.zzl--;
                    } else {
                        numArr[0] = Integer.valueOf(numArr[0].intValue() - 1);
                    }
                }
                if (!this.zzi) {
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        zza(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(int i) {
        if (this.zzb.isHeld()) {
            try {
                this.zzb.release();
            } catch (RuntimeException e) {
                if (e.getClass().equals(RuntimeException.class)) {
                    Log.e("WakeLock", String.valueOf(this.zze).concat(" was already released!"), e);
                } else {
                    throw e;
                }
            }
            this.zzb.isHeld();
        }
    }

    private final String zza(String str) {
        return (!this.zzi || TextUtils.isEmpty(str)) ? this.zzf : str;
    }

    public void setReferenceCounted(boolean z) {
        this.zzb.setReferenceCounted(z);
        this.zzi = z;
    }
}
