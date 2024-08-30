package com.google.android.gms.internal.mlkit_vision_label;
/* loaded from: classes.dex */
final class zznc extends zzng {
    private String zza;
    private boolean zzb;
    private int zzc;
    private byte zzd;

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzng
    public final zzng zza(boolean z) {
        this.zzb = true;
        this.zzd = (byte) (1 | this.zzd);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzng
    public final zzng zzb(int i) {
        this.zzc = 1;
        this.zzd = (byte) (this.zzd | 2);
        return this;
    }

    public final zzng zzc(String str) {
        this.zza = "play-services-mlkit-image-labeling";
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzng
    public final zznh zzd() {
        String str;
        if (this.zzd != 3 || (str = this.zza) == null) {
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
        return new zzne(str, this.zzb, this.zzc, null);
    }
}
