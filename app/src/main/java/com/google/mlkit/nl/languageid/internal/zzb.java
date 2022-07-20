package com.google.mlkit.nl.languageid.internal;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzb implements Executor {
    private final LanguageIdentificationJni zza;
    private final AtomicReference zzb;
    private final Executor zzc;

    public zzb(LanguageIdentificationJni languageIdentificationJni, AtomicReference atomicReference, Executor executor) {
        this.zza = languageIdentificationJni;
        this.zzb = atomicReference;
        this.zzc = executor;
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        LanguageIdentificationJni languageIdentificationJni = this.zza;
        AtomicReference atomicReference = this.zzb;
        Executor executor = this.zzc;
        if (Thread.currentThread().equals(atomicReference.get()) && languageIdentificationJni.isLoaded()) {
            runnable.run();
        } else {
            executor.execute(runnable);
        }
    }
}
