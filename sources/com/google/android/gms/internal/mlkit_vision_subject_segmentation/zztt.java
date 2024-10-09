package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import android.content.Context;
import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.Event;
import com.google.android.datatransport.Transformer;
import com.google.android.datatransport.Transport;
import com.google.android.datatransport.TransportFactory;
import com.google.android.datatransport.cct.CCTDestination;
import com.google.android.datatransport.runtime.TransportRuntime;
import com.google.firebase.components.Lazy;
import com.google.firebase.inject.Provider;

/* loaded from: classes.dex */
public final class zztt implements zztb {
    private Provider zza;
    private final Provider zzb;
    private final zztd zzc;

    public zztt(Context context, zztd zztdVar) {
        this.zzc = zztdVar;
        CCTDestination cCTDestination = CCTDestination.INSTANCE;
        TransportRuntime.initialize(context);
        final TransportFactory newFactory = TransportRuntime.getInstance().newFactory(cCTDestination);
        if (cCTDestination.getSupportedEncodings().contains(Encoding.of("json"))) {
            this.zza = new Lazy(new Provider() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztq
                @Override // com.google.firebase.inject.Provider
                public final Object get() {
                    return TransportFactory.this.getTransport("FIREBASE_ML_SDK", byte[].class, Encoding.of("json"), new Transformer() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzts
                        @Override // com.google.android.datatransport.Transformer
                        public final Object apply(Object obj) {
                            return (byte[]) obj;
                        }
                    });
                }
            });
        }
        this.zzb = new Lazy(new Provider() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztr
            @Override // com.google.firebase.inject.Provider
            public final Object get() {
                return TransportFactory.this.getTransport("FIREBASE_ML_SDK", byte[].class, Encoding.of("proto"), new Transformer() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztp
                    @Override // com.google.android.datatransport.Transformer
                    public final Object apply(Object obj) {
                        return (byte[]) obj;
                    }
                });
            }
        });
    }

    static Event zzb(zztd zztdVar, zzta zztaVar) {
        int zza = zztdVar.zza();
        int zza2 = zztaVar.zza();
        byte[] zze = zztaVar.zze(zza, false);
        return zza2 != 0 ? Event.ofData(zze) : Event.ofTelemetry(zze);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztb
    public final void zza(zzta zztaVar) {
        Provider provider;
        if (this.zzc.zza() == 0) {
            provider = this.zza;
            if (provider == null) {
                return;
            }
        } else {
            provider = this.zzb;
        }
        ((Transport) provider.get()).send(zzb(this.zzc, zztaVar));
    }
}
