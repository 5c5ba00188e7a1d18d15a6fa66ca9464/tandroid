package com.google.android.gms.internal.mlkit_vision_label;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzne extends zznh {
    private final String zza;
    private final boolean zzb;
    private final int zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzne(String str, boolean z, int i, zznd zzndVar) {
        this.zza = str;
        this.zzb = z;
        this.zzc = i;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zznh) {
            zznh zznhVar = (zznh) obj;
            if (this.zza.equals(zznhVar.zzb()) && this.zzb == zznhVar.zzc() && this.zzc == zznhVar.zza()) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return ((((this.zza.hashCode() ^ 1000003) * 1000003) ^ (true != this.zzb ? 1237 : 1231)) * 1000003) ^ this.zzc;
    }

    public final String toString() {
        String str = this.zza;
        boolean z = this.zzb;
        int i = this.zzc;
        return "MLKitLoggingOptions{libraryName=" + str + ", enableFirelog=" + z + ", firelogEventType=" + i + "}";
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zznh
    public final int zza() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zznh
    public final String zzb() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zznh
    public final boolean zzc() {
        return this.zzb;
    }
}
