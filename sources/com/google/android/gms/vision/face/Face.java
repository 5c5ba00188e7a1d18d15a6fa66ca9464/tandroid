package com.google.android.gms.vision.face;

import android.graphics.PointF;
import androidx.annotation.RecentlyNonNull;
import java.util.Arrays;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-vision@@20.1.3 */
/* loaded from: classes.dex */
public class Face {
    private int zza;
    private PointF zzb;
    private float zzc;
    private float zzd;
    private float zze;
    private float zzf;
    private float zzg;
    private List<Landmark> zzh;
    private final List<Contour> zzi;
    private float zzj;
    private float zzk;
    private float zzl;
    private final float zzm;

    private static float zza(float f) {
        if (f < 0.0f || f > 1.0f) {
            return -1.0f;
        }
        return f;
    }

    @RecentlyNonNull
    public List<Landmark> getLandmarks() {
        return this.zzh;
    }

    public int getId() {
        return this.zza;
    }

    public Face(int i, @RecentlyNonNull PointF pointF, float f, float f2, float f3, float f4, float f5, @RecentlyNonNull Landmark[] landmarkArr, @RecentlyNonNull Contour[] contourArr, float f6, float f7, float f8, float f9) {
        this.zza = i;
        this.zzb = pointF;
        this.zzc = f;
        this.zzd = f2;
        this.zze = f3;
        this.zzf = f4;
        this.zzg = f5;
        this.zzh = Arrays.asList(landmarkArr);
        this.zzi = Arrays.asList(contourArr);
        this.zzj = zza(f6);
        this.zzk = zza(f7);
        this.zzl = zza(f8);
        this.zzm = zza(f9);
    }
}
