package com.google.android.gms.internal.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ClientIdentity;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.location.LocationRequest;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class zzde implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        LocationRequest locationRequest = null;
        ArrayList arrayList = null;
        String str = null;
        String str2 = null;
        long j = Long.MAX_VALUE;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            int fieldId = SafeParcelReader.getFieldId(readHeader);
            if (fieldId == 1) {
                locationRequest = (LocationRequest) SafeParcelReader.createParcelable(parcel, readHeader, LocationRequest.CREATOR);
            } else if (fieldId != 5) {
                switch (fieldId) {
                    case 8:
                        z = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 9:
                        z2 = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 10:
                        str = SafeParcelReader.createString(parcel, readHeader);
                        break;
                    case 11:
                        z3 = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 12:
                        z4 = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 13:
                        str2 = SafeParcelReader.createString(parcel, readHeader);
                        break;
                    case 14:
                        j = SafeParcelReader.readLong(parcel, readHeader);
                        break;
                    default:
                        SafeParcelReader.skipUnknownField(parcel, readHeader);
                        break;
                }
            } else {
                arrayList = SafeParcelReader.createTypedList(parcel, readHeader, ClientIdentity.CREATOR);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzdd(locationRequest, arrayList, z, z2, str, z3, z4, str2, j);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i) {
        return new zzdd[i];
    }
}
