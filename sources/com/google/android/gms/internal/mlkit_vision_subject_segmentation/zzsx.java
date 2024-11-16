package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

/* loaded from: classes.dex */
final class zzsx extends zztc {
    private String zza;
    private boolean zzb;
    private int zzc;
    private byte zzd;

    zzsx() {
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztc
    public final zztc zza(boolean z) {
        this.zzb = true;
        this.zzd = (byte) (1 | this.zzd);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztc
    public final zztc zzb(int i) {
        this.zzc = 1;
        this.zzd = (byte) (this.zzd | 2);
        return this;
    }

    public final zztc zzc(String str) {
        this.zza = "subject-segmentation";
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztc
    public final zztd zzd() {
        String str;
        if (this.zzd == 3 && (str = this.zza) != null) {
            return new zzsz(str, this.zzb, this.zzc, null);
        }
        StringBuilder sb = new StringBuilder();
        if (this.zza == null) {
            sb.append(" libraryName");
        }
        if ((this.zzd & 1) == 0) {
            sb.append(" enableFirelog");
        }
        if ((this.zzd & 2) == 0) {
            sb.append(" firelogEventType");
        }
        throw new IllegalStateException("Missing required properties:".concat(sb.toString()));
    }
}
