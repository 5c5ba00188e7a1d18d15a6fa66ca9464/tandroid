package com.google.mlkit.nl.languageid;

import com.google.mlkit.nl.languageid.internal.LanguageIdentificationJni;
import java.util.concurrent.Callable;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
final /* synthetic */ class zzd implements Callable {
    private final LanguageIdentifierImpl zza;
    private final LanguageIdentificationJni zzb;
    private final String zzc;
    private final boolean zzd;

    public zzd(LanguageIdentifierImpl languageIdentifierImpl, LanguageIdentificationJni languageIdentificationJni, String str, boolean z) {
        this.zza = languageIdentifierImpl;
        this.zzb = languageIdentificationJni;
        this.zzc = str;
        this.zzd = z;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        return this.zza.zza(this.zzb, this.zzc, this.zzd);
    }
}
