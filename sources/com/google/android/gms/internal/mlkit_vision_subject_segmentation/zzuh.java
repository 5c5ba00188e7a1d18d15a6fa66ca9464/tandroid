package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.List;

/* loaded from: classes.dex */
public final class zzuh extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzuh> CREATOR = new zzui();
    private final List zza;
    private final float[] zzb;
    private final Bitmap zzc;
    private final List zzd;

    public zzuh(List list, float[] fArr, Bitmap bitmap, List list2) {
        this.zza = list;
        this.zzb = fArr;
        this.zzc = bitmap;
        this.zzd = list2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        List list = this.zza;
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeTypedList(parcel, 1, list, false);
        SafeParcelWriter.writeFloatArray(parcel, 2, this.zzb, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzc, i, false);
        SafeParcelWriter.writeFloatList(parcel, 4, this.zzd, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final Bitmap zza() {
        return this.zzc;
    }

    public final List zzb() {
        return this.zzd;
    }

    public final List zzc() {
        return this.zza;
    }

    public final float[] zzd() {
        return this.zzb;
    }
}
