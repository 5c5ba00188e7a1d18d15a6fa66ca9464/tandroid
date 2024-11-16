package com.google.android.gms.stats;

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
    private final Map zzj;
    private final Set zzk;
    private int zzl;
    private AtomicInteger zzm;

    public interface zza {
    }

    public WakeLock(Context context, int i, String str) {
        this(context, i, str, null, context == null ? null : context.getPackageName());
    }

    private WakeLock(Context context, int i, String str, String str2, String str3) {
        this(context, i, str, null, str3, null);
    }

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
        if ("com.google.android.gms".equals(context.getPackageName())) {
            this.zze = str;
        } else {
            String valueOf = String.valueOf(str);
            this.zze = valueOf.length() != 0 ? "*gcore*:".concat(valueOf) : new String("*gcore*:");
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

    private final String zza(String str) {
        return (!this.zzi || TextUtils.isEmpty(str)) ? this.zzf : str;
    }

    private final List zza() {
        return WorkSourceUtil.getNames(this.zzc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(int i) {
        if (this.zzb.isHeld()) {
            try {
                this.zzb.release();
            } catch (RuntimeException e) {
                if (!e.getClass().equals(RuntimeException.class)) {
                    throw e;
                }
                Log.e("WakeLock", String.valueOf(this.zze).concat(" was already released!"), e);
            }
            this.zzb.isHeld();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0061, code lost:
    
        if (r16.zzl == 0) goto L24;
     */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0036 A[Catch: all -> 0x0020, TryCatch #0 {all -> 0x0020, blocks: (B:4:0x0013, B:6:0x001b, B:9:0x0032, B:11:0x0036, B:13:0x0040, B:14:0x0063, B:15:0x0083, B:23:0x004e, B:24:0x005b, B:26:0x005f, B:28:0x0023, B:30:0x002b), top: B:3:0x0013 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x005f A[Catch: all -> 0x0020, TryCatch #0 {all -> 0x0020, blocks: (B:4:0x0013, B:6:0x001b, B:9:0x0032, B:11:0x0036, B:13:0x0040, B:14:0x0063, B:15:0x0083, B:23:0x004e, B:24:0x005b, B:26:0x005f, B:28:0x0023, B:30:0x002b), top: B:3:0x0013 }] */
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
                        Integer[] numArr = (Integer[]) this.zzj.get(zza2);
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

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0055, code lost:
    
        if (r12.zzl == 1) goto L22;
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
                if (this.zzi && (numArr = (Integer[]) this.zzj.get(zza2)) != null) {
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

    public void setReferenceCounted(boolean z) {
        this.zzb.setReferenceCounted(z);
        this.zzi = z;
    }
}
