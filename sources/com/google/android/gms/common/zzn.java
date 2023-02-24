package com.google.android.gms.common;

import android.content.Context;
import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-basement@@18.1.0 */
/* loaded from: classes.dex */
public final class zzn {
    static final zzl zzc;
    static final zzl zzd;
    private static Context zzg;

    static {
        new zzf(zzj.zze("0\u0082\u0005È0\u0082\u0003° \u0003\u0002\u0001\u0002\u0002\u0014\u0010\u008ae\bsù/\u008eQí"));
        new zzg(zzj.zze("0\u0082\u0006\u00040\u0082\u0003ì \u0003\u0002\u0001\u0002\u0002\u0014\u0003£²\u00ad×árÊkì"));
        zzc = new zzh(zzj.zze("0\u0082\u0004C0\u0082\u0003+ \u0003\u0002\u0001\u0002\u0002\t\u0000Âà\u0087FdJ0\u008d0"));
        zzd = new zzi(zzj.zze("0\u0082\u0004¨0\u0082\u0003\u0090 \u0003\u0002\u0001\u0002\u0002\t\u0000Õ\u0085¸l}ÓNõ0"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void zze(Context context) {
        synchronized (zzn.class) {
            if (zzg != null) {
                Log.w("GoogleCertificates", "GoogleCertificates has been initialized already");
            } else if (context != null) {
                zzg = context.getApplicationContext();
            }
        }
    }
}
