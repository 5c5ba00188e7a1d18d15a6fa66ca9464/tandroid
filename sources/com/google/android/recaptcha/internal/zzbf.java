package com.google.android.recaptcha.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import java.util.Locale;
import java.util.MissingResourceException;

/* loaded from: classes.dex */
public final class zzbf {
    public static final zzbe zza = new zzbe(null);
    private static zzmo zzb;
    private final String zzc;
    private final zzac zzd;
    private final zznc zze;
    private final long zzf;

    public zzbf(zzbb zzbbVar, String str, zzac zzacVar) {
        this.zzc = str;
        this.zzd = zzacVar;
        zznc zzi = zznf.zzi();
        this.zze = zzi;
        this.zzf = System.currentTimeMillis();
        zzi.zzp(zzbbVar.zza());
        zzi.zzd(zzbbVar.zzb());
        zzi.zzr(zzbbVar.zzc());
        if (zzbbVar.zzd() != null) {
            zzi.zzu(zzbbVar.zzd());
        }
        zzi.zzt(zzmg.zzc(zzmg.zzb(System.currentTimeMillis())));
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0040, code lost:
    
        if (r2 == (-1)) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0023, code lost:
    
        if (r2 == (-1)) goto L8;
     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0047 A[Catch: NameNotFoundException -> 0x0086, TryCatch #0 {NameNotFoundException -> 0x0086, blocks: (B:10:0x0043, B:12:0x0047, B:13:0x0059, B:19:0x0067, B:20:0x0074), top: B:9:0x0043 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static final zzmo zzb(Context context) {
        String str;
        int i;
        PackageInfo packageInfo;
        long longVersionCode;
        PackageManager.PackageInfoFlags of;
        int i2;
        PackageManager.ApplicationInfoFlags of2;
        ApplicationInfo applicationInfo;
        String str2 = "unknown";
        try {
            if (Build.VERSION.SDK_INT >= 33) {
                PackageManager packageManager = context.getPackageManager();
                String packageName = context.getPackageName();
                of2 = PackageManager.ApplicationInfoFlags.of(128L);
                applicationInfo = packageManager.getApplicationInfo(packageName, of2);
                i2 = applicationInfo.metaData.getInt("com.google.android.gms.version", -1);
            } else {
                i2 = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getInt("com.google.android.gms.version", -1);
            }
            str = String.valueOf(i2);
        } catch (PackageManager.NameNotFoundException unused) {
        }
        try {
            i = Build.VERSION.SDK_INT;
        } catch (PackageManager.NameNotFoundException unused2) {
        }
        if (i < 33) {
            PackageManager packageManager2 = context.getPackageManager();
            String packageName2 = context.getPackageName();
            of = PackageManager.PackageInfoFlags.of(0L);
            packageInfo = packageManager2.getPackageInfo(packageName2, of);
        } else {
            if (i < 28) {
                str2 = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
                zzmn zzf = zzmo.zzf();
                zzf.zzd(Build.VERSION.SDK_INT);
                zzf.zzq(str);
                zzf.zzs("18.4.0");
                zzf.zzp(Build.MODEL);
                zzf.zzr(Build.MANUFACTURER);
                zzf.zze(str2);
                return (zzmo) zzf.zzj();
            }
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }
        longVersionCode = packageInfo.getLongVersionCode();
        str2 = String.valueOf(longVersionCode);
        zzmn zzf2 = zzmo.zzf();
        zzf2.zzd(Build.VERSION.SDK_INT);
        zzf2.zzq(str);
        zzf2.zzs("18.4.0");
        zzf2.zzp(Build.MODEL);
        zzf2.zzr(Build.MANUFACTURER);
        zzf2.zze(str2);
        return (zzmo) zzf2.zzj();
        str = "unknown";
        i = Build.VERSION.SDK_INT;
        if (i < 33) {
        }
        longVersionCode = packageInfo.getLongVersionCode();
        str2 = String.valueOf(longVersionCode);
        zzmn zzf22 = zzmo.zzf();
        zzf22.zzd(Build.VERSION.SDK_INT);
        zzf22.zzq(str);
        zzf22.zzs("18.4.0");
        zzf22.zzp(Build.MODEL);
        zzf22.zzr(Build.MANUFACTURER);
        zzf22.zze(str2);
        return (zzmo) zzf22.zzj();
    }

    public final zznf zza(int i, zzmr zzmrVar, Context context) {
        String str;
        String str2 = "";
        long currentTimeMillis = System.currentTimeMillis() - this.zzf;
        zznc zzncVar = this.zze;
        zzncVar.zze(currentTimeMillis);
        zzncVar.zzv(i);
        if (zzmrVar != null) {
            this.zze.zzq(zzmrVar);
        }
        if (zzb == null) {
            zzb = zzb(context);
        }
        try {
            str = Locale.getDefault().getISO3Language();
        } catch (MissingResourceException unused) {
            str = "";
        }
        try {
            str2 = Locale.getDefault().getISO3Country();
        } catch (MissingResourceException unused2) {
        }
        zznc zzncVar2 = this.zze;
        String str3 = this.zzc;
        zznq zzf = zznr.zzf();
        zzf.zzq(str3);
        zzmo zzmoVar = zzb;
        if (zzmoVar == null) {
            zzmoVar = zzb(context);
        }
        zzf.zzd(zzmoVar);
        zzf.zzp(str);
        zzf.zze(str2);
        zzncVar2.zzs((zznr) zzf.zzj());
        return (zznf) this.zze.zzj();
    }
}
