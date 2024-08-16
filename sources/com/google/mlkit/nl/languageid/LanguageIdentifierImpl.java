package com.google.mlkit.nl.languageid;

import android.os.SystemClock;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.mlkit_language_id.zzai;
import com.google.android.gms.internal.mlkit_language_id.zzaj;
import com.google.android.gms.internal.mlkit_language_id.zzcv;
import com.google.android.gms.internal.mlkit_language_id.zzeo;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzad;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzaf;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzau;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.sdkinternal.ExecutorSelector;
import com.google.mlkit.nl.languageid.internal.LanguageIdentificationJni;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.NotificationCenter;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public class LanguageIdentifierImpl implements LanguageIdentifier {
    private final LanguageIdentificationOptions zza;
    private final zzcv zzb;
    private final Executor zzc;
    private final AtomicReference<LanguageIdentificationJni> zzd;
    private final CancellationTokenSource zze = new CancellationTokenSource();

    private LanguageIdentifierImpl(LanguageIdentificationOptions languageIdentificationOptions, LanguageIdentificationJni languageIdentificationJni, zzcv zzcvVar, Executor executor) {
        this.zza = languageIdentificationOptions;
        this.zzb = zzcvVar;
        this.zzc = executor;
        this.zzd = new AtomicReference<>(languageIdentificationJni);
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class Factory {
        private final zzcv zza;
        private final LanguageIdentificationJni zzb;
        private final ExecutorSelector zzc;

        public Factory(zzcv zzcvVar, LanguageIdentificationJni languageIdentificationJni, ExecutorSelector executorSelector) {
            this.zza = zzcvVar;
            this.zzb = languageIdentificationJni;
            this.zzc = executorSelector;
        }

        public final LanguageIdentifier create(LanguageIdentificationOptions languageIdentificationOptions) {
            return LanguageIdentifierImpl.zza(languageIdentificationOptions, this.zzb, this.zza, this.zzc);
        }
    }

    static LanguageIdentifier zza(LanguageIdentificationOptions languageIdentificationOptions, LanguageIdentificationJni languageIdentificationJni, zzcv zzcvVar, ExecutorSelector executorSelector) {
        LanguageIdentifierImpl languageIdentifierImpl = new LanguageIdentifierImpl(languageIdentificationOptions, languageIdentificationJni, zzcvVar, executorSelector.getExecutorToUse(languageIdentificationOptions.zzc()));
        languageIdentifierImpl.zzb.zza(zzy$zzad.zzb().zza(true).zza(zzy$zzau.zza().zza(languageIdentifierImpl.zza.zza())), zzaj.zzb);
        languageIdentifierImpl.zzd.get().pin();
        return languageIdentifierImpl;
    }

    @Override // com.google.mlkit.nl.languageid.LanguageIdentifier
    public Task<String> identifyLanguage(final String str) {
        Preconditions.checkNotNull(str, "Text can not be null");
        final LanguageIdentificationJni languageIdentificationJni = this.zzd.get();
        Preconditions.checkState(languageIdentificationJni != null, "LanguageIdentification has been closed");
        final boolean isLoaded = true ^ languageIdentificationJni.isLoaded();
        return languageIdentificationJni.zza(this.zzc, new Callable(this, languageIdentificationJni, str, isLoaded) { // from class: com.google.mlkit.nl.languageid.zzd
            private final LanguageIdentifierImpl zza;
            private final LanguageIdentificationJni zzb;
            private final String zzc;
            private final boolean zzd;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = this;
                this.zzb = languageIdentificationJni;
                this.zzc = str;
                this.zzd = isLoaded;
            }

            @Override // java.util.concurrent.Callable
            public final Object call() {
                return this.zza.zza(this.zzb, this.zzc, this.zzd);
            }
        }, this.zze.getToken());
    }

    @Override // com.google.mlkit.nl.languageid.LanguageIdentifier, java.io.Closeable, java.lang.AutoCloseable
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void close() {
        LanguageIdentificationJni andSet = this.zzd.getAndSet(null);
        if (andSet == null) {
            return;
        }
        this.zze.cancel();
        andSet.unpin(this.zzc);
    }

    private final void zza(long j, final boolean z, final zzy$zzau.zzd zzdVar, final zzy$zzau.zzc zzcVar, final zzai zzaiVar) {
        final long elapsedRealtime = SystemClock.elapsedRealtime() - j;
        this.zzb.zza(new zzcv.zza(this, elapsedRealtime, z, zzaiVar, zzdVar, zzcVar) { // from class: com.google.mlkit.nl.languageid.zzf
            private final LanguageIdentifierImpl zza;
            private final long zzb;
            private final boolean zzc;
            private final zzai zzd;
            private final zzy$zzau.zzd zze;
            private final zzy$zzau.zzc zzf;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = this;
                this.zzb = elapsedRealtime;
                this.zzc = z;
                this.zzd = zzaiVar;
                this.zze = zzdVar;
                this.zzf = zzcVar;
            }

            @Override // com.google.android.gms.internal.mlkit_language_id.zzcv.zza
            public final zzy$zzad.zza zza() {
                return this.zza.zza(this.zzb, this.zzc, this.zzd, this.zze, this.zzf);
            }
        }, zzaj.zza);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ zzy$zzad.zza zza(long j, boolean z, zzai zzaiVar, zzy$zzau.zzd zzdVar, zzy$zzau.zzc zzcVar) {
        zzy$zzau.zza zza = zzy$zzau.zza().zza(this.zza.zza()).zza(zzy$zzaf.zza().zza(j).zza(z).zza(zzaiVar));
        if (zzdVar != null) {
            zza.zza(zzdVar);
        }
        if (zzcVar != null) {
            zza.zza(zzcVar);
        }
        return zzy$zzad.zzb().zza(true).zza(zza);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String zza(LanguageIdentificationJni languageIdentificationJni, String str, boolean z) throws Exception {
        zzy$zzau.zzc zzcVar;
        Float zzb = this.zza.zzb();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            String zza = languageIdentificationJni.zza(str.substring(0, Math.min(str.length(), (int) NotificationCenter.storyQualityUpdate)), zzb != null ? zzb.floatValue() : 0.5f);
            if (zza == null) {
                zzcVar = zzy$zzau.zzc.zzb();
            } else {
                zzcVar = (zzy$zzau.zzc) ((zzeo) zzy$zzau.zzc.zza().zza(zzy$zzau.zzb.zza().zza(zza)).zzg());
            }
            zza(elapsedRealtime, z, (zzy$zzau.zzd) null, zzcVar, zzai.zza);
            return zza;
        } catch (RuntimeException e) {
            zza(elapsedRealtime, z, (zzy$zzau.zzd) null, zzy$zzau.zzc.zzb(), zzai.zzb);
            throw e;
        }
    }
}
