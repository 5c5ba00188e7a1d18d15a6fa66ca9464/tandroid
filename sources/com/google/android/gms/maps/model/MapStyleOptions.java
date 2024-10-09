package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.util.IOUtils;
import java.io.IOException;

/* loaded from: classes.dex */
public final class MapStyleOptions extends AbstractSafeParcelable {
    public static final Parcelable.Creator<MapStyleOptions> CREATOR = new zzh();
    private String zzb;

    public MapStyleOptions(String str) {
        Preconditions.checkNotNull(str, "json must not be null");
        this.zzb = str;
    }

    public static MapStyleOptions loadRawResourceStyle(Context context, int i) {
        try {
            return new MapStyleOptions(new String(IOUtils.readInputStreamFully(context.getResources().openRawResource(i)), "UTF-8"));
        } catch (IOException e) {
            throw new Resources.NotFoundException("Failed to read resource " + i + ": " + e.toString());
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzb, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
