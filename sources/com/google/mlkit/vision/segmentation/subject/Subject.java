package com.google.mlkit.vision.segmentation.subject;

import android.graphics.Bitmap;
import java.nio.FloatBuffer;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public class Subject {
    private final FloatBuffer zza;
    private final Bitmap zzb;
    private final int zzc;
    private final int zzd;
    private final int zze;
    private final int zzf;

    public Subject(FloatBuffer floatBuffer, Bitmap bitmap, int i, int i2, int i3, int i4) {
        this.zza = floatBuffer;
        this.zzb = bitmap;
        this.zzc = i;
        this.zzd = i2;
        this.zze = i3;
        this.zzf = i4;
    }

    public Bitmap getBitmap() {
        return this.zzb;
    }

    public int getHeight() {
        return this.zzd;
    }

    public int getStartX() {
        return this.zze;
    }

    public int getStartY() {
        return this.zzf;
    }

    public int getWidth() {
        return this.zzc;
    }
}
