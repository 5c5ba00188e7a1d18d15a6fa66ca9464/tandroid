package com.google.mlkit.nl.languageid;

import com.google.android.gms.internal.mlkit_language_id.zzai;
import com.google.android.gms.internal.mlkit_language_id.zzcv;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzad;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzau;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzf implements zzcv.zza {
    private final LanguageIdentifierImpl zza;
    private final long zzb;
    private final boolean zzc;
    private final zzai zzd;
    private final zzy$zzau.zzd zze;
    private final zzy$zzau.zzc zzf;

    public zzf(LanguageIdentifierImpl languageIdentifierImpl, long j, boolean z, zzai zzaiVar, zzy$zzau.zzd zzdVar, zzy$zzau.zzc zzcVar) {
        this.zza = languageIdentifierImpl;
        this.zzb = j;
        this.zzc = z;
        this.zzd = zzaiVar;
        this.zze = zzdVar;
        this.zzf = zzcVar;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzcv.zza
    public final zzy$zzad.zza zza() {
        return this.zza.zza(this.zzb, this.zzc, this.zzd, this.zze, this.zzf);
    }
}
