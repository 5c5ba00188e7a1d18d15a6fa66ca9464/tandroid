package com.google.mlkit.vision.label;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.internal.mlkit_vision_label_common.zzd;
import com.google.android.gms.internal.mlkit_vision_label_common.zze;
import com.google.android.gms.internal.mlkit_vision_label_common.zzi;
/* compiled from: com.google.mlkit:image-labeling-common@@18.1.0 */
/* loaded from: classes.dex */
public class ImageLabel {
    private final String zza;
    private final float zzb;
    private final int zzc;
    private final String zzd;

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ImageLabel) {
            ImageLabel imageLabel = (ImageLabel) obj;
            return Objects.equal(this.zza, imageLabel.getText()) && Float.compare(this.zzb, imageLabel.getConfidence()) == 0 && this.zzc == imageLabel.getIndex() && Objects.equal(this.zzd, imageLabel.zzd);
        }
        return false;
    }

    public float getConfidence() {
        return this.zzb;
    }

    public int getIndex() {
        return this.zzc;
    }

    public String getText() {
        return this.zza;
    }

    public int hashCode() {
        return Objects.hashCode(this.zza, Float.valueOf(this.zzb), Integer.valueOf(this.zzc), this.zzd);
    }

    public String toString() {
        zzd zza = zze.zza(this);
        zza.zzc("text", this.zza);
        zza.zza("confidence", this.zzb);
        zza.zzb("index", this.zzc);
        zza.zzc("mid", this.zzd);
        return zza.toString();
    }

    public ImageLabel(String str, float f, int i, String str2) {
        this.zza = zzi.zza(str);
        this.zzb = f;
        this.zzc = i;
        this.zzd = str2;
    }
}
