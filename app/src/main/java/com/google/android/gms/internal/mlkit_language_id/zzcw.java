package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzy$zzad;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzcw implements Runnable {
    private final zzcv zza;
    private final zzy$zzad.zza zzb;
    private final zzaj zzc;

    public zzcw(zzcv zzcvVar, zzy$zzad.zza zzaVar, zzaj zzajVar) {
        this.zza = zzcvVar;
        this.zzb = zzaVar;
        this.zzc = zzajVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.zzb(this.zzb, this.zzc);
    }
}
