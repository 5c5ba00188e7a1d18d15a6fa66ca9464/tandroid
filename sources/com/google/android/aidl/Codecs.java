package com.google.android.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public abstract class Codecs {
    private static final ClassLoader CLASS_LOADER = Codecs.class.getClassLoader();

    public static boolean createBoolean(Parcel parcel) {
        return parcel.readInt() != 0;
    }

    public static void writeParcelable(Parcel parcel, Parcelable parcelable) {
        if (parcelable == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            parcelable.writeToParcel(parcel, 0);
        }
    }
}
