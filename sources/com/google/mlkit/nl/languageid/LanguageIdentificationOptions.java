package com.google.mlkit.nl.languageid;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.internal.mlkit_language_id.zzeo;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzai;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class LanguageIdentificationOptions {
    static final LanguageIdentificationOptions zza = new Builder().build();
    private final Float zzb;
    private final Executor zzc;

    /* loaded from: classes.dex */
    public static class Builder {
        private Float zza;
        private Executor zzb;

        public LanguageIdentificationOptions build() {
            return new LanguageIdentificationOptions(this.zza, this.zzb);
        }
    }

    private LanguageIdentificationOptions(Float f, Executor executor) {
        this.zzb = f;
        this.zzc = executor;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LanguageIdentificationOptions) {
            return Objects.equal(((LanguageIdentificationOptions) obj).zzb, this.zzb);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.zzb);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzy$zzai zza() {
        return this.zzb == null ? zzy$zzai.zzb() : (zzy$zzai) ((zzeo) zzy$zzai.zza().zza(this.zzb.floatValue()).zzg());
    }

    public final Float zzb() {
        return this.zzb;
    }

    public final Executor zzc() {
        return this.zzc;
    }
}
