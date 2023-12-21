package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.encoders.json.JsonDataEncoderBuilder;
import java.io.UnsupportedEncodingException;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public final class zzto implements zzta {
    private final zzoc zza;
    private zzsa zzb = new zzsa();
    private final int zzc;

    private zzto(zzoc zzocVar, int i) {
        this.zza = zzocVar;
        zztx.zza();
        this.zzc = i;
    }

    public static zzta zzf(zzoc zzocVar) {
        return new zzto(zzocVar, 0);
    }

    public static zzta zzg(zzoc zzocVar, int i) {
        return new zzto(zzocVar, 1);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzta
    public final int zza() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzta
    public final zzta zzb(zzob zzobVar) {
        this.zza.zzf(zzobVar);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzta
    public final zzta zzc(zzsa zzsaVar) {
        this.zzb = zzsaVar;
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzta
    public final String zzd() {
        zzsc zzg = this.zza.zzk().zzg();
        return (zzg == null || zzj.zzb(zzg.zzk())) ? "NA" : (String) Preconditions.checkNotNull(zzg.zzk());
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzta
    public final byte[] zze(int i, boolean z) {
        this.zzb.zzf(Boolean.valueOf(1 == (i ^ 1)));
        this.zzb.zze(Boolean.FALSE);
        this.zza.zzj(this.zzb.zzm());
        try {
            zztx.zza();
            if (i == 0) {
                return new JsonDataEncoderBuilder().configureWith(zzlw.zza).ignoreNullValues(true).build().encode(this.zza.zzk()).getBytes("utf-8");
            }
            zzoe zzk = this.zza.zzk();
            zzch zzchVar = new zzch();
            zzlw.zza.configure(zzchVar);
            return zzchVar.zza().zza(zzk);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException("Failed to covert logging to UTF-8 byte array", e);
        }
    }
}
